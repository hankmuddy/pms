package com.idgindigo.pms.repository.extranet;

import com.idgindigo.pms.domain.extranet.roomtype.BaseRoomValue;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.filtering.AbstractFilteredRepository;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 2/20/14 11:23 AM
 */
public class BaseRoomValueRepositoryImpl extends AbstractFilteredRepository<BaseRoomValue> {
    @Inject
    private BaseRoomValueRepository repository;

    @Override
    public BaseRepository<BaseRoomValue> getRepository() {
        return repository;
    }
}
