package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.domain.extranet.BaseGroupRoomUse;
import com.idgindigo.pms.domain.extranet.CustomerGroup;
import com.idgindigo.pms.domain.pms.BaseServiceUse;
import com.idgindigo.pms.domain.pms.Bill;
import com.idgindigo.pms.domain.pms.Room;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.repository.extranet.CustomerGroupRepository;
import com.idgindigo.pms.repository.pms.BaseServiceUseRepository;
import com.idgindigo.pms.repository.pms.BillRepository;
import com.idgindigo.pms.repository.pms.RoomRepository;
import com.idgindigo.pms.repository.pms.RoomUseRepository;
import com.idgindigo.pms.restutils.exception.RoomNotAvailableException;
import com.idgindigo.pms.restutils.exception.RoomUseException;
import com.idgindigo.pms.service.admin.SettingsService;
import com.idgindigo.pms.service.pms.RoomUseService;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.RoomUseUtils;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.extranet.CustomerGroupProvider;
import com.idgindigo.pms.utils.pms.RoomProvider;
import com.idgindigo.pms.utils.pms.RoomUseProvider;
import com.idgindigo.pms.web.controller.BaseWebCrudTest;
import com.idgindigo.pms.web.controller.WebTest;
import mockit.Injectable;
import mockit.NonStrictExpectations;
import mockit.Tested;
import mockit.Verifications;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.ACCOMODATIONZ_COM;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.BOOKING;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.FRONT_DESK;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Status.BOOKING_FREE;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Status.BOOKING_WARRANTY;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Status.LIVING;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Status.OUTGO;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Status.REFUSE;
import static com.idgindigo.pms.service.pms.RoomUseService.GroupRoomUseDto;
import static com.idgindigo.pms.web.controller.pms.RoomUseController.CheckDto;
import static com.idgindigo.pms.web.controller.pms.RoomUseController.RefuseDto;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author valentyn_vakatsiienko
 * @since 11/19/13 5:38 PM
 */
public class RoomUseControllerTest extends BaseWebCrudTest<RoomUse> {
    public static final int TOTAL_DAYS = 5;
    public static final int DAYS_TO_ADD = 5;
    private static final ResultMatcher BAD_REQUEST = status().isBadRequest();
    @Inject
    private BillRepository billRepository;
    @Inject
    private BaseServiceUseRepository baseServiceUseRepository;
    @Inject
    private SettingsService settingsService;
    @Inject
    private RoomUseProvider provider;
    @Inject
    private CustomerGroupProvider customerGroupProvider;
    @Inject
    private RoomUseUtils roomUseUtils;
    @Inject
    private RoomProvider roomProvider;
    @Tested
    private RoomUseController roomUseController;
    @Injectable
    private RoomUseService roomUseService;
    @Injectable
    @Inject
    private RoomUseRepository repository;
    @Injectable
    @Inject
    private CustomerGroupRepository customerGroupRepository;
    @Injectable
    @Inject
    private RoomRepository roomRepository;

    @Override
    protected boolean doRunDeleteStep() {
        return false;
    }

    @Test
    public void testCreate() throws Exception {
        final RoomUse roomUse = provider.getTransientEntity(new Visitor<RoomUse>() {
            @Override
            public void visit(RoomUse entity) {
                entity.setEndDate(entity.getStartDate().plusDays(TOTAL_DAYS));
            }
        });
        RoomUse saved = convertResponseWithSingleObject(objectMapper, mvc.perform(preparePost(roomUse)).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), RoomUse.class);

        //Test proper bill is created
        Bill bill = billRepository.findByRoomUse(saved).get(0);
        List<BaseServiceUse> serviceUses = baseServiceUseRepository.findByBill(bill);
        Assert.assertEquals(serviceUses.size(), TOTAL_DAYS);

        //Test overlapping booking not allowed
        RoomUse overlappingRoomUse = provider.getTransientEntity(new Visitor<RoomUse>() {
            @Override
            public void visit(RoomUse entity) {
                entity.setRoom(roomUse.getRoom());
                entity.setPlan(roomUse.getPlan());
                entity.setBaseRoom(roomUse.getBaseRoom());
                entity.setStartDate(roomUse.getEndDate().minusDays(1));
                entity.setEndDate(entity.getStartDate().plusDays(1));
            }
        });
        mvc.perform(preparePost(overlappingRoomUse))
                .andExpect(BAD_REQUEST)
                .andExpect(jsonPath(WebTest.BAD_REQUEST_CODE_MESSAGE_PATH, equalTo(RoomNotAvailableException.CODE)));

    }

    @Test
    public void testCreateGroup() {
        CustomerGroup group = customerGroupProvider.getPersistentEntity();
        int totalRoomUses = 5;
        List<RoomUseService.RoomUseWithOverrides> roomUses = new ArrayList<>(totalRoomUses);
        for (int i = 0; i < totalRoomUses; i++) {
            roomUses.add(new RoomUseService.RoomUseWithOverrides(provider.getTransientEntity()));
        }
        final GroupRoomUseDto dto = new GroupRoomUseDto(null, group, roomUses);

        roomUseController.create(dto);

        new Verifications() {{
            roomUseService.create(dto, true);
        }};
    }

    @Test
    public void testConfirm() {
        final RoomUse roomUse = provider.getPersistentEntity();

        roomUseController.confirm(roomUse.getId());

        new Verifications() {{
            roomUseService.confirm(roomUse);
        }};
    }

    @Test(dataProvider = "testConfirmByGroup")
    public void testConfirmByGroup(RoomUse roomUse) throws Exception {
        mvc.perform(preparePost("confirmedByGroup/" + roomUse.getCustomerGroup().getId())).andExpect(status().isOk());

        RoomUse.Status updated = provider.getRepository().findOne(roomUse.getId()).getStatus();
        if (roomUse.getStatus() == BOOKING_FREE || roomUse.getStatus() == BOOKING_WARRANTY) {
            Assert.assertEquals(updated, BOOKING_WARRANTY);
        } else {
            Assert.assertNotEquals(updated, BOOKING_WARRANTY);
        }
    }

    @DataProvider(name = "testConfirmByGroup")
    public Object[][] getData_testConfirmByGroup() {
        List<Object[]> result = new ArrayList<>(8);
        for (RoomUse.Status status : BaseGroupRoomUse.Status.values()) {
            result.add(new Object[]{getWithStatus(status)});
        }
        return result.toArray(new Object[result.size()][]);
    }

    private RoomUse getWithStatus(final RoomUse.Status status) {
        return provider.getPersistentEntity(new Visitor<RoomUse>() {
            @Override
            public void visit(RoomUse entity) {
                entity.setStatus(status);
            }
        });
    }

    @Test
    public void testUpdateInvalidEndDate() throws Exception {
        RoomUse roomUse = provider.getPersistentEntity();
        roomUse.setEndDate(roomUse.getStartDate().minusDays(1));

        testBadRequest(preparePut(roomUse), RoomUseException.NEW_END_DATE_BEFORE_OLD);
    }

    @Test
    public void testUpdateMoved() throws Exception {
        final RoomUse roomUse = provider.getPersistentEntity();
        provider.getPersistentEntity(new Visitor<RoomUse>() {
            @Override
            public void visit(RoomUse entity) {
                entity.setMovedFrom(roomUse);
            }
        });

        testBadRequest(preparePut(roomUse), RoomUseException.WIDEN_MOVED_ROOM_USE);
    }

    @Test(dataProvider = "testUpdateInvalidStatus")
    public void testUpdateInvalidStatus(RoomUse roomUse) throws Exception {
        testBadRequest(preparePut(roomUse), RoomUseException.UPDATE_INVALID_STATUS);
    }

    @DataProvider(name = "testUpdateInvalidStatus")
    public Object[][] getData_testUpdateInvalidStatus() {
        List<Object[]> result = new ArrayList<>(2);
        result.add(new Object[]{getWithStatus(REFUSE)});
        result.add(new Object[]{getWithStatus(OUTGO)});
        return result.toArray(new Object[result.size()][]);
    }

    @Test
    public void testCreateServiceUsesWithNewBill() throws Exception {
        RoomUse roomUse = getRoomUse();

        Bill bill = billRepository.findByRoomUse(roomUse).get(0);
        int sizeBefore = baseServiceUseRepository.findByBill(bill).size();

        roomUse.setEndDate(roomUse.getEndDate().plusDays(DAYS_TO_ADD));
//        roomUse.setBill(null);
        mvc.perform(preparePut(roomUse)).andExpect(status().isOk());

        int sizeAfter = baseServiceUseRepository.findByBill(bill).size();
        Assert.assertEquals(sizeAfter, sizeBefore);

        bill = billRepository.findByRoomUse(roomUse).get(1);
        int sizeNew = baseServiceUseRepository.findByBill(bill).size();
        Assert.assertEquals(DAYS_TO_ADD, sizeNew);
    }

    private RoomUse getRoomUse(Visitor<RoomUse>... visitors) throws Exception {
        RoomUse roomUse = provider.getTransientEntity(visitors);
        MockHttpServletResponse response = mvc.perform(preparePost(roomUse)).andExpect(status().isCreated()).andReturn().getResponse();
        Long id = convertResponseWithSingleObject(objectMapper, response.getContentAsString(), RoomUse.class).getId();
        roomUse.setId(id);
        return roomUse;
    }

    @Test(dataProvider = "testCheckIn")
    public void testCheckIn(RoomUse roomUse, ResultMatcher matcher) throws Exception {
        CheckDto dto = new CheckDto();
        dto.setTime(settingsService.getHotelTime());
        roomUseUtils.populate(roomUse.getCustomerGroup(), roomUse);
        mvc.perform(preparePost(dto, roomUse.getId() + "/checkedIn")).andExpect(matcher);
    }

    @DataProvider(name = "testCheckIn")
    public Object[][] getData_testCheckIn() throws Exception {
        ResultMatcher ok = status().isOk();
        List<Object[]> result = new ArrayList<Object[]>(5);
        result.add(new Object[]{provider.getPersistentEntity(new Visitor<RoomUse>() {
            @Override
            public void visit(RoomUse entity) {
                entity.setStatus(BaseGroupRoomUse.Status.BOOKING_FREE);
            }
        }), BAD_REQUEST});
        result.add(new Object[]{provider.getPersistentEntity(new Visitor<RoomUse>() {
            @Override
            public void visit(RoomUse entity) {
                entity.setStatus(BaseGroupRoomUse.Status.BOOKING_WARRANTY);
            }
        }), ok});
        result.add(new Object[]{provider.getPersistentEntity(new Visitor<RoomUse>() {
            @Override
            public void visit(RoomUse entity) {
                entity.setStatus(LIVING);
            }
        }), BAD_REQUEST});
        result.add(new Object[]{provider.getPersistentEntity(new Visitor<RoomUse>() {
            @Override
            public void visit(RoomUse entity) {
                entity.setStatus(BaseGroupRoomUse.Status.OUTGO);
            }
        }), BAD_REQUEST});
        result.add(new Object[]{provider.getPersistentEntity(new Visitor<RoomUse>() {
            @Override
            public void visit(RoomUse entity) {
                entity.setStatus(BaseGroupRoomUse.Status.REFUSE);
            }
        }), BAD_REQUEST});
        return result.toArray(new Object[result.size()][]);
    }

    @Test
    public void testGroupCheckIn() throws Exception {
        final CustomerGroup group = customerGroupProvider.getPersistentEntity();
        final LocalDate now = LocalDate.now(DateTimeZone.UTC);
        final LocalDate end = now.plusDays(5);
        RoomUse first = getRoomUse(group, now.minusDays(1), end);
        RoomUse second = getRoomUse(group, now, end);
        RoomUse third = getRoomUse(group, now.plusDays(1), end);
        roomUseUtils.populate(group, first, second, third);
        confirm(group);
        checkIn(group, new CheckDto(now.toLocalDateTime(LocalTime.MIDNIGHT)));
        assertStatus(first, BOOKING_WARRANTY);
        assertStatus(second, LIVING);
        assertStatus(third, BOOKING_WARRANTY);
    }

    @Test
    public void testCheckOut() {
        final RoomUse roomUse = provider.getPersistentEntity();
        final CheckDto dto = new CheckDto(LocalDateTime.now(DateTimeZone.UTC));
        roomUseController.checkOut(roomUse.getId(), dto);

        new Verifications() {{
            roomUseService.checkOut(roomUse, dto.time);
        }};
    }

    @Test
    public void testGroupCheckOut() throws Exception {
        final CustomerGroup group = customerGroupProvider.getPersistentEntity();
        final LocalDate now = LocalDate.now(DateTimeZone.UTC);
        final LocalDate end = now.plusDays(5);
        RoomUse first = getRoomUse(group, now, end.minusDays(1));
        final RoomUse second = getRoomUse(group, now, end);
        RoomUse third = getRoomUse(group, now, end.plusDays(1));
        roomUseUtils.populate(group, first, second, third);
        confirm(group);
        checkIn(group, new CheckDto(now.toLocalDateTime(LocalTime.MIDNIGHT)));
        new NonStrictExpectations(settingsService) {{
            settingsService.getHotelDate();
            result = end;
        }};
        fullyPay(second);
        checkOut(group, new CheckDto(end.toLocalDateTime(LocalTime.MIDNIGHT)));
        assertStatus(first, LIVING);
        assertStatus(second, OUTGO);
        assertStatus(third, LIVING);
    }

    private void fullyPay(RoomUse roomUse) {
        List<Bill> bills = billRepository.findByRoomUse(roomUse);
        for (Bill bill : bills) {
            billRepository.updateTotalPaid(bill, bill.getTotal());
        }
    }

    private RoomUse getRoomUse(final CustomerGroup group, final LocalDate start, final LocalDate end) throws Exception {
        return getRoomUse(new Visitor<RoomUse>() {
            @Override
            public void visit(RoomUse entity) {
                entity.setCustomerGroup(group);
                entity.setStartDate(start);
                entity.setEndDate(end);
            }
        });
    }

    private void checkOut(CustomerGroup group, CheckDto dto) throws Exception {
        mvc.perform(preparePost(dto, "checkedOutByGroup/" + group.getId())).andExpect(status().isOk());
    }

    private void checkIn(CustomerGroup group, CheckDto dto) throws Exception {
        mvc.perform(preparePost(dto, "checkedInByGroup/" + group.getId())).andExpect(status().isOk());
    }

    private void confirm(CustomerGroup group) throws Exception {
        mvc.perform(preparePost("confirmedByGroup/" + group.getId())).andExpect(status().isOk());
    }

    private void assertStatus(RoomUse roomUse, RoomUse.Status status) {
        roomUse = getProvider().getRepository().findOne(roomUse.getId());
        Assert.assertEquals(roomUse.getStatus(), status);
    }

    @Test(dataProvider = "testNotArrived")
    public void testNotArrived(RoomUse roomUse, ResultMatcher matcher, String message) throws Exception {
        ResultActions resultActions = mvc.perform(preparePost(roomUse.getId() + "/notArrived")).andExpect(matcher);
        if (matcher.equals(BAD_REQUEST)) {
            resultActions.andExpect(jsonPath(WebTest.BAD_REQUEST_CODE_MESSAGE_PATH, equalTo(message)));
        }
    }

    @DataProvider(name = "testNotArrived")
    public Object[][] getData_testNotArrived() {
        List<Object[]> result = new ArrayList<>(3);

        result.add(new Object[]{
                getRoomUse(null, FRONT_DESK),
                BAD_REQUEST,
                RoomUseException.NOT_ARRIVED_ONLY_FOR_CHANNEL});

        result.add(new Object[]{
                getRoomUse(LIVING, BOOKING),
                BAD_REQUEST,
                RoomUseException.NOT_ARRIVED_ONLY_FOR_BOOKING});

        result.add(new Object[]{
                getRoomUse(BOOKING_FREE, BOOKING),
                status().isOk(),
                null});

        return result.toArray(new Object[result.size()][]);
    }

    private RoomUse getRoomUse(final RoomUse.Status status, final RoomUse.Source source) {
        return provider.getPersistentEntity(new Visitor<RoomUse>() {
            @Override
            public void visit(RoomUse entity) {
                if (status != null) {
                    entity.setStatus(status);
                }
                if (source != null) {
                    entity.setSource(source);
                    entity.setRcode(EntityProvider.randomAlphabeticString());
                }
            }
        });
    }

    @Test(dataProvider = "testRefuse")
    public void testRefuse(final RoomUse roomUse, final RefuseDto dto) {
        roomUseController.refuse(roomUse.getId(), dto);

        new Verifications() {{
            roomUseService.refuse(roomUse, dto.getDate(), true, null);
        }};
    }

    @Test(dataProvider = "testRefuse")
    public void testGroupRefuse(final RoomUse roomUse, final RefuseDto dto) {
        roomUseController.groupRefuse(roomUse.getCustomerGroup().getId(), dto);

        new Verifications() {{
            roomUseService.refuse(roomUse.getCustomerGroup(), dto.getDate(), true, null);
        }};
    }

    @DataProvider(name = "testRefuse")
    public Object[][] getData_testRefuse() {
        List<Object[]> result = new ArrayList<Object[]>(1);
        final RoomUse roomUse = provider.getPersistentEntity();
        final RefuseDto dto = new RefuseDto();
        dto.setDate(roomUse.getStartDate());

        result.add(new Object[]{roomUse, dto});
        return result.toArray(new Object[result.size()][]);
    }

    @Test
    public void testRefuseChannelBooking() {
        RoomUse roomUse = provider.getPersistentEntity(new Visitor<RoomUse>() {
            @Override
            public void visit(RoomUse entity) {
                entity.setSource(ACCOMODATIONZ_COM);
                entity.setRcode(EntityProvider.randomAlphabeticString());

            }
        });
        try {
            roomUseController.refuse(roomUse.getId(), null);
            throw new AssertionError("Should not happen");
        } catch (RoomUseException ex) {
            Assert.assertEquals(ex.getMessage(), RoomUseException.REFUSE_CHANNEL_BOOKING);
        }
    }

    @Test
    public void testRefuseFakeChannelBooking() {
        loginAs(userProvider.getPersistentEntity());
        RoomUse roomUse = provider.getPersistentEntity(new Visitor<RoomUse>() {
            @Override
            public void visit(RoomUse entity) {
                entity.setSource(ACCOMODATIONZ_COM);
                entity.setRcode(EntityProvider.randomAlphabeticString());
            }
        });
        final RefuseDto dto = new RefuseDto();
        dto.setDate(roomUse.getStartDate());
        try {
            roomUseController.refuse(roomUse.getId(), dto);
        } catch (RoomUseException ex) {
            Assert.fail("Should not happen");
        }
        logout();
    }

    @Test
    public void testMove() {
        final RoomUse roomUse = provider.getPersistentEntity();
        final RoomUseController.MoveDto moveDto = new RoomUseController.MoveDto();
        moveDto.setRoom(roomProvider.getPersistentEntity());
        moveDto.setSinceDate(roomUse.getStartDate());
        moveDto.setUpgrade(true);

        roomUseController.move(roomUse.getId(), moveDto);

        new Verifications() {{
            roomUseService.move(roomUse, moveDto, true, null);
        }};
    }

    @Test
    public void testMoveChannelBooking() {
        final RoomUse roomUse = getChannelBooking();
        final RoomUseController.MoveDto dto = new RoomUseController.MoveDto();
        dto.setRoom(roomProvider.getPersistentEntity());
        dto.setSinceDate(roomUse.getStartDate());

        try {
            roomUseController.move(roomUse.getId(), dto);
            Assert.fail();
        } catch (RoomUseException e) {
            Assert.assertEquals(e.getMessage(), RoomUseException.MOVE_CHANNEL_BOOKING_WRONG_ROOM_TYPE);
            Assert.assertEquals(e.getSource(), "room.roomType");
        }
    }


    @Test
    public void testMoveChannelBookingPreserveBaseRoom() {
        final RoomUse roomUse = getChannelBooking();
        final RoomUseController.MoveDto dto = new RoomUseController.MoveDto();
        dto.setRoom(roomProvider.getPersistentEntity(new Visitor<Room>() {
            @Override
            public void visit(Room entity) {
                entity.setRoomType(roomUse.getRoom().getRoomType());
            }
        }));
        dto.setSinceDate(roomUse.getStartDate());

        Assert.assertNotEquals(dto.getBaseRoom(), roomUse.getBaseRoom());
        roomUseController.move(roomUse.getId(), dto);
        Assert.assertEquals(dto.getBaseRoom(), roomUse.getBaseRoom());
    }

    private RoomUse getChannelBooking() {
        return provider.getPersistentEntity(new Visitor<RoomUse>() {
            @Override
            public void visit(RoomUse entity) {
                entity.setSource(BOOKING);
                entity.setRcode(RandomStringUtils.randomAlphabetic(10));
            }
        });
    }

    @Override
    protected EntityProvider<RoomUse> getProvider() {
        return provider;
    }

    @Override
    protected String getUrl() {
        return RoomUseController.URL + "/";
    }
}
