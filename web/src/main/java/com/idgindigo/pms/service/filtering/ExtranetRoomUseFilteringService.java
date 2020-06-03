package com.idgindigo.pms.service.filtering;

import com.idgindigo.pms.domain.extranet.ExtranetRoomUse;
import com.idgindigo.pms.repository.extranet.roomuse.ExtranetRoomUseRepository;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 6/12/14 10:52 AM
 */
@Service
public class ExtranetRoomUseFilteringService extends GenericFilteringService<ExtranetRoomUse> {
    @Inject
    private ExtranetRoomUseRepository repository;

    @Override
    public FilteredRepository<ExtranetRoomUse> getRepository() {
        return repository;
    }
}
