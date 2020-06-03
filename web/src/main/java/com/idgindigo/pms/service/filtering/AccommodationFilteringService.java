package com.idgindigo.pms.service.filtering;

import com.idgindigo.pms.domain.pms.Accommodation;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import com.idgindigo.pms.repository.pms.AccommodationRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 1/17/14 11:58 AM
 */
@Service
public class AccommodationFilteringService extends GenericFilteringService<Accommodation> {
    @Inject
    private AccommodationRepository repository;

    @Override
    public FilteredRepository<Accommodation> getRepository() {
        return repository;
    }
}
