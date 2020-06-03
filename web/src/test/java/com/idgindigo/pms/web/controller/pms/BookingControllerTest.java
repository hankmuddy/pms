package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.axmlrpc.ResponseParser;
import com.idgindigo.pms.axmlrpc.XmlRpcClient;
import com.idgindigo.pms.axmlrpc.serializer.SerializerHandler;
import com.idgindigo.pms.channel.wubook.WubookImpl;
import com.idgindigo.pms.domain.extranet.CustomerGroup;
import com.idgindigo.pms.domain.extranet.plan.Plan;
import com.idgindigo.pms.domain.extranet.roomtype.BaseRoom;
import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeValue;
import com.idgindigo.pms.domain.extranet.roomtype.VirtualRoom;
import com.idgindigo.pms.domain.extranet.service.Living;
import com.idgindigo.pms.domain.pms.BankDetails;
import com.idgindigo.pms.domain.pms.Bill;
import com.idgindigo.pms.domain.pms.Room;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.domain.pms.SimpleService;
import com.idgindigo.pms.domain.pms.SimpleServiceUse;
import com.idgindigo.pms.logins.domain.Hotel;
import com.idgindigo.pms.repository.pms.BankDetailsRepository;
import com.idgindigo.pms.repository.pms.BillRepository;
import com.idgindigo.pms.repository.pms.RoomUseRepository;
import com.idgindigo.pms.repository.pms.SimpleServiceUseRepository;
import com.idgindigo.pms.service.broadcast.BroadcastService;
import com.idgindigo.pms.service.channels.BookingService;
import com.idgindigo.pms.service.channels.ChannelService;
import com.idgindigo.pms.service.channels.WubookRoomUses;
import com.idgindigo.pms.service.pms.RoomUseService;
import com.idgindigo.pms.service.pms.RoomUseServiceTest;
import com.idgindigo.pms.utils.HotelProvider;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.extranet.CustomerGroupProvider;
import com.idgindigo.pms.utils.extranet.LivingProvider;
import com.idgindigo.pms.utils.extranet.RoomTypeValueProvider;
import com.idgindigo.pms.utils.extranet.VirtualRoomProvider;
import com.idgindigo.pms.utils.extranet.plan.PlanProvider;
import com.idgindigo.pms.utils.pms.BankDetailsProvider;
import com.idgindigo.pms.utils.pms.RoomProvider;
import com.idgindigo.pms.utils.pms.RoomUseProvider;
import com.idgindigo.pms.utils.pms.SimpleServiceProvider;
import com.idgindigo.pms.web.controller.InMemoryDbWebTest;
import mockit.Deencapsulation;
import mockit.Injectable;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Tested;
import mockit.Verifications;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.BOOKING;
import static com.idgindigo.pms.service.pms.RoomUseService.GroupRoomUseDto;
import static com.idgindigo.pms.service.pms.RoomUseService.RoomUseWithOverrides;
import static com.idgindigo.pms.utils.EntityProvider.randomString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author vomel
 * @since 07.02.14 19:36
 */
public class BookingControllerTest extends InMemoryDbWebTest<RoomUse> {
    private static final Logger logger = LoggerFactory.getLogger(BookingControllerTest.class);
    private static final int TOTAL_ROOM_USES = 5;
    @Inject
    private HotelProvider hotelProvider;
    @Inject
    private RoomProvider roomProvider;
    @Inject
    private RoomUseRepository roomUseRepository;
    @Inject
    private PlanProvider planProvider;
    @Inject
    private LivingProvider livingProvider;
    @Inject
    private VirtualRoomProvider virtualRoomProvider;
    @Inject
    private CustomerGroupProvider customerGroupProvider;

    @Mocked
    private WubookImpl wubookImpl;

    @AfterMethod
    public void afterMethod() {
        Deencapsulation.setField(WubookImpl.class, "ENABLED", false);
    }

    /*@Test
    public void testRefuse() {
        List<RoomUseWithOverrides> refusesFromWubook = new ArrayList<>(Arrays.asList(
                roomUseWO(1),
                roomUseWO(2),
                roomUseWO(3),
                roomUseWO(4),
                roomUseWO(5)
        ));

        final List<RoomUse> refusedByService = Arrays.asList(roomUse(1), roomUse(3), roomUse(5));

        List<RoomUseWithOverrides> refused = BookingController.refuse(
                new CustomerGroup(),
                refusesFromWubook,
                new RoomUseService() {
                    @Override
                    public Collection<RoomUse> refuse(CustomerGroup group, LocalDate date, boolean updateChannels, BankDetails details) {
                        return refusedByService;
                    }
                }
        );

        Assert.assertEquals(refused.size(), refusedByService.size());
        for (RoomUseWithOverrides ruwo : refused) {
            Assert.assertTrue(refusedByService.contains(ruwo.getRoomUse()));
        }
    }*/

    public static RoomUseWithOverrides roomUseWO(int id) {
        return new RoomUseWithOverrides(roomUse(id));
    }

    public static RoomUse roomUse(int id) {
        RoomUse roomUse = new RoomUse();
        roomUse.setStartDate(LocalDate.now());
        roomUse.setEndDate(roomUse.getStartDate().plusDays(1));
        roomUse.setId(Long.valueOf(id));
        roomUse.setBaseRoom(new VirtualRoom());
        roomUse.setRoom(new Room());
        return roomUse;
    }

    @Inject
    private RoomTypeValueProvider roomTypeValueProvider;
    @Inject
    private SimpleServiceProvider simpleServiceProvider;

    @Inject
    private SimpleServiceUseRepository simpleServiceUseRepository;
    @Inject
    private BillRepository billRepository;
    @Inject
    private BankDetailsProvider bankDetailsProvider;

    @Test
    public void testForcedRefund() throws Exception {
        Hotel hotel = hotelProvider.getPersistentEntity(new Visitor<Hotel>() {
            @Override
            public void visit(Hotel entity) {
                entity.setLcode(RandomStringUtils.random(10, false, true));
                entity.setWuName(randomString());
                entity.setWuPass(randomString());
            }
        });
        if (((BankDetailsRepository) bankDetailsProvider.getRepository()).getDefault() == null) {
            bankDetailsProvider.getPersistentEntity(new Visitor<BankDetails>() {
                @Override
                public void visit(BankDetails entity) {
                    entity.setDefaultDetails(Boolean.TRUE);
                }
            });
        }
        int ratesQuantity = 5;
        final Living living = livingProvider.getPersistentEntity();
        final BaseRoom baseRoom = living.getRoom();
        final Plan plan = living.getPlan();
        final List<RoomTypeValue> roomTypeValues = RoomUseServiceTest.getRates(living, ratesQuantity, 2, roomTypeValueProvider);

        /*
            Create a room use that spans over rates(1) to rates(size - 2)
            and leaves out rates(0) and rates(size -1) to check that roomUseService doesnt touch surrounding rates
        */
        RoomUse roomUse = roomUseProvider.getTransientEntity(new Visitor<RoomUse>() {
            @Override
            public void visit(RoomUse entity) {
                entity.setBaseRoom(baseRoom);
                entity.setPlan(plan);
                entity.setRoom(roomProvider.getPersistentEntity(new Visitor<Room>() {
                    @Override
                    public void visit(Room entity) {
                        entity.setRoomType(living.getRoom().roomType());
                    }
                }));
                entity.setStartDate(roomTypeValues.get(1).getDate());
                entity.setEndDate(roomTypeValues.get(roomTypeValues.size() - 2).getDate());
                entity.setSource(BOOKING);
                entity.setRcode("1391524753");
                entity.setTotal(100L);
                entity.setTotalPaid(1L);
            }
        });
        RoomUse finalRoomUse = roomUseService.create(roomUse, new HashMap<LocalDate, Long>(), true);
        SimpleService simpleService = simpleServiceProvider.getPersistentEntity();
        SimpleServiceUse simpleServiceUse = new SimpleServiceUse();
        simpleServiceUse.setRoomUse(finalRoomUse);
        simpleServiceUse.setService(simpleService);
        Bill bill = new Bill();
        bill.setRoomUse(roomUse);
        bill.setTotal(roomUse.getTotal());
        bill.setTotalPaid(roomUse.getTotal());
        bill.setRawTotal(roomUse.getTotal() / 2);
        billRepository.save(bill);
        simpleServiceUse.setBill(bill);
        simpleServiceUse.setTotal(2L);
        simpleServiceUse.setRawTotal(3L);
        simpleServiceUse.setDate(roomTypeValues.get(1).getDate());
        simpleServiceUse.setQuantity(14);
        simpleServiceUse.setRoomUse(finalRoomUse);
        simpleServiceUseRepository.save(simpleServiceUse);
        final String token = randomString();
        SerializerHandler.initialize(XmlRpcClient.FLAGS_NONE);
        final Object[] fetchRefusedResponse = getFromWubook(MockWubook.FETCH_NEW_BOOKINGS);
        new NonStrictExpectations() {{
            wubookImpl.getToken();
            result = token;
            wubookImpl.fetchNewBookings(token);
            result = fetchRefusedResponse[1];
        }};

        Assert.assertEquals(roomUseRepository.findOne(roomUse.getId()).getStatus(), RoomUse.Status.BOOKING_FREE);

        MockHttpServletResponse response = mvc.perform(
                post(getUrl() + BookingController.CATCH)
                        .param("lcode", hotel.getLcode())
                        .param("rcode", roomUse.getRcode())
                        .session(session))
                .andExpect(status().isOk()).andReturn().getResponse();

        Assert.assertEquals(roomUseRepository.findOne(roomUse.getId()).getStatus(), RoomUse.Status.REFUSE);
    }

    @Test
    public void testNewWaiting() throws Exception {
        int rid = 60213;
        String reservationCode = "1394616433";
        Assert.assertEquals(roomUseRepository.findByRcode(reservationCode).size(), 0);
        final Plan plan = planProvider.getFirst();
        Room roomsA = createRooms(plan, rid, 1).get(0);

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
        SerializerHandler.initialize(XmlRpcClient.FLAGS_NONE);
        final Object[] fetchNewWaitingResponse = getFromWubook(MockWubook.NEW_WAITING);
        final Object[] fetchApprovedResponse = getFromWubook(MockWubook.NEW_APPROVED);
        final String token = "1238200946.2025";
        new NonStrictExpectations() {{
            wubookImpl.getToken();
            result = token;
            wubookImpl.fetchNewBookings(token);
            result = fetchNewWaitingResponse[1];
            wubookImpl.fetchNewBookings(token);
            result = fetchApprovedResponse[1];
        }};
        MockHttpServletResponse response = mvc.perform(
                post(getUrl() + BookingController.CATCH).param("lcode", lcode).param("rcode", reservationCode).session(session)).andExpect(status().isOk()).andReturn().getResponse();
        Assert.assertEquals(roomUseRepository.findByRcode(reservationCode).size(), 1);
        MockHttpServletResponse response2 = mvc.perform(
                post(getUrl() + BookingController.CATCH).param("lcode", lcode).param("rcode", reservationCode).session(session)).andExpect(status().isOk()).andReturn().getResponse();
        Assert.assertEquals(roomUseRepository.findByRcode(reservationCode).size(), 1);
    }

    @Test(invocationTimeOut = 15000, invocationCount = 1)
    public void testPushBooking() throws Exception {

        final Plan plan = planProvider.getFirst();


        List<Room> roomsA = createRooms(plan, 54882L, 6);
        List<Room> roomsB = createRooms(plan, 54834L, 1);
        List<Room> roomsC = createRooms(plan, 54805L, 1);

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

        SerializerHandler.initialize(XmlRpcClient.FLAGS_NONE);
        final Object getTokenResponse = getFromWubook(MockWubook.GET_TOKEN);
        final Object singleBookingResponse = getFromWubook(MockWubook.FETCH_BOOKINGS);
        final Object groupBookingResponse = getFromWubook(MockWubook.FETCH_GROUP_BOOKINGS);
        final Object refusedBookingResponse = getFromWubook(MockWubook.REFUSED_GROUP_BOOKINGS);
        final Object[] releaseTokenResponse = getFromWubook(MockWubook.RELEASE_TOKEN);
        final Object[] heavyLoad1Response = getFromWubook(MockWubook.HEAVY_LOAD_1);
        final Object[] heavyLoad2Response = getFromWubook(MockWubook.HEAVY_LOAD_2);
        final Object[] heavyLoad3Response = getFromWubook(MockWubook.HEAVY_LOAD_3);
        final Object[] heavyLoad4Response = getFromWubook(MockWubook.HEAVY_LOAD_4);
        final String token = "1238200946.2025";
        new NonStrictExpectations() {{
            wubookImpl.getToken();
            result = token;
            wubookImpl.fetchNewBookings(token);
//          17:30:04 FETCH_BOOKINGS: heavyLoad1.xml (1391873279+(54834,54882,54882,54882,54882,54805))
            result = heavyLoad1Response[1];
//          18:34:03 FETCH_BOOKINGS: heavyLoad2.xml
// (1391877215+(54882,),1391877205+(54834,54882,54882,54805),1391877193(54882)-,1391877182-(54834,54882,54882,54805),1391873279-(54834,54882,54882,54882,54882,54805))
            result = heavyLoad2Response[1];
//          18:36:03 FETCH_BOOKINGS: heavyLoad3.xml
// (1391877324+(54882),1391877313+(54882),1391877303+(54834,54882,54805),1391877215-(54882),1391877205-(54834,54882,54882,54805))
            result = heavyLoad3Response[1];
//          18:38:02 FETCH_BOOKINGS: heavyLoad4.xml
// (1391877386+(54882),1391877374+(54882,54882),1391877363+(54834,54805),1391877324-(54882),1391877313-(54882))
            result = heavyLoad4Response[1];
//EXPECTED:
// (-1391877303+(54834,54882,54805), 1391877386+(54882),   -1391877374+(54882,54882), -1391877363+(54834,54805))
//MOD:                               1391877363,1391877374 1391877363                 1391877303,1391877324,1391877313
        }};

        Collection<?> firstBatch = Arrays.asList(1391873279, 1391873279, 1391873279, 1391873279, 1391873279);
        processBatch(lcode, firstBatch);

        Collection<?> secondBatch = Arrays.asList(1391873279, 1391877182, 1391877215, 1391877193, 1391877193, 1391877182, 1391877205, 1391877182, 1391877205, 1391877182, 1391877193, 1391877182, 1391877193, 1391877215, 1391877205, 1391877215, 1391877193, 1391877193, 1391877182, 1391877182, 1391877205, 1391877193, 1391877182, 1391877215, 1391877193, 1391877182, 1391877205, 1391877182, 1391877193, 1391877205, 1391877215, 1391877182, 1391877193, 1391877193, 1391877182, 1391877215, 1391877193, 1391873279, 1391873279, 1391873279, 1391873279, 1391873279);
        processBatch(lcode, secondBatch);

        Collection<?> thirdBatch = Arrays.asList(1391877303, 1391877313, 1391877303, 1391877313, 1391877205, 1391877215, 1391877215, "ð", 1391877215, 1391877215, 1391877215, 1391877215, 1391877303, 1391877303, 1391877303, 1391877303, 1391877313, 1391877313, 1391877313, 1391877324, 1391877313, 1391877324, 1391877324, 1391877324, 1391877324, 1391877324, 1391877205, 1391877205, 1391877205, 1391877205, 1391877205);
        processBatch(lcode, thirdBatch);

        Collection<?> fourthBatch = Arrays.asList(1391877303, 1391877313, 1391877324, 1391877313, 1391877313, 1391877324, 1391877363, 1391877324, 1391877313, 1391877363, 1391877374, 1391877324, 1391877313, 1391877386, 1391877363, 1391877374, 1391877324, 1391877313, 1391877363, 1391877374, 1391877386, 1391877324, 1391877386, 1391877363, 1391877374, 1391877386, 1391877374, 1391877363, 1391877386, 1391877374, 1391877386, 1391877303, 1391877303, 1391877303, 1391877303, 1391877303);
        processBatch(lcode, fourthBatch);
    }

    private void processBatch(String lcode, Collection<?> batch) throws InterruptedException {
        logger.debug("Starting batch for {} threads:{}", batch.size(), batch);
        CountDownLatch delay = new CountDownLatch(batch.size());
        for (Object rcode : batch) {
            new Thread(new Runner(delay, mvc, session, lcode, String.valueOf(rcode))).start();
        }
        logger.debug("Awaiting for batch to finish");
        delay.await();
        logger.debug("Batch finished\n");
        logger.debug("\n\n");
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

    private static class Runner implements Runnable {
        private final CountDownLatch finish;
        private final MockMvc mvc;
        private MockHttpSession session;
        private final String lcode;
        private final String rcode;

        private Runner(CountDownLatch finish, MockMvc mvc, MockHttpSession session, String lcode, String rcode) {
            this.finish = finish;
            this.mvc = mvc;
            this.session = session;
            this.lcode = lcode;
            this.rcode = rcode;
        }

        @Override
        public void run() {
            try {
//                logger.debug("Thread {} started", Thread.currentThread().getId());
                MockHttpServletResponse response = mvc.perform(
                        post(BookingController.URL + "/catch").param("lcode", lcode).param("rcode", rcode).session(session)).andExpect(status().isOk()).andReturn().getResponse();
//                logger.debug("Thread {} finished", Thread.currentThread().getId());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                finish.countDown();
            }
        }
    }

    private Object[] getFromWubook(String... data) {
        Object[] response;
        try {
            Object toParse = new ResponseParser().parse(new ByteArrayInputStream(mvc.perform(post("/" + MockWubook.URL + "/xrws").content(Arrays.toString(data))).andReturn().getResponse().getContentAsByteArray()));
            response = (Object[]) toParse;
        } catch (Exception e) {
            logger.debug("Arrays.toString(data) = " + Arrays.toString(data));
            throw new RuntimeException(e);
        }
        return response;
    }

    @Tested
    private BookingController mockController;
    @Injectable
    @Inject
    private BookingService bookingService;
    @Injectable
    private ChannelService channelService;
    @Injectable
    private BroadcastService broadcastService;
    @Injectable
    @Inject
    private RoomUseService roomUseService;
    @Inject
    private RoomUseProvider roomUseProvider;

    @Test(dataProvider = "testMoveBooking")
    public void testMoveBooking(final GroupRoomUseDto dto) {
        List<Map<String, Object>> confirmed = new ArrayList<>();
        confirmed.add(new HashMap<String, Object>());
        final WubookRoomUses wubookRoomUses = new WubookRoomUses(confirmed, new ArrayList<Map<String, Object>>());
        Hotel hotel = hotelProvider.getPersistentEntity();

        new NonStrictExpectations(channelService) {{
            channelService.fetchBookings(null, null, null, null);
            result = wubookRoomUses;
            channelService.parseRoomUse(new HashMap<String, Object>(), new HashSet<Long>());
            result = dto;
        }};

        new NonStrictExpectations(bookingService) {{
            RoomUseWithOverrides capture = withCapture(new ArrayList<RoomUseWithOverrides>());
            bookingService.handleWithoutRoom(capture, false);
            result = bookingService.handleWithoutRoom(capture, false);
        }};

        mockController.fetchBookings(null, null, hotel, null, null);

        new Verifications() {{
            for (RoomUseWithOverrides useWithOverrides : dto.getRoomUses()) {
                bookingService.handleWithoutRoom(useWithOverrides, false);
            }
        }};
    }

    @DataProvider(name = "testMoveBooking")
    public Object[][] getData_testMoveBooking() {
        List<Object[]> result = new ArrayList<>(8);
        result.add(new Object[]{getDto(TOTAL_ROOM_USES)});
        result.add(new Object[]{getDto(1)});
        return result.toArray(new Object[result.size()][]);
    }

    private GroupRoomUseDto getDto(int roomUses) {
        final LocalDate startDate = LocalDate.now(DateTimeZone.UTC);
        final LocalDate endDate = startDate.plusDays(10);

        final String rcode = "rcode" + roomUses;
        final CustomerGroup group = customerGroupProvider.getTransientEntity();
        List<RoomUseWithOverrides> ruWithOverrides = new ArrayList<>();
        for (int i = 0; i < roomUses; i++) {
            ruWithOverrides.add(new RoomUseWithOverrides(roomUseProvider.getTransientEntity(new Visitor<RoomUse>() {
                @Override
                public void visit(RoomUse entity) {
                    entity.setCustomerGroup(group);
                    entity.setRcode(rcode);
                    entity.setStartDate(startDate);
                    entity.setEndDate(endDate);
                    entity.setRoom(null);
                }
            })));
        }
        return new GroupRoomUseDto(rcode, group, ruWithOverrides);
    }

    @Override
    protected String getUrl() {
        return BookingController.URL + "/";
    }
}
