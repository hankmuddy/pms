package com.idgindigo.pms.repository.extranet;

import com.idgindigo.pms.domain.extranet.CustomerGroup;
import com.idgindigo.pms.domain.extranet.GroupMember;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.filtering.FilteredRepository;

import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 12/26/13 3:28 PM
 */
public interface GroupMemberRepository extends BaseRepository<GroupMember>, FilteredRepository<GroupMember> {
    List<GroupMember> findByCustomerGroup(CustomerGroup group);
}
