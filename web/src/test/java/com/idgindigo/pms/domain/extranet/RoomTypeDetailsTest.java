package com.idgindigo.pms.domain.extranet;

import com.idgindigo.pms.domain.BasePersistenceTest;
import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeDetails;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.extranet.RoomTypeDetailsProvider;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 3/21/14 3:19 PM
 */
public class RoomTypeDetailsTest extends BasePersistenceTest<RoomTypeDetails> {
    @Inject
    private RoomTypeDetailsProvider provider;

    @Override
    protected EntityProvider<RoomTypeDetails> getProvider() {
        return provider;
    }
}
