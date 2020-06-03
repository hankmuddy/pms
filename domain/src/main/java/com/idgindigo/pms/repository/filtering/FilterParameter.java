package com.idgindigo.pms.repository.filtering;

import com.idgindigo.pms.utils.SmartToStringBuilder;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: andreykorotkov
 * Date: 23.07.13
 * Time: 10:49
 * To change this template use File | Settings | File Templates.
 */
public class FilterParameter<T> {

    private static final Pattern DOT_SEPARATOR = Pattern.compile("\\.");
    private final String field;
    private final Type type;
    private final String[] valueStrings;
    private final Comparison comparison;

    public FilterParameter(String field, String type, String[] value, String comparison) {
        this.field = field;
        this.type = type != null ? Types.valueOf(type.toUpperCase()) : null;
        this.comparison = Comparisons.valueOf(comparison.toUpperCase());
        valueStrings = value;
    }

    protected Predicate toPredicate(Root<? extends Comparable<?>> root, CriteriaBuilder cb) {
        Expression<? extends Comparable<?>> expression = getExpression(field, root);
        return (type != null && type == Types.FIELD) ?
                comparison.toPredicate(expression, getExpression(valueStrings[0], root), cb) :
                comparison.toPredicate(expression, parseComparison(), cb);
    }

    private Object parseComparison() {
        if (valueStrings == null) {
            return null;
        }
        List<Object> values = parseValueStrings(type, Arrays.asList(valueStrings));
        if (comparison == Comparisons.IN) {
            return values;
        } else {
            return values.get(0);
        }
    }

    private Expression<? extends Comparable<?>> getExpression(String field, Root<?> root) {
        String[] split = DOT_SEPARATOR.split(field);
        if (split.length > 1) {
            Join<Object, Object> join = root.join(split[0], JoinType.LEFT);
            for (int i = 1; i < split.length - 1; i++) {
                join = join.join(split[i], JoinType.LEFT);
            }
            return join.get(split[split.length - 1]);

        } else {
            return root.get(field);
        }
    }


    protected static List<Object> parseValueStrings(Type type, Collection<String> valueStrings) {
        if (valueStrings == null) {
            return Collections.emptyList();
        }
        List<Object> values = new ArrayList<>(valueStrings.size());
        for (String s : valueStrings) {
            values.add(type.parseValue(s));
        }
        return values;
    }

    @Override
    public String toString() {
        return new SmartToStringBuilder(this)
                .append("field", field)
                .append("type", type)
                .append("valueStrings", valueStrings)
                .append("comparison", comparison)
                .toString();
    }
}
