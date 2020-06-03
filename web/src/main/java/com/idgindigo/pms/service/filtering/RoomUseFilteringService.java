package com.idgindigo.pms.service.filtering;

import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import com.idgindigo.pms.repository.pms.RoomUseRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 12/26/13 7:03 PM
 */
@Service
public class RoomUseFilteringService extends GenericFilteringService<RoomUse> {
    @Inject
    private RoomUseRepository repository;

    @Override
    public FilteredRepository<RoomUse> getRepository() {
        return repository;
    }
}
