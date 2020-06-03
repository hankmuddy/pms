package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.domain.extranet.CustomerGroup;
import com.idgindigo.pms.domain.pms.Bill;
import com.idgindigo.pms.domain.pms.LivingUse;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.repository.pms.BillRepository;
import com.idgindigo.pms.repository.pms.LivingUseRepository;
import com.idgindigo.pms.service.pms.RoomUseService;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.extranet.CustomerGroupProvider;
import com.idgindigo.pms.utils.pms.RoomUseProvider;
import com.idgindigo.pms.web.controller.InMemoryDbWebTest;
import org.joda.time.Days;
import org.springframework.mock.web.MockHttpServletResponse;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static com.idgindigo.pms.service.pms.RoomUseService.RoomUseWithOverrides;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertEquals;

/**
 * @author valentyn_vakatsiienko
 * @since 1/22/14 2:34 PM
 */
public class GroupRoomUseTest extends InMemoryDbWebTest<RoomUse> {
    public static final int ROOM_USES = 5;
    @Inject
    private CustomerGroupProvider customerGroupProvider;
    @Inject
    private RoomUseProvider roomUseProvider;
    @Inject
    private BillRepository billRepository;
    @Inject
    private LivingUseRepository livingUseRepository;

    @Test
    public void testCreate() throws Exception {
        RoomUseService.GroupRoomUseDto dto = new RoomUseService.GroupRoomUseDto();
        CustomerGroup group = customerGroupProvider.getTransientEntity();
        List<RoomUse> roomUses = new ArrayList<>(ROOM_USES);
        for (int i = 0; i < ROOM_USES; i++) {
            roomUses.add(roomUseProvider.getTransientEntity(new Visitor<RoomUse>() {
                @Override
                public void visit(RoomUse entity) {
                    entity.setCustomerPays(true);
                }
            }));
        }
        dto.setGroup(group);
        List<RoomUseWithOverrides> roomUseWithOverrides = new ArrayList<>();
        for (RoomUse roomUse : roomUses) {
            roomUseWithOverrides.add(new RoomUseWithOverrides(roomUse));
        }
        dto.setRoomUses(roomUseWithOverrides);

        MockHttpServletResponse response = mvc.perform(preparePost(dto, "group")).andExpect(status().isCreated()).andReturn().getResponse();
        List<RoomUse> saved = convertResponseWithObjectList(objectMapper, response, RoomUse.class);
        assertEquals(saved.size(), ROOM_USES);

        CustomerGroup savedGroup = saved.get(0).getCustomerGroup();
        for (RoomUse roomUse : saved) {
            assertEquals(roomUse.getCustomerGroup(), savedGroup);
        }

        List<Bill> bills = billRepository.findByGroup(savedGroup);
        assertEquals(bills.size(), 1);

        int expectedDays = calculateExpectedDays(roomUses);
        List<LivingUse> livingUses = livingUseRepository.findByBillGroup(savedGroup);
        assertEquals(livingUses.size(), expectedDays);
    }

    private int calculateExpectedDays(List<RoomUse> roomUses) {
        int res = 0;
        for (RoomUse roomUse : roomUses) {
            res += Days.daysBetween(roomUse.getStartDate(), roomUse.getEndDate()).getDays();
        }
        return res;
    }

    @Override
    protected String getUrl() {
        return RoomUseController.URL + "/";
    }
}
