package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.domain.BaseEntity;
import com.idgindigo.pms.domain.extranet.plan.Plan;
import com.idgindigo.pms.domain.extranet.roomtype.BaseRoom;
import com.idgindigo.pms.domain.pms.BankDetails;
import com.idgindigo.pms.domain.pms.Room;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.extranet.CustomerGroupRepository;
import com.idgindigo.pms.repository.extranet.roomuse.BaseRoomUseRepository;
import com.idgindigo.pms.repository.pms.RoomRepository;
import com.idgindigo.pms.repository.pms.RoomUseRepository;
import com.idgindigo.pms.restutils.exception.RoomUseException;
import com.idgindigo.pms.restutils.view.ResponseView;
import com.idgindigo.pms.service.filtering.FilteringService;
import com.idgindigo.pms.service.filtering.RoomUseFilteringService;
import com.idgindigo.pms.service.pms.RoomUseService;
import com.idgindigo.pms.web.controller.BaseCrudController;
import com.idgindigo.pms.web.controller.ResponseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;

import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.BOOKING_BUTTON;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.WUBOOK_BUTTON;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Status.*;


/**
 * @author valentyn_vakatsiienko
 * @since 11/19/13 3:17 PM
 */
@Controller
@RequestMapping(RoomUseController.URL)
public class RoomUseController extends BaseCrudController<RoomUse> {
    public static final String URL = WebConfiguration.REST_URL_PREFIX + RoomUse.ROOM_USE;
    private static final Set<Source> FORBIDDEN_SOURCES = new HashSet<>(Arrays.asList(BOOKING_BUTTON, WUBOOK_BUTTON));
    @Inject
    private RoomUseRepository repository;
    @Inject
    private BaseRoomUseRepository baseRoomUseRepository;
    @Inject
    private RoomUseService roomUseService;
    @Inject
    private RoomRepository roomRepository;
    @Inject
    private RoomUseFilteringService filteringService;
    @Inject
    private CustomerGroupRepository customerGroupRepository;
    @Inject
    private RoomUseRepository roomUseRepository;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @ResponseBody
    @ResponseView(BaseEntity.SoloView.class)
    public ResponseEntity<RoomUse> create(@RequestBody RoomUse roomUse) {
        validateSource(roomUse.getSource());
        roomUse.setStatus(BOOKING_FREE);
        roomUse.setId(null);
        RoomUse savedRoomUse = roomUseService.create(roomUse, true);
        return new ResponseEntity<>(savedRoomUse);
    }

    private void validateSource(Source source) {
        if (FORBIDDEN_SOURCES.contains(source)) {
            throw new RoomUseException(RoomUseException.FORBIDDEN_SOURCE, "source");
        }
    }

    @RequestMapping("group")
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseView(BaseEntity.SoloView.class)
    public ResponseEntity<List<RoomUse>> create(@RequestBody @Valid RoomUseService.GroupRoomUseDto roomUseDto) {
        for (RoomUseService.RoomUseWithOverrides roomUseWithOverrides : roomUseDto.getRoomUses()) {
            if (roomUseWithOverrides.getPrices() == null) {
                roomUseWithOverrides.setPrices(new HashMap<LocalDate, Long>());
            }
            RoomUse roomUse = roomUseWithOverrides.getRoomUse();
            roomUse.setStatus(BOOKING_FREE);
            validateSource(roomUse.getSource());
            roomUse.setId(null);
        }
        return new ResponseEntity<>(roomUseService.create(roomUseDto, true));
    }

    @RequestMapping(value = "{id}/confirmed", method = RequestMethod.POST)
    @Transactional
    @ResponseBody
    public void confirm(@PathVariable("id") Long... ids) {
        for (RoomUse roomUse : repository.findByIdIn(Arrays.asList(ids))) {
            roomUseService.confirm(roomUse);
        }
    }

    @RequestMapping(value = "confirmedByGroup/{id}", method = RequestMethod.POST)
    @Transactional
    @ResponseBody
    public void confirmByGroup(@PathVariable("id") Long id) {
        List<RoomUse> roomUses = repository.findByCustomerGroupIdAndStatus(id, BOOKING_FREE);
        for (RoomUse roomUse : roomUses) {
            if (!roomUse.isMoved() || roomUse.getMovedFrom().isRefused()) {
                roomUseService.confirm(roomUse);
            }
        }
    }

    @Override
    @Transactional
    public ResponseEntity<RoomUse> update(@RequestBody RoomUse roomUse, @PathVariable("id") Long id) {
        RoomUse oldRoomUse = repository.findOne(id);
        validateUpdate(roomUse, oldRoomUse);

        roomUse.setId(id);
        roomUse.setStartDate(oldRoomUse.getStartDate());


        LocalDate oldEndDate = oldRoomUse.getEndDate();
        if (roomUse.getEndDate().isAfter(oldEndDate)) {
            roomUse = roomUseService.widen(roomUse, oldEndDate, roomUse.getEndDate());
        }

        return super.update(roomUse, id);
    }

    private void validateUpdate(RoomUse n, RoomUse o) {
        RoomUse.Status status = o.getStatus();
        if (status == REFUSE || status == OUTGO) {
            throw new RoomUseException(RoomUseException.UPDATE_INVALID_STATUS, "status");
        }
        if (repository.isMoved(o)) {
            throw new RoomUseException(RoomUseException.WIDEN_MOVED_ROOM_USE, "movedFrom");
        }
        if (n.getEndDate().isBefore(o.getEndDate())) {
            //New departure date must be after old
            throw new RoomUseException(RoomUseException.NEW_END_DATE_BEFORE_OLD, "endDate");
        }
    }
//    @Override
//    @ResponseBody
//    public void delete(@PathVariable("id") Long id) {
//        roomUseToServiceUseRepository.deleteByRoomUse(id);
//        super.delete(id);
//    }

    @RequestMapping(value = "{id}/checkedIn", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void checkIn(@PathVariable("id") Long id, @RequestBody @Valid CheckDto dto) {
        roomUseService.checkIn(repository.findOne(id), dto.time, true);
    }

    @RequestMapping(value = "checkedInByGroup/{id}", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public void checkInByGroup(@PathVariable("id") Long id, @RequestBody @Valid CheckDto dto) {
        List<RoomUse> roomUses = repository.findByCustomerGroupIdAndStatus(id, BOOKING_WARRANTY);
        for (RoomUse roomUse : roomUses) {
            if (!roomUse.getStartDate().isEqual(dto.getTime().toLocalDate())) {
                continue;
            }
            roomUseService.checkIn(roomUse, dto.time, true);
        }
    }

    @RequestMapping(value = "{id}/checkedOut", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public void checkOut(@PathVariable("id") Long id, @RequestBody @Valid CheckDto dto) {
        roomUseService.checkOut(repository.findOne(id), dto.time);
    }

    @RequestMapping(value = "checkedOutByGroup/{id}", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public void checkOutByGroup(@PathVariable("id") Long id, @RequestBody @Valid CheckDto dto) {
        List<RoomUse> roomUses = repository.findByCustomerGroupIdAndStatus(id, LIVING);
        for (RoomUse roomUse : roomUses) {
            if (!roomUse.getEndDate().isEqual(dto.getTime().toLocalDate())) {
                continue;
            }
            roomUseService.checkOut(roomUse, dto.time);
        }
    }

    @RequestMapping(value = "{id}/notArrived", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public void notArrived(@PathVariable("id") Long id) {
        RoomUse roomUse = repository.findOne(id);
        if (!roomUse.isFromChannel()) {
            throw new RoomUseException(RoomUseException.NOT_ARRIVED_ONLY_FOR_CHANNEL, "source");
        } else if (!roomUse.isBooking()) {
            throw new RoomUseException(RoomUseException.NOT_ARRIVED_ONLY_FOR_BOOKING, "status");
        }
        roomUseService.setNotArrived(roomUse);

    }

    @RequestMapping(value = "{id}/refused", method = RequestMethod.POST)
    @Transactional
    @ResponseBody
    public void refuse(@PathVariable("id") Long id, @RequestBody @Valid RefuseDto dto) {
        RoomUse roomUse = repository.findOne(id);
        if (roomUse.isFromChannel() && roomUse.isBooking() && roomUse.getCreatedBy() == null) {
            throw new RoomUseException(RoomUseException.REFUSE_CHANNEL_BOOKING, "source");
        }
        roomUseService.refuse(roomUse, dto.date, true, dto.bankDetails);
    }

    @RequestMapping(value = "/refusedByGroup/{id}", method = RequestMethod.POST)
    @Transactional
    @ResponseBody
    public void groupRefuse(@PathVariable("id") Long id, @RequestBody @Valid RefuseDto dto) {
        roomUseService.refuse(customerGroupRepository.findOne(id), dto.date, true, dto.bankDetails);
    }

    @RequestMapping(value = "{id}/moved", method = RequestMethod.POST)
    @Transactional
    @ResponseBody
    public void move(@PathVariable("id") Long id, @Valid @RequestBody MoveDto moveDto) {//TODO Tests
        RoomUse roomUse = repository.findOne(id);
        Room moveRoom = roomRepository.findOne(moveDto.getRoom().getId());
        moveDto.setRoom(moveRoom);
        validateMove(roomUse, moveDto);
        if (roomUse.isFromChannel()) {
            moveDto.setBaseRoom(roomUse.getBaseRoom());
        }
        roomUseService.move(roomUse, moveDto, true, moveDto.bankDetails);
    }

    private void validateMove(RoomUse roomUse, MoveDto moveDto) {
        if (roomUse.isFromChannel() && !roomUse.getRoom().getRoomType().equals(moveDto.getRoom().getRoomType())) {
            throw new RoomUseException(RoomUseException.MOVE_CHANNEL_BOOKING_WRONG_ROOM_TYPE, "room.roomType");
        }
    }

    @RequestMapping("{id}/containsCustomer")
    @ResponseBody
    public ResponseEntity<Boolean> containsCustomer(@PathVariable("id") Long id) {//TODO Tests
        return new ResponseEntity<>(repository.containsCustomer(id));
    }

    @RequestMapping("getFreeRooms")
    @ResponseBody
    @ResponseView(BaseEntity.ListView.class)
    public ResponseEntity<List<Room>> findFreeRooms(@RequestParam(value = "roomTypeId", required = false) Long roomTypeId, @RequestParam("start") Long startSecs, @RequestParam("end") Long endSecs) {
        LocalDate start = new LocalDate(startSecs * 1000, DateTimeZone.UTC);
        LocalDate end = new LocalDate(endSecs * 1000, DateTimeZone.UTC);
        List<Room> rooms;
        if (roomTypeId != null) {
            rooms = roomUseRepository.getFreeRooms(roomTypeId, start, end);
        } else {
            rooms = roomUseRepository.getFreeRooms(start, end);
        }
        return new ResponseEntity<>(rooms);
    }

    @Getter
    @Setter
    public static class MoveDto {
        @NotNull
        private Room room;
        @NotNull
        private BaseRoom baseRoom;
        private Plan plan;
        @NotNull
        private LocalDate sinceDate;
        private boolean upgrade;
        private boolean customerPays;
        private BankDetails bankDetails;
    }

    @Getter
    @Setter
    public static class RefuseDto {
        @NotNull
        private LocalDate date;
        private BankDetails bankDetails;
    }


    @Override
    protected FilteringService<RoomUse> getFilteringService() {
        return filteringService;
    }

    @Override
    public BaseRepository<RoomUse> getRepository() {
        return repository;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    public static class CheckDto {
        @NotNull
        public LocalDateTime time;

        public CheckDto(LocalDateTime hotelTime) {
            time = hotelTime;
        }
    }
}
