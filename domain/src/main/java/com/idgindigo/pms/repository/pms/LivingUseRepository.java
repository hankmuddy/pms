package com.idgindigo.pms.repository.pms;

import com.idgindigo.pms.domain.extranet.CustomerGroup;
import com.idgindigo.pms.domain.pms.Bill;
import com.idgindigo.pms.domain.pms.LivingUse;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.repository.ApprovableRepository;
import org.joda.time.LocalDate;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 1/8/14 3:16 PM
 */
public interface LivingUseRepository extends ApprovableRepository<LivingUse> {

    List<LivingUse> findByBillRoomUseId(Long id);

    List<LivingUse> findByBill(Bill bill);

    List<LivingUse> findByBillRoomUse(RoomUse roomUse);

    List<LivingUse> findByRoomUse(RoomUse roomUse);

    @Query("select distinct e.bill from #{#entityName} e where e.roomUse = ?1")
    List<Bill> findBillsByRoomUse(RoomUse roomUse);

    List<LivingUse> findByBillGroup(CustomerGroup group);

    List<LivingUse> findByBillRoomUseCustomerGroupId(Long id);

    @Query("select e from #{#entityName} e join e.bill b where b.roomUse = ?1 and e.date >= ?2")
    List<LivingUse> findByRoomUseAndAfterDateIncluding(RoomUse roomUse, LocalDate date);
}
