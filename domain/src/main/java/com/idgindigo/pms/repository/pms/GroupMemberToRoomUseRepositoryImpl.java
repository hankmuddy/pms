package com.idgindigo.pms.repository.pms;

import com.idgindigo.pms.domain.pms.GroupMemberToRoomUse;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.filtering.AbstractFilteredRepository;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 3/11/14 11:40 AM
 */
public class GroupMemberToRoomUseRepositoryImpl extends AbstractFilteredRepository<GroupMemberToRoomUse> {
    @Inject
    private GroupMemberToRoomUseRepository repository;

    @Override
    public BaseRepository<GroupMemberToRoomUse> getRepository() {
        return repository;
    }
}
