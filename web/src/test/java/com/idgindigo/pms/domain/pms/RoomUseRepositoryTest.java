package com.idgindigo.pms.domain.pms;

import com.idgindigo.pms.domain.JpaTests;
import com.idgindigo.pms.repository.pms.RoomUseRepository;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.pms.RoomUseProvider;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 6/18/14 5:10 PM
 */
public class RoomUseRepositoryTest extends JpaTests {
    @Inject
    private RoomUseRepository repository;
    @Inject
    private RoomUseProvider provider;

    @Test(dataProvider = "testOverlapValidation")
    public void testOverlapValidation(Room room, LocalDate start, LocalDate end, boolean isBooked) {
        Assert.assertEquals(repository.isBooked(room, start, end).booleanValue(), isBooked);
    }

    @DataProvider(name = "testOverlapValidation")
    public Object[][] getData_testOverlapValidation() {
        List<Object[]> result = new ArrayList<>(7);

        //Create room use to test overlapping on
        final LocalDate start = LocalDate.now(DateTimeZone.UTC);
        final LocalDate end = start.plusDays(5);
        RoomUse roomUse = provider.getPersistentEntity(new Visitor<RoomUse>() {
            @Override
            public void visit(RoomUse entity) {
                entity.setStartDate(start);
                entity.setEndDate(end);
            }
        });
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

}
