package com.idgindigo.pms.service.channels;

import com.idgindigo.pms.domain.pms.Room;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.repository.extranet.roomuse.BaseRoomUseRepository;
import com.idgindigo.pms.repository.pms.RoomUseRepository;
import com.idgindigo.pms.service.pms.RoomUseService;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.idgindigo.pms.service.pms.RoomUseService.RoomUseWithOverrides;

/**
 * @author valentyn_vakatsiienko
 * @since 4/2/14 4:28 PM
 */
@Service
public class BookingService {
    @Inject
    private RoomUseService roomUseService;
    @Inject
    private BaseRoomUseRepository baseRoomUseRepository;
    @Inject
    private RoomUseRepository roomUseRepository;

    public List<RoomUse> handleWithoutRoom(RoomUseWithOverrides ruWithOverrides, boolean updateChannels) {
        RoomUse roomUse = ruWithOverrides.getRoomUse();
        List<RoomUse> res = new ArrayList<>();

        LocalDate terminalDate = roomUse.getEndDate();
        RoomSearchResult result = findPartial(roomUse);
        if (result == null) {
            return Collections.emptyList();
        }
        LocalDate endDate = result.getEndDate();
        roomUse.setEndDate(endDate);
        roomUse.setRoom(result.getRoom());
//        roomUse = roomUseService.create(roomUse, ruWithOverrides.getPrices(), updateChannels);
        res.add(roomUse);

        RoomUse toMove = roomUse;
        while (endDate.isBefore(terminalDate)) {
            RoomUse moved = new RoomUse();
            BeanUtils.copyProperties(toMove, moved, "id", "startDate", "endDate", "total", "totalPaid");
            moved.setStartDate(endDate);
            moved.setEndDate(terminalDate);
            result = findPartial(moved);
            if (result == null) {
//                return res;
                break;
            }
            moved.setRoom(result.getRoom());
            moved.setEndDate(result.getEndDate());
            moved.setMovedFrom(toMove);

//            toMove = roomUseService.create(moved, ruWithOverrides.getPrices(), updateChannels);
            toMove = moved;
            res.add(toMove);
            endDate = result.getEndDate();
        }

        if (!endDate.equals(terminalDate)) {
            return Collections.emptyList();
        }

        RoomUse prev = null;
        for (RoomUse toCreate : res) {
            if (prev != null) {
                BeanUtils.copyProperties(prev, toCreate, "id", "startDate", "endDate", "total", "totalPaid", "room", "movedFrom");
            }
            prev = roomUseService.create(toCreate, ruWithOverrides.getPrices(), updateChannels);
        }

        return res;
    }

    private RoomSearchResult findPartial(RoomUse roomUse) {
        return tryBinary(roomUse, roomUse.getStartDate(), roomUse.getEndDate(), false, new RoomSearchResult());
    }

    private RoomSearchResult tryBinary(RoomUse roomUse, LocalDate lower, LocalDate upper, boolean found, RoomSearchResult result) {
        LocalDate endDate = roomUse.getEndDate();
        LocalDate startDate = roomUse.getStartDate();

        List<Room> freeRooms = roomUseRepository.getFreeRooms(roomUse.getBaseRoom().roomType().getId(), startDate, upper);
        if (!freeRooms.isEmpty()) {
            result.setRoom(freeRooms.get(0));
            result.setEndDate(upper);
            if (found) {
                return tryLinear(roomUse, upper, endDate, result);
            } else {
                LocalDate newUpper = upper.plusDays(Days.daysBetween(upper, endDate).getDays() / 2);
                if (!upper.equals(newUpper)) {
                    lower = upper;
                    upper = newUpper;
                    found = true;
                } else {
                    return result;
                }
            }
        } else {
            if (found) {
                return tryLinear(roomUse, lower, upper, result);
            } else {
                LocalDate newUpper = lower.plusDays(Days.daysBetween(lower, upper).getDays() / 2);
                if (lower.equals(newUpper)) {
                    return null;
                }
                upper = newUpper;
            }
        }
        return tryBinary(roomUse, lower, upper, found, result);
    }

    private RoomSearchResult tryLinear(RoomUse roomUse, LocalDate lower, LocalDate upper, RoomSearchResult result) {
        for (LocalDate now = lower.plusDays(1); !now.isAfter(upper); now = now.plusDays(1)) {
            List<Room> freeRooms = roomUseRepository.getFreeRooms(roomUse.getBaseRoom().roomType().getId(), roomUse.getStartDate(), upper);
            if (freeRooms.isEmpty()) {
                return result;
            } else {
                result.setRoom(freeRooms.get(0));
                result.setEndDate(now);
            }
        }
        return result;
    }

    @Getter
    @Setter
    private static class RoomSearchResult {
        private Room room;
        private LocalDate endDate;
    }
}
