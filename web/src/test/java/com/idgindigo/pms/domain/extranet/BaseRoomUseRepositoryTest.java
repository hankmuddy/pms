package com.idgindigo.pms.domain.extranet;

import com.idgindigo.pms.domain.JpaTests;
import com.idgindigo.pms.domain.User;
import com.idgindigo.pms.domain.pms.Repair;
import com.idgindigo.pms.domain.pms.Room;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.repository.extranet.roomuse.BaseRoomUseRepository;
import com.idgindigo.pms.repository.pms.RoomUseRepository;
import com.idgindigo.pms.restutils.PageWithTotalCount;
import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.pms.RepairProvider;
import com.idgindigo.pms.utils.pms.RoomUseProvider;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.data.domain.Pageable;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * @author valentyn_vakatsiienko
 * @since 12/31/13 9:57 AM
 */
public class BaseRoomUseRepositoryTest extends JpaTests {
    private final LocalDate START_DATE = LocalDate.now(DateTimeZone.forID("UTC"));
    private final LocalDate END_DATE = START_DATE.plusDays(5);
    private final Pageable PAGEABLE = new PageWithTotalCount(0, 10000);

    @Inject
    private RoomUseProvider roomUseProvider;
    @Inject
    private RepairProvider repairProvider;
    @Inject
    private BaseRoomUseRepository repository;
    @Inject
    private RoomUseRepository roomUseRepository;

    @Test(dataProvider = "testGetWithoutRefuses")
    public void testGetWithoutRefuses(Collection<BaseRoomUse> expected, Collection<BaseRoomUse> ignored) {
        List<BaseRoomUse> actual = repository.getForDatesAndStatuses(START_DATE, END_DATE, BaseGroupRoomUse.NOT_REFUSED_STATUSES, PAGEABLE);
        for (BaseRoomUse roomUse : ignored) {
            assertFalse(actual.contains(roomUse));
        }
        assertTrue(actual.containsAll(expected), "actual=" + actual + ", expected=" + expected);
    }

    @DataProvider(name = "testGetWithoutRefuses")
    public Object[][] getData_testGetWithoutRefuses() {
        List<Object[]> result = new ArrayList<>(8);
        RoomUse refuse = create(START_DATE.minusDays(1), START_DATE.plusDays(1));
        User currentUser = SecurityUtils.getCurrentUser();
        roomUseRepository.setStatus(RoomUse.Status.REFUSE, refuse.getId(), currentUser, LocalDateTime.now());

        //Start overlap
        List<BaseRoomUse> startOverlap = Collections.singletonList((BaseRoomUse) create(START_DATE.minusDays(1), START_DATE.plusDays(1)));
        result.add(new Object[]{startOverlap, Collections.singletonList(refuse)});

        startOverlap = Collections.singletonList((BaseRoomUse) createRepair(START_DATE.minusDays(1), START_DATE.plusDays(1)));
        result.add(new Object[]{startOverlap, Collections.singletonList(refuse)});

        //End overlap
        refuse = create(END_DATE.minusDays(1), END_DATE.plusDays(1));
        roomUseRepository.setStatus(RoomUse.Status.REFUSE, refuse.getId(), currentUser, LocalDateTime.now());

        List<BaseRoomUse> endOverlap = Collections.singletonList((BaseRoomUse) create(END_DATE.minusDays(1), END_DATE.plusDays(1)));
        result.add(new Object[]{endOverlap, Collections.singletonList(refuse)});

        endOverlap = Collections.singletonList((BaseRoomUse) createRepair(END_DATE.minusDays(1), END_DATE.plusDays(1)));
        result.add(new Object[]{endOverlap, Collections.singletonList(refuse)});

        //Full overlap
        refuse = create(START_DATE.minusDays(1), END_DATE.plusDays(1));
        roomUseRepository.setStatus(RoomUse.Status.REFUSE, refuse.getId(), currentUser, LocalDateTime.now());

        List<BaseRoomUse> fullOverlap = Collections.singletonList((BaseRoomUse) create(START_DATE.minusDays(1), END_DATE.plusDays(1)));
        result.add(new Object[]{fullOverlap, Collections.singletonList(refuse)});

        fullOverlap = Collections.singletonList((BaseRoomUse) createRepair(END_DATE.minusDays(1), END_DATE.plusDays(1)));
        result.add(new Object[]{fullOverlap, Collections.singletonList(refuse)});

        return result.toArray(new Object[result.size()][]);
    }

    @Test(dataProvider = "testOverlapValidation")
    public void testOverlapValidation(Room room, LocalDate start, LocalDate end, boolean isBooked) {
        Assert.assertEquals(roomUseRepository.isBooked(room, start, end).booleanValue(), isBooked);
    }

    @DataProvider(name = "testOverlapValidation")
    public Object[][] getData_testOverlapValidation() {
        List<Object[]> result = new ArrayList<>(7);

        //Create room use to test overlapping on
        LocalDate start = LocalDate.now(DateTimeZone.UTC);
        LocalDate end = start.plusDays(5);
        RoomUse roomUse = create(start, end);
        Room room = roomUse.getRoom();

        //Test it is booked now
        result.add(new Object[]{room, start, end, true});
        //Test overlapping from right
        result.add(new Object[]{room, end.minusDays(1), end, true});
        result.add(new Object[]{room, end, end.plusDays(1), false});
        //Test overlapping from left
        result.add(new Object[]{room, start, start.plusDays(1), true});
        result.add(new Object[]{room, start.minusDays(1), start, false});
        //Test overlapping inside
        result.add(new Object[]{room, start.plusDays(1), end.minusDays(1), true});
        //Test overlapping outside
        result.add(new Object[]{room, start.minusDays(1), end.plusDays(1), true});

        return result.toArray(new Object[result.size()][]);
    }

    private RoomUse create(LocalDate start, LocalDate end) {
        return createWithProvider(start, end, roomUseProvider);
    }

    private Repair createRepair(LocalDate start, LocalDate end) {
        return createWithProvider(start, end, repairProvider);
    }

    private <T extends BaseRoomUse> T createWithProvider(final LocalDate start, final LocalDate end, EntityProvider<T> provider) {
        return provider.getPersistentEntity(new Visitor<T>() {
            @Override
            public void visit(T entity) {
                entity.setStartDate(start);
                entity.setEndDate(end);
            }
        });
    }

}

