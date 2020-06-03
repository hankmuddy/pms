package com.idgindigo.pms.repository.pms;

import com.idgindigo.pms.domain.pms.Child;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.filtering.AbstractFilteredRepository;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 1/14/14 3:41 PM
 */
public class ChildRepositoryImpl extends AbstractFilteredRepository<Child> {
    @Inject
    private ChildRepository repository;

    @Override
    public BaseRepository<Child> getRepository() {
        return repository;
    }
}
