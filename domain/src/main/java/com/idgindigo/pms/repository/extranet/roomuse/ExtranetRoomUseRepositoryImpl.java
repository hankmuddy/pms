package com.idgindigo.pms.repository.extranet.roomuse;

import com.idgindigo.pms.domain.extranet.ExtranetRoomUse;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.filtering.AbstractFilteredRepository;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 6/12/14 10:53 AM
 */
public class ExtranetRoomUseRepositoryImpl extends AbstractFilteredRepository<ExtranetRoomUse> {
    @Inject
    private ExtranetRoomUseRepository repository;

    @Override
    public BaseRepository<ExtranetRoomUse> getRepository() {
        return repository;
    }
}
