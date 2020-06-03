package com.idgindigo.pms.service.filtering;

import com.idgindigo.pms.domain.pms.BankDetails;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import com.idgindigo.pms.repository.pms.BankDetailsRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 2/4/14 5:33 PM
 */
@Service
public class BankDetailsFilteringService extends GenericFilteringService<BankDetails> {
    @Inject
    private BankDetailsRepository repository;

    @Override
    public FilteredRepository<BankDetails> getRepository() {
        return repository;
    }
}
