package com.idgindigo.pms.service.filtering;

import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeValue;
import com.idgindigo.pms.repository.extranet.rate.RoomTypeValueRepository;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 11/13/13 10:14 AM
 */
@Service
public class RoomTypeValueFilteringService extends GenericFilteringService<RoomTypeValue> {
    @Inject
    private RoomTypeValueRepository repository;

    @Override
    public FilteredRepository<RoomTypeValue> getRepository() {
        return repository;
    }
}
