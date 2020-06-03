package com.idgindigo.pms.service.pms;

import com.idgindigo.pms.domain.pms.Room;
import com.idgindigo.pms.repository.pms.RoomRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 6/6/14 10:08 AM
 */
@Service
public class RoomService {
    @Inject
    private RoomRepository repository;

    public void setPosition(Room room, int pos) {
        int oldPos = room.getPosition();
        if (pos > oldPos) {
            changePositions(oldPos, pos, false);
        } else if (pos < oldPos) {
            changePositions(pos, oldPos, true);
        }
        repository.setPosition(room, pos);
    }

    private void changePositions(int bot, int top, boolean up) {
        List<Room> rooms = repository.findByPositionInRange(bot, top);
        int pos = bot + (up ? 1 : -1);
        for (Room room : rooms) {
            repository.setPosition(room, pos);
            pos++;
        }
    }
}
