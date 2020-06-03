package com.idgindigo.pms.service.filtering;

import com.idgindigo.pms.domain.extranet.CustomerGroup;
import com.idgindigo.pms.repository.extranet.CustomerGroupRepository;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 3/1/14 2:09 PM
 */
@Service
public class CustomerGroupFilteringService extends GenericFilteringService<CustomerGroup> {
    @Inject
    private CustomerGroupRepository repository;

    @Override
    public FilteredRepository<CustomerGroup> getRepository() {
        return repository;
    }
}
