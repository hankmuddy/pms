package com.idgindigo.pms.service.filtering;

import com.idgindigo.pms.domain.pms.Child;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import com.idgindigo.pms.repository.pms.ChildRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 1/14/14 3:37 PM
 */
@Service
public class ChildFilteringService extends GenericFilteringService<Child> {
    @Inject
    private ChildRepository repository;

    @Override
    public FilteredRepository<Child> getRepository() {
        return repository;
    }
}
