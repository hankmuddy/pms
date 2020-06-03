package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.domain.extranet.plan.Plan;
import com.idgindigo.pms.domain.extranet.roomtype.BaseRoom;
import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeQuota;
import com.idgindigo.pms.domain.extranet.roomtype.VirtualRoom;
import com.idgindigo.pms.domain.extranet.service.Living;
import com.idgindigo.pms.domain.pms.Room;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.repository.extranet.RoomTypeRepository;
import com.idgindigo.pms.repository.pms.RoomUseRepository;
import com.idgindigo.pms.restutils.exception.ApiException;
import com.idgindigo.pms.service.channels.ChannelService;
import com.idgindigo.pms.service.pms.QuotaService;
import com.idgindigo.pms.utils.HotelProvider;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.extranet.CustomerGroupProvider;
import com.idgindigo.pms.utils.extranet.LivingProvider;
import com.idgindigo.pms.utils.extranet.VirtualRoomProvider;
import com.idgindigo.pms.utils.extranet.plan.PlanProvider;
import com.idgindigo.pms.utils.pms.RoomProvider;
import com.idgindigo.pms.web.controller.InMemoryDbWebTest;
import com.idgindigo.pms.web.controller.ResponseEntity;
import mockit.Injectable;
import mockit.NonStrictExpectations;
import mockit.Tested;
import mockit.Verifications;
import org.joda.time.LocalDate;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author vomel
 * @since 29.03.14 08:40
 */
public class ApiControllerTest extends InMemoryDbWebTest<RoomUse> {
    @Inject
    private RoomProvider roomProvider;
    @Inject
    private LivingProvider livingProvider;
    @Inject
    private HotelProvider hotelProvider;
    @Inject
    private RoomUseRepository roomUseRepository;
    @Inject
    private PlanProvider planProvider;
    @Inject
    private VirtualRoomProvider virtualRoomProvider;
    @Inject
    private CustomerGroupProvider customerGroupProvider;
    @Inject
    private ApiController controller;

    @Tested
    private ApiController mockController;
    @Injectable
    private QuotaService quotaService;
    @Injectable
    private RoomTypeRepository roomTypeRepository;

    @Test
    public void testFindAvailable() throws Exception {
        new NonStrictExpectations(quotaService) {{
            quotaService.getRoomTypeQuota(withInstanceOf(LocalDate.class), withInstanceOf(LocalDate.class));
            result = Arrays.asList(new RoomTypeQuota(null, 5), new RoomTypeQuota(null, 0));
        }};
        final LocalDate now = LocalDate.now();
        ResponseEntity<List<RoomTypeQuota>> response = mockController.findAvailable(now.toString(ChannelService.FORMATTER), now.toString(ChannelService.FORMATTER));
        new Verifications() {{
            quotaService.getRoomTypeQuota(now, now);
        }};
        List<RoomTypeQuota> res = response.getContent();
        Assert.assertEquals(res.size(), 1);
        Assert.assertEquals(res.get(0).getQuota(), 5);
        /*int ridA = 60212;
        int ridB = 60214;
        int ridC = 60215;
        final Plan plan = planProvider.getFirst();
        List<Room> roomsA = createRooms(plan, ridA, 2);
        List<Room> roomsB = createRooms(plan, ridB, 3);
        final List<Room> roomsC = createRooms(plan, ridC, 5);
        final String lcode = RandomStringUtils.random(10, false, true);
        final String wuName = "wuname:" + randomString();
        final String wuPass = "wupass:" + randomString();
        Hotel hotel = hotelProvider.getPersistentEntity(new Visitor<Hotel>() {
            @Override
            public void visit(Hotel entity) {
                entity.setLcode(lcode);
                entity.setWuName(wuName);
                entity.setWuPass(wuPass);
            }
        });

        final RoomUse roomUse = new RoomUse();
        roomUse.setStartDate(LocalDate.parse("24/12/2025", ChannelService.FORMATTER));
        roomUse.setEndDate(LocalDate.parse("25/12/2025", ChannelService.FORMATTER));

        List<BaseRoom> baseRooms = ((VirtualRoomRepository) virtualRoomProvider.getRepository()).findByRoomType(roomsC.get(0).getRoomType());
        roomUse.setBaseRoom(baseRooms.get(0));
        roomUse.setPlan(plan);
        roomUse.setRoom(roomsC.get(0));
        roomUse.setCustomerGroup(customerGroupProvider.getPersistentEntity());
        roomUse.setStatus(BaseGroupRoomUse.Status.BOOKING_FREE);
        roomUseRepository.saveAndFlush(roomUse);

        MockHttpServletResponse response = mvc.perform(post(getUrl() + ApiController.FIND_AVAILABLE)
                .param("dfrom", "23/12/2025")
                .param("dto", "26/12/2025")
                .param("hotelId", String.valueOf(hotel.getTenantId()))).andExpect(status().isOk()).andReturn().getResponse();
        List<RoomTypeQuota> result = convertResponseWithObjectList(objectMapper, response, RoomTypeQuota.class);
        int found = 0;
        for (RoomTypeQuota dto : result) {
            if (dto.getRoomType().getName().equals(roomsA.get(0).getRoomType().getName())) {
                Assert.assertEquals(dto.getQuota(), 2);
                found++;
                continue;
            }
            if (dto.getRoomType().getName().equals(roomsB.get(0).getRoomType().getName())) {
                Assert.assertEquals(dto.getQuota(), 3);
                found++;
                continue;
            }
            if (dto.getRoomType().getName().equals(roomsC.get(0).getRoomType().getName())) {
                Assert.assertEquals(dto.getQuota(), 4);
                found++;
                continue;
            }
        }
        Assert.assertEquals(found, 3);*/
    }

    @Test
    public void testBookingDateValidation() {
        ApiController.BookDto dto = new ApiController.BookDto();
        dto.setStartDate(LocalDate.now());
        dto.setEndDate(dto.getStartDate().minusDays(1));
        try {
            controller.book(dto, null);
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), ApiException.BOOKING_INVALID_DATES);
            Assert.assertEquals(e.getSource(), "startDate, endDate");
        }
        dto.setStartDate(LocalDate.now().minusDays(10));
        try {
            controller.book(dto, null);
        } catch (ApiException e) {
            Assert.assertEquals(e.getMessage(), ApiException.BOOKING_IN_PAST);
            Assert.assertEquals(e.getSource(), "startDate");
        }
    }

    private List<Room> createRooms(final Plan plan, final long rid, int total) {
        final Room roomA = roomProvider.getPersistentEntity();
        List<Room> rooms = new ArrayList<>();
        rooms.add(roomA);
        createVrAndLiving(plan, rid, roomA);
        for (int i = 0; i < total - 1; i++) {
            Room roomc = roomProvider.getPersistentEntity(new Visitor<Room>() {
                @Override
                public void visit(Room entity) {
                    entity.setRoomType(roomA.getRoomType());
                }
            });
            rooms.add(roomc);
        }
        return rooms;
    }

    private void createVrAndLiving(final Plan plan, final long rid, final Room room) {
        final BaseRoom vr = virtualRoomProvider.getPersistentEntity(new Visitor<VirtualRoom>() {
            @Override
            public void visit(VirtualRoom entity) {
                entity.setRid(rid);
                entity.setRoomType(room.getRoomType());
            }
        });
        Living living = livingProvider.getPersistentEntity(new Visitor<Living>() {
            @Override
            public void visit(Living entity) {
                entity.setRoom(vr);
                entity.setPlan(plan);
            }
        });
    }

    @Override
    public String getUrl() {
        return ApiController.URL + "/";
    }
}
