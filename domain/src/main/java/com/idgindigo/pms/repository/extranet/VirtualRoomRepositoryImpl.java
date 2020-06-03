package com.idgindigo.pms.repository.extranet;

import com.idgindigo.pms.domain.extranet.roomtype.VirtualRoom;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.filtering.AbstractFilteredRepository;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 1/15/14 4:28 PM
 */
public class VirtualRoomRepositoryImpl extends AbstractFilteredRepository<VirtualRoom> {
    @Inject
    private VirtualRoomRepository repository;

    @Override
    public BaseRepository<VirtualRoom> getRepository() {
        return repository;
    }
}
