package com.idgindigo.pms.repository.filtering;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.LinkedList;
import java.util.List;

/**
 * @author avoinovan
 * @since 8/6/13 4:55 PM
 */
public class MultiFilterSpec<T> implements Specification<T> {
    private final Iterable<FilterParameter> filterParametersList;
    private final String connective;

    public MultiFilterSpec(Iterable<FilterParameter> filterParametersList, String connective) {
        this.filterParametersList = filterParametersList;
        this.connective = connective;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new LinkedList<Predicate>();
        for (FilterParameter parameters : filterParametersList) {
            @SuppressWarnings("unchecked")
            Predicate predicate = parameters.toPredicate(root, cb);
            predicates.add(predicate);
        }
        if ("or".equals(connective)) {
            return cb.or(predicates.toArray(new Predicate[predicates.size()]));
        }
        return cb.and(predicates.toArray(new Predicate[predicates.size()]));
    }

}
