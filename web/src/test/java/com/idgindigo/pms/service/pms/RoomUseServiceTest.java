package com.idgindigo.pms.service.pms;

import com.idgindigo.pms.domain.extranet.BaseGroupRoomUse;
import com.idgindigo.pms.domain.extranet.GroupMember;
import com.idgindigo.pms.domain.extranet.plan.Plan;
import com.idgindigo.pms.domain.extranet.roomtype.BaseRoom;
import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeValue;
import com.idgindigo.pms.domain.extranet.service.Living;
import com.idgindigo.pms.domain.pms.*;
import com.idgindigo.pms.repository.extranet.rate.RoomTypeValueRepository;
import com.idgindigo.pms.repository.extranet.roomuse.BaseGroupRoomUseRepository;
import com.idgindigo.pms.repository.pms.BillRepository;
import com.idgindigo.pms.repository.pms.GroupMemberToRoomUseRepository;
import com.idgindigo.pms.repository.pms.RoomUseRepository;
import com.idgindigo.pms.repository.pms.SimpleServiceUseRepository;
import com.idgindigo.pms.restutils.exception.RefundException;
import com.idgindigo.pms.restutils.exception.RoomUseException;
import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.service.ServiceTest;
import com.idgindigo.pms.service.admin.SettingsService;
import com.idgindigo.pms.utils.RoomUseUtils;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.extranet.GroupMemberProvider;
import com.idgindigo.pms.utils.extranet.LivingProvider;
import com.idgindigo.pms.utils.extranet.RoomTypeValueProvider;
import com.idgindigo.pms.utils.pms.GroupMemberToRoomUseProvider;
import com.idgindigo.pms.utils.pms.RoomProvider;
import com.idgindigo.pms.utils.pms.RoomUseProvider;
import com.idgindigo.pms.utils.pms.SimpleServiceProvider;
import com.idgindigo.pms.web.controller.pms.RoomUseController;
import mockit.NonStrictExpectations;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.dao.DataIntegrityViolationException;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.BOOKING;
import static com.idgindigo.pms.utils.EntityProvider.randomString;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


/**
 * @author valentyn_vakatsiienko
 * @since 11/20/13 11:29 AM
 */
public class RoomUseServiceTest extends ServiceTest {
    @Inject
    private RoomUseProvider roomUseProvider;
    @Inject
    private RoomUseService roomUseService;
    @Inject
    private LivingProvider livingProvider;
    @Inject
    private RoomTypeValueProvider roomTypeValueProvider;
    @Inject
    private RoomTypeValueRepository roomTypeValueRepository;
    @Inject
    private RoomProvider roomProvider;
    @Inject
    private RoomUseRepository repository;
    @Inject
    private BaseGroupRoomUseRepository baseGroupRoomUseRepository;
    @Inject
    private SettingsService settingsService;
    @Inject
    private BillRepository billRepository;
    @Inject
    private GroupMemberProvider groupMemberProvider;
    @Inject
    private RoomUseUtils roomUseUtils;
    @Inject
    private GroupMemberToRoomUseProvider groupMemberToRoomUseProvider;
    @Inject
    private GroupMemberToRoomUseRepository groupMemberToRoomUseRepository;

    @Test(expectedExceptions = RefundException.class, expectedExceptionsMessageRegExp = "refund.bankDetails.required")
    public void testForcedRefund() {
        int ratesQuantity = 5;
        final Living living = livingProvider.getPersistentEntity();
        final BaseRoom baseRoom = living.getRoom();
        final Plan plan = living.getPlan();
        final List<RoomTypeValue> roomTypeValues = getRates(living, ratesQuantity, 2);

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
                entity.setRcode(randomString());
                entity.setTotal(100L);
                entity.setTotalPaid(1L);
            }
        });
        Map<LocalDate, Long> prices = new HashMap<>();
        RoomUse finalRoomUse = roomUseService.create(roomUse, prices, true);
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

        roomUseService.refuse(roomUse, roomTypeValues.get(1).getDate(), true, null);
    }

    @Inject
    private SimpleServiceProvider simpleServiceProvider;

    @Inject
    private SimpleServiceUseRepository simpleServiceUseRepository;

    @Test
    public void testRoomUseCreate() throws Exception {
        int ratesQuantity = 5;
        final Living living = livingProvider.getPersistentEntity();
        final BaseRoom baseRoom = living.getRoom();
        final Plan plan = living.getPlan();
        final List<RoomTypeValue> roomTypeValues = getRates(living, ratesQuantity, 10);

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
            }
        });

        roomUseService.create(roomUse, true);

        //Check that roomUseService decreases quota of rates(1) to rates(size -2 (check out day))
        for (int i = 1; i < ratesQuantity - 2; i++) {
            RoomTypeValue roomTypeValue = roomTypeValues.get(i);
            int roomsAvailableBefore = roomTypeValue.getRoomsAvailable();
            int roomsAvailableAfter = roomTypeValueRepository.findOne(roomTypeValue.getId()).getRoomsAvailable();
            assertEquals(roomsAvailableAfter, roomsAvailableBefore - 1);
        }

        //Check that the service doesnt decrease quota of rate(size -2) (check out day)
        RoomTypeValue roomTypeValue = roomTypeValues.get(roomTypeValues.size() - 2);
        Integer after = roomTypeValueRepository.findOne(roomTypeValue.getId()).getRoomsAvailable();
        assertEquals(after, roomTypeValue.getRoomsAvailable());

        //Check that service doesn`t touch the first and the last rate
        roomTypeValue = roomTypeValues.get(0);
        int roomsAvailableAfter = roomTypeValueRepository.findOne(roomTypeValue.getId()).getRoomsAvailable();
        int roomsAvailableBefore = roomTypeValue.getRoomsAvailable();
        assertEquals(roomsAvailableAfter, roomsAvailableBefore);

        roomTypeValue = roomTypeValues.get(roomTypeValues.size() - 1);
        roomsAvailableAfter = roomTypeValueRepository.findOne(roomTypeValue.getId()).getRoomsAvailable();
        roomsAvailableBefore = roomTypeValue.getRoomsAvailable();
        assertEquals(roomsAvailableAfter, roomsAvailableBefore);
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void testRoomsAvailableCanNotBecomeNegative() throws Exception {
        final Living item = livingProvider.getPersistentEntity();
        final List<RoomTypeValue> roomTypeValues = getRates(item, 3, 0);
        RoomUse roomUse = roomUseProvider.getTransientEntity(new Visitor<RoomUse>() {
            @Override
            public void visit(RoomUse entity) {
                entity.setBaseRoom(item.getRoom());
                entity.setPlan(item.getPlan());
                entity.setRoom(roomProvider.getPersistentEntity(new Visitor<Room>() {
                    @Override
                    public void visit(Room entity) {
                        entity.setRoomType(item.getRoom().roomType());
                    }
                }));
                entity.setStartDate(roomTypeValues.get(0).getDate());
                entity.setEndDate(roomTypeValues.get(roomTypeValues.size() - 1).getDate());
            }
        });

        roomUseService.create(roomUse, true);
    }

    @Test
    public void testCascadeGroupAndPerson() throws Exception {
        final RoomUse roomUse = roomUseProvider.getTransientEntity(new Visitor<RoomUse>() {
            @Override
            public void visit(RoomUse entity) {
                entity.setEndDate(entity.getStartDate().plusDays(5));
            }
        });
        final RoomUse saved = roomUseService.create(roomUse, true);
        final GroupMember member = groupMemberProvider.getPersistentEntity(new Visitor<GroupMember>() {
            @Override
            public void visit(GroupMember entity) {
                entity.setCustomerGroup(saved.getCustomerGroup());
            }
        });
        GroupMemberToRoomUse linkage = groupMemberToRoomUseProvider.getPersistentEntity(new Visitor<GroupMemberToRoomUse>() {
            @Override
            public void visit(GroupMemberToRoomUse entity) {
                entity.setRoomUse(saved);
                entity.setGroupMember(member);
            }
        });
        List<GroupMemberToRoomUse> initial = groupMemberToRoomUseRepository.findByRoomUse(saved);
        List<GroupMemberToRoomUse> initialByPersonId = groupMemberToRoomUseRepository.findByPersonId(member.getPerson().getId());

        //Move
        RoomUseController.MoveDto moveDto = new RoomUseController.MoveDto();
        moveDto.setPlan(saved.getPlan());
        moveDto.setRoom(roomProvider.getPersistentEntity(new Visitor<Room>() {
            @Override
            public void visit(Room entity) {
                entity.setRoomType(roomUse.getRoom().getRoomType());
            }
        }));
        moveDto.setSinceDate(saved.getStartDate().plusDays(1));
        moveDto.setUpgrade(true);
        moveDto.setBaseRoom(roomUse.getBaseRoom());

        roomUseService.move(saved, moveDto, false, null);

        //Get cascaded group members
        List<GroupMemberToRoomUse> cascaded = groupMemberToRoomUseRepository.findByRoomUse(repository.getMovedTo(saved));
        assertEquals(cascaded.size(), initial.size());

        List<GroupMember> cascadedMembers = new ArrayList<>(cascaded.size());
        for (GroupMemberToRoomUse groupMemberToRoomUse : cascaded) {
            cascadedMembers.add(groupMemberToRoomUse.getGroupMember());
        }

        List<GroupMember> initialMembers = new ArrayList<>(cascaded.size());
        for (GroupMemberToRoomUse groupMemberToRoomUse : initial) {
            initialMembers.add(groupMemberToRoomUse.getGroupMember());
        }

        assertTrue(cascadedMembers.containsAll(initialMembers));

        List<GroupMemberToRoomUse> byPersonId = groupMemberToRoomUseRepository.findByPersonId(member.getPerson().getId());
        Assert.assertEquals(byPersonId.size(), initialByPersonId.size() + 1);
        Assert.assertTrue(byPersonId.containsAll(initialByPersonId));
    }

    @Test
    public void testCheckout() {
        RoomUse roomUse = roomUseProvider.getTransientEntity();
        roomUse = roomUseService.create(roomUse, true);
        repository.setStatus(RoomUse.Status.BOOKING_WARRANTY, roomUse.getId(), SecurityUtils.getCurrentUser(), LocalDateTime.now());
        roomUseUtils.populate(roomUse.getCustomerGroup(), roomUse);
        roomUseService.checkIn(roomUse, roomUse.getStartDate().toDateTimeAtStartOfDay(DateTimeZone.UTC).toLocalDateTime(), true);
        roomUse = repository.findOne(roomUse.getId());

        final LocalDate endDate = roomUse.getEndDate();
        new NonStrictExpectations(settingsService) {{
            settingsService.getHotelDate();
            result = endDate;
        }};

        try {
            roomUseService.checkOut(roomUse, roomUse.getEndDate().toDateTimeAtStartOfDay(DateTimeZone.UTC).toLocalDateTime());
            throw new AssertionError("Shouldn`t happen");
        } catch (RoomUseException e) {
            assertEquals(e.getMessage(), RoomUseException.OUTGO_FOR_FULLY_PAID);
        }

        for (Bill bill : billRepository.findByRoomUse(roomUse)) {
            billRepository.setForCustomer(bill);
        }
        roomUseService.checkOut(roomUse, roomUse.getEndDate().toDateTimeAtStartOfDay(DateTimeZone.UTC).toLocalDateTime());
        assertEquals(baseGroupRoomUseRepository.getStatus(roomUse.getId()), BaseGroupRoomUse.Status.OUTGO);
    }

    private List<RoomTypeValue> getRates(Living item, int quantity, int quota) {
        return getRates(item, quantity, quota, roomTypeValueProvider);
    }

    public static List<RoomTypeValue> getRates(Living item, int quantity, int quota, RoomTypeValueProvider roomTypeValueProvider) {
        List<RoomTypeValue> roomTypeValues = new ArrayList<RoomTypeValue>(quantity);
        for (int i = 0; i < quantity; i++) {
            roomTypeValues.add(roomTypeValueProvider.getRepository().findOne(roomTypeValueProvider.getPersistentEntity(getLivingRateVisitor(item, quota, i)).getId()));
        }
        return roomTypeValues;
    }

    private static Visitor<RoomTypeValue> getLivingRateVisitor(final Living item, final int quota, final int daysOffset) {
        return new Visitor<RoomTypeValue>() {
            @Override
            public void visit(RoomTypeValue entity) {
                entity.setDate(LocalDate.now(DateTimeZone.forID("UTC")).plusDays(daysOffset));
                entity.setRoomType(item.getRoom().roomType());
                entity.setRoomsAvailable(quota);
            }
        };
    }
}
