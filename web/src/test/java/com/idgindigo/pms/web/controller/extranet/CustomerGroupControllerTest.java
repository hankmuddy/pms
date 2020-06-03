package com.idgindigo.pms.web.controller.extranet;

import com.idgindigo.pms.domain.extranet.CustomerGroup;
import com.idgindigo.pms.domain.pms.Bill;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.repository.extranet.CustomerGroupRepository;
import com.idgindigo.pms.restutils.exception.CustomerGroupException;
import com.idgindigo.pms.restutils.exception.RestFriendlyException;
import com.idgindigo.pms.service.pms.CustomerGroupService;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.extranet.CustomerGroupProvider;
import com.idgindigo.pms.utils.pms.BankDetailsProvider;
import com.idgindigo.pms.utils.pms.BillProvider;
import com.idgindigo.pms.utils.pms.RoomUseProvider;
import com.idgindigo.pms.web.controller.ApprovableControllerTest;
import com.idgindigo.pms.web.controller.DiscountDto;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Status.BOOKING_FREE;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Status.BOOKING_WARRANTY;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Status.LIVING;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Status.NOT_ARRIVED;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Status.OUTGO;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Status.REFUSE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author valentyn_vakatsiienko
 * @since 11/19/13 5:36 PM
 */
public class CustomerGroupControllerTest extends ApprovableControllerTest<CustomerGroup> {
    public static final ResultMatcher OK = status().isOk();
    public static final ResultMatcher BAD_REQUEST = status().isBadRequest();
    private static final int TOTAL_BILLS = 5;

    @Inject
    private BankDetailsProvider bankDetailsProvider;
    @Inject
    private CustomerGroupProvider provider;
    @Inject
    private RoomUseProvider roomUseProvider;
    @Inject
    private BillProvider billProvider;
    @Tested
    private CustomerGroupController mockController;
    @Injectable
    private CustomerGroupService customerGroupService;
    @Injectable @Inject
    private CustomerGroupRepository repository;

    @Test
    public void testCreateWithoutCustomer() throws Exception {
        CustomerGroup group = provider.getTransientEntity(new Visitor<CustomerGroup>() {
            @Override
            public void visit(CustomerGroup entity) {
                entity.setCompany(null);
                entity.setCustomer(null);
            }
        });
        testBadRequest(preparePost(group), RestFriendlyException.MAY_NOT_BE_NULL);
    }

    @Test(dataProvider = "testClose")
    public void testClose(CustomerGroup group, ResultMatcher result, String code) throws Exception {
        MockHttpServletRequestBuilder closeRequest = preparePut(group.getId() + "/closed");
        if (result == OK) {
            testOk(closeRequest);
        } else if (result == BAD_REQUEST) {
            testBadRequest(closeRequest, code);
        } else {
            throw new IllegalArgumentException("Unexpected result" + result);
        }
    }

    @DataProvider(name = "testClose")
    public Object[][] getData() {
        List<Object[]> result = new ArrayList<>(10);
        //Try close with not checked out room use
        List<List<RoomUse.Status>> suites = Arrays.asList(
                Arrays.asList(LIVING, OUTGO),
                Arrays.asList(BOOKING_FREE, BOOKING_WARRANTY, LIVING, OUTGO, REFUSE, NOT_ARRIVED),
                Arrays.asList(BOOKING_FREE),
                Arrays.asList(BOOKING_WARRANTY),
                Arrays.asList(LIVING)
        );
        for (List<RoomUse.Status> suite : suites) {
            addBadRequest(result, suite);
        }

        //Try close with all room uses checked out
        suites = Arrays.asList(
                Arrays.asList(OUTGO),
                Arrays.asList(OUTGO, OUTGO, OUTGO),
                Arrays.asList(NOT_ARRIVED),
                Arrays.asList(REFUSE),
                Arrays.asList(OUTGO, NOT_ARRIVED, REFUSE)
        );
        for (List<RoomUse.Status> suite : suites) {
            addOk(result, suite);
        }

        //Try close not fully paid
        addBadRequest(result, true);
        addBadRequest(result, false);
        //Try close fully paid
        addOk(result, true);
        addOk(result, false);
        return result.toArray(new Object[result.size()][]);
    }

    private void addBadRequest(List<Object[]> result, List<RoomUse.Status> statuses) {
        CustomerGroup group = createGroup(statuses);
        result.add(getBadRequest(group, CustomerGroupException.CLOSE_ROOM_USE_NOT_CHECKED_OUT));
    }

    private void addOk(List<Object[]> result, List<RoomUse.Status> statuses) {
        CustomerGroup group = createGroup(statuses);
        result.add(getOk(group));
    }

    private void addBadRequest(List<Object[]> result, boolean roomUsesFullyPaid) {
        CustomerGroup group = createGroup(false, roomUsesFullyPaid);
        result.add(getBadRequest(group, CustomerGroupException.CLOSE_ONLY_FOR_FULLY_PAID));
    }

    private void addOk(List<Object[]> result, boolean roomUsesFullyPaid) {
        CustomerGroup group = createGroup(true, roomUsesFullyPaid);
        result.add(getOk(group));
    }

    private Object[] getBadRequest(CustomerGroup group, String code) {
        return new Object[]{group, BAD_REQUEST, code};
    }

    private Object[] getOk(CustomerGroup group) {
        return new Object[]{group, OK, null};
    }

    private CustomerGroup createGroup(List<RoomUse.Status> statuses) {
        final CustomerGroup group = provider.getPersistentEntity();
        for (final RoomUse.Status status : statuses) {
            roomUseProvider.getPersistentEntity(new Visitor<RoomUse>() {
                @Override
                public void visit(RoomUse entity) {
                    entity.setCustomerGroup(group);
                    entity.setStatus(status);
                }
            });
        }
        return group;
    }

    private CustomerGroup createGroup(final boolean groupFullyPaid, boolean roomUsesFullyPaid) {
        final CustomerGroup group = provider.getPersistentEntity();
        final RoomUse roomUse = roomUseProvider.getPersistentEntity(new Visitor<RoomUse>() {
            @Override
            public void visit(RoomUse entity) {
                entity.setCustomerGroup(group);
                entity.setStatus(OUTGO);
            }
        });
        for (int i = 0; i < TOTAL_BILLS; i++) {
            createBill(true, groupFullyPaid, roomUse);
        }
        for (int i = 0; i < TOTAL_BILLS; i++) {
            createBill(false, roomUsesFullyPaid, roomUse);
        }
        return group;
    }

    private Bill createBill(final boolean forCustomer, final boolean fullyPaid, final RoomUse roomUse) {
        return billProvider.getPersistentEntity(new Visitor<Bill>() {
            @Override
            public void visit(Bill entity) {
                if (forCustomer) {
                    entity.setGroup(roomUse.getCustomerGroup());
                    entity.setRoomUse(null);
                } else {
                    entity.setRoomUse(roomUse);
                    entity.setGroup(null);
                }
                entity.setTotal(100L);
                if (fullyPaid) {
                    entity.setTotalPaid(entity.getTotal());
                }
            }
        });
    }

    @Test
    public void testSetDiscount() {
        final CustomerGroup group = provider.getPersistentEntity();
        final DiscountDto dto = new DiscountDto(66);

        mockController.setDiscount(dto, group.getId());

        new Verifications() {{
            customerGroupService.setDiscount(group, dto.getDiscount(), null);
        }};
    }

    @Override
    protected EntityProvider<CustomerGroup> getProvider() {
        return provider;
    }

    @Override
    protected String getUrl() {
        return CustomerGroupController.URL + "/";
    }
}
