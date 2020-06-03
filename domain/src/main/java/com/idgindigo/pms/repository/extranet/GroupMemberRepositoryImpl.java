package com.idgindigo.pms.repository.extranet;

import com.idgindigo.pms.domain.extranet.GroupMember;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.filtering.AbstractFilteredRepository;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 1/23/14 12:53 PM
 */
public class GroupMemberRepositoryImpl extends AbstractFilteredRepository<GroupMember> {
    @Inject
    private GroupMemberRepository repository;

    @Override
    public BaseRepository<GroupMember> getRepository() {
        return repository;
    }
}
