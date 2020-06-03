package com.idgindigo.pms.domain.extranet;

import com.idgindigo.pms.domain.BasePersistenceTest;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.extranet.SeasonProvider;

import javax.inject.Inject;

/**
 * @author vomel
 * @since 01.11.13 12:38
 */
public class SeasonTest extends BasePersistenceTest<Season> {
    @Inject
    private SeasonProvider provider;

    @Override
    protected EntityProvider<Season> getProvider() {
        return provider;
    }

}
