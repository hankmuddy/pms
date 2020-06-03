package com.idgindigo.pms.repository.pms;

import com.idgindigo.pms.domain.extranet.CustomerGroup;
import com.idgindigo.pms.domain.pms.BaseServiceUse;
import com.idgindigo.pms.domain.pms.Bill;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.dto.BillTotalDto;
import com.idgindigo.pms.repository.ApprovableRepository;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import org.joda.time.LocalDateTime;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 11/22/13 11:39 AM
 */
public interface BillRepository extends ApprovableRepository<Bill>, FilteredRepository<Bill> {
    List<Bill> findByRoomUse(RoomUse roomUse);

    List<Bill> findByGroup(CustomerGroup group);

    List<Bill> findByRoomUseCustomerGroup(CustomerGroup group);

    List<Bill> findByRoomUseCustomerGroupId(Long groupId);

    List<Bill> findByRoomUseId(Long roomUseId);

    @Query("select b from BaseServiceUse su join su.bill b where su in ?1")
    List<Bill> findByServiceUses(List<BaseServiceUse> serviceUses);

    @Query("update #{#entityName} e set e.rawTotal = ?2 where e = ?1")
    @Modifying
    @Transactional
    void updateRawTotal(Bill bill, Long total);

    @Query("update #{#entityName} e set e.total = ?2 where e = ?1")
    @Modifying
    @Transactional
    void updateTotal(Bill bill, Long total);

    @Query("update #{#entityName} e set e.totalPaid = ?2 where e = ?1")
    @Modifying
    @Transactional
    void updateTotalPaid(Bill bill, Long totalPaid);

    @Query("delete from #{#entityName} e where (e.total = 0 or e.total is null) and e.roomUse = ?1 and e.approved = false")
    @Modifying
    @Transactional
    void deleteEmptyByRoomUse(RoomUse roomUse);

    @Query("select case when (count(e) > 0) then true else false end from #{#entityName} e where e.totalPaid = e.total and e.id = ?1")
    boolean isFullyPaid(Long id);

    @Query("update #{#entityName} e set e.group = (select ru.customerGroup from RoomUse ru where ru = e.roomUse), e.roomUse = null where e = ?1")
    @Modifying
    @Transactional
    void setForCustomer(Bill bill);

    @Query("update #{#entityName} e set e.roomUse = ?1, e.group = null where e = ?2")
    @Modifying
    @Transactional
    void setForRoomUse(RoomUse roomUse, Bill bill);

    @Query("update #{#entityName} e set e.discount = ?2 where e = ?1")
    @Modifying
    @Transactional
    void setDiscount(Bill bill, long discount);

    @Query("select new com.idgindigo.pms.dto.BillTotalDto(sum(total), sum(totalPaid)) from #{#entityName} where createdDate >= ?1 and createdDate <= ?2")
    BillTotalDto getTotal(LocalDateTime startDate, LocalDateTime endDate);
}
