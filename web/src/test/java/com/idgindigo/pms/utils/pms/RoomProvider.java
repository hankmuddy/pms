package com.idgindigo.pms.utils.pms;

import com.idgindigo.pms.domain.pms.Room;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.pms.RoomRepository;
import com.idgindigo.pms.utils.ApprovableEntityProvider;
import com.idgindigo.pms.utils.extranet.RoomTypeProvider;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author vomel
 * @since 01.11.13 17:09
 */
@Component
public class RoomProvider extends ApprovableEntityProvider<Room> {
    @Inject
    private AccommodationProvider accommodationProvider;
    @Inject
    private RoomTypeProvider roomTypeProvider;
    @Inject
    private RoomRepository repository;

    @Override
    public Room createAndFill() {
        Room room = new Room();
        room.setNumber(randomString());
        room.setAccommodation(accommodationProvider.getPersistentEntity());
        room.setRoomType(roomTypeProvider.getPersistentEntity());
        room.setPosition(randomInt());
        return room;
    }

    @Override
    public BaseRepository<Room> getRepository() {
        return repository;
    }
}
