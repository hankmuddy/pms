package com.idgindigo.pms.utils;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.LazyInitializationException;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * @author vomel
 * @since 29.10.13 17:32
 */
public class SmartToStringBuilder extends ToStringBuilder {

    private final SimpleDateFormat SDF = new SimpleDateFormat("MM/dd/yyyy");
    public static final ToStringStyle NO_FIELD_NAMES_STYLE = new NoFieldNames();
    public static final ToStringStyle SHORT_PREFIX_STYLE = new ShortPrefix();
    public static final ToStringStyle SIMPLE_STYLE = new SimpleStyle();

    public SmartToStringBuilder(Object object, ToStringStyle style) {
        super(object, style);
    }

    public SmartToStringBuilder(Object object) {
        this(object, SHORT_PREFIX_STYLE);
    }

    @Override
    public ToStringBuilder append(String fieldName, Object value, boolean fullDetail) {
        if (value == null || value instanceof Collection && ((Collection) value).isEmpty() || value instanceof Map && ((Map) value).isEmpty()) {
            return this;
        }
        try {
            return fullDetail ? super.append(fieldName, value, fullDetail) : super.append(fieldName, value);
        } catch (LazyInitializationException e) {
            return this;
        }
    }

    @Override
    public ToStringBuilder append(String fieldName, Object value) {
        return append(fieldName, value, false);
    }

    public String format(Date value) {
        return value != null ? SDF.format(value) : null;
    }

    private static final class ShortPrefix extends ToStringStyle {
        ShortPrefix() {
            setUseShortClassName(true);
            setUseIdentityHashCode(false);
        }
    }

    private static final class NoFieldNames extends ToStringStyle {
        NoFieldNames() {
            setUseFieldNames(false);
            setUseShortClassName(true);
            setUseIdentityHashCode(false);
        }
    }

    private static final class SimpleStyle extends ToStringStyle {
        SimpleStyle() {
            setUseFieldNames(false);
            setUseClassName(false);
            setUseIdentityHashCode(false);
        }
    }

}