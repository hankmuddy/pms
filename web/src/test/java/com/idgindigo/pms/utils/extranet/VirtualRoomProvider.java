package com.idgindigo.pms.utils.extranet;

import com.idgindigo.pms.domain.extranet.roomtype.VirtualRoom;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.extranet.BaseRoomRepository;
import com.idgindigo.pms.repository.extranet.VirtualRoomRepository;
import com.idgindigo.pms.utils.ApprovableEntityProvider;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 1/15/14 4:35 PM
 */
@Component
public class VirtualRoomProvider extends ApprovableEntityProvider<VirtualRoom> {
    @Inject
    private VirtualRoomRepository repository;
    @Inject
    private BaseRoomRepository baseRoomRepository;
    @Inject
    private RoomTypeProvider roomTypeProvider;

    @Override
    public VirtualRoom createAndFill() {
        VirtualRoom room = new VirtualRoom();
        room.setRoomType(roomTypeProvider.getPersistentEntity());
        room.setAdults(2);
        room.setDefaultPrice(room.roomType().getDefaultPrice());
        room.setName(randomAlphabeticString());
        do {
            room.setShortname(randomAlphabeticString(4));
        } while (baseRoomRepository.findByShortname(room.getShortname()) != null);
        return room;
    }

    @Override
    public BaseRepository<VirtualRoom> getRepository() {
        return repository;
    }
}
