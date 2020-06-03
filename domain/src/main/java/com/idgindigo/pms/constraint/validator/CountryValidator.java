package com.idgindigo.pms.constraint.validator;

import com.idgindigo.pms.constraint.Country;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * @author valentyn_vakatsiienko
 * @since 6/2/14 4:39 PM
 */
public class CountryValidator extends CodeValidator<Country> {
    private static final Set<String> codes = new HashSet<>(Arrays.asList(Locale.getISOCountries()));

    @Override
    public Set<String> getCodes() {
        return codes;
    }

}
