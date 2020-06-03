package com.idgindigo.pms.repository.extranet.person;

import com.idgindigo.pms.domain.extranet.person.Adult;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.filtering.AbstractFilteredRepository;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 1/13/14 3:59 PM
 */
@Component
public class AdultRepositoryImpl extends AbstractFilteredRepository<Adult> {
    @Inject
    private AdultRepository repository;

    @Override
    public BaseRepository<Adult> getRepository() {
        return repository;
    }
}
