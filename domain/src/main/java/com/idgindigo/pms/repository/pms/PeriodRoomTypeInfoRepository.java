package com.idgindigo.pms.repository.pms;

import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.domain.pms.PeriodRoomTypeInfo;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import org.joda.time.LocalDate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 12/27/13 4:01 PM
 */
public interface PeriodRoomTypeInfoRepository extends BaseRepository<PeriodRoomTypeInfo>, FilteredRepository<PeriodRoomTypeInfo> {
    @Query("select e.livingPrice from #{#entityName} e where e.roomType = ?1 and e.dateStart <= ?2 order by e.dateStart desc")
    List<Long> getLivingPrices(RoomType roomType, LocalDate date, Pageable pageable);
}
