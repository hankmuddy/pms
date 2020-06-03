package com.idgindigo.pms.domain.pms;

import com.idgindigo.pms.domain.BasePersistenceTest;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.pms.SimpleServiceUseProvider;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 11/20/13 2:53 PM
 */
public class ServiceUseTest extends BasePersistenceTest<SimpleServiceUse> {
    @Inject
    private SimpleServiceUseProvider provider;

    @Override
    protected EntityProvider<SimpleServiceUse> getProvider() {
        return provider;
    }
}
