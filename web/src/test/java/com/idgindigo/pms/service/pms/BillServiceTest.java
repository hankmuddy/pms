package com.idgindigo.pms.service.pms;

import com.idgindigo.pms.domain.extranet.CustomerGroup;
import com.idgindigo.pms.domain.pms.Bill;
import com.idgindigo.pms.domain.pms.Refund;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.domain.pms.SimpleServiceUse;
import com.idgindigo.pms.repository.pms.BaseServiceUseRepository;
import com.idgindigo.pms.repository.pms.BillRepository;
import com.idgindigo.pms.repository.pms.RefundRepository;
import com.idgindigo.pms.service.ServiceTest;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.extranet.CustomerGroupProvider;
import com.idgindigo.pms.utils.pms.BankDetailsProvider;
import com.idgindigo.pms.utils.pms.BillProvider;
import com.idgindigo.pms.utils.pms.RoomUseProvider;
import com.idgindigo.pms.utils.pms.SimpleServiceUseProvider;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * @author valentyn_vakatsiienko
 * @since 3/25/14 2:24 PM
 */
public class BillServiceTest extends ServiceTest {
    public static final int LIST_SIZE = 5;
    public static final long TOTAL = 1L;
    public static final long FULL_TOTAL = LIST_SIZE * TOTAL;
    @Inject
    private BillProvider provider;
    @Inject
    private BillService service;
    @Inject
    private SimpleServiceUseProvider serviceUseProvider;
    @Inject
    private RoomUseProvider roomUseProvider;
    @Inject
    private CustomerGroupProvider customerGroupProvider;

    @Tested
    private BillService mockService;
    @Injectable
    @Inject
    private BillRepository billRepository;
    @Injectable
    @Inject
    private BaseServiceUseRepository baseServiceUseRepository;
    @Injectable
    private RoomUseService roomUseService;
    @Inject
    private RefundRepository refundRepository;
    @Inject
    private BankDetailsProvider bankDetailsProvider;

    @Test(dataProvider = "testUpdateBillTotal")
    public void testUpdateBillTotal(final Bill bill) {
        for (int i = 0; i < LIST_SIZE; i++) {
            serviceUseProvider.getPersistentEntity(new Visitor<SimpleServiceUse>() {
                @Override
                public void visit(SimpleServiceUse entity) {
                    entity.setBill(bill);
                    entity.setTotal(TOTAL);
                }
            });
        }

        service.updateTotal(bill, null);

        Bill updatedBill = provider.getRepository().findOne(bill.getId());
        assertEquals(updatedBill.getTotal().longValue(), FULL_TOTAL);
        if (bill.isForCustomer()) {
            assertEquals(updatedBill.getGroup().getTotal().longValue(), FULL_TOTAL);
        } else {
            assertEquals(updatedBill.getRoomUse().getTotal(), FULL_TOTAL);
        }
    }

    @DataProvider(name = "testUpdateBillTotal")
    public Object[][] getData_testUpdateBillTotal() {
        final Bill forRoomUse = provider.getPersistentEntity(new Visitor<Bill>() {
            @Override
            public void visit(Bill entity) {
                entity.setTotal(0L);
                entity.setRoomUse(roomUseProvider.getPersistentEntity(new Visitor<RoomUse>() {
                    @Override
                    public void visit(RoomUse entity) {
                        entity.setTotal(0L);
                    }
                }));
            }
        });

        List<Object[]> result = new ArrayList<>(2);
        result.add(new Object[]{forRoomUse});

        final Bill forCustomer = provider.getPersistentEntity(new Visitor<Bill>() {
            @Override
            public void visit(Bill entity) {
                entity.setTotal(0L);
                entity.setRoomUse(null);
                entity.setGroup(customerGroupProvider.getPersistentEntity(new Visitor<CustomerGroup>() {
                    @Override
                    public void visit(CustomerGroup entity) {
                        entity.setTotal(0L);
                    }
                }));
            }
        });

        result.add(new Object[]{forCustomer});

        return result.toArray(new Object[result.size()][]);
    }

    @Test
    public void testUpdateRoomUseTotal() {
        final RoomUse roomUse = roomUseProvider.getPersistentEntity();
        for (int i = 0; i < LIST_SIZE; i++) {
            getBill(roomUse);
        }

        mockService.updateTotal(roomUse, null);

        final List<Bill> bills = billRepository.findByRoomUse(roomUse);
        assertEquals(bills.size(), LIST_SIZE);

        new Verifications() {{
            for (Bill bill : bills) {
                mockService.updateTotal(bill, new Refund(roomUse, false), null);
            }
        }};
    }

    private Bill getBill(final RoomUse roomUse) {
        final Bill bill = provider.getPersistentEntity(new Visitor<Bill>() {
            @Override
            public void visit(Bill entity) {
                entity.setRoomUse(roomUse);
            }
        });
        serviceUseProvider.getPersistentEntity(new Visitor<SimpleServiceUse>() {
            @Override
            public void visit(SimpleServiceUse entity) {
                entity.setBill(bill);
                entity.setRoomUse(roomUse);
            }
        });
        return bill;
    }

    @Test(dataProvider = "testGetBill")
    public void testGetBill(RoomUse roomUse, boolean forCustomer) {
        Bill bill = service.getBill(roomUse, forCustomer);
        assertEquals(bill.isForCustomer(), forCustomer);
        if (forCustomer) {
            assertEquals(bill.getGroup(), roomUse.getCustomerGroup());
        } else {
            assertEquals(bill.getRoomUse(), roomUse);
        }
        assertNotNull(bill.getId());
    }

    @DataProvider(name = "testGetBill")
    public Object[][] getData_testGetBill() {
        List<Object[]> result = new ArrayList<>(2);
        RoomUse roomUse = roomUseProvider.getPersistentEntity();
        result.add(new Object[]{roomUse, true});
        result.add(new Object[]{roomUse, false});
        return result.toArray(new Object[result.size()][]);
    }

    @Test
    public void testSetDiscount() {
        Bill bill = getBillWithServiceUses();
        Long rawTotal = bill.getRawTotal();
        long discount = rawTotal / 2;
        long totalPaid = rawTotal - discount + 1;
        bill.setTotalPaid(totalPaid);
        billRepository.updateTotalPaid(bill, totalPaid);

        service.setDiscount(bill, discount, bankDetailsProvider.getPersistentEntity());

        bill = billRepository.findOne(bill.getId());
        assertEquals(bill.getRawTotal(), rawTotal);
        assertEquals(bill.getTotal().longValue(), rawTotal - discount);

        List<Refund> refunds = refundRepository.findByRoomUseCustomerGroup(bill.getRoomUse().getCustomerGroup());
        assertEquals(refunds.size(), 1);
        assertEquals(refunds.get(0).getTotal().longValue(), 1L);
    }

    public Bill getBillWithServiceUses() {
        final Bill bill = provider.getPersistentEntity();
        for (int i = 0; i < 5; i++) {
            serviceUseProvider.getPersistentEntity(new Visitor<SimpleServiceUse>() {
                @Override
                public void visit(SimpleServiceUse entity) {
                    entity.setBill(bill);
                }
            });
        }
        service.updateTotal(bill, null);
        return billRepository.findOne(bill.getId());
    }

    @Test
    public void testSetForCustomer() {
        ;
    }
}
