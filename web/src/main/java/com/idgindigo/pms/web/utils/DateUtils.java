package com.idgindigo.pms.web.utils;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author valentyn_vakatsiienko
 * @since 6/17/14 1:04 PM
 */
@Component
public class DateUtils {
    @Inject
    private MessageSource messageSource;
    private static Map<Integer, String> daysOfWeek = new HashMap<Integer, String>() {{
        put(DateTimeConstants.MONDAY, "monday");
        put(DateTimeConstants.TUESDAY, "tuesday");
        put(DateTimeConstants.WEDNESDAY, "wednesday");
        put(DateTimeConstants.THURSDAY, "thursday");
        put(DateTimeConstants.FRIDAY, "friday");
        put(DateTimeConstants.SATURDAY, "saturday");
        put(DateTimeConstants.SUNDAY, "sunday");
    }};
    private static Map<Integer, String> months = new HashMap<Integer, String>() {{
        put(DateTimeConstants.JANUARY, "ofJanuary");
        put(DateTimeConstants.FEBRUARY, "ofFebruary");
        put(DateTimeConstants.MARCH, "ofMarch");
        put(DateTimeConstants.APRIL, "ofApril");
        put(DateTimeConstants.MAY, "ofMay");
        put(DateTimeConstants.JUNE, "ofJune");
        put(DateTimeConstants.JULY, "ofJuly");
        put(DateTimeConstants.AUGUST, "ofAugust");
        put(DateTimeConstants.SEPTEMBER, "ofSeptember");
        put(DateTimeConstants.OCTOBER, "ofOctober");
        put(DateTimeConstants.NOVEMBER, "ofNovember");
        put(DateTimeConstants.DECEMBER, "ofDecember");
    }};

    public String getDayOfWeek(LocalDate date, Locale locale) {
        int dayOfWeek = date.getDayOfWeek();
        if (daysOfWeek.containsKey(dayOfWeek)) {
            return getLabel(daysOfWeek.get(dayOfWeek), locale);
        } else {
            throw new IllegalArgumentException("Couldn't find day of week value for constant: " + date.getDayOfWeek());
        }
    }

    public String getOfMonth(LocalDate date, Locale locale) {
        int month = date.getMonthOfYear();
        if (months.containsKey(month)) {
            return getLabel(months.get(month), locale);
        } else {
            throw new IllegalArgumentException("Couldn't find month value for constant: " + date.getMonthOfYear());
        }
    }

    private String getLabel(String code, Locale locale) {
        return messageSource.getMessage(code, null, code, locale);
    }
}
