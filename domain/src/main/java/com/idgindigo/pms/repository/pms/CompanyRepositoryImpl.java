package com.idgindigo.pms.repository.pms;

import com.idgindigo.pms.domain.pms.Company;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.filtering.AbstractFilteredRepository;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 1/14/14 5:42 PM
 */
public class CompanyRepositoryImpl extends AbstractFilteredRepository<Company> {
    @Inject
    private CompanyRepository repository;

    @Override
    public BaseRepository<Company> getRepository() {
        return repository;
    }
}
