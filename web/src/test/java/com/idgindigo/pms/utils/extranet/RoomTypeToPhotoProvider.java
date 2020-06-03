package com.idgindigo.pms.utils.extranet;

import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeToPhoto;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.extranet.RoomTypeToPhotoRepository;
import com.idgindigo.pms.utils.EntityProvider;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author vomel
 * @since 28.02.14 15:56
 */
@Component
public class RoomTypeToPhotoProvider extends EntityProvider<RoomTypeToPhoto> {
    @Inject
    private RoomTypeToPhotoRepository repository;
    @Inject
    private RoomTypeProvider roomTypeProvider;
    @Inject
    private PhotoProvider photoProvider;

    @Override
    public RoomTypeToPhoto createAndFill() {
        return new RoomTypeToPhoto(roomTypeProvider.getFirst(), photoProvider.getPersistentEntity());
    }

    @Override
    public BaseRepository<RoomTypeToPhoto> getRepository() {
        return repository;
    }
}
