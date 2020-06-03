package com.idgindigo.pms.web.controller.extranet;

import com.idgindigo.pms.channel.wubook.WubookImpl;
import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.domain.extranet.roomtype.VirtualRoom;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.extranet.RoomTypeRepository;
import com.idgindigo.pms.repository.extranet.VirtualRoomRepository;
import com.idgindigo.pms.restutils.exception.RestFriendlyException;
import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.service.approve.ApproveService;
import com.idgindigo.pms.service.approve.VirtualRoomApproveService;
import com.idgindigo.pms.service.channels.ChannelService;
import com.idgindigo.pms.service.extranet.LivingService;
import com.idgindigo.pms.service.filtering.FilteringService;
import com.idgindigo.pms.service.filtering.VirtualRoomFilteringService;
import com.idgindigo.pms.web.controller.ApprovableController;
import com.idgindigo.pms.web.controller.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static com.idgindigo.pms.logins.domain.Hotel.WubookImportStatus.RESERVATIONS_IMPORTED;

/**
 * @author valentyn_vakatsiienko
 * @since 1/15/14 4:32 PM
 */
@Controller
@RequestMapping(VirtualRoomController.URL)
@Transactional
public class VirtualRoomController extends ApprovableController<VirtualRoom> {
    private static final Logger logger = LoggerFactory.getLogger(VirtualRoomController.class);
    public static final String URL = WebConfiguration.REST_URL_PREFIX + VirtualRoom.VIRTUAL_ROOM;

    @Inject
    private VirtualRoomRepository repository;
    @Inject
    private RoomTypeRepository roomTypeRepository;
    @Inject
    private VirtualRoomFilteringService filteringService;
    @Inject
    private VirtualRoomApproveService virtualRoomApproveService;
    @Inject
    private LivingService livingService;
    @Inject
    private ChannelService channelService;

    @Override
    @Transactional
    public ResponseEntity<VirtualRoom> create(@RequestBody VirtualRoom entity) {
        if (repository.findByShortname(entity.getShortname()) != null ||
                roomTypeRepository.findByShortname(entity.getShortname()) != null) {
            throw new RestFriendlyException(RestFriendlyException.DUPLICATE_ENTRY, "shortname");
        }
        return super.create(entity);
    }

    @Override
    @Transactional
    public void approve(@PathVariable("id") Long... ids) {
        List<VirtualRoom> approved = new ArrayList<>();
        for (Long id : ids) {
            VirtualRoom virtualRoom = repository.findOne(id);
            if (virtualRoom.getApproved()) {
                logger.warn("Could not approve already approved virtualRoom: {}", virtualRoom);
                continue;
            }
            super.approve(id);
            livingService.handleCreate(virtualRoom);
            approved.add(virtualRoom);
        }
        for (VirtualRoom baseRoom : approved) {
            if (WubookImpl.ENABLED && SecurityUtils.isWubookConfigured() && SecurityUtils.getHotel().getImportStatus() == RESERVATIONS_IMPORTED) {
//                baseRoom.roomType().setRid(roomTypeRepository.findOne(baseRoom.roomType().getId()).getRid());
                long rid = channelService.newVirtualRoom(baseRoom, SecurityUtils.getWubookAccount());
                logger.info("Pushed baseRoom to wubook with wubook id:{}", rid);
                baseRoom.setRid(rid);
                repository.saveAndFlush(baseRoom);
            }
        }
    }

    @Override
    public BaseRepository<VirtualRoom> getRepository() {
        return repository;
    }

    @Override
    public ApproveService<VirtualRoom> getApproveService() {
        return virtualRoomApproveService;
    }

    @Override
    protected FilteringService<VirtualRoom> getFilteringService() {
        return filteringService;
    }
}
