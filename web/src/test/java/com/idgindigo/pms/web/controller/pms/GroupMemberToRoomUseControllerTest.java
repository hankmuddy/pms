package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.domain.extranet.GroupMember;
import com.idgindigo.pms.domain.pms.GroupMemberToRoomUse;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.repository.pms.GroupMemberToRoomUseRepository;
import com.idgindigo.pms.restutils.exception.GroupMemberException;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.extranet.GroupMemberProvider;
import com.idgindigo.pms.utils.pms.GroupMemberToRoomUseProvider;
import com.idgindigo.pms.utils.pms.RoomUseProvider;
import com.idgindigo.pms.web.controller.BaseWebCrudTest;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertEquals;

/**
 * @author valentyn_vakatsiienko
 * @since 3/11/14 12:20 PM
 */
public class GroupMemberToRoomUseControllerTest extends BaseWebCrudTest<GroupMemberToRoomUse> {
    @Inject
    private GroupMemberToRoomUseProvider provider;
    @Inject
    private RoomUseProvider roomUseProvider;
    @Inject
    private GroupMemberProvider groupMemberProvider;
    @Inject
    private GroupMemberToRoomUseRepository repository;

    @Test
    public void testInvalidRoomUse() throws Exception {
        final GroupMemberToRoomUse member = getProvider().getTransientEntity(new Visitor<GroupMemberToRoomUse>() {
            @Override
            public void visit(GroupMemberToRoomUse entity) {
                entity.setRoomUse(roomUseProvider.getPersistentEntity());
            }
        });
        testBadRequest(preparePost(member), GroupMemberException.INVALID_ROOM_USE);
    }

    @Test
    public void testGroupMemberCascade() throws Exception {
        GroupMemberToRoomUse member = provider.getTransientEntity(new Visitor<GroupMemberToRoomUse>() {
            @Override
            public void visit(final GroupMemberToRoomUse entity) {
                entity.setGroupMember(groupMemberProvider.getTransientEntity());
                entity.setRoomUse(roomUseProvider.getPersistentEntity(new Visitor<RoomUse>() {
                    @Override
                    public void visit(RoomUse roomUse) {
                        roomUse.setCustomerGroup(entity.getGroupMember().getCustomerGroup());
                    }
                }));
            }
        });

        GroupMemberToRoomUse saved = convertResponseWithSingleObject(objectMapper, mvc.perform(preparePost(member)).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), GroupMemberToRoomUse.class);
        saved = repository.findOne(saved.getId());

        assertEquals(member.getRoomUse(), saved.getRoomUse());
        assertEquals(member.getGroupMember().getCustomerGroup(), saved.getGroupMember().getCustomerGroup());
        assertEquals(member.getGroupMember().getPerson(), saved.getGroupMember().getPerson());
    }

    @Test
    public void testCascade() throws Exception {
        //Create room use and move it 2 times
        final RoomUse roomUse = roomUseProvider.getPersistentEntity();
        final RoomUse moved1 = createMove(roomUse);
        RoomUse moved2 = createMove(moved1);
        Iterable<RoomUse> roomUses = new ArrayList<>(Arrays.asList(roomUse, moved1, moved2));

        //Create room member for 1st room use
        GroupMemberToRoomUse member = getProvider().getTransientEntity(new Visitor<GroupMemberToRoomUse>() {
            @Override
            public void visit(GroupMemberToRoomUse entity) {
                entity.setGroupMember(groupMemberProvider.getPersistentEntity(new Visitor<GroupMember>() {
                    @Override
                    public void visit(GroupMember entity) {
                        entity.setCustomerGroup(roomUse.getCustomerGroup());
                    }
                }));
                entity.setRoomUse(roomUse);
            }
        });
        member = convertResponseWithSingleObject(objectMapper, mvc.perform(preparePost(member)).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), GroupMemberToRoomUse.class);

        //Check cascade
        for (RoomUse use : roomUses) {
            List<GroupMemberToRoomUse> byRoomUse = repository.findByRoomUse(use);
            assertEquals(byRoomUse.size(), 1);

            GroupMemberToRoomUse linkage = byRoomUse.get(0);
            assertEquals(linkage.getGroupMember(), member.getGroupMember());
        }
    }

    private RoomUse createMove(final RoomUse roomUse) {
        return roomUseProvider.getPersistentEntity(new Visitor<RoomUse>() {
            @Override
            public void visit(RoomUse entity) {
                entity.setCustomerGroup(roomUse.getCustomerGroup());
                entity.setMovedFrom(roomUse);
            }
        });
    }

    @Override
    protected EntityProvider<GroupMemberToRoomUse> getProvider() {
        return provider;
    }

    @Override
    protected String getUrl() {
        return GroupMemberToRoomUseController.URL + "/";
    }
}
