package com.idgindigo.pms.service.approve;

import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.repository.ApprovableRepository;
import com.idgindigo.pms.repository.extranet.RoomTypeRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 12/26/13 6:15 PM
 */
@Service
public class RoomTypeApproveService extends GenericApproveService<RoomType> {
    @Inject
    private RoomTypeRepository repository;

    @Override
    public ApprovableRepository<RoomType> getApproveRepository() {
        return repository;
    }
}
