package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.domain.extranet.CustomerGroup;
import com.idgindigo.pms.domain.extranet.GroupMember;
import com.idgindigo.pms.domain.extranet.plan.Plan;
import com.idgindigo.pms.domain.extranet.roomtype.BaseRoom;
import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.domain.extranet.roomtype.VirtualRoom;
import com.idgindigo.pms.domain.extranet.service.Living;
import com.idgindigo.pms.domain.extranet.service.Service;
import com.idgindigo.pms.domain.pms.BaseServiceUse;
import com.idgindigo.pms.domain.pms.Bill;
import com.idgindigo.pms.domain.pms.Room;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.repository.pms.BaseServiceUseRepository;
import com.idgindigo.pms.repository.pms.BillRepository;
import com.idgindigo.pms.repository.pms.RoomUseRepository;
import com.idgindigo.pms.service.admin.SettingsService;
import com.idgindigo.pms.service.pms.SystemServiceService;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.extranet.AdultProvider;
import com.idgindigo.pms.utils.extranet.CustomerGroupProvider;
import com.idgindigo.pms.utils.extranet.LivingProvider;
import com.idgindigo.pms.utils.extranet.VirtualRoomProvider;
import com.idgindigo.pms.utils.extranet.plan.PlanProvider;
import com.idgindigo.pms.utils.pms.ChildProvider;
import com.idgindigo.pms.utils.pms.RoomProvider;
import com.idgindigo.pms.utils.pms.RoomUseProvider;
import com.idgindigo.pms.web.controller.InMemoryDbWebTest;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Status.REFUSE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertEquals;

/**
 * @author valentyn_vakatsiienko
 * @since 12/17/13 3:02 PM
 */
public class AdditionalBedsTest extends InMemoryDbWebTest<RoomUse> {
    public static final int TOTAL_DAYS = 5;
    public static final int DAYS_TO_ADD = TOTAL_DAYS - 1;
    public static final int DAYS_BEFORE_REFUSE = DAYS_TO_ADD / 2;
    public static final int MAX_ADDITIONAL = 100;
    public static final int MAX_ADULTS = 5;
    public static final int MAX_CHILDREN = 5;
    public static final LocalDate NOW = new LocalDate(DateTimeZone.UTC);

    @Inject
    private RoomUseProvider provider;
    @Inject
    private LivingProvider livingProvider;
    @Inject
    private PlanProvider planProvider;
    @Inject
    private VirtualRoomProvider virtualRoomProvider;
    @Inject
    private RoomProvider roomProvider;
    @Inject
    private CustomerGroupProvider customerGroupProvider;
    @Inject
    private SystemServiceService systemServiceService;
    @Inject
    private BillRepository billRepository;
    @Inject
    private BaseServiceUseRepository baseServiceUseRepository;
    @Inject
    private RoomUseRepository roomUseRepository;
    @Inject
    private AdultProvider adultProvider;
    @Inject
    private ChildProvider childProvider;
    @Inject
    private SettingsService settingsService;

    @Test(dataProvider = "additionalBedTest", enabled = false)
    public void testAdditionalBedUsesCreation(RoomUse roomUse, int additionalAdults, int additionalChildren) {
        Service adultBed = systemServiceService.getAdultBedService();
        Service childBed = systemServiceService.getChildBedService();

        Bill bill = billRepository.findByRoomUse(roomUse).get(0);
        List<BaseServiceUse> serviceUses = baseServiceUseRepository.findByBill(bill);
        int expectedAdultBedServiceUsesQuantity = additionalAdults > 0 ? DAYS_TO_ADD : 0;
        int expectedChildBedServiceUsesQuantity = additionalChildren > 0 ? DAYS_TO_ADD : 0;
        assertEquals(serviceUses.size(), DAYS_TO_ADD + expectedAdultBedServiceUsesQuantity + expectedChildBedServiceUsesQuantity);

        checkAdditionalBedServiceUses(roomUse, additionalAdults, adultBed, expectedAdultBedServiceUsesQuantity);
        checkAdditionalBedServiceUses(roomUse, additionalChildren, childBed, expectedChildBedServiceUsesQuantity);
    }

    private void checkAdditionalBedServiceUses(RoomUse roomUse, int additionalBeds, Service bedService, int expectedBedServiceUsesQuantity) {
        List<BaseServiceUse> bedServiceUses = baseServiceUseRepository.findByServiceAndBillRoomUseCustomerGroup(bedService, roomUse.getCustomerGroup());
        assertEquals(bedServiceUses.size(), expectedBedServiceUsesQuantity);
        for (BaseServiceUse bedServiceUse : bedServiceUses) {
            assertEquals(bedServiceUse.getQuantity().intValue(), additionalBeds);
        }
    }

    @Test(dataProvider = "additionalBedTest", enabled = false)
    public void testRefuse(RoomUse roomUse, int additionalAdults, int additionalChildren) throws Exception {
        RoomUseController.RefuseDto dto = new RoomUseController.RefuseDto();
        dto.setDate(NOW.plusDays(DAYS_BEFORE_REFUSE));
        mvc.perform(preparePost(dto, roomUse.getId() + "/refused")).andExpect(status().isOk());
        roomUse = roomUseRepository.findOne(roomUse.getId());
        //Find the created refuse
        List<RoomUse> roomUses = roomUseRepository.findByCustomerGroup(roomUse.getCustomerGroup());
        RoomUse refuse = roomUses.get(0).getStatus() == REFUSE ? roomUses.get(0) : roomUses.get(1);

        //Service uses by refuse
        validateAdditionalServiceUses(additionalAdults, additionalChildren, refuse);
    }

    @Test(dataProvider = "additionalBedTest", enabled = false)
    public void testMove(RoomUse roomUse, final int additionalAdults, final int additionalChildren) throws Exception {
        //Create moveDto and perform move
        final RoomType roomType = roomUse.getRoom().getRoomType();
        RoomUseController.MoveDto moveDto = new RoomUseController.MoveDto();
        final Plan plan = planProvider.getPersistentEntity();
        moveDto.setUpgrade(false);
        moveDto.setSinceDate(roomUse.getStartDate().plusDays(DAYS_BEFORE_REFUSE));
        final BaseRoom baseRoom = virtualRoomProvider.getPersistentEntity(new Visitor<VirtualRoom>() {
            @Override
            public void visit(VirtualRoom entity) {
                entity.setAdditional(MAX_ADDITIONAL);
                entity.setAdults(MAX_ADULTS);
                entity.setChildren(MAX_CHILDREN);
                entity.setRoomType(roomType);
            }
        });
        moveDto.setBaseRoom(baseRoom);
        moveDto.setRoom(roomProvider.getPersistentEntity(new Visitor<Room>() {
            @Override
            public void visit(Room entity) {
                entity.setRoomType(roomType);
            }
        }));
        moveDto.setPlan(plan);
        livingProvider.getPersistentEntity(new Visitor<Living>() {
            @Override
            public void visit(Living entity) {
                entity.setPlan(plan);
                entity.setRoom(baseRoom);
            }
        });
        mvc.perform(post(getUrl() + roomUse.getId() + "/moved")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(moveDto)))
                .andExpect(status().isOk());
        roomUse = roomUseRepository.findOne(roomUse.getId());

        //Find the created room uses
        List<RoomUse> roomUses = roomUseRepository.findByCustomerGroup(roomUse.getCustomerGroup());
        RoomUse move = null;
        RoomUse refuse = null;
        for (RoomUse use : roomUses) {
            if (!use.equals(roomUse)) {
                if (use.getStatus() == REFUSE) {
                    refuse = use;
                } else {
                    move = use;
                }
            }
        }
        validateAdditionalServiceUses(additionalAdults, additionalChildren, refuse);
        validateAdditionalServiceUses(additionalAdults, additionalChildren, move);
    }

    private void validateAdditionalServiceUses(int additionalAdults, int additionalChildren, RoomUse afterAction) {
        Service adultBed = systemServiceService.getAdultBedService();
        Service childBed = systemServiceService.getChildBedService();

        List<BaseServiceUse> serviceUsesByRefuse = baseServiceUseRepository.findByRoomUseAndAfterDateIncluding(afterAction, afterAction.getStartDate());
        int expectedAdultBedServiceUsesQuantity = additionalAdults > 0 ? DAYS_TO_ADD - DAYS_BEFORE_REFUSE : 0;
        int expectedChildBedServiceUsesQuantity = additionalChildren > 0 ? DAYS_TO_ADD - DAYS_BEFORE_REFUSE : 0;

        int actualAdultBedUses = 0;
        int actualChildBedUses = 0;
        for (BaseServiceUse serviceUse : serviceUsesByRefuse) {
            if (serviceUse.getService().equals(adultBed)) {
                actualAdultBedUses++;
                assertEquals(serviceUse.getQuantity().intValue(), additionalAdults);

            } else if (serviceUse.getService().equals(childBed)) {
                actualChildBedUses++;
                assertEquals(serviceUse.getQuantity().intValue(), additionalChildren);
            }
        }
        assertEquals(actualAdultBedUses, expectedAdultBedServiceUsesQuantity);
        assertEquals(actualChildBedUses, expectedChildBedServiceUsesQuantity);
    }

    @DataProvider(name = "additionalBedTest")
    public Object[][] getData() throws Exception {
        List<Object[]> result = new ArrayList<Object[]>(7);
        int adults = 5;
        int children = 0;
        RoomUse roomUse = create(adults, children);
        result.add(new Object[]{roomUse, 0, 0});

        adults = 3;
        children = 2;
        roomUse = create(adults, children);
        result.add(new Object[]{roomUse, 0, 0});

        adults = 5;
        children = 1;
        roomUse = create(adults, children);
        result.add(new Object[]{roomUse, 0, 0});

        adults = 6;
        children = 1;
        roomUse = create(adults, children);
        result.add(new Object[]{roomUse, 1, 0});

        adults = 6;
        children = 0;
        roomUse = create(adults, children);
        result.add(new Object[]{roomUse, 1, 0});

        adults = 50;
        children = 0;
        roomUse = create(adults, children);
        result.add(new Object[]{roomUse, 45, 0});

        adults = 2;
        children = 50;
        roomUse = create(adults, children);
        result.add(new Object[]{roomUse, 0, 45});

        adults = 25;
        children = 25;
        roomUse = create(adults, children);
        result.add(new Object[]{roomUse, 20, 20});
        return result.toArray(new Object[result.size()][]);
    }

    public RoomUse create(final int adults, final int children) throws Exception {
        final int maxAdditional = MAX_ADDITIONAL;
        final int maxAdults = MAX_ADULTS;
        final int maxChildren = MAX_CHILDREN;
        final List<GroupMember> members = new LinkedList<>();
        for (int i = 0; i < adults; i++) {
            members.add(new GroupMember(adultProvider.getPersistentEntity(), null));
        }
        for (int i = 0; i < children; i++) {
            members.add(new GroupMember(childProvider.getPersistentEntity(), null));
        }
        final BaseRoom baseRoom = virtualRoomProvider.getPersistentEntity(new Visitor<VirtualRoom>() {
            @Override
            public void visit(VirtualRoom virtualRoom) {
                virtualRoom.setAdditional(maxAdditional);
                virtualRoom.setAdults(maxAdults);
                virtualRoom.setChildren(maxChildren);
            }
        });
        final Plan plan = planProvider.getPersistentEntity();
        RoomUse roomUse = provider.getTransientEntity(new Visitor<RoomUse>() {
            @Override
            public void visit(final RoomUse entity) {
                entity.setStartDate(settingsService.getHotelDate());
                entity.setEndDate(entity.getStartDate().plusDays(DAYS_TO_ADD));
                entity.setCustomerGroup(customerGroupProvider.getTransientEntity(new Visitor<CustomerGroup>() {
                    @Override
                    public void visit(CustomerGroup entity) {
                        entity.setGroupMembers(members);
                    }
                }));
                entity.setBaseRoom(baseRoom);
                entity.setRoom(roomProvider.getPersistentEntity(new Visitor<Room>() {
                    @Override
                    public void visit(Room entity) {
                        entity.setRoomType(baseRoom.roomType());
                    }
                }));
                entity.setPlan(plan);
            }
        });
        livingProvider.getPersistentEntity(new Visitor<Living>() {
            @Override
            public void visit(Living entity) {
                entity.setRoom(baseRoom);
                entity.setPlan(plan);
            }
        });
        MockHttpServletResponse response = mvc.perform(preparePost(roomUse)).andExpect(status().isCreated()).andReturn().getResponse();
        roomUse = convertResponseWithSingleObject(objectMapper, response.getContentAsString(), RoomUse.class);
        //Confirm and check in the roomUse
        mvc.perform(preparePost(roomUse.getId() + "/confirmed")).andExpect(status().isOk());
        RoomUseController.CheckDto dto = new RoomUseController.CheckDto(settingsService.getHotelTime());
        mvc.perform(preparePost(dto, roomUse.getId() + "/checkedIn")).andExpect(status().isOk());
        return roomUse;
    }

    @Override
    protected String getUrl() {
        return RoomUseController.URL + "/";
    }
}
