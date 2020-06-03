package com.idgindigo.pms.repository.pms;

import com.idgindigo.pms.domain.pms.Bill;
import com.idgindigo.pms.domain.pms.Payment;
import com.idgindigo.pms.repository.ApprovableRepository;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import org.joda.time.LocalDateTime;
import org.springframework.data.jpa.repository.Query;

/**
 * @author valentyn_vakatsiienko
 * @since 11/22/13 11:51 AM
 */
public interface PaymentRepository extends ApprovableRepository<Payment>, FilteredRepository<Payment> {

    @Query("select coalesce(sum(p.total), 0) from Payment p where p.bill = ?1 and p.approved = true")
    long getCurrentSum(Bill bill);

    @Query("select b from #{#entityName} e join e.bill b where e.id = ?1")
    Bill getBill(Long paymentId);

    @Query("select case when sum(total) > 0 then sum(total) else 0 end from #{#entityName} where date >= ?1 and date <= ?2")
    Long getTotal(LocalDateTime startDate, LocalDateTime endDate);
}