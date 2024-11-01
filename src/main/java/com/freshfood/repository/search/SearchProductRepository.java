package com.freshfood.repository.search;

import com.freshfood.dto.response.CategoryResponseDTO;
import com.freshfood.dto.response.PageResponse;
import com.freshfood.dto.response.ProductImageResponseDTO;
import com.freshfood.dto.response.ProductResponseDTO;
import com.freshfood.model.Category;
import com.freshfood.model.Product;
import com.freshfood.repository.ProductRepository;
import com.freshfood.repository.criteria.SearchCriteria;
import com.freshfood.repository.criteria.SearchCriteriaQueryConsumer;
import com.freshfood.repository.specification.ProductSpecification;
import com.freshfood.repository.specification.SpecSearchCriteria;
import com.freshfood.repository.specification.SpecificationsBuilder;
import com.freshfood.util.AppConst;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.freshfood.repository.specification.SearchOperation.LIKE;
import static com.freshfood.util.AppConst.SEARCH_SPEC_OPERATOR;

@Repository
@Slf4j
public class SearchProductRepository {
    private final ProductRepository productRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public SearchProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public PageResponse<?> getAllProductsWithSortAndSearch(int pageNo, int pageSize, String sort, String search) {
        StringBuilder sqlQuery = new StringBuilder("Select u FROM Product u WHERE 1=1 ");
        if(StringUtils.hasLength(search)){
            sqlQuery.append(" AND lower(u.name) LIKE lower(?1) ");
            sqlQuery.append(" OR lower(u.unit) LIKE lower(?2) ");
        }
        if(StringUtils.hasLength(sort)){
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sort);
            if(matcher.find()){
                if(matcher.group(3).equalsIgnoreCase("asc")){
                    sqlQuery.append(String.format(" ORDER BY u.%s %s ", matcher.group(1), matcher.group(3)));
                }else{
                    sqlQuery.append(String.format(" ORDER BY u.%s %s ", matcher.group(1), matcher.group(3)));
                }
            }
        }

        Query query = entityManager.createQuery(sqlQuery.toString());
        if(StringUtils.hasLength(search)){
            query.setParameter(1, String.format("%%%s%%", search));
            query.setParameter(2, String.format("%%%s%%", search));
        }
        query.setFirstResult((pageNo));
        query.setMaxResults(pageSize);
        List products = query.getResultList();
        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .items(products)
                .totalPage(10)
                .build();
    }
    public PageResponse<?> advanceSearchByCriteria(int pageNo, int pageSize,String category, String sort, String... search) {

        List<SearchCriteria> searchCriterias = new ArrayList<>();
        if(search != null && search.length > 0){
            for(String s : search){
                if(StringUtils.hasLength(s)){
                    Pattern pattern = Pattern.compile("(\\w+?)(:|>|<)(.*)");
                    Matcher matcher = pattern.matcher(s);
                    if(matcher.find()){
                        searchCriterias.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
                    }
                }
            }
        }
        List<Product> products = getProducts(pageNo, pageSize, category , searchCriterias, sort);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .items(products)
                .totalPage(10)
                .build();
    }

    private List<Product> getProducts(int pageNo, int pageSize, String category , List<SearchCriteria> searchCriterias, String sort) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);

        Predicate predicate = criteriaBuilder.conjunction();
        SearchCriteriaQueryConsumer queryConsumer= new SearchCriteriaQueryConsumer(criteriaBuilder, root, predicate);


        if(StringUtils.hasLength(category)){
            Join<Category, Product> categoryProductJoin = root.join("category", JoinType.INNER);
            Predicate categoryPredicate = criteriaBuilder.like(categoryProductJoin.get("name"), String.format("%%%s%%", category));
            query.where(predicate, categoryPredicate);
        }else {
            searchCriterias.forEach(queryConsumer);
            predicate = queryConsumer.getPredicate();
            query.where(predicate);
        }
        log.info(sort);
        if(StringUtils.hasLength(sort)){
            log.info("da vao -1");
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sort);
            if(matcher.find()){
                String fieldName = matcher.group(1);
                String direction = matcher.group(3);
                if (direction.equalsIgnoreCase("asc")) {
                    query.orderBy(criteriaBuilder.asc(root.get(fieldName)));
                } else {
                    query.orderBy(criteriaBuilder.desc(root.get(fieldName)));
                }
            }
        }

        return  entityManager.createQuery(query).setFirstResult(pageNo).setMaxResults(pageSize).getResultList();
    }
    public PageResponse<?> advanceSearchWithSpecification(Pageable pageable, String[] product, String[] category){
        if(category != null){
           return getProductJoinCategory(pageable, product, category);
        }else if(product !=null){
            SpecificationsBuilder builder = new SpecificationsBuilder();
            Pattern pattern = Pattern.compile(SEARCH_SPEC_OPERATOR);
            for(String p : product){
                Matcher matcher = pattern.matcher(p);
                if(matcher.find()){
                    builder.with(matcher.group(1),matcher.group(2),matcher.group(3),matcher.group(4),matcher.group(5));
                }
            }
            List productList = productRepository.findAll(builder.buildProduct());
            return PageResponse.builder()
                    .pageNo(pageable.getPageNumber())
                    .pageSize(pageable.getPageSize())
                    .items(convertProductResponseDTO(productList.stream().toList()))
                    .totalPage(10)
                    .build();
        }
        Page<Product> productList = productRepository.findAll(pageable);
        return PageResponse.builder()
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .items(convertProductResponseDTO(productList.stream().toList()))
                .totalPage(10)
                .build();
    }

    private PageResponse<?> getProductJoinCategory(Pageable pageable, String[] product, String[] category) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = builder.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);

        Join<Category, Product> categoryJoin = root.join("category");

        List<Predicate> productPredicates = new ArrayList<>();
        List<Predicate> categoryPredicates = new ArrayList<>();
        if(product!=null){
            Pattern pattern = Pattern.compile(SEARCH_SPEC_OPERATOR);
            for (String p : product){
                Matcher matcher = pattern.matcher(p);
                if(matcher.find()){
                    SpecSearchCriteria specSearchPd = new SpecSearchCriteria(matcher.group(1),matcher.group(2),matcher.group(3),matcher.group(4),matcher.group(5));
                    Predicate predicate = toPredicate(root, builder, specSearchPd);
                    productPredicates.add(predicate);
                }
            }
        }
        if(category !=null){
            Pattern pattern = Pattern.compile(SEARCH_SPEC_OPERATOR);
            for (String p : category){
                Matcher matcher = pattern.matcher(p);
                if(matcher.find()){
                    SpecSearchCriteria specSearchCate = new SpecSearchCriteria(matcher.group(1),matcher.group(2),matcher.group(3),matcher.group(4),matcher.group(5));
                    Predicate predicate = toPredicateCategoryJoin(categoryJoin, builder, specSearchCate);
                    categoryPredicates.add(predicate);
                }
            }
        }
        Predicate productPredicate = builder.or(productPredicates.toArray(new Predicate[0]));
        Predicate categoryPredicate = builder.or(categoryPredicates.toArray(new Predicate[0]));
        Predicate finalPredicate;
        if(category!=null && product==null) finalPredicate = categoryPredicate;
        else finalPredicate = builder.and(productPredicate, categoryPredicate);
        query.where(finalPredicate);
        List<Product> productList = entityManager.createQuery(query).setFirstResult(pageable.getPageNumber())
                .setMaxResults(pageable.getPageSize()).getResultList();
        return PageResponse.builder()
                .pageNo(pageable.getPageNumber())
                .pageSize(pageable.getPageSize())
                .totalPage(10)
                .items(convertProductResponseDTO(productList.stream().toList()))
                .build();
    }
    public Predicate toPredicate(Root<Product> root, CriteriaBuilder builder, SpecSearchCriteria criteria) {
        log.info(criteria.getValue().toString() + " day la no");
        Path<?> path = root.get(criteria.getKey());

        if (criteria.getOperation() == LIKE && path.getJavaType() != String.class) {
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
    public Predicate toPredicateCategoryJoin(Join<Category, Product> root, CriteriaBuilder builder, SpecSearchCriteria criteria) {
        log.info(criteria.getValue().toString() + " day la no");
        Path<?> path = root.get(criteria.getKey());

        if (criteria.getOperation() == LIKE && path.getJavaType() != String.class) {
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
    private List<ProductResponseDTO> convertProductResponseDTO(List<Product> products){
        List<ProductResponseDTO> productResponseDTOS = products.stream().map(product -> ProductResponseDTO.builder()
                .category(new CategoryResponseDTO(product.getCategory()))
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .thumbnailUrl(product.getThumbnailUrl())
                .unit(product.getUnit().toString())
                .expiryDate(product.getExpiryDate())
                .id(product.getId())
                .status(product.getStatus().toString())
                .productImages((Set<ProductImageResponseDTO>) product.getProductImages().stream().map(productImage -> ProductImageResponseDTO.builder()
                        .id(productImage.getId())
                        .imageUrl(productImage.getImageUrl())
                        .altText(productImage.getAltText())
                        .build()).collect(Collectors.toSet()))
                .build()).toList();
        return productResponseDTOS;
    }
}
