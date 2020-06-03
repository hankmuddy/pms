package com.idgindigo.pms.domain.pms;

import com.idgindigo.pms.domain.ApprovableEntityTest;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.pms.RoomProvider;

import javax.inject.Inject;

/**
 * @author vomel
 * @since 01.11.13 17:15
 */
public class RoomTest extends ApprovableEntityTest<Room> {
    @Inject
    private RoomProvider provider;

    @Override
    protected EntityProvider<Room> getProvider() {
        return provider;
    }
}
