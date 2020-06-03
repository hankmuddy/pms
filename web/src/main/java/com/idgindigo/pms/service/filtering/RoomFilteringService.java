package com.idgindigo.pms.service.filtering;

import com.idgindigo.pms.domain.pms.Room;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import com.idgindigo.pms.repository.pms.RoomRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 1/9/14 1:59 PM
 */
@Service
public class RoomFilteringService extends GenericFilteringService<Room> {
    @Inject
    private RoomRepository repository;

    @Override
    public FilteredRepository<Room> getRepository() {
        return repository;
    }
}
