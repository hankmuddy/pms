package com.idgindigo.pms.web.controller.extranet;

import com.idgindigo.pms.channel.wubook.WubookImpl;
import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeValue;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.extranet.rate.RoomTypeValueRepository;
import com.idgindigo.pms.restutils.exception.VirtualRoomValueException;
import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.service.channels.ChannelService;
import com.idgindigo.pms.service.filtering.FilteringService;
import com.idgindigo.pms.service.filtering.RoomTypeValueFilteringService;
import com.idgindigo.pms.web.controller.BaseCrudController;
import com.idgindigo.pms.web.controller.ResponseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.Days;
import org.joda.time.LocalDate;
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
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.idgindigo.pms.logins.domain.Hotel.WubookImportStatus.RESERVATIONS_IMPORTED;

/**
 * @author valentyn_vakatsiienko
 * @since 11/11/13 6:26 PM
 */
@Controller
@RequestMapping(RoomTypeValueController.URL)
public class RoomTypeValueController extends BaseCrudController<RoomTypeValue> {
    public static final String URL = WebConfiguration.REST_URL_PREFIX + RoomTypeValue.ROOM_TYPE_VALUE;
    @Inject
    private RoomTypeValueRepository repository;
    @Inject
    private RoomTypeValueFilteringService roomTypeValueFilteringService;
    @Inject
    private ChannelService channelService;

    @Override
    public BaseRepository<RoomTypeValue> getRepository() {
        return repository;
    }

    @Override
    public ResponseEntity<RoomTypeValue> create(@RequestBody RoomTypeValue entity) {
        ResponseEntity<RoomTypeValue> res = super.create(entity);
        updateSingleValue(entity, res);
        return res;
    }

    @Override
    public ResponseEntity<RoomTypeValue> update(@RequestBody RoomTypeValue entity, @PathVariable("id") Long id) {
        ResponseEntity<RoomTypeValue> res = super.update(entity, id);
        updateSingleValue(entity, res);
        return res;
    }

    private void updateSingleValue(RoomTypeValue entity, ResponseEntity<RoomTypeValue> res) {
        if (SecurityUtils.isWubookConfigured() && SecurityUtils.getHotel().getImportStatus() == RESERVATIONS_IMPORTED && WubookImpl.ENABLED) {
            channelService.updateRoomValues(Collections.singletonList(res.getContent()), entity.getDate(), SecurityUtils.getWubookAccount());
        }
    }

    @RequestMapping(value = "period", method = RequestMethod.POST)
    @Transactional
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public void createPeriod(@RequestBody @Valid PeriodDto dto) {
        List<RoomTypeValue> values = new ArrayList<>((Days.daysBetween(dto.getDateStart(), dto.getDateEnd()).getDays() + 1) * dto.getRoomTypes().size());
        if (dto.getDateStart().isAfter(dto.getDateEnd())) {
            throw new VirtualRoomValueException(VirtualRoomValueException.INVALID_DATES, "dateEnd");
        }
        for (RoomType room : dto.getRoomTypes()) {
            Map<LocalDate, RoomTypeValue> dateToValue = getExisting(dto.getDateStart(), dto.getDateEnd(), room);
            for (LocalDate date = dto.getDateStart(); !date.isAfter(dto.getDateEnd()); date = date.plusDays(1)) {
                RoomTypeValue value = dateToValue.containsKey(date) ? dateToValue.get(date) : new RoomTypeValue();
                value.setDate(date);
                value.setRoomType(room);
                value.setRoomsAvailable(dto.getRoomsAvailable());
                values.add(repository.save(value));
            }
        }
        repository.flush();
        if (SecurityUtils.isWubookConfigured() && SecurityUtils.getHotel().getImportStatus() == RESERVATIONS_IMPORTED && WubookImpl.ENABLED) {
            channelService.updateRoomValues(values, dto.getDateStart(), SecurityUtils.getWubookAccount());
        }
    }

    private Map<LocalDate, RoomTypeValue> getExisting(LocalDate startDate, LocalDate endDate, RoomType room) {
        List<RoomTypeValue> existing = repository.findByRoomTypeAndDateBetween(room, startDate, endDate);
        Map<LocalDate, RoomTypeValue> res = new HashMap<>();
        for (RoomTypeValue value : existing) {
            res.put(value.getDate(), value);
        }
        return res;
    }
/*
    @Override
    @ResponseView(RoomTypeValue.CalendarView.class)
    public ResponseEntity<List<RoomTypeValue>> listFiltered(@RequestParam(value = BaseCrudController.PAGE, defaultValue = "1") Integer page,
                                                            @RequestParam(value = BaseCrudController.START, defaultValue = "0") Integer start,
                                                            @RequestParam(value = BaseCrudController.LIMIT, defaultValue = BaseCrudController.DEFAULT_RECORDS_ON_PAGE) Integer limit,
                                                            @RequestParam(value = BaseCrudController.SORT, required = false) String sortString,
                                                            @RequestParam(value = CONNECTIVE, defaultValue = "and") String connective,
                                                            HttpServletRequest request) throws Exception {
        return super.listFiltered(page, start, limit, sortString, connective, request);
    }
*/

    @Override
    @SuppressWarnings("unchecked")
    protected FilteringService getFilteringService() {
        return roomTypeValueFilteringService;
    }

    @Getter
    @Setter
    public static class PeriodDto {
        @NotNull
        private LocalDate dateStart;
        @NotNull
        private LocalDate dateEnd;
        @NotNull
        private int roomsAvailable;
        @NotEmpty
        private List<RoomType> roomTypes;
    }
}
