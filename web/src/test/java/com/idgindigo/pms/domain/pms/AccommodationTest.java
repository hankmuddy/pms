package com.idgindigo.pms.domain.pms;

import com.idgindigo.pms.domain.ApprovableEntityTest;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.pms.AccommodationProvider;

import javax.inject.Inject;

/**
 * @author vomel
 * @since 31.10.13 17:01
 */
public class AccommodationTest extends ApprovableEntityTest<Accommodation> {
    @Inject
    private AccommodationProvider provider;

    @Override
    protected EntityProvider<Accommodation> getProvider() {
        return provider;
    }
}
