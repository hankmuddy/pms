package com.idgindigo.pms.service.filtering;

import com.idgindigo.pms.domain.extranet.GroupMember;
import com.idgindigo.pms.repository.extranet.GroupMemberRepository;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 1/23/14 12:54 PM
 */
@Service
public class GroupMemberFilteringService extends GenericFilteringService<GroupMember> {
    @Inject
    private GroupMemberRepository repository;

    @Override
    public FilteredRepository<GroupMember> getRepository() {
        return repository;
    }
}
