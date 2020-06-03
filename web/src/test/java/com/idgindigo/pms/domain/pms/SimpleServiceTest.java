package com.idgindigo.pms.domain.pms;

import com.idgindigo.pms.domain.BasePersistenceTest;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.pms.SimpleServiceProvider;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 11/20/13 2:41 PM
 */
public class SimpleServiceTest extends BasePersistenceTest<SimpleService> {
    @Inject
    private SimpleServiceProvider simpleServiceProvider;

    @Override
    protected EntityProvider<SimpleService> getProvider() {
        return simpleServiceProvider;
    }
}
