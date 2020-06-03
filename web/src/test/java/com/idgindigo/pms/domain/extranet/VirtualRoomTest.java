package com.idgindigo.pms.domain.extranet;

import com.idgindigo.pms.domain.BasePersistenceTest;
import com.idgindigo.pms.domain.extranet.roomtype.VirtualRoom;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.extranet.VirtualRoomProvider;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 1/15/14 4:38 PM
 */
public class VirtualRoomTest extends BasePersistenceTest<VirtualRoom> {
    @Inject
    private VirtualRoomProvider provider;

    @Override
    protected EntityProvider<VirtualRoom> getProvider() {
        return provider;
    }
}
