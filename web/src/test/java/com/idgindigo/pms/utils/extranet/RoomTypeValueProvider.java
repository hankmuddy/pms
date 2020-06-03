package com.idgindigo.pms.utils.extranet;

import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeValue;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.extranet.rate.RoomTypeValueRepository;
import com.idgindigo.pms.utils.EntityProvider;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 11/11/13 5:11 PM
 */
@Component
public class RoomTypeValueProvider extends EntityProvider<RoomTypeValue> {
    @Inject
    private RoomTypeValueRepository repository;
    @Inject
    private RoomTypeProvider roomTypeProvider;

    @Override
    public RoomTypeValue createAndFill() {
        RoomTypeValue roomTypeValue = new RoomTypeValue();
        roomTypeValue.setRoomType(roomTypeProvider.getPersistentEntity());
        roomTypeValue.setDate(LocalDate.now(DateTimeZone.forID("UTC")));
        roomTypeValue.setAdultBedPrice(randomPositiveLong());
        roomTypeValue.setRoomsAvailable(roomTypeValue.getRoomType().getOtaRooms());
        return roomTypeValue;
    }

    @Override
    public BaseRepository<RoomTypeValue> getRepository() {
        return repository;
    }
}
