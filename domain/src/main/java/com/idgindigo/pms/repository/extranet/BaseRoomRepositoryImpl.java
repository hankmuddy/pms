package com.idgindigo.pms.repository.extranet;

import com.idgindigo.pms.domain.extranet.roomtype.BaseRoom;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.filtering.AbstractFilteredRepository;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 3/21/14 11:18 AM
 */
public class BaseRoomRepositoryImpl extends AbstractFilteredRepository<BaseRoom> {
    @Inject
    private BaseRoomRepository repository;

    @Override
    public BaseRepository<BaseRoom> getRepository() {
        return repository;
    }
}
