package com.idgindigo.pms.utils.pms;

import com.idgindigo.pms.domain.pms.Bill;
import com.idgindigo.pms.domain.pms.Refund;
import com.idgindigo.pms.domain.pms.SimpleServiceUse;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.pms.BillRepository;
import com.idgindigo.pms.repository.pms.RefundRepository;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.Visitor;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 1/10/14 1:44 PM
 */
@Component
public class RefundProvider extends EntityProvider<Refund> {
    @Inject
    private RefundRepository repository;
    @Inject
    private EntityProvider<SimpleServiceUse> serviceUseProvider;
    @Inject
    private EntityProvider<Bill> billProvider;
    @Inject
    private BillRepository billRepository;

    @Override
    public Refund createAndFill() {
        Refund refund = new Refund();
        final Bill bill = billProvider.getPersistentEntity(new Visitor<Bill>() {
            @Override
            public void visit(Bill entity) {
                entity.setTotal(0L);
            }
        });
        refund.setRoomUse(bill.getRoomUse());
        for (int i = 0; i < 5; i++) {
            refund.getServiceUses().add(serviceUseProvider.getPersistentEntity(new Visitor<SimpleServiceUse>() {
                @Override
                public void visit(SimpleServiceUse entity) {
                    entity.setBill(bill);
                    entity.setRoomUse(bill.getRoomUse());
                    entity.setTotal(100L);
                    bill.setTotal(bill.getTotal() + entity.getTotal());
                }
            }));
        }
        billRepository.updateTotal(bill, bill.getTotal());
        return refund;
    }

    @Override
    public BaseRepository<Refund> getRepository() {
        return repository;
    }
}
