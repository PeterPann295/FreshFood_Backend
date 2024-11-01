package com.freshfood.repository.criteria;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.function.Consumer;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchCriteriaQueryConsumer implements Consumer<SearchCriteria> {
    private CriteriaBuilder criteriaBuilder;
    private Root root;
    private Predicate predicate;
    @Override
    public void accept(SearchCriteria searchCriteria) {
        if(searchCriteria.getOperation().equalsIgnoreCase(">")){
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get(searchCriteria.getKey()), searchCriteria.getValue().toString()));
        }else if(searchCriteria.getOperation().equalsIgnoreCase("<")){
            predicate =  criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get(searchCriteria.getKey()), searchCriteria.getValue().toString()));
        }else {
            if(root.get(searchCriteria.getKey()).getJavaType() == String.class){
                predicate =   criteriaBuilder.and(predicate, criteriaBuilder.like(root.get(searchCriteria.getKey()), String.format("%%%s%%", searchCriteria.getValue().toString())));
            }else {
                predicate =  criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get(searchCriteria.getKey()), searchCriteria.getValue()));
            }
        }
    }
}
