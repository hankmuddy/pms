package com.idgindigo.pms.repository.pms;

import com.idgindigo.pms.domain.pms.Refund;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.filtering.AbstractFilteredRepository;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 1/10/14 12:49 PM
 */
public class RefundRepositoryImpl extends AbstractFilteredRepository<Refund> {
    @Inject
    private RefundRepository repository;

    @Override
    public BaseRepository<Refund> getRepository() {
        return repository;
    }
}
