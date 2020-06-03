package com.idgindigo.pms.repository.pms;

import com.idgindigo.pms.domain.pms.BankDetails;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.filtering.AbstractFilteredRepository;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 2/4/14 5:34 PM
 */
public class BankDetailsRepositoryImpl extends AbstractFilteredRepository<BankDetails> {
    @Inject
    private BankDetailsRepository repository;

    @Override
    public BaseRepository<BankDetails> getRepository() {
        return repository;
    }
}
