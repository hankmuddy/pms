package com.idgindigo.pms.repository.extranet;

import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author vomel
 * @since 01.11.13 15:31
 */
public interface RoomTypeRepository extends WubookRoomRepository<RoomType>, FilteredRepository<RoomType> {
    @Query("update #{#entityName} e set e.otaRooms = e.otaRooms + 1 where e = ?1")
    @Modifying
    @Transactional
    void addRoom(RoomType roomType);

}
