package com.idgindigo.pms.domain.extranet;

import com.idgindigo.pms.domain.BasePersistenceTest;
import com.idgindigo.pms.domain.extranet.service.Living;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.extranet.LivingProvider;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 11/11/13 5:16 PM
 */
public class LivingTest extends BasePersistenceTest<Living> {
    @Inject
    private LivingProvider provider;

    @Override
    protected EntityProvider<Living> getProvider() {
        return provider;
    }
}
