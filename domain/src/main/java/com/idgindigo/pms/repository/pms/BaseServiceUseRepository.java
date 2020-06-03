package com.idgindigo.pms.repository.pms;

import com.idgindigo.pms.domain.extranet.CustomerGroup;
import com.idgindigo.pms.domain.extranet.service.Living;
import com.idgindigo.pms.domain.extranet.service.Service;
import com.idgindigo.pms.domain.pms.BaseServiceUse;
import com.idgindigo.pms.domain.pms.Bill;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.repository.ApprovableRepository;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import org.joda.time.LocalDate;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 1/8/14 3:40 PM
 */
public interface BaseServiceUseRepository extends ApprovableRepository<BaseServiceUse>, FilteredRepository<BaseServiceUse> {
    List<BaseServiceUse> findByBill(Bill bill);

    List<BaseServiceUse> findByIdIn(List<Long> ids);

    @Query("select distinct e.bill from #{#entityName} e where e.roomUse = ?1")
    List<Bill> findBillsByRoomUse(RoomUse roomUse);

    @Query("update #{#entityName} su set su.approved = true where su.service = ?1 and su.bill in ?2")
    @Modifying
    @Transactional
    void approveByLivingAndBills(Living living, List<Bill> bills);

    @Query("select su.bill from #{#entityName} su where su.id = ?1")
    Bill getBill(Long id);

    @Query("select sum(su.total) from #{#entityName} su where su.bill = ?1 and su.refund is null")
    Long getTotalByBill(Bill bill);

    @Query("select case when (count(e) > 0) then true else false end " +
            " from #{#entityName} e join e.service s where type (s) = ?2 and e.id = ?1")
    boolean isServiceOfType(Long id, Class<? extends Service> serviceClass);

    List<BaseServiceUse> findByServiceAndBillRoomUseCustomerGroup(Service service, CustomerGroup group);

    @Query("select ru from #{#entityName} e join e.bill b join b.roomUse ru where e = ?1")
    RoomUse getRoomUse(BaseServiceUse serviceUse);

    @Query("select e from #{#entityName} e where e.roomUse = ?1 and e.date >= ?2")
    List<BaseServiceUse> findByRoomUseAndAfterDateIncluding(RoomUse roomUse, LocalDate date);

    List<BaseServiceUse> findByBillRoomUse(RoomUse roomUse);

    @Query("update #{#entityName} e set e.bill = ?2 where e in ?1")
    @Modifying
    @Transactional
    void setBill(List<BaseServiceUse> serviceUses, Bill bill);

    @Query("update #{#entityName} e set e.roomUse = ?2 where e in ?1")
    @Modifying
    @Transactional
    void setRoomUse(List<BaseServiceUse> serviceUses, RoomUse roomUse);

}
