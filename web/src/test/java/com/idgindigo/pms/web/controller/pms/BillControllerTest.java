package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.domain.extranet.BaseGroupRoomUse;
import com.idgindigo.pms.domain.pms.Bill;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.domain.pms.SimpleServiceUse;
import com.idgindigo.pms.restutils.exception.BillException;
import com.idgindigo.pms.restutils.exception.RestFriendlyException;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.pms.BillProvider;
import com.idgindigo.pms.utils.pms.RoomUseProvider;
import com.idgindigo.pms.utils.pms.SimpleServiceUseProvider;
import com.idgindigo.pms.web.controller.ApprovableControllerTest;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static com.idgindigo.pms.web.controller.pms.BillController.ForCustomerDto;

/**
 * @author valentyn_vakatsiienko
 * @since 11/22/13 11:47 AM
 */
public class BillControllerTest extends ApprovableControllerTest<Bill> {
    private static final int TOTAL_SERVICE_USES = 5;
    @Inject
    private BillProvider provider;
    @Inject
    private SimpleServiceUseProvider serviceUseProvider;
    @Inject
    private RoomUseProvider roomUseProvider;

    @Test
    public void testSetForRoomUseWithRoomUseCheckedOut() throws Exception {
        //Get bill for customer;
        final Bill bill = provider.getPersistentEntity(new Visitor<Bill>() {
            @Override
            public void visit(Bill entity) {
                entity.setGroup(entity.getRoomUse().getCustomerGroup());
                entity.setRoomUse(null);
            }
        });
        //Try set for checked out room use
        ForCustomerDto dto = new ForCustomerDto(roomUseProvider.getPersistentEntity(new Visitor<RoomUse>() {
            @Override
            public void visit(RoomUse entity) {
                entity.setCustomerGroup(bill.getGroup());
                entity.setStatus(BaseGroupRoomUse.Status.OUTGO);
            }
        }));
        testBadRequest(preparePut(dto, bill.getId() + "/forCustomer"), BillException.ROOM_USE_CHECKED_OUT);
    }

    @Test
    public void testDeleteWithServiceUses() throws Exception {
        //Create bill with service uses
        final Bill bill = provider.getPersistentEntity(new Visitor<Bill>() {
            @Override
            public void visit(Bill entity) {
                entity.setApproved(false);
            }
        });
        List<SimpleServiceUse> simpleServiceUses = new ArrayList<>(TOTAL_SERVICE_USES);
        for (int i = 0; i < TOTAL_SERVICE_USES; i++) {
            simpleServiceUses.add(serviceUseProvider.getPersistentEntity(new Visitor<SimpleServiceUse>() {
                @Override
                public void visit(SimpleServiceUse entity) {
                    entity.setBill(bill);
                }
            }));
        }
        //Try delete
        MockHttpServletRequestBuilder deleteRequest = prepareDelete(bill.getId());
        testBadRequest(deleteRequest, RestFriendlyException.ENTITY_IS_USED);
        //Remove service uses
        serviceUseProvider.getRepository().delete(simpleServiceUses);
        //Try delete
        testOk(deleteRequest);
    }

    @Override
    protected EntityProvider<Bill> getProvider() {
        return provider;
    }

    @Override
    protected String getUrl() {
        return BillController.URL + "/";
    }
}