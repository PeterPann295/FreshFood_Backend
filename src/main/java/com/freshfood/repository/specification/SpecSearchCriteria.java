package com.freshfood.repository.specification;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.freshfood.repository.specification.SearchOperation.*;

@Getter
@Setter
@NoArgsConstructor
public class SpecSearchCriteria {
    private String key;
    private SearchOperation operation;
    private Object value;
    private boolean orPredicate;

    public SpecSearchCriteria(String key, SearchOperation operation, Object value){
        super();
        this.value = value;
        this.key = key;
        this.operation = operation;
    }
    public SpecSearchCriteria(String orPredicate,String key, SearchOperation operation, Object value){
        super();
        this.orPredicate = orPredicate != null && orPredicate.contains(OR_PREDICATE_FLAG);
        this.value = value;
        this.key = key;
        this.operation = operation;
    }

    public SpecSearchCriteria(String key, String operation, String value, String prefix, String suffix){
        SearchOperation searchOperation = SearchOperation.getSimpleOperation(operation.charAt(0));
        if(searchOperation == SearchOperation.EQUALITY){
            boolean startWithAsterisk = prefix !=null && prefix.contains(ZERO_OR_MORE_REGEX);
            boolean endWithAsterrisk = suffix != null && suffix.contains(ZERO_OR_MORE_REGEX);
            if(startWithAsterisk && endWithAsterrisk){
                searchOperation = CONTAINS;
            }else if(startWithAsterisk){
                searchOperation = STARTS_WITH;
            }else{
                searchOperation = ENDS_WITH;
            }
        }
        this.key = key;
        this.operation = searchOperation;
        this.value = value;
    }
}
