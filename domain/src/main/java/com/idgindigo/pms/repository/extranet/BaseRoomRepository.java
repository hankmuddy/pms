package com.idgindigo.pms.repository.extranet;

import com.idgindigo.pms.domain.extranet.roomtype.BaseRoom;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author valentyn_vakatsiienko
 * @since 1/15/14 4:28 PM
 */
public interface BaseRoomRepository extends WubookRoomRepository<BaseRoom>, FilteredRepository<BaseRoom> {

    @Query("update #{#entityName} e set e.defaultBaseRoom = true where e.id = ?1")
    @Modifying
    @Transactional
    void setDefault(Long id);

    @Query("update #{#entityName} e set e.defaultBaseRoom = false where e.defaultBaseRoom = true and (e.roomType.id = ?1 or e.id = ?1)")
    @Modifying
    @Transactional
    void eraseDefaults(Long roomTypeId);

    @Query("from #{#entityName} e where e.defaultBaseRoom = true and (e.roomType.id = ?1 or e.id = ?1)")
    BaseRoom getDefault(Long roomTypeId);

    @Query("select case when e.defaultBaseRoom = true then true else false end from #{#entityName} e where e.id = ?1")
    boolean isDefault(Long id);

}
