package com.idgindigo.pms.service.filtering;

import com.idgindigo.pms.domain.pms.GroupMemberToRoomUse;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import com.idgindigo.pms.repository.pms.GroupMemberToRoomUseRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 3/11/14 11:41 AM
 */
@Service
public class GroupMemberToRoomUseFilteringService extends GenericFilteringService<GroupMemberToRoomUse> {
    @Inject
    private GroupMemberToRoomUseRepository repository;

    @Override
    public FilteredRepository<GroupMemberToRoomUse> getRepository() {
        return repository;
    }
}
