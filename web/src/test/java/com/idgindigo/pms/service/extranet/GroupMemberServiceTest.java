package com.idgindigo.pms.service.extranet;

import com.idgindigo.pms.domain.extranet.GroupMember;
import com.idgindigo.pms.service.ServiceTest;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.extranet.AdultProvider;
import com.idgindigo.pms.utils.extranet.GroupMemberProvider;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static org.testng.Assert.assertNotNull;

/**
 * @author valentyn_vakatsiienko
 * @since 3/18/14 5:51 PM
 */
public class GroupMemberServiceTest extends ServiceTest {
    //    @Tested
//    private GroupMemberService mockGroupMemberService;
    @Inject
    private GroupMemberService service;
    @Inject
    private GroupMemberProvider provider;
    @Inject
    private AdultProvider adultProvider;

    @Test
    public void testGroupMemberCascade() {
        GroupMember member = provider.getTransientEntity(new Visitor<GroupMember>() {
            @Override
            public void visit(GroupMember entity) {
                entity.setPerson(adultProvider.getTransientEntity());
            }
        });

        member = service.create(member);
        assertNotNull(member.getPerson());
        assertNotNull(member.getPerson().getId());
    }
}