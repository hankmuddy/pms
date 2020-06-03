package com.idgindigo.pms.web.controller.extranet;

import com.idgindigo.pms.domain.extranet.GroupMember;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.extranet.GroupMemberProvider;
import com.idgindigo.pms.web.controller.BaseWebCrudTest;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 12/30/13 4:34 PM
 */
public class GroupMemberControllerTest extends BaseWebCrudTest<GroupMember> {
    @Inject
    private GroupMemberProvider provider;

    @Override
    protected EntityProvider<GroupMember> getProvider() {
        return provider;
    }

    @Override
    protected String getUrl() {
        return GroupMemberController.URL + "/";
    }
}
