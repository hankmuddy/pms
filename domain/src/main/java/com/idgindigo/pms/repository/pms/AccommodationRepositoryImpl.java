package com.idgindigo.pms.repository.pms;

import com.idgindigo.pms.domain.pms.Accommodation;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.filtering.AbstractFilteredRepository;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 1/17/14 11:58 AM
 */
public class AccommodationRepositoryImpl extends AbstractFilteredRepository<Accommodation> {
    @Inject
    private AccommodationRepository repository;

    @Override
    public BaseRepository<Accommodation> getRepository() {
        return repository;
    }
}
