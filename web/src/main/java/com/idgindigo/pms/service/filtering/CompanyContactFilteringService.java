package com.idgindigo.pms.service.filtering;

import com.idgindigo.pms.domain.pms.CompanyContact;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import com.idgindigo.pms.repository.pms.CompanyContactRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 1/11/14 12:59 PM
 */
@Service
public class CompanyContactFilteringService extends GenericFilteringService<CompanyContact> {
    @Inject
    private CompanyContactRepository repository;

    @Override
    public FilteredRepository<CompanyContact> getRepository() {
        return repository;
    }
}
