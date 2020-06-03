package com.idgindigo.pms.logins.domain;

import com.idgindigo.pms.domain.BasePersistenceTest;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.HotelToFacilityProvider;

import javax.inject.Inject;

/**
 * @author vomel
 * @since 18.02.14 16:25
 */
public class HotelToFacilityTest extends BasePersistenceTest<HotelToFacility> {
    @Inject
    private HotelToFacilityProvider provider;

    @Override
    protected EntityProvider<HotelToFacility> getProvider() {
        return provider;
    }
}
