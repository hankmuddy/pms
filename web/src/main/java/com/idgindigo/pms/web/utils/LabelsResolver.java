package com.idgindigo.pms.web.utils;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * @author etipalchuk
 * @since 17.04.14 10:39
 */
@Component
public class LabelsResolver {
    @Inject
    private MessageSource messageSource;
    @Inject
    private LocaleResolver localeResolver;
    private static final Map<Locale, Map<String, String>> cache = new HashMap<>();

    public Map<String, String> getLabels(HttpServletRequest request) {
        Locale locale = localeResolver.resolveLocale(request);
        if (cache.containsKey(locale)) {
            return cache.get(locale);
        }
        Map<String, String> result = new HashMap<>();
        Set<String> stringsPropNames = ResourceBundle.getBundle("messages", locale).keySet();
        for (String stringsPropName : stringsPropNames) {
            result.put(stringsPropName, messageSource.getMessage(stringsPropName, null, stringsPropName, locale));
        }
        cache.put(locale, result);
        return result;
    }
}
