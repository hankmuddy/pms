package com.idgindigo.pms.utils.pms;

import com.idgindigo.pms.domain.pms.Room;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.pms.RoomUseRepository;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.extranet.GenericRoomUseProvider;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 11/19/13 4:46 PM
 */
@Component
public class RoomUseProvider extends GenericRoomUseProvider<RoomUse> {
    @Inject
    private RoomUseRepository repository;
    @Inject
    private RoomProvider roomProvider;

    @Override
    public RoomUse createAndFill() {
        final RoomUse roomUse = super.createAndFill();
        roomUse.setRoom(roomProvider.getPersistentEntity(new Visitor<Room>() {
            @Override
            public void visit(Room entity) {
                entity.setRoomType(roomUse.getBaseRoom().roomType());
            }
        }));
        return roomUse;
    }

    @Override
    protected RoomUse create() {
        return new RoomUse();
    }

    @Override
    public BaseRepository<RoomUse> getRepository() {
        return repository;
    }
}
