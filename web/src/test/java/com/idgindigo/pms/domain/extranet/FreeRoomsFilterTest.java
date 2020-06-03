package com.idgindigo.pms.domain.extranet;

import com.idgindigo.pms.domain.JpaTests;
import com.idgindigo.pms.domain.pms.Room;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.repository.pms.RoomUseRepository;
import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.pms.RoomProvider;
import com.idgindigo.pms.utils.pms.RoomUseProvider;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * @author valentyn_vakatsiienko
 * @since 1/28/14 12:13 PM
 */
public class FreeRoomsFilterTest extends JpaTests {
    @Inject
    private RoomProvider roomProvider;
    @Inject
    private RoomUseProvider roomUseProvider;
    @Inject
    private RoomUseRepository roomUseRepository;

    @Test
    public void testFilter() {
        final Room roomA = roomProvider.getPersistentEntity();
        Room roomB = roomProvider.getPersistentEntity();

        final LocalDate start = LocalDate.now(DateTimeZone.forID("UTC"));
        final LocalDate end = start.plusDays(5);

        //Get fresh rooms
        List<Room> freeRooms = roomUseRepository.getFreeRooms(start, end);
        assertTrue(freeRooms.contains(roomA));
        assertTrue(freeRooms.contains(roomB));

        //Get by type
        freeRooms = roomUseRepository.getFreeRooms(roomA.getRoomType().getId(), start, end);
        assertTrue(freeRooms.contains(roomA));
        assertFalse(freeRooms.contains(roomB));
        freeRooms = roomUseRepository.getFreeRooms(roomB.getRoomType().getId(), start, end);
        assertFalse(freeRooms.contains(roomA));
        assertTrue(freeRooms.contains(roomB));

        //Get with refuse
        RoomUse roomUse = createRoomUse(roomA, start, end);
        roomUseRepository.setStatus(BaseGroupRoomUse.Status.REFUSE, roomUse.getId(), SecurityUtils.getCurrentUser(), LocalDateTime.now());
        freeRooms = roomUseRepository.getFreeRooms(start, end);
        assertTrue(freeRooms.contains(roomA));
        assertTrue(freeRooms.contains(roomB));

        //Book a number and check filter doesnt return it
        roomUse = createRoomUse(roomA, start, end);
        freeRooms = roomUseRepository.getFreeRooms(start, end);
        assertFalse(freeRooms.contains(roomA));
        assertTrue(freeRooms.contains(roomB));
        roomUseRepository.delete(roomUse);

        //Book a number so, that the booking ends on the filter start date
        roomUse = createRoomUse(roomA, start.minusDays(5), start);
        freeRooms = roomUseRepository.getFreeRooms(start, end);
        assertTrue(freeRooms.contains(roomA));
        assertTrue(freeRooms.contains(roomB));
        roomUseRepository.delete(roomUse);

        //Book a number so, that the booking starts on the filter end date
        roomUse = createRoomUse(roomA, end, end.plusDays(5));
        freeRooms = roomUseRepository.getFreeRooms(start, end);
        assertTrue(freeRooms.contains(roomA));
        assertTrue(freeRooms.contains(roomB));
        roomUseRepository.delete(roomUse);

        //Book a number so, that the booking spans over filter period
        roomUse = createRoomUse(roomA, start.minusDays(1), end.plusDays(1));
        freeRooms = roomUseRepository.getFreeRooms(start, end);
        assertFalse(freeRooms.contains(roomA));
        assertTrue(freeRooms.contains(roomB));
        roomUseRepository.delete(roomUse);
    }

    private RoomUse createRoomUse(final Room roomA, final LocalDate start, final LocalDate end) {
        return roomUseProvider.getPersistentEntity(new Visitor<RoomUse>() {
            @Override
            public void visit(RoomUse entity) {
                entity.setStartDate(start);
                entity.setEndDate(end);
                entity.setRoom(roomA);
            }
        });
    }
}
