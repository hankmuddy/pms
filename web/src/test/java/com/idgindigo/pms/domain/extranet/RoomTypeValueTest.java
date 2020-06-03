package com.idgindigo.pms.domain.extranet;

import com.idgindigo.pms.domain.BasePersistenceTest;
import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeValue;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.extranet.RoomTypeValueProvider;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 11/11/13 5:31 PM
 */
public class RoomTypeValueTest extends BasePersistenceTest<RoomTypeValue> {
    @Inject
    private RoomTypeValueProvider provider;

    @Override
    protected EntityProvider<RoomTypeValue> getProvider() {
        return provider;
    }
}
