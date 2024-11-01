package com.freshfood.repository.search;

import com.freshfood.dto.response.PageResponse;
import com.freshfood.model.Address;
import com.freshfood.model.User;
import com.freshfood.repository.UserRepository;
import com.freshfood.repository.criteria.SearchCriteria;
import com.freshfood.repository.criteria.SearchCriteriaQueryConsumer;
import com.freshfood.repository.specification.SpecSearchCriteria;
import com.freshfood.repository.specification.SpecificationsBuilder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.freshfood.repository.specification.SearchOperation.LIKE;
import static com.freshfood.util.AppConst.SEARCH_SPEC_OPERATOR;


@Repository
@Slf4j
public class SearchUserRepository {
    private final UserRepository userRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public SearchUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public PageResponse<?> getAllUserWithSortByColumnAndSearch(int pageNo, int pageSize, String search, String sortBy){
        StringBuilder sqlQuery = new StringBuilder("select u from User u where 1=1");

        if(StringUtils.hasLength(search)){
            sqlQuery.append(" and lower(u.username) like lower(:username)");
            sqlQuery.append("or lower(u.email) like lower(:email)");
            sqlQuery.append("or lower(u.phone) like lower(:phone)");
        }

        if(StringUtils.hasLength(sortBy)){
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
            if(matcher.find()){
                if(matcher.group(3).equalsIgnoreCase("desc")){
                    sqlQuery.append(String.format("order by u.%s %s", matcher.group(1), matcher.group(3)));
                }else{
                    sqlQuery.append(String.format("order by u.%s %s", matcher.group(1), matcher.group(3)));
                }
            }
        }

        Query selectQuery = entityManager.createQuery(sqlQuery.toString());
        selectQuery.setFirstResult((pageNo));
        selectQuery.setMaxResults(pageSize);
        if(StringUtils.hasLength(search)){
            selectQuery.setParameter("username", String.format("%%%s%%", search));
            selectQuery.setParameter("email", String.format("%%%s%%", search));
            selectQuery.setParameter("phone", String.format("%%%s%%", search));
        }
        List users = selectQuery.getResultList();

        sqlQuery = new StringBuilder("select count(*) from User u where 1=1");
        if(StringUtils.hasLength(search)){
            sqlQuery.append(" and lower(u.username) like lower(?1)");
            sqlQuery.append("or lower(u.email) like lower(?2)");
            sqlQuery.append("or lower(u.phone) like lower(?3)");
        }
        selectQuery = entityManager.createQuery(sqlQuery.toString());
        if(StringUtils.hasLength(search)){
            selectQuery.setParameter(1, String.format("%%%s%%", search));
            selectQuery.setParameter(2, String.format("%%%s%%", search));
            selectQuery.setParameter(3, String.format("%%%s%%", search));
        }
        Long totalRecords = (Long) selectQuery.getSingleResult();

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage((int) (totalRecords/pageSize))
                .items(users)
                .build();
    }
    public PageResponse<?> advanceSearchCriteria(int pageNo, int pageSize, String address, String sort, String... search ){

        List<SearchCriteria> searchCriterias = new ArrayList<>();
        if(search != null && search.length > 0){
            for(String s: search){
                Pattern pattern = Pattern.compile("(\\w+?)(:|>|<)(.*)");
                Matcher matcher = pattern.matcher(s);
                if(matcher.find()){
                    searchCriterias.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
                }
            }
        }
        List users = getUsersAdvance(pageNo, pageSize, address, sort, searchCriterias);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .items(users)
                .totalPage(10)
                .build();
    }

    private List getUsersAdvance(int pageNo, int pageSize, String address, String sort, List<SearchCriteria> searchCriteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery query = criteriaBuilder.createQuery(User.class);
        Root root = query.from(User.class);

        Predicate predicate = criteriaBuilder.conjunction();
        SearchCriteriaQueryConsumer queryConsumer = new SearchCriteriaQueryConsumer(criteriaBuilder, root, predicate);
        searchCriteria.forEach(queryConsumer);
        predicate = queryConsumer.getPredicate();
        query.where(predicate);
        if(StringUtils.hasLength(sort)){
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sort);
            if(matcher.find()){
                if(matcher.group(3).equalsIgnoreCase("asc")){
                    query.orderBy(criteriaBuilder.asc(root.get(matcher.group(1))));
                }else{
                    query.orderBy(criteriaBuilder.desc(root.get(matcher.group(1))));
                }
            }
        }

        return entityManager.createQuery(query).setFirstResult(pageNo).setMaxResults(pageSize).getResultList();
    }
    public PageResponse<?> advanceSearchWithSpecification(Pageable pageable, String[] users, String[] address){
        log.info("Starting advanceSearchWithSpecification...");
        if(users != null && address!= null){
            log.info("da vao join address");
            return getUserJoinAdresses(pageable, users, address);
        }else if(users != null && address == null){
            log.info("khong vao join address");
            SpecificationsBuilder builder = new SpecificationsBuilder();
            Pattern pattern = Pattern.compile("(\\w+?)([<:>~!])(.*)(\\p{Punct}?)(\\p{Punct}?)");
            for (String s : users){
                Matcher matcher = pattern.matcher(s);
                if(matcher.find()){
                    builder.with(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5));
                }
            }
            List userList = userRepository.findAll(builder.buildUser());
            return PageResponse.builder()
                    .pageNo(pageable.getPageNumber())
                    .pageSize(pageable.getPageSize())
                    .totalPage(10)
                    .items(userList)
                    .build();
        }
        log.info("no xuong duoi cung me r");
        Page<User> usersPage = userRepository.findAll(pageable);
        return PageResponse.builder()
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalPage(usersPage.getTotalPages())
                .items(usersPage.stream().toList())
                .build();
    }
    public PageResponse getUserJoinAdresses(Pageable pageable, String[] user, String[] address){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);

        Join<Address, User> addressJoin = root.join("addresses");

        List<Predicate> userPredicates = new ArrayList<>();
        List<Predicate> addressPredicates = new ArrayList<>();

        Pattern pattern = Pattern.compile(SEARCH_SPEC_OPERATOR);
        if(user != null && user.length > 0){
            for (String s : user){
                log.info(s);
                Matcher matcher = pattern.matcher(s);
                if(matcher.find()){
                    SpecSearchCriteria criteria = new SpecSearchCriteria(matcher.group(1),matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5));
                    Predicate predicate = toPredicate(root, criteriaBuilder, criteria);
                    userPredicates.add(predicate);
                }
            }
        }
        if(address != null && address.length > 0){
            for (String s : address){
                log.info(s);
                Matcher matcher = pattern.matcher(s);
                if(matcher.find()){
                    SpecSearchCriteria criteria = new SpecSearchCriteria(matcher.group(1),matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5));
                    Predicate predicate = toPredicateAddress(addressJoin, criteriaBuilder, criteria);
                    addressPredicates.add(predicate);
                }
            }
        }
        Predicate userPredicate = criteriaBuilder.or(userPredicates.toArray(new Predicate[0]));
        Predicate addressPredicate = criteriaBuilder.or(addressPredicates.toArray(new Predicate[0]));
        Predicate predicate = criteriaBuilder.and(userPredicate, addressPredicate);
        criteriaQuery.where(predicate);
        List<User> users = entityManager.createQuery(criteriaQuery).setFirstResult(pageable.getPageNumber())
                .setMaxResults(pageable.getPageSize()).getResultList();

        return PageResponse.builder()
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalPage(10)
                .items(users)
                .build();
    }
    public Predicate toPredicate(@NonNull final Root<User> root, @NonNull final CriteriaBuilder builder, SpecSearchCriteria criteria) {
        Path<?> path = root.get(criteria.getKey());

        if (criteria.getOperation() == LIKE && path.getJavaType() != String.class) {
            // Nếu kiểu không phải String, chuyển LIKE thành EQUALITY
            return builder.equal(path, criteria.getValue());
        }

        return switch (criteria.getOperation()) {
            case EQUALITY -> builder.equal(path, criteria.getValue());
            case NEGATION -> builder.notEqual(path, criteria.getValue());
            case GREATER_THAN -> builder.greaterThan(path.as(String.class), criteria.getValue().toString());
            case LESS_THAN -> builder.lessThan(path.as(String.class), criteria.getValue().toString());
            case LIKE -> builder.like(path.as(String.class), "%" + criteria.getValue().toString() + "%");
            case STARTS_WITH -> builder.like(path.as(String.class), criteria.getValue() + "%");
            case ENDS_WITH -> builder.like(path.as(String.class), "%" + criteria.getValue());
            case CONTAINS -> builder.like(path.as(String.class), "%" + criteria.getValue() + "%");
        };
    }

    public Predicate toPredicateAddress(@NonNull final Join<Address, User> root, @NonNull final CriteriaBuilder builder, SpecSearchCriteria criteria) {
        log.info(criteria.getValue().toString() + " day la no");
        Path<?> path = root.get(criteria.getKey());

        if (criteria.getOperation() == LIKE && path.getJavaType() != String.class) {
            // Nếu kiểu không phải String, chuyển LIKE thành EQUALITY
            return builder.equal(path, criteria.getValue());
        }

        return switch (criteria.getOperation()) {
            case EQUALITY -> builder.equal(path, criteria.getValue());
            case NEGATION -> builder.notEqual(path, criteria.getValue());
            case GREATER_THAN -> builder.greaterThan(path.as(String.class), criteria.getValue().toString());
            case LESS_THAN -> builder.lessThan(path.as(String.class), criteria.getValue().toString());
            case LIKE -> builder.like(path.as(String.class), "%" + criteria.getValue().toString() + "%");
            case STARTS_WITH -> builder.like(path.as(String.class), criteria.getValue() + "%");
            case ENDS_WITH -> builder.like(path.as(String.class), "%" + criteria.getValue());
            case CONTAINS -> builder.like(path.as(String.class), "%" + criteria.getValue() + "%");
        };
    }

}

