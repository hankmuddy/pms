package com.idgindigo.pms.repository.extranet.roomuse;

import com.idgindigo.pms.domain.extranet.BaseGroupRoomUse;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author valentyn_vakatsiienko
 * @since 6/19/14 3:06 PM
 */
public interface BaseGroupRoomUseRepository<T extends BaseGroupRoomUse> extends BaseRepository<T> {

    @Query("select case when (count(e) > 0) then true else false end from #{#entityName} e where e.acode = ?1")
    boolean existsByAcode(String acode);

    @Query("SELECT status FROM #{#entityName} WHERE id = ?1")
    BaseGroupRoomUse.Status getStatus(Long id);

    @Query("UPDATE #{#entityName} SET status = ?1 WHERE id = ?2")
    @Modifying
    @Transactional
    void setStatus(RoomUse.Status status, Long id);

}
