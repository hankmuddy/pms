package com.idgindigo.pms.repository.pms;

import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.domain.pms.Room;
import com.idgindigo.pms.repository.ApprovableRepository;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author vomel
 * @since 01.11.13 17:08
 */
public interface RoomRepository extends ApprovableRepository<Room>, FilteredRepository<Room> {

    @Query("select rt from #{#entityName} e join e.roomType rt where e = ?1")
    RoomType getRoomType(Room room);

    @Query("update #{#entityName} e set e.position = ?2 where e = ?1")
    @Modifying
    @Transactional
    void setPosition(Room room, int position);

    @Query("from #{#entityName} e where e.position >= ?1 and e.position <= ?2 order by e.position asc")
    List<Room> findByPositionInRange(int start, int end);
}
