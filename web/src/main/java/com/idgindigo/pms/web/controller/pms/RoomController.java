package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.domain.pms.Room;
import com.idgindigo.pms.repository.extranet.RoomTypeRepository;
import com.idgindigo.pms.repository.pms.RoomRepository;
import com.idgindigo.pms.restutils.exception.RestFriendlyException;
import com.idgindigo.pms.service.admin.SettingsService;
import com.idgindigo.pms.service.approve.ApproveService;
import com.idgindigo.pms.service.approve.RoomApproveService;
import com.idgindigo.pms.service.channels.ChannelService;
import com.idgindigo.pms.service.filtering.FilteringService;
import com.idgindigo.pms.service.filtering.RoomFilteringService;
import com.idgindigo.pms.service.pms.QuotaService;
import com.idgindigo.pms.service.pms.RoomService;
import com.idgindigo.pms.web.controller.ApprovableController;
import com.idgindigo.pms.web.controller.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;

/**
 * @author vomel
 * @since 01.11.13 17:21
 */
@Controller
@RequestMapping(RoomController.URL)
public class RoomController extends ApprovableController<Room> {
    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);
    public static final String URL = WebConfiguration.REST_URL_PREFIX + "room";
    @Inject
    private RoomRepository repository;
    @Inject
    private RoomTypeRepository roomTypeRepository;
    @Inject
    private RoomApproveService approveService;
    @Inject
    private RoomFilteringService filteringService;
    @Inject
    private QuotaService quotaService;
    @Inject
    private SettingsService settingsService;
    @Inject
    private ChannelService channelService;
    @Inject
    private RoomService roomService;

    @Override
    public ResponseEntity<Room> create(@RequestBody Room entity) {
        ResponseEntity<Room> response = super.create(entity);
        Room room = response.getContent();

        repository.flush();
        room.setPosition(room.getId() <= Integer.MAX_VALUE ? room.getId().intValue() : Integer.MAX_VALUE);
        repository.setPosition(room, room.getPosition());
        return response;
    }

    @Override
    @Transactional
    public void approve(@PathVariable("id") Long... ids) {
        for (Long id : ids) {
            Room room = repository.findOne(id);
            if (room.getApproved()) {
                logger.warn("Could not approve already approved entity: {}", room);
                continue;
            }
            if (repository.findByApprovedTrue().size() >= settingsService.getMaxRooms()) {
                throw new RestFriendlyException(RestFriendlyException.MAX_ROOMS_EXCEEDED, "room");
            }
            RoomType roomType = room.getRoomType();
            roomTypeRepository.addRoom(roomType);
            int delta = 1;
            quotaService.updateQuota(settingsService.getHotelDate(), roomType, delta);
            super.approve(id);
        }
    }

    @RequestMapping(value = ID + "/position/{pos}", method = RequestMethod.PUT)
    @ResponseBody
    public void setPosition(@PathVariable("id") long id, @PathVariable("pos") int pos) {
        Room room = repository.findOne(id);
        if (room == null) {
            throw new EntityNotFoundException();
        }
        roomService.setPosition(room, pos);
    }

    @RequestMapping("count")
    @ResponseBody
    public ResponseEntity<Long> getCount() {
        return new ResponseEntity<>(repository.countApproved());
    }

    @Override
    protected void applyModifications(Room toUpdate, Room updated) {
        toUpdate.setNumber(updated.getNumber());
        toUpdate.setFloor(updated.getFloor());
        toUpdate.setAccommodation(updated.getAccommodation());
    }

    @Override
    protected FilteringService<Room> getFilteringService() {
        return filteringService;
    }

    @Override
    public ApproveService<Room> getApproveService() {
        return approveService;
    }
}