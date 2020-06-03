package com.idgindigo.pms.repository.pms;

import com.idgindigo.pms.domain.pms.CompanyContact;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.filtering.AbstractFilteredRepository;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 1/11/14 1:01 PM
 */
public class CompanyContactRepositoryImpl extends AbstractFilteredRepository<CompanyContact> {
    @Inject
    private CompanyContactRepository repository;

    @Override
    public BaseRepository<CompanyContact> getRepository() {
        return repository;
    }
}
