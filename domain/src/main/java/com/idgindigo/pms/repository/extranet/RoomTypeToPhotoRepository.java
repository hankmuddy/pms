package com.idgindigo.pms.repository.extranet;

import com.idgindigo.pms.domain.extranet.CodeDto;
import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeToPhoto;
import com.idgindigo.pms.repository.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author vomel
 * @since 28.02.14 15:48
 */
public interface RoomTypeToPhotoRepository extends BaseRepository<RoomTypeToPhoto> {

    String GET_DTOS = "SELECT new com.idgindigo.pms.domain.extranet.CodeDto(rttp.document.accessKey) FROM RoomTypeToPhoto rttp WHERE rttp.roomType.id=?1";

    @Query(GET_DTOS)
    List<CodeDto> findDtosByRoomType(Long id);

    @Query(GET_DTOS)
    List<CodeDto> findDtosByRoomType(Long id, Pageable pageable);

    @Query("SELECT rttp.document.accessKey FROM RoomTypeToPhoto rttp WHERE rttp.roomType.id=?1")
    List<String> findByRoomType(Long id);

    @Query("DELETE RoomTypeToPhoto rttp WHERE rttp.roomType =?1 AND rttp.document.id IN (SELECT d.id FROM Document d WHERE d.accessKey IN (?2))")
    @Modifying
    @Transactional
    void removeSelected(RoomType roomType, List<String> accessKeys);

    @Query("DELETE RoomTypeToPhoto rttp WHERE rttp.document.id IN (SELECT d.id FROM Document d WHERE d.accessKey IN (?1))")
    @Modifying
    @Transactional
    void deleteByKey(String accessKey);
}
