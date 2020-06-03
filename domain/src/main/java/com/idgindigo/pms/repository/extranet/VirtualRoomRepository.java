package com.idgindigo.pms.repository.extranet;

import com.idgindigo.pms.domain.extranet.roomtype.BaseRoom;
import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.domain.extranet.roomtype.VirtualRoom;
import com.idgindigo.pms.repository.ApprovableRepository;
import com.idgindigo.pms.repository.filtering.FilteredRepository;

import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 3/19/14 10:46 AM
 */
public interface VirtualRoomRepository extends ApprovableRepository<VirtualRoom>, WubookRoomRepository<VirtualRoom>, FilteredRepository<VirtualRoom> {
    List<BaseRoom> findByRoomType(RoomType roomType);
}
