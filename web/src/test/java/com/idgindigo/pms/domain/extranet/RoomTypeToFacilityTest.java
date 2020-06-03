package com.idgindigo.pms.domain.extranet;

import com.idgindigo.pms.domain.BasePersistenceTest;
import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeToFacility;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.extranet.RoomTypeToFacilityProvider;

import javax.inject.Inject;

/**
 * @author vomel
 * @since 25.02.14 00:30
 */
public class RoomTypeToFacilityTest extends BasePersistenceTest<RoomTypeToFacility> {
    @Inject
    private RoomTypeToFacilityProvider provider;

    @Override
    protected EntityProvider<RoomTypeToFacility> getProvider() {
        return provider;
    }

}
