package com.idgindigo.pms.domain.extranet;

import com.idgindigo.pms.domain.BasePersistenceTest;
import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeToPhoto;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.extranet.RoomTypeToPhotoProvider;

import javax.inject.Inject;

/**
 * @author vomel
 * @since 28.02.14 15:55
 */
public class RoomTypeToPhotoTest extends BasePersistenceTest<RoomTypeToPhoto> {
    @Inject
    private RoomTypeToPhotoProvider provider;

    @Override
    protected EntityProvider<RoomTypeToPhoto> getProvider() {
        return provider;
    }
}
