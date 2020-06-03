package com.idgindigo.pms.service.filtering;

import com.idgindigo.pms.domain.pms.Payment;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import com.idgindigo.pms.repository.pms.PaymentRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 12/25/13 12:34 PM
 */
@Service
public class PaymentFilteringService extends GenericFilteringService<Payment> {
    @Inject
    private PaymentRepository repository;

    @Override
    public FilteredRepository<Payment> getRepository() {
        return repository;
    }
}
