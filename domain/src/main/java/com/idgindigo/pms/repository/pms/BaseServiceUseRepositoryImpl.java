package com.idgindigo.pms.repository.pms;

import com.idgindigo.pms.domain.pms.BaseServiceUse;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.filtering.AbstractFilteredRepository;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 12/26/13 5:03 PM
 */
public class BaseServiceUseRepositoryImpl extends AbstractFilteredRepository<BaseServiceUse> {
    @Inject
    private BaseServiceUseRepository repository;

    @Override
    public BaseRepository<BaseServiceUse> getRepository() {
        return repository;
    }
}
