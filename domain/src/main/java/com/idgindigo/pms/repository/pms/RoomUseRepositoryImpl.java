package com.idgindigo.pms.repository.pms;

import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.filtering.AbstractFilteredRepository;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 12/26/13 7:04 PM
 */
public class RoomUseRepositoryImpl extends AbstractFilteredRepository<RoomUse> {
    @Inject
    private RoomUseRepository repository;

    @Override
    public BaseRepository<RoomUse> getRepository() {
        return repository;
    }
}
