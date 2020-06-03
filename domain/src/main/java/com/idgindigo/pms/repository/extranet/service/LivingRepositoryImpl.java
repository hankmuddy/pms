package com.idgindigo.pms.repository.extranet.service;

import com.idgindigo.pms.domain.extranet.service.Living;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.filtering.AbstractFilteredRepository;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 1/27/14 12:34 PM
 */
public class LivingRepositoryImpl extends AbstractFilteredRepository<Living> {
    @Inject
    private LivingRepository repository;

    @Override
    public BaseRepository<Living> getRepository() {
        return repository;
    }
}
