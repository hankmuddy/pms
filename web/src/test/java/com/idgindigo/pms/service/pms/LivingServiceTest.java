package com.idgindigo.pms.service.pms;

import com.idgindigo.pms.domain.extranet.Season;
import com.idgindigo.pms.domain.extranet.plan.Plan;
import com.idgindigo.pms.domain.extranet.roomtype.BaseRoom;
import com.idgindigo.pms.domain.extranet.service.Living;
import com.idgindigo.pms.repository.extranet.service.LivingRepository;
import com.idgindigo.pms.service.ServiceTest;
import com.idgindigo.pms.service.extranet.LivingService;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.extranet.LivingProvider;
import com.idgindigo.pms.utils.extranet.SeasonProvider;
import com.idgindigo.pms.utils.extranet.VirtualRoomProvider;
import com.idgindigo.pms.utils.extranet.plan.PlanProvider;
import org.joda.time.LocalDate;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.joda.time.DateTimeZone.UTC;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

/**
 * @author valentyn_vakatsiienko
 * @since 1/20/14 4:30 PM
 */
public class LivingServiceTest extends ServiceTest {
    public static final int LIST_SIZE = 5;
    @Inject
    private LivingService livingService;
    @Inject
    private PlanProvider planProvider;
    @Inject
    private SeasonProvider seasonProvider;
    @Inject
    private VirtualRoomProvider virtualRoomProvider;
    @Inject
    private LivingRepository livingRepository;
    @Inject
    private LivingProvider livingProvider;

    @Test
    public void testCreateRoom() {
        //Create some plans and seasons
        final List<Plan> plans = new ArrayList<>();
        for (int i = 0; i < LIST_SIZE; i++) {
            plans.add(planProvider.getPersistentEntity());
        }
        List<Season> seasons = new ArrayList<>();
        for (int i = 0; i < LIST_SIZE; i++) {
            final int index = i;
            seasons.add(seasonProvider.getPersistentEntity(new Visitor<Season>() {
                @Override
                public void visit(Season entity) {
                    entity.setPlan(plans.get(index));
                }
            }));
        }

        //Create virtual room
        BaseRoom room = virtualRoomProvider.getPersistentEntity();
        livingService.handleCreate(room);

        //Livings for each plan and season must have been created
        for (Plan plan : plans) {
            validateLivings(room, plan, livingRepository.findByPlanId(plan.getId()));
        }

        for (Season season : seasons) {
            validateLivings(room, season, livingRepository.findByRoomAndSeason(room, season));
        }
    }

    @Test
    public void testCreatePlan() {
        //create some rooms
        List<BaseRoom> rooms = createVirtualRooms();

        //create plan
        Plan plan = planProvider.getPersistentEntity();
        livingService.handleCreate(plan);

        //Livings for each virtual room must have been created
        for (BaseRoom room : rooms) {
            validateLivings(room, plan, livingRepository.findByRoomId(room.getId()));
        }
    }

    @Test
    public void testCreateSeason() {
        //create some rooms
        List<BaseRoom> rooms = createVirtualRooms();

        //create season
        Season season = seasonProvider.getPersistentEntity();
        livingService.handleCreate(season.getPlan());

        //Change default prices
        for (BaseRoom room : rooms) {
            Living byPlan = livingRepository.findByRoomAndPlan(room, season.getPlan());
            byPlan.setMon(1L);
            byPlan.setTue(2L);
            byPlan.setWed(3L);
            byPlan.setThu(4L);
            byPlan.setFri(5L);
            byPlan.setSat(6L);
            byPlan.setSun(7L);
            livingRepository.save(byPlan);
        }

        livingService.handleCreate(season);

        //Livings for each virtual room with proper prices must have been created
        for (BaseRoom room : rooms) {
            validateLivings(room, season, livingRepository.findByRoomAndSeason(room, season));
        }
    }

    private List<BaseRoom> createVirtualRooms() {
        List<BaseRoom> rooms = new ArrayList<>();
        for (int i = 0; i < LIST_SIZE; i++) {
            rooms.add(virtualRoomProvider.getPersistentEntity());
        }
        return rooms;
    }

    private void validateLivings(BaseRoom room, Plan plan, List<Living> livings) {
        assertEquals(livings.size(), 1);
        Living living = livings.get(0);

        if (plan == null) {
            assertNull(living.getPlan());
        } else {
            assertEquals(living.getPlan(), plan);
        }
        assertEquals(living.getRoom(), room);

        checkPrices(living, room.getDefaultPrice());
    }

    private void validateLivings(BaseRoom room, Season season, Living living) {
        assertNotNull(living);

        if (season == null) {
            assertNull(living.getSeason());
        } else {
            assertEquals(living.getSeason(), season);
        }
        assertEquals(living.getRoom(), room);

        checkPrices(living, livingRepository.findByRoomAndPlan(room, season.getPlan()));
    }

    private void checkPrices(Living target, Living source) {
        assertEquals(target.getMon(), source.getMon());
        assertEquals(target.getTue(), source.getTue());
        assertEquals(target.getWed(), source.getWed());
        assertEquals(target.getThu(), source.getThu());
        assertEquals(target.getFri(), source.getFri());
        assertEquals(target.getSat(), source.getSat());
        assertEquals(target.getSun(), source.getSun());
    }

    private void checkPrices(Living living, Long defaultPrice) {
        assertEquals(living.getMon(), defaultPrice);
        assertEquals(living.getTue(), defaultPrice);
        assertEquals(living.getWed(), defaultPrice);
        assertEquals(living.getThu(), defaultPrice);
        assertEquals(living.getFri(), defaultPrice);
        assertEquals(living.getSat(), defaultPrice);
        assertEquals(living.getSun(), defaultPrice);
    }

    @Test(dataProvider = "testGetForDate")
    public void testGetForDate(BaseRoom room, Plan plan, LocalDate date, Living living) {
        assertEquals(livingService.get(room, plan, date), living);
    }

    @DataProvider(name = "testGetForDate")
    public Object[][] getData_testGetForDate() {
        List<Object[]> result = new ArrayList<>(4);
        final LocalDate seasonStart = LocalDate.now(UTC).plusDays(1);
        final LocalDate seasonEnd = LocalDate.now(UTC).plusDays(2);

        final BaseRoom room = virtualRoomProvider.getPersistentEntity();
        final Plan plan = planProvider.getPersistentEntity();
        final Season season = seasonProvider.getPersistentEntity(new Visitor<Season>() {
            @Override
            public void visit(Season entity) {
                entity.setPlan(plan);
                entity.setStart(seasonStart);
                entity.setUntil(seasonEnd);
            }
        });

        Living forPlan = getLiving(room, plan, null);
        Living forSeason = getLiving(room, null, season);

        result.add(new Object[]{room, plan, seasonStart.minusDays(1), forPlan});
        result.add(new Object[]{room, plan, seasonEnd.plusDays(1), forPlan});
        result.add(new Object[]{room, plan, seasonStart, forSeason});
        result.add(new Object[]{room, plan, seasonEnd, forSeason});
        return result.toArray(new Object[result.size()][]);
    }

    public Living getLiving(final BaseRoom room, final Plan plan, final Season season) {
        return livingProvider.getPersistentEntity(new Visitor<Living>() {
            @Override
            public void visit(Living entity) {
                entity.setPlan(plan);
                entity.setRoom(room);
                entity.setSeason(season);
            }
        });
    }
}