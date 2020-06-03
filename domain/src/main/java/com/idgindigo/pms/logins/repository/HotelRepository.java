package com.idgindigo.pms.logins.repository;

import com.idgindigo.pms.logins.domain.Hotel;
import com.idgindigo.pms.repository.BaseRepository;
import org.joda.time.LocalDate;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 11/18/13 12:07 PM
 */
public interface HotelRepository extends BaseRepository<Hotel> {

    Hotel findByTenantId(String tenantId);

    @Query("SELECT h FROM Hotel h WHERE h.lcode=?1")
    Hotel findByLcode(String lcode);

    @Query("update #{#entityName} e set e.blocked = ?2 where e.id = ?1")
    @Modifying
    @Transactional("loginsTransactionManager")
    void setBlocked(Long id, boolean blocked);

    @Query("update #{#entityName} e set e.maxRooms = ?2 where e.id = ?1")
    @Modifying
    @Transactional("loginsTransactionManager")
    void setMaxRooms(Long id, int maxRooms);

    @Query("update #{#entityName} e set e.paidUntil = ?2 where e.id = ?1")
    @Modifying
    @Transactional("loginsTransactionManager")
    void setPaidUntil(Long id, LocalDate paidUntil);

    @Query("from #{#entityName} where blocked = false and paidUntil < ?1")
    List<Hotel> findBlockableByDate(LocalDate date);
}
