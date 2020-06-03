package com.idgindigo.pms.utils.extranet;

import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.extranet.BaseRoomRepository;
import com.idgindigo.pms.repository.extranet.RoomTypeRepository;
import com.idgindigo.pms.utils.ApprovableEntityProvider;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author vomel
 * @since 01.11.13 15:34
 */
@Component
public class RoomTypeProvider extends ApprovableEntityProvider<RoomType> {
    public static final int BEDS = 2;
    @Inject
    private BaseRoomRepository baseRoomRepository;
    @Inject
    private RoomTypeRepository repository;

    @Override
    public RoomType createAndFill() {
        RoomType roomType = new RoomType();
        roomType.setName("RoomType: " + randomString());
        roomType.setOtaRooms(randomPositiveInt() + 1);
        roomType.setDefaultPrice(randomPositiveLong());
        roomType.setAdultBedPrice(randomPositiveLong());
        do {
            roomType.setShortname(randomAlphabeticString(4));
        } while (baseRoomRepository.findByShortname(roomType.getShortname()) != null);
        roomType.setAdults(BEDS);
        return roomType;
    }

    @Override
    public BaseRepository<RoomType> getRepository() {
        return repository;
    }
}