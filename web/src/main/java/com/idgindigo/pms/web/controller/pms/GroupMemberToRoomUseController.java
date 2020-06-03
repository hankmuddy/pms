package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.domain.pms.GroupMemberToRoomUse;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.extranet.GroupMemberRepository;
import com.idgindigo.pms.repository.pms.GroupMemberToRoomUseRepository;
import com.idgindigo.pms.repository.pms.RoomUseRepository;
import com.idgindigo.pms.restutils.exception.GroupMemberException;
import com.idgindigo.pms.service.extranet.GroupMemberService;
import com.idgindigo.pms.service.filtering.FilteringService;
import com.idgindigo.pms.service.filtering.GroupMemberToRoomUseFilteringService;
import com.idgindigo.pms.web.controller.BaseCrudController;
import com.idgindigo.pms.web.controller.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 3/11/14 11:52 AM
 */
@Controller
@RequestMapping(GroupMemberToRoomUseController.URL)
public class GroupMemberToRoomUseController extends BaseCrudController<GroupMemberToRoomUse> {
    public static final String URL = WebConfiguration.REST_URL_PREFIX + GroupMemberToRoomUse.GROUP_MEMBER_TO_ROOM_USE;

    @Inject
    private GroupMemberToRoomUseRepository repository;
    @Inject
    private GroupMemberToRoomUseFilteringService filteringService;
    @Inject
    private GroupMemberRepository groupMemberRepository;
    @Inject
    private RoomUseRepository roomUseRepository;
    @Inject
    private GroupMemberService groupMemberService;

    @Override
    @Transactional
    public ResponseEntity<GroupMemberToRoomUse> create(@RequestBody @Valid GroupMemberToRoomUse entity) {
        if (entity.getGroupMember().getId() == null) {
            entity.setGroupMember(groupMemberService.create(entity.getGroupMember()));
        }
        validateCreate(entity);
        ResponseEntity<GroupMemberToRoomUse> res = super.create(entity);
        tryCascadeCreate(res.getContent());
        return res;
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public ResponseEntity<List<GroupMemberToRoomUse>> create(@RequestBody @Valid List<GroupMemberToRoomUse> toCreate) {
        List<GroupMemberToRoomUse> res = new ArrayList<>(toCreate.size());
        for (GroupMemberToRoomUse groupMemberToRoomUse : toCreate) {
            res.add(create(groupMemberToRoomUse).getContent());
        }
        return new ResponseEntity<>(res);
    }

    private void validateCreate(GroupMemberToRoomUse entity) {
        List<RoomUse> roomUses = roomUseRepository.findByCustomerGroup(groupMemberRepository.findOne(entity.getGroupMember().getId()).getCustomerGroup());
        if (!roomUses.contains(entity.getRoomUse())) {
            throw new GroupMemberException(GroupMemberException.INVALID_ROOM_USE, "roomUse");
        }
    }

    private void tryCascadeCreate(GroupMemberToRoomUse entity) {
        RoomUse movedTo = roomUseRepository.getMovedTo(entity.getRoomUse());
        if (movedTo != null) {
            GroupMemberToRoomUse cascade = new GroupMemberToRoomUse(entity.getGroupMember(), movedTo);
            create(cascade);
        }
    }

    @Override
    public BaseRepository<GroupMemberToRoomUse> getRepository() {
        return repository;
    }

    @Override
    protected FilteringService<GroupMemberToRoomUse> getFilteringService() {
        return filteringService;
    }
}
