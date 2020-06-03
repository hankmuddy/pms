package com.idgindigo.pms.repository.extranet.rate;

import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeQuota;
import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeValue;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import org.joda.time.LocalDate;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 11/26/13 11:40 AM
 */
public interface RoomTypeValueRepository extends BaseRepository<RoomTypeValue>, FilteredRepository<RoomTypeValue> {

    RoomTypeValue findByRoomTypeAndDate(RoomType roomType, LocalDate date);

    @Query("from #{#entityName} e where e.roomType = ?1 and e.date >= ?2 and e.date <= ?3")
    List<RoomTypeValue> findByRoomTypeAndDateBetween(RoomType roomType, LocalDate start, LocalDate end);

    @Query("from #{#entityName} e where e.date >= ?1 and e.date <= ?2")
    List<RoomTypeValue> findByDateBetween(LocalDate start, LocalDate end);

    @Query("from #{#entityName} e where e.roomType = ?1 and e.date >= ?2")
    List<RoomTypeValue> findByRoomTypeAndDateNotBefore(RoomType roomType, LocalDate start);

    @Query(" update #{#entityName} e set e.roomsAvailable = e.roomsAvailable + (?4) where " +
            " e.date between ?1 and ?2 and e.roomType = ?3)")
    @Modifying(clearAutomatically = true)
    @Transactional
    void updateQuota(LocalDate dateStart, LocalDate dateEnd, RoomType roomType, int delta);

    @Query(" update #{#entityName} e set e.roomsAvailable = e.roomsAvailable + (?3) where " +
            " e.date >= ?1 and e.roomType = ?2)")
    @Modifying(clearAutomatically = true)
    @Transactional
    void updateQuota(LocalDate dateStart, RoomType roomType, int delta);

    @Query("select new com.idgindigo.pms.domain.extranet.roomtype.RoomTypeQuota(r.roomType, min(r.roomsAvailable)) from #{#entityName} r where r.date >= ?1 and r.date <= ?2 group by r.roomType")
    List<RoomTypeQuota> getRoomTypeQuota(LocalDate startDate, LocalDate endDate);
/*
    @Query("select new com.idgindigo.pms.domain.extranet.roomtype.RoomTypeQuota(r.roomType, r.roomsAvailable) from #{#entityName} r " +
            "where r.roomsAvailable = (select min(rtv.roomsAvailable) from #{#entityName} rtv where rtv.roomType = r.roomType and rtv.date >= ?1 and rtv.date <= ?2)")
    List<RoomTypeQuota> getRoomTypeQuota(LocalDate startDate, LocalDate endDate);*/
}
