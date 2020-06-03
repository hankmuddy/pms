package com.idgindigo.pms.repository.extranet.roomuse;

import com.idgindigo.pms.domain.extranet.BaseRoomUse;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.filtering.AbstractFilteredRepository;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 12/23/13 6:15 PM
 */
public class BaseRoomUseRepositoryImpl extends AbstractFilteredRepository<BaseRoomUse> {
    @Inject
    private BaseRoomUseRepository repository;

    @Override
    public BaseRepository<BaseRoomUse> getRepository() {
        return repository;
    }
}
