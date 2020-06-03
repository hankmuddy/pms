package com.idgindigo.pms.repository.pms;

import com.idgindigo.pms.domain.extranet.CustomerGroup;
import com.idgindigo.pms.domain.pms.BankDetails;
import com.idgindigo.pms.domain.pms.Refund;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 12/9/13 4:53 PM
 */
public interface RefundRepository extends BaseRepository<Refund>, FilteredRepository<Refund> {
    List<Refund> findByRoomUseCustomerGroup(CustomerGroup group);

    List<Refund> findByGroup(CustomerGroup group);

    @Query("update #{#entityName} e set e.total = ?2 where e = ?1")
    @Transactional
    @Modifying
    void updateTotal(Refund refund, Long total);

    @Query("update #{#entityName} e set e.bankDetails = ?2 where e.id = ?1")
    @Transactional
    @Modifying
    void setDetails(long id, BankDetails details);
}
