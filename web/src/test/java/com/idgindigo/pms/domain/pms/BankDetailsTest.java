package com.idgindigo.pms.domain.pms;

import com.idgindigo.pms.domain.BasePersistenceTest;
import com.idgindigo.pms.repository.pms.BankDetailsRepository;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.pms.BankDetailsProvider;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static org.testng.Assert.assertEquals;

/**
 * @author valentyn_vakatsiienko
 * @since 12/30/13 12:59 PM
 */
public class BankDetailsTest extends BasePersistenceTest<BankDetails> {
    @Inject
    private BankDetailsProvider provider;
    @Inject
    private BankDetailsRepository repository;

    @Test
    public void testSetDefault() {
        BankDetails details = provider.getPersistentEntity();
        repository.eraseDefaults();
        repository.setDefault(details.getId());
        assertEquals(repository.getDefault(), details);
    }

    @Override
    protected EntityProvider<BankDetails> getProvider() {
        return provider;
    }
}
