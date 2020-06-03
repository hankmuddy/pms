package com.idgindigo.pms.utils.extranet;

import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeToFacility;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.extranet.RoomTypeToFacilityRepository;
import com.idgindigo.pms.utils.EntityProvider;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author vomel
 * @since 25.02.14 00:31
 */
@Component
public class RoomTypeToFacilityProvider extends EntityProvider<RoomTypeToFacility> {

    @Inject
    private RoomTypeToFacilityRepository repository;
    @Inject
    private RoomTypeProvider roomTypeProvider;
    @Inject
    private RoomTypeFacilityProvider rtfProvider;

    @Override
    public RoomTypeToFacility createAndFill() {
        RoomTypeToFacility rttf = new RoomTypeToFacility(roomTypeProvider.getFirst(), rtfProvider.getPersistentEntity());
        return rttf;
    }

    @Override
    public BaseRepository<RoomTypeToFacility> getRepository() {
        return repository;
    }
}
