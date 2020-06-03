package com.idgindigo.pms.constraint.validator;

import com.idgindigo.pms.constraint.Language;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * @author valentyn_vakatsiienko
 * @since 6/2/14 4:39 PM
 */
public class LanguageValidator extends CodeValidator<Language> {
    private static final Set<String> codes = new HashSet<>(Arrays.asList(Locale.getISOLanguages()));

    @Override
    public Set<String> getCodes() {
        return codes;
    }
}
