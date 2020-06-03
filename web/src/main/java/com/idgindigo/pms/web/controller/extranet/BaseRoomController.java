package com.idgindigo.pms.web.controller.extranet;

import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.domain.BaseEntity;
import com.idgindigo.pms.domain.extranet.roomtype.BaseRoom;
import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.repository.extranet.BaseRoomRepository;
import com.idgindigo.pms.repository.extranet.RoomTypeRepository;
import com.idgindigo.pms.restutils.exception.PlanException;
import com.idgindigo.pms.restutils.view.ResponseView;
import com.idgindigo.pms.web.controller.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 3/21/14 11:24 AM
 */
@Controller
@RequestMapping(BaseRoomController.URL)
public class BaseRoomController {
    public static final String URL = WebConfiguration.REST_URL_PREFIX + BaseRoom.BASE_ROOM;
    @Inject
    private RoomTypeRepository roomTypeRepository;
    @Inject
    private BaseRoomRepository repository;

    @RequestMapping("tree")
    @ResponseBody
    @ResponseView(RoomType.TreeView.class)
    public ResponseEntity<List<RoomType>> tree() {
        return new ResponseEntity<>(roomTypeRepository.findByApprovedTrue());
    }

    @RequestMapping(value = "{id}/default", method = RequestMethod.PUT)
    @ResponseBody
    @Transactional
    public void setDefault(@PathVariable("id") long id) {
        if (!repository.isApproved(id)) {
            throw new PlanException(PlanException.SET_DEFAULT_ONLY_FOR_APPROVED, "approved");
        }
        BaseRoom one = repository.findOne(id);
        repository.eraseDefaults(one.roomType().getId());
        repository.setDefault(id);
    }

    @RequestMapping(value = "defaultByRoomType/{id}")
    @ResponseBody
    @Transactional
    @ResponseView(BaseEntity.SoloView.class)
    public ResponseEntity<BaseRoom> getDefault(@PathVariable("id") long roomTypeId) {
        BaseRoom baseRoom = repository.getDefault(roomTypeId);
        if (baseRoom == null) {
            throw new EntityNotFoundException();
        }
        return new ResponseEntity<>(baseRoom);
    }


    @RequestMapping
    @ResponseBody
    @ResponseView(BaseEntity.ListView.class)
    public ResponseEntity<List<BaseRoom>> all() {
        return new ResponseEntity<>(repository.findAll());
    }
}
