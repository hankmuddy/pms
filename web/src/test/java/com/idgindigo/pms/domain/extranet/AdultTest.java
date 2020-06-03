package com.idgindigo.pms.domain.extranet;

import com.idgindigo.pms.constraint.Country;
import com.idgindigo.pms.constraint.Language;
import com.idgindigo.pms.domain.BasePersistenceTest;
import com.idgindigo.pms.domain.extranet.person.Adult;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.extranet.AdultProvider;
import junit.framework.Assert;
import org.springframework.transaction.TransactionSystemException;
import org.testng.annotations.Test;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * @author valentyn_vakatsiienko
 * @since 1/11/14 12:52 PM
 */
public class AdultTest extends BasePersistenceTest<Adult> {
    @Inject
    private AdultProvider provider;

    @Test
    public void testValidateCountry() {
        Adult adult = provider.getPersistentEntity();
        adult.setCountry("Wrong!");
        verifyConstraintViolation(adult, Country.MESSAGE);
        adult.setCountry("UA");
        provider.getRepository().save(adult);
    }

    @Test
    public void testValidateLanguage() {
        Adult adult = provider.getPersistentEntity();
        adult.setLanguage("Wrong!");
        verifyConstraintViolation(adult, Language.MESSAGE);
        adult.setLanguage("uk");
        provider.getRepository().save(adult);
    }

    private void verifyConstraintViolation(Adult adult, String message) {
        try {
            provider.getRepository().save(adult);
            Assert.fail();
        } catch (TransactionSystemException e) {
            Throwable cause = e.getCause().getCause();
            assertTrue(cause instanceof ConstraintViolationException);
            ConstraintViolationException cve = (ConstraintViolationException) cause;
            Set<ConstraintViolation<?>> violations = cve.getConstraintViolations();
            assertEquals(violations.size(), 1);
            for (ConstraintViolation<?> violation : violations) {
                assertEquals(violation.getMessage(), message);
            }
        }
    }

    @Override
    protected EntityProvider<Adult> getProvider() {
        return provider;
    }
}
