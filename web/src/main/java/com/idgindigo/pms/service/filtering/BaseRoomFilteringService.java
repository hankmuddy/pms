package com.idgindigo.pms.service.filtering;

import com.idgindigo.pms.domain.extranet.roomtype.BaseRoom;
import com.idgindigo.pms.repository.extranet.BaseRoomRepository;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 3/21/14 11:22 AM
 */
@Service
public class BaseRoomFilteringService extends GenericFilteringService<BaseRoom> {
    @Inject
    private BaseRoomRepository repository;

    @Override
    public FilteredRepository<BaseRoom> getRepository() {
        return repository;
    }
}
