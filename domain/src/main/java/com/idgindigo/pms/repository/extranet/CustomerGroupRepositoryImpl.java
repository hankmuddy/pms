package com.idgindigo.pms.repository.extranet;

import com.idgindigo.pms.domain.extranet.CustomerGroup;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.filtering.AbstractFilteredRepository;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 3/1/14 2:08 PM
 */
public class CustomerGroupRepositoryImpl extends AbstractFilteredRepository<CustomerGroup> {
    @Inject
    private CustomerGroupRepository repository;

    @Override
    public BaseRepository<CustomerGroup> getRepository() {
        return repository;
    }
}
