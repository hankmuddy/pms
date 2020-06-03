package com.idgindigo.pms.service.filtering;

import com.idgindigo.pms.domain.pms.BaseServiceUse;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import com.idgindigo.pms.repository.pms.BaseServiceUseRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 12/26/13 5:05 PM
 */
@Service
public class BaseServiceUseFilteringService extends GenericFilteringService<BaseServiceUse> {
    @Inject
    private BaseServiceUseRepository repository;

    @Override
    public FilteredRepository<BaseServiceUse> getRepository() {
        return repository;
    }
}
