package com.idgindigo.pms.service.approve;

import com.idgindigo.pms.domain.pms.Bill;
import com.idgindigo.pms.domain.pms.Payment;
import com.idgindigo.pms.repository.ApprovableRepository;
import com.idgindigo.pms.repository.pms.BillRepository;
import com.idgindigo.pms.repository.pms.PaymentRepository;
import com.idgindigo.pms.restutils.exception.PaymentException;
import com.idgindigo.pms.service.pms.CustomerGroupService;
import com.idgindigo.pms.service.pms.RoomUseService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 12/26/13 6:06 PM
 */
@Service
public class PaymentApproveService extends GenericApproveService<Payment> {
    @Inject
    private PaymentRepository repository;
    @Inject
    private BillRepository billRepository;
    @Inject
    private RoomUseService roomUseService;
    @Inject
    private CustomerGroupService customerGroupService;

    @Override
    public Payment approve(Long id) {
        Payment payment = repository.findOne(id);
        if (payment.getApproved()) {
            return payment;
        }
        Bill bill = billRepository.findOne(payment.getBill().getId());
        Long paid = bill.getTotalPaid();
        paid += payment.getTotal();
        if (bill.getTotal() < paid) {
            throw new PaymentException(PaymentException.PAYMENT_TOTAL_EXCEEDS_NON_PAID_PART, "total");
        }

        payment = super.approve(id);

        billRepository.updateTotalPaid(bill, paid);
        if (bill.isForCustomer()) {
            customerGroupService.updateTotalPaid(bill.getGroup());
        } else {
            roomUseService.updateTotalPaid(bill.getRoomUse());
        }

        return payment;
    }

    @Override
    protected List<String> checkDependenciesApproved(Payment entity) {
        if (!billRepository.isApproved(entity.getBill().getId())) {
            return Collections.singletonList("bill");
        }
        return super.checkDependenciesApproved(entity);
    }

    @Override
    public ApprovableRepository<Payment> getApproveRepository() {
        return repository;
    }
}
