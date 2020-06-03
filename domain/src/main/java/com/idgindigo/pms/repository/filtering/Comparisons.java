package com.idgindigo.pms.repository.filtering;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import java.util.Collection;
import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 10/23/13 6:31 PM
 */
public enum Comparisons implements Comparison {
    EQ {
        @Override
        public Predicate toPredicate(Expression<? extends Comparable> expression, Object value, CriteriaBuilder cb) {
            return cb.equal(expression, value);
        }

        @Override
        public Predicate toPredicate(Expression<? extends Comparable> expression, Expression<? extends Comparable> value, CriteriaBuilder cb) {
            return cb.equal(expression, value);
        }
    },
    NEQ {
        @Override
        public Predicate toPredicate(Expression<? extends Comparable> expression, Object value, CriteriaBuilder cb) {
            return cb.notEqual(expression, value);
        }

        @Override
        public Predicate toPredicate(Expression<? extends Comparable> expression, Expression<? extends Comparable> value, CriteriaBuilder cb) {
            return cb.notEqual(expression, value);
        }
    },
    LT {
        @Override
        public Predicate toPredicate(Expression<? extends Comparable> expression, Object value, CriteriaBuilder cb) {
            return cb.lessThan(expression, (Comparable<?>) value);
        }

        @Override
        public Predicate toPredicate(Expression<? extends Comparable> expression, Expression<? extends Comparable> value, CriteriaBuilder cb) {
            return cb.lessThan(expression, value);
        }
    },
    LTE {
        @Override
        public Predicate toPredicate(Expression<? extends Comparable> expression, Object value, CriteriaBuilder cb) {
            return cb.lessThanOrEqualTo(expression, (Comparable<?>) value);
        }

        @Override
        public Predicate toPredicate(Expression<? extends Comparable> expression, Expression<? extends Comparable> value, CriteriaBuilder cb) {
            return cb.lessThanOrEqualTo(expression, value);
        }
    },
    GTE {
        @Override
        public Predicate toPredicate(Expression<? extends Comparable> expression, Object value, CriteriaBuilder cb) {
            return cb.greaterThanOrEqualTo(expression, (Comparable<?>) value);
        }

        @Override
        public Predicate toPredicate(Expression<? extends Comparable> expression, Expression<? extends Comparable> value, CriteriaBuilder cb) {
            return cb.greaterThanOrEqualTo(expression, (Comparable<?>) value);
        }
    },
    GT {
        @Override
        public Predicate toPredicate(Expression<? extends Comparable> expression, Object value, CriteriaBuilder cb) {
            return cb.greaterThan(expression, (Comparable<?>) value);
        }

        @Override
        public Predicate toPredicate(Expression<? extends Comparable> expression, Expression<? extends Comparable> value, CriteriaBuilder cb) {
            return cb.greaterThan(expression, value);
        }
    },
    EMPTY {
        @Override
        public Predicate toPredicate(Expression<? extends Comparable> expression, Object value, CriteriaBuilder cb) {
            return cb.equal(expression, value);
        }

        @Override
        public Predicate toPredicate(Expression<? extends Comparable> expression, Expression<? extends Comparable> value, CriteriaBuilder cb) {
            return cb.equal(expression, value);
        }
    },
    IS_NULL {
        @Override
        public Predicate toPredicate(Expression<? extends Comparable> expression, Object value, CriteriaBuilder cb) {
            return cb.isNull(expression);
        }

        @Override
        public Predicate toPredicate(Expression<? extends Comparable> expression, Expression<? extends Comparable> value, CriteriaBuilder cb) {
            return cb.isNull(expression);
        }
    },
    LIKE {
        @Override
        public Predicate toPredicate(Expression<? extends Comparable> expression, Object value, CriteriaBuilder cb) {
            return cb.like(cb.lower(expression.as(String.class)), "%" + value.toString().toLowerCase() + "%");
        }

        @Override
        public Predicate toPredicate(Expression<? extends Comparable> expression, Expression<? extends Comparable> value, CriteriaBuilder cb) {
            return cb.like(cb.lower(expression.as(String.class)), cb.lower(value.as(String.class)));
        }
    },
    IS_EMPTY_LIST {
        @Override
        public Predicate toPredicate(Expression<? extends Comparable> expression, Object value, CriteriaBuilder cb) {
            return cb.isEmpty(expression.as(List.class));
        }

        @Override
        public Predicate toPredicate(Expression<? extends Comparable> expression, Expression<? extends Comparable> value, CriteriaBuilder cb) {
            return cb.isEmpty(expression.as(List.class));
        }
    },
    IN {
        @Override
        public Predicate toPredicate(Expression<? extends Comparable> expression, Object value, CriteriaBuilder cb) {
            return expression.in((Collection) value);
        }

        @Override
        public Predicate toPredicate(Expression<? extends Comparable> expression, Expression<? extends Comparable> value, CriteriaBuilder cb) {
            return expression.in(value);
        }
    };

}
