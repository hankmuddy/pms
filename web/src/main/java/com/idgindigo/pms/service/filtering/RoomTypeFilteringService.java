package com.idgindigo.pms.service.filtering;

import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.repository.extranet.RoomTypeRepository;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 1/17/14 11:47 AM
 */
@Service
public class RoomTypeFilteringService extends GenericFilteringService<RoomType> {
    @Inject
    private RoomTypeRepository repository;

    @Override
    public FilteredRepository<RoomType> getRepository() {
        return repository;
    }
}
