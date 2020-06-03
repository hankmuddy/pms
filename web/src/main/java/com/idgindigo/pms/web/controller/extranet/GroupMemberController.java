package com.idgindigo.pms.web.controller.extranet;

import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.domain.extranet.GroupMember;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.extranet.GroupMemberRepository;
import com.idgindigo.pms.service.extranet.GroupMemberService;
import com.idgindigo.pms.service.filtering.FilteringService;
import com.idgindigo.pms.service.filtering.GroupMemberFilteringService;
import com.idgindigo.pms.web.controller.BaseCrudController;
import com.idgindigo.pms.web.controller.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 12/30/13 4:29 PM
 */
@Controller
@RequestMapping(GroupMemberController.URL)
public class GroupMemberController extends BaseCrudController<GroupMember> {
    public static final String URL = WebConfiguration.REST_URL_PREFIX + GroupMember.GROUP_MEMBER;

    @Inject
    private GroupMemberRepository repository;
    @Inject
    private GroupMemberService groupMemberService;
    @Inject
    private GroupMemberFilteringService filteringService;

    @Override
    public ResponseEntity<GroupMember> create(@RequestBody GroupMember entity) {
        return new ResponseEntity<>(groupMemberService.create(entity));
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<List<GroupMember>> update(@RequestBody List<GroupMember> entities) {
        List<GroupMember> res = new ArrayList<>();
        for (GroupMember entity : entities) {
            res.add(update(entity, entity.getId()).getContent());
        }
        return new ResponseEntity<>(res);
    }


    @Override
    public BaseRepository<GroupMember> getRepository() {
        return repository;
    }

    @Override
    protected FilteringService<GroupMember> getFilteringService() {
        return filteringService;
    }
}