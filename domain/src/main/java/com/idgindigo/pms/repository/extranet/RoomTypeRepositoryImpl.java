package com.idgindigo.pms.repository.extranet;

import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.filtering.AbstractFilteredRepository;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 1/17/14 11:46 AM
 */
public class RoomTypeRepositoryImpl extends AbstractFilteredRepository<RoomType> {
    @Inject
    private RoomTypeRepository repository;

    @Override
    public BaseRepository<RoomType> getRepository() {
        return repository;
    }
}
