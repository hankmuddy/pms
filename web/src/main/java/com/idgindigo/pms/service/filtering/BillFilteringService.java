package com.idgindigo.pms.service.filtering;

import com.idgindigo.pms.domain.pms.Bill;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import com.idgindigo.pms.repository.pms.BillRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 12/25/13 6:17 PM
 */
@Service
public class BillFilteringService extends GenericFilteringService<Bill> {
    @Inject
    private BillRepository repository;

    @Override
    public FilteredRepository<Bill> getRepository() {
        return repository;
    }
}
