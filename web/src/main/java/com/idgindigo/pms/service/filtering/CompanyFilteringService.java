package com.idgindigo.pms.service.filtering;

import com.idgindigo.pms.domain.pms.Company;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import com.idgindigo.pms.repository.pms.CompanyRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 1/14/14 5:40 PM
 */
@Service
public class CompanyFilteringService extends GenericFilteringService<Company> {
    @Inject
    private CompanyRepository repository;

    @Override
    public FilteredRepository<Company> getRepository() {
        return repository;
    }
}
