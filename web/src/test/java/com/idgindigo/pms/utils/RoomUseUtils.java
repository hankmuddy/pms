package com.idgindigo.pms.utils;

import com.idgindigo.pms.domain.extranet.CustomerGroup;
import com.idgindigo.pms.domain.extranet.GroupMember;
import com.idgindigo.pms.domain.pms.GroupMemberToRoomUse;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.utils.extranet.GroupMemberProvider;
import com.idgindigo.pms.utils.pms.GroupMemberToRoomUseProvider;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 3/7/14 3:44 PM
 */
@Component
public class RoomUseUtils {
    @Inject
    private GroupMemberProvider groupMemberProvider;
    @Inject
    private GroupMemberToRoomUseProvider groupMemberToRoomUseProvider;

    public void populate(final CustomerGroup group, RoomUse... roomUses) {
        for (final RoomUse roomUse : roomUses) {
            final GroupMember groupMember = groupMemberProvider.getPersistentEntity(new Visitor<GroupMember>() {
                @Override
                public void visit(GroupMember entity) {
                    entity.setCustomerGroup(group);
                }
            });
            groupMemberToRoomUseProvider.getPersistentEntity(new Visitor<GroupMemberToRoomUse>() {
                @Override
                public void visit(GroupMemberToRoomUse entity) {
                    entity.setGroupMember(groupMember);
                    entity.setRoomUse(roomUse);
                }
            });
        }
    }
}
