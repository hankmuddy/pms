package com.idgindigo.pms.repository.pms;

import com.idgindigo.pms.domain.User;
import com.idgindigo.pms.domain.extranet.CustomerGroup;
import com.idgindigo.pms.domain.pms.Room;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 11/19/13 3:09 PM
 */
public interface RoomUseRepository extends BaseRepository<RoomUse>, FilteredRepository<RoomUse> {
    List<RoomUse> findByIdIn(Collection<Long> ids);

    List<RoomUse> findByCustomerGroup(CustomerGroup group);

    List<RoomUse> findByCustomerGroupIdAndStatus(Long id, RoomUse.Status status);

    List<RoomUse> findByCustomerGroupIdAndStatusIn(Long id, Iterable<RoomUse.Status> statuses);

    List<RoomUse> findByCustomerGroupId(Long id);

/*
    @Query("SELECT CASE WHEN (COUNT(ru) > 0) THEN TRUE ELSE FALSE END " +
            " FROM RoomUse ru WHERE ru.room = ?1 AND ru.endDate > ?2 AND ru.startDate < ?3")
    Boolean existsByRoomAndPeriod(Room room, LocalDate startDate, LocalDate endDate);
*/

    @Query("UPDATE RoomUse SET movedFrom = ?1 WHERE id = ?2")
    @Modifying
    @Transactional
    void setMoved(RoomUse movedFrom, Long movedToId);

    @Query("UPDATE RoomUse SET status = ?1 , updatedBy = ?3 , updatedDate = ?4 WHERE id = ?2")
    @Modifying
    @Transactional
    void setStatus(RoomUse.Status status, Long id, User updatedBy, LocalDateTime updatedDate);

    @Query("UPDATE RoomUse SET room = ?1 WHERE id = ?2")
    @Modifying
    @Transactional
    void setRoom(Room room, Long id);

    List<RoomUse> findByRcode(String reservationCode);

    List<RoomUse> findByAcode(String aCode);

    @Query("SELECT CASE WHEN (COUNT(ru) > 0) THEN TRUE ELSE FALSE END " +
            " FROM RoomUse ru WHERE ru.rcode = ?1")
    Boolean existsByRcode(String rcode);

    @Query("UPDATE RoomUse SET checkInTime = ?1 WHERE id = ?2")
    @Modifying
    @Transactional
    void setCheckInTime(LocalDateTime hotelTime, Long id);

    @Query("UPDATE RoomUse SET checkOutTime = ?1 WHERE id = ?2")
    @Modifying
    @Transactional
    void setCheckOutTime(LocalDateTime hotelTime, Long id);

    @Query("update #{#entityName} e set e.total = (select case when (sum(b.total) > 0) then sum(b.total) else 0 end from Bill b where b.roomUse = ?1) where e = ?1")
    @Modifying
    @Transactional
    void updateTotal(RoomUse roomUse);

    @Query("update #{#entityName} e set e.totalPaid = (select case when (sum(b.totalPaid) > 0) then sum(b.totalPaid) else 0 end from Bill b where b.roomUse = ?1) where e = ?1")
    @Modifying
    @Transactional
    void updateTotalPaid(RoomUse roomUse);

    @Query("select case when (count(e) > 0) then true else false end from #{#entityName} e where e.movedFrom = ?1 and e.status != 'REFUSE'")
    boolean isMoved(RoomUse roomUse);

    @Query("from #{#entityName} where movedFrom = ?1")
    RoomUse getMovedTo(RoomUse roomUse);

    @Query("select case when (count(b) > 0) then false else true end from Bill b where b.roomUse = ?1 and b.total > b.totalPaid")
    boolean isFullyPaid(RoomUse roomUse);

    @Query("select case when (count(p) > 0) then true else false end from GroupMemberToRoomUse grm2ru join grm2ru.groupMember grm join grm.person p where grm2ru.roomUse.id = ?1 and p = (select cg.customer from #{#entityName} e join e.customerGroup cg where e.id = ?1)")
    boolean containsCustomer(Long id);


    @Query("select case when (count(ru) > 0) then true else false end " +
            " from BaseRoomUse ru where ru.room = ?1 and ru.endDate > ?2 and ru.startDate < ?3 and (TYPE(ru) IN (Repair) or ru.status not in ('REFUSE', 'NOT_ARRIVED'))")
    Boolean isBooked(Room room, LocalDate startDate, LocalDate endDate);

    @Query("select r from Room r where r.approved = true and not exists (" +
            "select ru from BaseRoomUse ru where ru.room = r and ru.endDate > ?1 and ru.startDate < ?2 and (TYPE(ru) IN (Repair) or ru.status not in ('REFUSE', 'NOT_ARRIVED')))")
    List<Room> getFreeRooms(LocalDate startDate, LocalDate endDate);

    @Query("select r from Room r where r.roomType.id = ?1 and r.approved = true and not exists (" +
            "select ru from BaseRoomUse ru where ru.room = r and ru.endDate > ?2 and ru.startDate < ?3 and (TYPE(ru) IN (Repair) or ru.status not in ('REFUSE', 'NOT_ARRIVED')))")
    List<Room> getFreeRooms(Long roomTypeId, LocalDate startDate, LocalDate endDate);

    @Query("select case when (count(e) > 0) then true else false end from #{#entityName} e where e.acode = ?1")
    boolean existsByAcode(String acode);
}