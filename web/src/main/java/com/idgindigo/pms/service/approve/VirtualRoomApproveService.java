package com.idgindigo.pms.service.approve;

import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.domain.extranet.roomtype.VirtualRoom;
import com.idgindigo.pms.repository.ApprovableRepository;
import com.idgindigo.pms.repository.extranet.RoomTypeRepository;
import com.idgindigo.pms.repository.extranet.VirtualRoomRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 1/16/14 3:25 PM
 */
@Service
public class VirtualRoomApproveService extends GenericApproveService<VirtualRoom> {
    @Inject
    private VirtualRoomRepository repository;
    @Inject
    private RoomTypeRepository roomTypeRepository;

    @Override
    public VirtualRoom approve(Long id) {
        return super.approve(id);
    }

    @Override
    protected List<String> checkDependenciesApproved(VirtualRoom entity) {
        List<String> sources = new ArrayList<>();
        if (!roomTypeRepository.isApproved(entity.roomType().getId())) {
            sources.add(RoomType.ROOM_TYPE);
        }
        return sources;
    }

    @Override
    public ApprovableRepository<VirtualRoom> getApproveRepository() {
        return repository;
    }
}