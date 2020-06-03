package com.idgindigo.pms.service.pms;

import com.idgindigo.pms.service.ServiceTest;
import com.idgindigo.pms.utils.pms.BillProvider;
import com.idgindigo.pms.utils.pms.RefundProvider;
import com.idgindigo.pms.utils.pms.SimpleServiceUseProvider;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 3/17/14 9:59 AM
 */
public class RefundServiceTest extends ServiceTest {
    private static final Long SERVICE_USE_TOTAL = 100L;
    @Inject
    private RefundProvider provider;
    @Inject
    private BillProvider billProvider;
    @Inject
    private SimpleServiceUseProvider serviceUseProvider;

/*
    @Test
    public void testCreate() {
        final Bill forCustomer = billProvider.getPersistentEntity(new Visitor<Bill>() {
            @Override
            public void visit(Bill entity) {
                entity.setForCustomer(true);
            }
        });
        final Bill forRoomUse = billProvider.getPersistentEntity(new Visitor<Bill>() {
            @Override
            public void visit(Bill entity) {
                entity.setForCustomer(false);
            }
        });
        SimpleServiceUse sufc = serviceUseProvider.getPersistentEntity(new Visitor<SimpleServiceUse>() {
            @Override
            public void visit(SimpleServiceUse entity) {
                entity.setBill(forCustomer);
                entity.setTotal(SERVICE_USE_TOTAL);
            }
        });
        SimpleServiceUse sufru = serviceUseProvider.getPersistentEntity(new Visitor<SimpleServiceUse>() {
            @Override
            public void visit(SimpleServiceUse entity) {
                entity.setBill(forRoomUse);
                entity.setTotal(SERVICE_USE_TOTAL);
            }
        });

    }
*/

}
