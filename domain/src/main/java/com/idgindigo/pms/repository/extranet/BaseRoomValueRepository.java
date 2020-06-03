package com.idgindigo.pms.repository.extranet;

import com.idgindigo.pms.domain.extranet.roomtype.BaseRoom;
import com.idgindigo.pms.domain.extranet.roomtype.BaseRoomValue;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import org.joda.time.LocalDate;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 2/20/14 11:22 AM
 */
public interface BaseRoomValueRepository extends BaseRepository<BaseRoomValue>, FilteredRepository<BaseRoomValue> {
    List<BaseRoomValue> findByRoom(BaseRoom room);

    @Query("from #{#entityName} e where e.date >= ?1 and e.date <= ?2 and e.room = ?3")
    List<BaseRoomValue> findByRoomAndDates(LocalDate start, LocalDate end, BaseRoom room);
}
