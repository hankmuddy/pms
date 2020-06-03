package com.idgindigo.pms.repository.pms;

import com.idgindigo.pms.domain.pms.Bill;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.filtering.AbstractFilteredRepository;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 12/25/13 6:20 PM
 */
public class BillRepositoryImpl extends AbstractFilteredRepository<Bill> {
    @Inject
    private BillRepository repository;

    @Override
    public BaseRepository<Bill> getRepository() {
        return repository;
    }
}
