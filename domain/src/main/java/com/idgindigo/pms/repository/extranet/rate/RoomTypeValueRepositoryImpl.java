package com.idgindigo.pms.repository.extranet.rate;

import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeValue;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.filtering.AbstractFilteredRepository;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 11/13/13 10:25 AM
 */
public class RoomTypeValueRepositoryImpl extends AbstractFilteredRepository<RoomTypeValue> {
    @Inject
    private RoomTypeValueRepository repository;

    @Override
    public BaseRepository<RoomTypeValue> getRepository() {
        return repository;
    }
}
