package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.domain.extranet.plan.Plan;
import com.idgindigo.pms.domain.extranet.roomtype.BaseRoom;
import com.idgindigo.pms.domain.extranet.roomtype.VirtualRoom;
import com.idgindigo.pms.domain.extranet.service.Living;
import com.idgindigo.pms.domain.pms.BaseServiceUse;
import com.idgindigo.pms.domain.pms.Bill;
import com.idgindigo.pms.domain.pms.LivingUse;
import com.idgindigo.pms.domain.pms.Room;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.price.LivingPriceResolver;
import com.idgindigo.pms.repository.pms.BaseServiceUseRepository;
import com.idgindigo.pms.repository.pms.BillRepository;
import com.idgindigo.pms.repository.pms.LivingUseRepository;
import com.idgindigo.pms.repository.pms.RoomUseRepository;
import com.idgindigo.pms.service.admin.SettingsService;
import com.idgindigo.pms.service.extranet.LivingService;
import com.idgindigo.pms.utils.RoomUseUtils;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.extranet.LivingProvider;
import com.idgindigo.pms.utils.extranet.VirtualRoomProvider;
import com.idgindigo.pms.utils.pms.RoomProvider;
import com.idgindigo.pms.utils.pms.RoomUseProvider;
import com.idgindigo.pms.web.controller.InMemoryDbWebTest;
import mockit.Mock;
import mockit.MockUp;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.http.MediaType;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Status.BOOKING_WARRANTY;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Status.LIVING;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Status.REFUSE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * @author valentyn_vakatsiienko
 * @since 12/12/13 10:27 AM
 */
public class MoveTest extends InMemoryDbWebTest {
    public static final int TOTAL_DAYS = 5;
    public static final int DAYS_TO_ADD = TOTAL_DAYS - 1;
    final int DAYS_BEFORE_MOVE = 2;

    @Inject
    private RoomUseProvider provider;
    @Inject
    private VirtualRoomProvider virtualRoomProvider;
    @Inject
    private RoomProvider roomProvider;
    @Inject
    private BillRepository billRepository;
    @Inject
    private LivingService livingService;
    @Inject
    private LivingProvider livingProvider;
    @Inject
    private RoomUseRepository roomUseRepository;
    @Inject
    private LivingPriceResolver priceResolver;
    @Inject
    private BaseServiceUseRepository baseServiceUseRepository;
    @Inject
    private SettingsService settingsService;
    @Inject
    private LivingUseRepository livingUseRepository;
    @Inject
    private RoomUseUtils roomUseUtils;

    @Test
    public void testUpgradeOnCheckInDate() throws Exception {
        RoomUse roomUse = create();
        Long id = roomUse.getId();
        RoomUseController.MoveDto moveDto = createMoveDto(roomUse.getStartDate(), true, null, roomUse.getPlan(), null);

        long totalBefore = getCurrentTotal(roomUse);
        move(id, moveDto);
        roomUse = provider.getRepository().findOne(id);
        MoveResult result = new MoveResult(roomUse).invoke();
        //RoomUse management
        assertEquals(result.getRefuse(), roomUse);
        assertEquals(result.getMove().getTotal(), totalBefore);
    }

    @Test(dataProvider = "testMove")
    public void testMove(RoomUse roomUse, RoomUseController.MoveDto moveDto, boolean moveToday) throws Exception {
        assertTrue(moveDto.getSinceDate().isAfter(roomUse.getStartDate()));
        boolean upgrade = moveDto.isUpgrade();

        initDate(moveToday);

        move(roomUse.getId(), moveDto);
        //Check that room use is divided properly
        RoomUse oldRoomUseAfterMove = getOldRoomUseAfterMove(roomUse, moveDto);
        assertEquals(oldRoomUseAfterMove.getEndDate(), moveDto.getSinceDate());
        //Find created room use (and refuse)
        MoveResult moveResult = new MoveResult(oldRoomUseAfterMove).invoke();
        RoomUse move = moveResult.getMove();
        RoomUse refuse = moveResult.getRefuse();
        //Check new room use is created properly
        checkGenericServiceUsesManagement(move);
        assertEquals(roomUse.getCustomerGroup(), move.getCustomerGroup());
        assertEquals(move.getStatus(), moveToday ? LIVING : BOOKING_WARRANTY);
        if (upgrade) {
            assertEquals(move.getPlan(), roomUse.getPlan());
            checkUpgradeServiceUsesManagement(move, refuse);
        } else {
            assertEquals(move.getPlan(), moveDto.getPlan());
            checkNonUpgradeServiceUsesManagement(move, refuse);
        }
    }

    private void checkNonUpgradeServiceUsesManagement(RoomUse move, RoomUse refuse) {
        List<BaseServiceUse> moveServiceUses = baseServiceUseRepository.findByRoomUseAndAfterDateIncluding(move, move.getStartDate());
        Bill bill = moveServiceUses.get(0).getBill();
        long totalAfterMove = 0;
        for (BaseServiceUse su : moveServiceUses) {
            assertEquals(su.getTotal(), priceResolver.getPrice(livingService.get(move.getBaseRoom(), move.getPlan(), su.getDate()), su.getDate()));
            totalAfterMove += su.getTotal();
        }
        assertEquals(bill.getTotal().longValue(), totalAfterMove);
        //Check refuse is created properly
        checkGenericServiceUsesManagement(refuse);
    }

    private void checkUpgradeServiceUsesManagement(RoomUse move, RoomUse refuse) {
        List<LivingUse> byRefuse = livingUseRepository.findByBillRoomUse(refuse);
        List<LivingUse> byMove = livingUseRepository.findByBillRoomUse(move);
        assertEquals(byRefuse.size(), byMove.size());
        for (LivingUse moved : byMove) {
            int found = 0;
            for (LivingUse refused : byRefuse) {
                if (refused.getDate().equals(moved.getDate())) {
                    found++;
                    assertEquals(refused.getTotal(), moved.getTotal());
                }
            }
            assertTrue(found == 1);
        }
    }

    @DataProvider(name = "testMove")
    public Object[][] getData_testUpgrade() throws Exception {
        List<Object[]> result = new ArrayList<>(4);
        addData(result, true, false, null, null);
        addData(result, true, true, null, null);

        final Living living = livingProvider.getPersistentEntity();

        Visitor<Room> roomVisitor = new Visitor<Room>() {
            @Override
            public void visit(Room entity) {
                entity.setRoomType(living.getRoom().roomType());
            }
        };
        addData(result, false, false, roomProvider.getPersistentEntity(roomVisitor), living);
        addData(result, false, true, roomProvider.getPersistentEntity(roomVisitor), living);

        return result.toArray(new Object[result.size()][]);
    }

    private void addData(List<Object[]> result, boolean upgrade, boolean moveToday, Room room, Living living) throws Exception {
        RoomUse roomUse = create();
        RoomUseController.MoveDto moveDto = createMoveDto(roomUse.getStartDate().plusDays(DAYS_BEFORE_MOVE), upgrade, room, living != null ? living.getPlan() : roomUse.getPlan(), living != null ? living.getRoom() : null);
        result.add(new Object[]{roomUse, moveDto, moveToday});
    }

    private RoomUse getOldRoomUseAfterMove(RoomUse roomUse, RoomUseController.MoveDto moveDto) {
        RoomUse roomUseAfterMove = provider.getRepository().findOne(roomUse.getId());
        assertEquals(roomUseAfterMove.getEndDate(), moveDto.getSinceDate());
        assertEquals(roomUse.getCustomerGroup(), roomUseAfterMove.getCustomerGroup());
        assertEquals(roomUse.getPlan(), roomUseAfterMove.getPlan());
        List<BaseServiceUse> serviceUsesInOldRoomUse = baseServiceUseRepository.findByRoomUseAndAfterDateIncluding(roomUse, roomUse.getStartDate());
        assertTrue(serviceUsesInOldRoomUse.size() == Days.daysBetween(roomUse.getStartDate(), moveDto.getSinceDate()).getDays());
        return roomUseAfterMove;
    }

    private long getCurrentTotal(RoomUse roomUse) {
        long total = 0;
        for (Bill b : billRepository.findByRoomUse(roomUse)) {
            total += b.getTotal();
        }
        return total;
    }

    private RoomUseController.MoveDto createMoveDto(LocalDate sinceDate, boolean upgrade, Room room, final Plan plan, BaseRoom baseRoom) {
        final RoomUseController.MoveDto moveDto = new RoomUseController.MoveDto();
        moveDto.setRoom(room != null ? room : roomProvider.getPersistentEntity());
        moveDto.setPlan(plan != null ? plan : null);
        moveDto.setBaseRoom(baseRoom != null ? baseRoom : livingProvider.getPersistentEntity(new Visitor<Living>() {
            @Override
            public void visit(Living entity) {
                entity.setRoom(virtualRoomProvider.getPersistentEntity(new Visitor<VirtualRoom>() {
                    @Override
                    public void visit(VirtualRoom entity) {
                        entity.setRoomType(moveDto.getRoom().getRoomType());
                    }
                }));
                entity.setPlan(plan);
            }
        }).getRoom());
        moveDto.setSinceDate(sinceDate);
        moveDto.setUpgrade(upgrade);
        return moveDto;
    }

    private RoomUse create() throws Exception {
        RoomUse roomUse = provider.getTransientEntity(new Visitor<RoomUse>() {
            @Override
            public void visit(RoomUse entity) {
                entity.setStartDate(settingsService.getHotelDate());
                entity.setEndDate(entity.getStartDate().plusDays(DAYS_TO_ADD));
            }
        });
        roomUse = convertResponseWithSingleObject(objectMapper, mvc.perform(preparePost(roomUse)).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), RoomUse.class);
        roomUseUtils.populate(roomUse.getCustomerGroup(), roomUse);
        mvc.perform(preparePost(roomUse.getId() + "/confirmed")).andExpect(status().isOk());
        RoomUseController.CheckDto dto = new RoomUseController.CheckDto(settingsService.getHotelTime());
        mvc.perform(preparePost(dto, roomUse.getId() + "/checkedIn")).andExpect(status().isOk());
        return roomUse;
    }

    private void move(Long id, RoomUseController.MoveDto moveDto) throws Exception {
        mvc.perform(preparePost(id + "/moved")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(moveDto)))
                .andExpect(status().isOk());
    }

    private void checkGenericServiceUsesManagement(RoomUse roomUse) {
        List<BaseServiceUse> serviceUsesInRefuse = baseServiceUseRepository.findByRoomUseAndAfterDateIncluding(roomUse, roomUse.getStartDate());
        assertTrue(serviceUsesInRefuse.size() == Days.daysBetween(roomUse.getStartDate(), roomUse.getEndDate()).getDays());
    }

    private void initDate(boolean moveToday) {
        if (moveToday) {
            new MockUp<LocalDate>() {
                @Mock
                public LocalDate now() {
                    return new LocalDate().plusDays(DAYS_BEFORE_MOVE);
                }

                @Mock
                public LocalDate now(DateTimeZone timeZone) {
                    return new LocalDate(timeZone).plusDays(DAYS_BEFORE_MOVE);
                }
            };
        }
    }

    @Override
    protected String getUrl() {
        return RoomUseController.URL + "/";
    }

    private class MoveResult {
        private RoomUse roomUseAfterMove;
        private RoomUse move;
        private RoomUse refuse;

        public MoveResult(RoomUse roomUseAfterMove) {
            this.roomUseAfterMove = roomUseAfterMove;
        }

        public RoomUse getMove() {
            return move;
        }

        public RoomUse getRefuse() {
            return refuse;
        }

        public MoveResult invoke() {
            List<RoomUse> roomUses = roomUseRepository.findByCustomerGroup(roomUseAfterMove.getCustomerGroup());
            move = null;
            refuse = null;
            for (RoomUse use : roomUses) {
                if (use.getStatus() == REFUSE) {
                    refuse = use;
                } else {
                    move = use;
                }
            }
            return this;
        }
    }
}
