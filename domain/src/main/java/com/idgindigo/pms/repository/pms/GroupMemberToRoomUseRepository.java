package com.idgindigo.pms.repository.pms;

import com.idgindigo.pms.domain.pms.GroupMemberToRoomUse;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 3/11/14 11:39 AM
 */
public interface GroupMemberToRoomUseRepository extends BaseRepository<GroupMemberToRoomUse>, FilteredRepository<GroupMemberToRoomUse> {
    List<GroupMemberToRoomUse> findByRoomUse(RoomUse roomUse);

    @Query("SELECT CASE WHEN (COUNT(e) > 0) THEN TRUE ELSE FALSE END FROM #{#entityName} e WHERE e.roomUse = ?1")
    boolean existsByRoomUse(RoomUse roomUse);

    @Query("SELECT gmtru FROM GroupMemberToRoomUse gmtru WHERE gmtru.groupMember.person.id = ?1")
    List<GroupMemberToRoomUse> findByPersonId(Long customerId);
}
