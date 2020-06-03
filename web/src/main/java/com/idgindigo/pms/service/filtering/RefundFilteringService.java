package com.idgindigo.pms.service.filtering;

import com.idgindigo.pms.domain.pms.Refund;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import com.idgindigo.pms.repository.pms.RefundRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 1/10/14 12:47 PM
 */
@Service
public class RefundFilteringService extends GenericFilteringService<Refund> {
    @Inject
    private RefundRepository repository;

    @Override
    public FilteredRepository<Refund> getRepository() {
        return repository;
    }
}
