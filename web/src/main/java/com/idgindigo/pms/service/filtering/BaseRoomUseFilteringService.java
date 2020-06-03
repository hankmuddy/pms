package com.idgindigo.pms.service.filtering;

import com.idgindigo.pms.domain.extranet.BaseRoomUse;
import com.idgindigo.pms.repository.extranet.roomuse.BaseRoomUseRepository;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 12/23/13 6:17 PM
 */
@Service
public class BaseRoomUseFilteringService extends GenericFilteringService<BaseRoomUse> {
    @Inject
    private BaseRoomUseRepository repository;

    @Override
    public FilteredRepository<BaseRoomUse> getRepository() {
        return repository;
    }
}
