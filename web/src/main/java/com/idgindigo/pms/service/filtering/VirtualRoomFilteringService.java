package com.idgindigo.pms.service.filtering;

import com.idgindigo.pms.domain.extranet.roomtype.VirtualRoom;
import com.idgindigo.pms.repository.extranet.VirtualRoomRepository;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 1/15/14 4:31 PM
 */
@Service
public class VirtualRoomFilteringService extends GenericFilteringService<VirtualRoom> {
    @Inject
    private VirtualRoomRepository repository;

    @Override
    public FilteredRepository<VirtualRoom> getRepository() {
        return repository;
    }
}
