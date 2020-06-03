package com.idgindigo.pms.repository.extranet.service;

import com.idgindigo.pms.domain.extranet.Season;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.extranet.SeasonRepository;
import com.idgindigo.pms.repository.filtering.AbstractFilteredRepository;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 4/10/14 1:01 PM
 */
public class SeasonRepositoryImpl extends AbstractFilteredRepository<Season> {
    @Inject
    private SeasonRepository repository;

    @Override
    public BaseRepository<Season> getRepository() {
        return repository;
    }
}