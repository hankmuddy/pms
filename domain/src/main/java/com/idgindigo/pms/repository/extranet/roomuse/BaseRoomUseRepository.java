package com.idgindigo.pms.repository.extranet.roomuse;

import com.idgindigo.pms.domain.extranet.BaseGroupRoomUse;
import com.idgindigo.pms.domain.extranet.BaseRoomUse;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import org.joda.time.LocalDate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 11/28/13 3:36 PM
 */
public interface BaseRoomUseRepository extends BaseRepository<BaseRoomUse>, FilteredRepository<BaseRoomUse> {

    @Query("select ru from BaseRoomUse ru where ru.endDate > ?1 and ru.startDate < ?2 and (TYPE(ru) IN (Repair) or ru.status in (?3))")
    List<BaseRoomUse> getForDatesAndStatuses(LocalDate startDate, LocalDate endDate, Iterable<BaseGroupRoomUse.Status> statuses, Pageable pageable);
}
