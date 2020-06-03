package com.idgindigo.pms.repository.filtering;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

/**
 * @author valentyn_vakatsiienko
 * @since 10/23/13 11:35 AM
 */
public interface Comparison {
    Predicate toPredicate(Expression<? extends Comparable> expression, Object value, CriteriaBuilder cb);

    Predicate toPredicate(Expression<? extends Comparable> expression, Expression<? extends Comparable> value, CriteriaBuilder cb);
}