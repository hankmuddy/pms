package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.domain.extranet.CustomerGroup;
import com.idgindigo.pms.domain.pms.BaseServiceUse;
import com.idgindigo.pms.domain.pms.Bill;
import com.idgindigo.pms.domain.pms.LivingUse;
import com.idgindigo.pms.domain.pms.Refund;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.repository.pms.BaseServiceUseRepository;
import com.idgindigo.pms.repository.pms.BillRepository;
import com.idgindigo.pms.repository.pms.LivingUseRepository;
import com.idgindigo.pms.repository.pms.RefundRepository;
import com.idgindigo.pms.service.admin.SettingsService;
import com.idgindigo.pms.service.pms.TouristTaxService;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.PriceUtils;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.extranet.CustomerGroupProvider;
import com.idgindigo.pms.utils.pms.BankDetailsProvider;
import com.idgindigo.pms.utils.pms.RoomUseProvider;
import com.idgindigo.pms.web.controller.DiscountDto;
import com.idgindigo.pms.web.controller.InMemoryDbWebTest;
import com.idgindigo.pms.web.controller.extranet.CustomerGroupController;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultMatcher;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

/**
 * @author valentyn_vakatsiienko
 * @since 07.01.14 14:10
 */
public class DiscountTest extends InMemoryDbWebTest {
    public static final int DAYS = 5;
    public static final int NIGHTS = DAYS - 1;

    @Inject
    private CustomerGroupProvider customerGroupProvider;
    @Inject
    private RoomUseProvider roomUseProvider;
    @Inject
    private BillRepository billRepository;
    @Inject
    private BaseServiceUseRepository baseServiceUseRepository;
    @Inject
    private RefundRepository refundRepository;
    @Inject
    private LivingUseRepository livingUseRepository;
    @Inject
    private TouristTaxService touristTaxService;
    @Inject
    private SettingsService settingsService;
    @Inject
    private BankDetailsProvider bankDetailsProvider;

    @Test
    public void testNonExistingGroup() throws Exception {
        CustomerGroup group = customerGroupProvider.getPersistentEntity();
        long id;
        do {
            id = EntityProvider.randomPositiveLong();
        } while (customerGroupProvider.getRepository().exists(id));
        group.setId(id);
        trySetDiscount(group, 100, status().isNotFound());
    }

    @Test(dataProvider = "testDiscount")
    public void testDiscount(CustomerGroup group, int discount, int paidPercent) throws Exception {
        List<Bill> bills = billRepository.findByGroup(group);
        assertEquals(bills.size(), 1);

        Bill billBefore = bills.get(0);
        if (group.getDiscount() == 100) {
            assertEquals(billBefore.getTotal().longValue(), 0L);
        } else {
            assertNotEquals(billBefore.getTotal(), 0L);
        }

        long paid = billBefore.getTotal() * paidPercent / 100;
        billBefore.setTotalPaid(paid);
        billRepository.updateTotalPaid(billBefore, paid);

        List<LivingUse> livingUses = livingUseRepository.findByBillGroup(group);
        List<BaseServiceUse> serviceUses = baseServiceUseRepository.findByBill(billBefore);
        assertTrue(livingUses.containsAll(serviceUses));//Check if there are only living uses in bill

        setDiscount(group, discount);
        long totalAfter = 0L;
        for (LivingUse livingUse : livingUseRepository.findByBillGroup(group)) {
            assertEquals(livingUse.getTotal().longValue(), livingUse.getRawTotal() - PriceUtils.getDiscountValue(livingUse.getRawTotal(), discount) + touristTaxService.getTaxAmount(livingUse.getRawTotal(), discount, livingUse.getTourismTax()));
            totalAfter += livingUse.getTotal();
        }
        Bill billAfter = billRepository.findOne(billBefore.getId());
        assertEquals(billAfter.getTotal().longValue(), totalAfter);

        List<Refund> refunds = refundRepository.findByGroup(group);
        if (paid > billAfter.getTotal()) {
            assertTrue(refunds.size() == 1);
            Refund refund = refunds.get(0);
            assertEquals(refund.getTotal().longValue(), paid - billAfter.getTotal());
        } else {
            assertTrue(refunds.isEmpty());
        }
    }

    @DataProvider(name = "testDiscount")
    public Object[][] getData_testDiscount() throws Exception {
        List<Object[]> result = new ArrayList<Object[]>(64);
        int[] values = {0, 33, 66, 100};
        for (int initialDiscount : values) {
            for (int discount : values) {
                for (int paid : values) {
                    result.add(new Object[]{createGroup(initialDiscount), discount, paid});
                }
            }
        }
        return result.toArray(new Object[result.size()][]);
    }

    private void trySetDiscount(CustomerGroup group, int discount, ResultMatcher matcher) throws Exception {
        mvc.perform(preparePut(group.getId() + "/discount")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new DiscountDto(discount, bankDetailsProvider.getPersistentEntity())))).andExpect(matcher);
    }

    private void setDiscount(CustomerGroup group, int discount) throws Exception {
        trySetDiscount(group, discount, status().isOk());
    }

    private CustomerGroup createGroup() throws Exception {
        return createGroup(0);
    }

    private CustomerGroup createGroup(final int discount) throws Exception {
        final CustomerGroup group = customerGroupProvider.getPersistentEntity(new Visitor<CustomerGroup>() {
            @Override
            public void visit(CustomerGroup entity) {
                entity.setDiscount(discount);
            }
        });
        RoomUse roomUse = roomUseProvider.getTransientEntity(new Visitor<RoomUse>() {
            @Override
            public void visit(RoomUse entity) {
                entity.setStartDate(settingsService.getHotelDate());
                entity.setEndDate(entity.getStartDate().plusDays(NIGHTS));
                entity.setCustomerGroup(group);
                entity.setCustomerPays(true);
            }
        });
        mvc.perform(preparePost(roomUse, RoomUseController.URL, true)).andExpect(status().isCreated());
        return group;
    }

    @Override
    protected String getUrl() {
        return CustomerGroupController.URL + "/";
    }
}
