package com.idgindigo.pms.utils.pms;

import com.idgindigo.pms.domain.pms.GroupMemberToRoomUse;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.pms.GroupMemberToRoomUseRepository;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.extranet.GroupMemberProvider;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 3/11/14 12:08 PM
 */
@Component
public class GroupMemberToRoomUseProvider extends EntityProvider<GroupMemberToRoomUse> {
    @Inject
    private GroupMemberToRoomUseRepository repository;
    @Inject
    private GroupMemberProvider groupMemberProvider;
    @Inject
    private RoomUseProvider roomUseProvider;

    @Override
    public GroupMemberToRoomUse createAndFill() {
        final GroupMemberToRoomUse entity = new GroupMemberToRoomUse();
        entity.setGroupMember(groupMemberProvider.getPersistentEntity());
        entity.setRoomUse(roomUseProvider.getPersistentEntity(new Visitor<RoomUse>() {
            @Override
            public void visit(RoomUse roomUse) {
                roomUse.setCustomerGroup(entity.getGroupMember().getCustomerGroup());
            }
        }));
        return entity;
    }

    @Override
    public BaseRepository<GroupMemberToRoomUse> getRepository() {
        return repository;
    }
}
