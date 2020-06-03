package com.idgindigo.pms.repository.pms;

import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.domain.pms.SimpleServiceUse;
import com.idgindigo.pms.repository.BaseRepository;
import org.joda.time.LocalDate;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 1/9/14 1:09 PM
 */
public interface SimpleServiceUseRepository extends BaseRepository<SimpleServiceUse> {

    @Query("select e from #{#entityName} e join e.service s join e.bill b where b.roomUse = ?1 and s.title in ?2 and e.date >= ?3")
    List<SimpleServiceUse> findByRoomUseAndServiceTitlesAfterDateIncluding(RoomUse roomUse, List<String> titles, LocalDate date);
}
