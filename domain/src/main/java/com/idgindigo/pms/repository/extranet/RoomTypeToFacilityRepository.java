package com.idgindigo.pms.repository.extranet;

import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeFacility;
import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeToFacility;
import com.idgindigo.pms.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author vomel
 * @since 19.02.14 00:04
 */
public interface RoomTypeToFacilityRepository extends BaseRepository<RoomTypeToFacility> {

    @Query("SELECT rttf.facility FROM RoomTypeToFacility rttf WHERE rttf.roomType.id=?1")
    List<RoomTypeFacility> findByRoomType(Long rtId);

    @Query("DELETE RoomTypeToFacility rttf WHERE rttf.roomType =?1 AND rttf.facility IN (?2)")
    @Modifying
    @Transactional
    void removeSelected(RoomType roomType, List<RoomTypeFacility> facilities);

}
