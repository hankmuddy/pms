package com.idgindigo.pms.domain.extranet;

import com.idgindigo.pms.domain.BasePersistenceTest;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.extranet.GroupMemberProvider;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 12/30/13 4:33 PM
 */
public class GroupMemberTest extends BasePersistenceTest<GroupMember> {
    @Inject
    private GroupMemberProvider provider;

    @Override
    protected EntityProvider<GroupMember> getProvider() {
        return provider;
    }
}
