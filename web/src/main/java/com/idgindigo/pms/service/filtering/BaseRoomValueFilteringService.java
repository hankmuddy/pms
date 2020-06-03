package com.idgindigo.pms.service.filtering;

import com.idgindigo.pms.domain.extranet.roomtype.BaseRoomValue;
import com.idgindigo.pms.repository.extranet.BaseRoomValueRepository;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 2/20/14 1:04 PM
 */
@Service
public class BaseRoomValueFilteringService extends GenericFilteringService<BaseRoomValue> {
    @Inject
    private BaseRoomValueRepository repository;

    @Override
    public FilteredRepository<BaseRoomValue> getRepository() {
        return repository;
    }
}
