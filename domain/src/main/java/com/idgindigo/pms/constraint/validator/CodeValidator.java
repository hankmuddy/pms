package com.idgindigo.pms.constraint.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * @author valentyn_vakatsiienko
 * @since 6/2/14 5:22 PM
 */
public abstract class CodeValidator<T extends Annotation> implements ConstraintValidator<T, String> {
    @Override
    public void initialize(T constraintAnnotation) {
    }

    public abstract Set<String> getCodes();

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || getCodes().contains(value);
    }
}
