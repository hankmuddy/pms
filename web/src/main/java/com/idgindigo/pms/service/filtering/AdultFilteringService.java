package com.idgindigo.pms.service.filtering;

import com.idgindigo.pms.domain.extranet.person.Adult;
import com.idgindigo.pms.repository.extranet.person.AdultRepository;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 1/13/14 3:58 PM
 */
@Service
public class AdultFilteringService extends GenericFilteringService<Adult> {
    @Inject
    private AdultRepository repository;

    @Override
    public FilteredRepository<Adult> getRepository() {
        return repository;
    }
}
