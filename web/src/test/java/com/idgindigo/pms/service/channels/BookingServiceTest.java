package com.idgindigo.pms.service.channels;

import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.domain.pms.Room;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.repository.pms.RoomUseRepository;
import com.idgindigo.pms.service.ServiceTest;
import com.idgindigo.pms.service.pms.RoomUseService;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.extranet.CustomerGroupProvider;
import com.idgindigo.pms.utils.pms.RoomProvider;
import com.idgindigo.pms.utils.pms.RoomUseProvider;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static com.idgindigo.pms.utils.EntityProvider.randomAlphabeticString;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * @author valentyn_vakatsiienko
 * @since 4/3/14 12:25 PM
 */
public class BookingServiceTest extends ServiceTest {
    @Inject
    private BookingService bookingService;
    @Inject
    private RoomUseProvider roomUseProvider;
    @Inject
    private RoomProvider roomProvider;
    @Inject
    private CustomerGroupProvider customerGroupProvider;
    @Inject
    private RoomUseRepository roomUseRepository;

    @Test(dataProvider = "testMoveBooking")
    public void testMoveBooking(final RoomUseService.RoomUseWithOverrides ruWithOverrides, String rcode, int totalBookings) {
        List<RoomUse> created = bookingService.handleWithoutRoom(ruWithOverrides, false);
        List<RoomUse> byRcode = roomUseRepository.findByRcode(rcode);

        assertEquals(created.size(), totalBookings);
        assertTrue(created.containsAll(byRcode));
        assertTrue(byRcode.containsAll(created));
    }

    @DataProvider(name = "testMoveBooking")
    public Object[][] getData_testMoveBooking() {
        List<Object[]> result = new ArrayList<>(8);
        String rcode = randomAlphabeticString();
        result.add(new Object[]{getRUWithOverrides(rcode, new OverlapManager() {
            @Override
            public void createOverlaps(RoomUse roomUse) {
                final Room room = roomUse.getRoom();
                final RoomType roomType = room.getRoomType();
                Room room2 = roomProvider.getPersistentEntity(new Visitor<Room>() {
                    @Override
                    public void visit(Room entity) {
                        entity.setRoomType(roomType);
                    }
                });
                LocalDate startDate = roomUse.getStartDate();
                LocalDate endDate = roomUse.getEndDate();
                createRoomUse(startDate.plusDays(7), endDate, room);
                createRoomUse(startDate, startDate.plusDays(5), room2);
            }
        }), rcode, 2});
        return result.toArray(new Object[result.size()][]);
    }

    @Test
    public void testMovePartial() {
        String rcode = randomAlphabeticString();
        RoomUseService.RoomUseWithOverrides toCreate = getRUWithOverrides(rcode, new OverlapManager() {
            @Override
            public void createOverlaps(RoomUse roomUse) {
                createRoomUse(roomUse.getStartDate().plusDays(7), roomUse.getEndDate(), roomUse.getRoom());
            }
        });
        List<RoomUse> created = bookingService.handleWithoutRoom(toCreate, false);

        assertTrue(created.isEmpty());
        assertTrue(roomUseRepository.findByRcode(rcode).isEmpty());
    }

    private RoomUseService.RoomUseWithOverrides getRUWithOverrides(final String rcode, OverlapManager overlapManager) {
        final LocalDate startDate = LocalDate.now(DateTimeZone.UTC);
        final LocalDate endDate = startDate.plusDays(10);

        final RoomUse roomUse = roomUseProvider.getTransientEntity(new Visitor<RoomUse>() {
            @Override
            public void visit(RoomUse entity) {
                entity.setCustomerGroup(customerGroupProvider.getTransientEntity());
                entity.setRcode(rcode);
                entity.setStartDate(startDate);
                entity.setEndDate(endDate);
            }
        });
        overlapManager.createOverlaps(roomUse);
        roomUse.setRoom(null);
        return new RoomUseService.RoomUseWithOverrides(roomUse);
    }

    private void createRoomUse(final LocalDate startDate, final LocalDate endDate, final Room room) {
        roomUseProvider.getPersistentEntity(new Visitor<RoomUse>() {
            @Override
            public void visit(RoomUse entity) {
                entity.setStartDate(startDate);
                entity.setEndDate(endDate);
                entity.setRoom(room);
            }
        });
    }


    private interface OverlapManager {
        void createOverlaps(RoomUse roomUse);
    }
}
