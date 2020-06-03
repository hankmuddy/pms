package com.idgindigo.pms.web.controller;

import com.idgindigo.pms.configuration.WebConfiguration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * @author valentyn_vakatsiienko
 * @since 6/6/14 12:32 PM
 */
@Controller
@RequestMapping(CommonsController.URL)
public class CommonsController {
    public static final String URL = WebConfiguration.REST_URL_PREFIX + "common";

    @Inject
    private LocaleResolver localeResolver;

    private Map<Locale, List<CurrencyDto>> map = new HashMap<>();

    @RequestMapping("currencies")
    @ResponseBody
    public ResponseEntity<List<CurrencyDto>> getCurrencies(HttpServletRequest request) {
        Locale locale = localeResolver.resolveLocale(request);

        if (map.containsKey(locale)) {
            return new ResponseEntity<>(map.get(locale));
        }

        Set<Currency> availableCurrencies = Currency.getAvailableCurrencies();
        List<CurrencyDto> dtos = new ArrayList<>(availableCurrencies.size());
        for (Currency currency : availableCurrencies) {
            dtos.add(new CurrencyDto(currency.getCurrencyCode(), currency.getDisplayName(locale)));
        }
        map.put(locale, dtos);
        return new ResponseEntity<>(dtos);
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class CurrencyDto {
        private String code;
        private String label;
    }
}
