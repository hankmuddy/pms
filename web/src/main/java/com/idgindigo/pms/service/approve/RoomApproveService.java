package com.idgindigo.pms.service.approve;

import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.domain.pms.Room;
import com.idgindigo.pms.repository.ApprovableRepository;
import com.idgindigo.pms.repository.extranet.RoomTypeRepository;
import com.idgindigo.pms.repository.pms.RoomRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 12/26/13 6:10 PM
 */
@Service
public class RoomApproveService extends GenericApproveService<Room> {
    @Inject
    private RoomRepository repository;
    @Inject
    private RoomTypeRepository roomTypeRepository;

    @Override
    protected List<String> checkDependenciesApproved(Room entity) {
        List<String> dependencies = super.checkDependenciesApproved(entity);
        if (!roomTypeRepository.isApproved(entity.getRoomType().getId())) {
            dependencies.add(RoomType.ROOM_TYPE);
        }
        return dependencies;
    }

    @Override
    public ApprovableRepository<Room> getApproveRepository() {
        return repository;
    }
}
