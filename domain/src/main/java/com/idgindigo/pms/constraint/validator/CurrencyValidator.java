package com.idgindigo.pms.constraint.validator;

import com.idgindigo.pms.constraint.Currency;

import java.util.HashSet;
import java.util.Set;

/**
 * @author valentyn_vakatsiienko
 * @since 6/6/14 12:25 PM
 */
public class CurrencyValidator extends CodeValidator<Currency> {
    private static final Set<String> codes = new HashSet<>();

    static {
        for (java.util.Currency currency : java.util.Currency.getAvailableCurrencies()) {
            codes.add(currency.getCurrencyCode());
        }
    }

    @Override
    public Set<String> getCodes() {
        return codes;
    }
}
