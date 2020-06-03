package com.idgindigo.pms.repository.pms;

import com.idgindigo.pms.domain.pms.Payment;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.filtering.AbstractFilteredRepository;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 12/25/13 12:35 PM
 */
public class PaymentRepositoryImpl extends AbstractFilteredRepository<Payment> {
    @Inject
    private PaymentRepository repository;

    @Override
    public BaseRepository<Payment> getRepository() {
        return repository;
    }
}
