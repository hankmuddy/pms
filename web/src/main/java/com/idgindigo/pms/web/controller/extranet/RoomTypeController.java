package com.idgindigo.pms.web.controller.extranet;

import com.idgindigo.pms.channel.wubook.WubookImpl;
import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.repository.extranet.BaseRoomRepository;
import com.idgindigo.pms.repository.extranet.RoomTypeRepository;
import com.idgindigo.pms.repository.extranet.RoomTypeToFacilityRepository;
import com.idgindigo.pms.restutils.exception.RestFriendlyException;
import com.idgindigo.pms.restutils.view.ResponseView;
import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.service.approve.ApproveService;
import com.idgindigo.pms.service.approve.RoomTypeApproveService;
import com.idgindigo.pms.service.channels.ChannelService;
import com.idgindigo.pms.service.extranet.LivingService;
import com.idgindigo.pms.service.filtering.FilteringService;
import com.idgindigo.pms.service.filtering.RoomTypeFilteringService;
import com.idgindigo.pms.web.controller.ApprovableController;
import com.idgindigo.pms.web.controller.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static com.idgindigo.pms.logins.domain.Hotel.WubookImportStatus.RESERVATIONS_IMPORTED;

/**
 * @author vomel
 * @since 01.11.13 15:58
 */

@Controller
@RequestMapping(RoomTypeController.URL)
public class RoomTypeController extends ApprovableController<RoomType> {
    public static final String URL = WebConfiguration.REST_URL_PREFIX + RoomType.ROOM_TYPE;
    @Inject
    private RoomTypeApproveService approveService;
    @Inject
    private RoomTypeRepository repository;
    @Inject
    private RoomTypeToFacilityRepository rttfRepository;
    @Inject
    private BaseRoomRepository baseRoomRepository;
    @Inject
    private ChannelService channelService;
    @Inject
    private RoomTypeFilteringService filteringService;
    @Inject
    private LivingService livingService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    @Override
    public ResponseEntity<RoomType> create(@RequestBody RoomType entity) {
        validateShortname(entity);
//        RoomType roomType = new RoomType();
//        BeanUtils.copyProperties(entity, roomType, "virtualRoom");
        //        BaseRoom baseRoom = entity.getBaseRoom();
//        baseRoom.setRoomType(created.getContent());
//        baseRoomRepository.save(baseRoom);
        return super.create(entity);
    }

    @Override
    @ResponseView(RoomType.DetailedView.class)
    public ResponseEntity<RoomType> getById(@PathVariable("id") Long id) {
        return super.getById(id);
    }

    private void validateShortname(RoomType entity) {
        if (baseRoomRepository.findByShortname(entity.getShortname()) != null) {
            throw new RestFriendlyException(RestFriendlyException.DUPLICATE_ENTRY, "shortname");
        }
    }

    @Override
    @Transactional
    public void approve(@PathVariable("id") Long... ids) {
        List<RoomType> approved = new ArrayList<>();
        for (Long id : ids) {
            RoomType roomType = repository.findOne(id);
            baseRoomRepository.setDefault(roomType.getId());
            if (roomType.getApproved()) {
                logger.warn("Could not approve already approved roomType: {}", roomType);
                continue;
            }
            super.approve(id);
            livingService.handleCreate(roomType);
            approved.add(roomType);
        }
        for (RoomType roomType : approved) {
            if (WubookImpl.ENABLED && SecurityUtils.isWubookConfigured() && SecurityUtils.getHotel().getImportStatus() == RESERVATIONS_IMPORTED) {
                long rid = channelService.newRoom(roomType, SecurityUtils.getWubookAccount());
                logger.info("Pushed roomType to wubook with wubook id:{}", rid);
                roomType.setRid(rid);
                repository.saveAndFlush(roomType);
            }
        }
    }

    @Override
    protected FilteringService<RoomType> getFilteringService() {
        return filteringService;
    }

    @Override
    public ApproveService<RoomType> getApproveService() {
        return approveService;
    }
}
