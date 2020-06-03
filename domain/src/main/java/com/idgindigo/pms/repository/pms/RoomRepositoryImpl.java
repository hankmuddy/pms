package com.idgindigo.pms.repository.pms;

import com.idgindigo.pms.domain.pms.Room;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.filtering.AbstractFilteredRepository;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 1/9/14 1:59 PM
 */
public class RoomRepositoryImpl extends AbstractFilteredRepository<Room> {
    @Inject
    private RoomRepository repository;

    @Override
    public BaseRepository<Room> getRepository() {
        return repository;
    }
}
