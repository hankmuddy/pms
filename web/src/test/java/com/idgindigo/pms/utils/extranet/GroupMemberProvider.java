package com.idgindigo.pms.utils.extranet;

import com.idgindigo.pms.domain.extranet.GroupMember;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.extranet.GroupMemberRepository;
import com.idgindigo.pms.utils.EntityProvider;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 12/30/13 4:31 PM
 */
@Component
public class GroupMemberProvider extends EntityProvider<GroupMember> {
    @Inject
    private GroupMemberRepository repository;
    @Inject
    private CustomerGroupProvider customerGroupProvider;
    @Inject
    private AdultProvider adultProvider;

    @Override
    public GroupMember createAndFill() {
        GroupMember member = new GroupMember();
        member.setCustomerGroup(customerGroupProvider.getPersistentEntity());
        member.setPerson(adultProvider.getPersistentEntity());
        return member;
    }

    @Override
    public BaseRepository<GroupMember> getRepository() {
        return repository;
    }
}
