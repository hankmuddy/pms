package com.idgindigo.pms.utils.pms;

import com.idgindigo.pms.domain.pms.Payment;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.pms.PaymentRepository;
import com.idgindigo.pms.utils.ApprovableEntityProvider;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Component;

import javax.inject.Inject;


/**
 * @author valentyn_vakatsiienko
 * @since 11/22/13 11:55 AM
 */
@Component
public class PaymentProvider extends ApprovableEntityProvider<Payment> {
    @Inject
    private PaymentRepository repository;
    @Inject
    private BillProvider billProvider;
    @Inject
    private BankDetailsProvider bankDetailsProvider;

    @Override
    public Payment createAndFill() {
        Payment payment = new Payment();
        payment.setDate(LocalDateTime.now());
        payment.setBill(billProvider.getPersistentEntity());
        payment.setTotal(payment.getBill().getTotal());
        payment.setBankDetails(bankDetailsProvider.getPersistentEntity());
        return payment;
    }

    @Override
    public BaseRepository<Payment> getRepository() {
        return repository;
    }
}
