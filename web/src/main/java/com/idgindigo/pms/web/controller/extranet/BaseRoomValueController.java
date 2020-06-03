package com.idgindigo.pms.web.controller.extranet;

import com.idgindigo.pms.channel.wubook.WubookImpl;
import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.domain.extranet.WubookRoomValue;
import com.idgindigo.pms.domain.extranet.roomtype.BaseRoom;
import com.idgindigo.pms.domain.extranet.roomtype.BaseRoomValue;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.extranet.BaseRoomValueRepository;
import com.idgindigo.pms.restutils.exception.VirtualRoomValueException;
import com.idgindigo.pms.restutils.view.ResponseView;
import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.service.channels.ChannelService;
import com.idgindigo.pms.service.filtering.BaseRoomValueFilteringService;
import com.idgindigo.pms.service.filtering.FilteringService;
import com.idgindigo.pms.web.controller.BaseCrudController;
import com.idgindigo.pms.web.controller.ResponseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.idgindigo.pms.domain.extranet.RoomClosed.OPEN;
import static com.idgindigo.pms.domain.extranet.roomtype.BaseRoomValue.CalendarView;
import static com.idgindigo.pms.logins.domain.Hotel.WubookImportStatus.RESERVATIONS_IMPORTED;

/**
 * @author valentyn_vakatsiienko
 * @since 2/20/14 12:15 PM
 */
@Controller
@RequestMapping(BaseRoomValueController.URL)
public class BaseRoomValueController extends BaseCrudController<BaseRoomValue> {
    public static final String URL = WebConfiguration.REST_URL_PREFIX + BaseRoomValue.BASE_ROOM_VALUE;

    @Inject
    private BaseRoomValueRepository repository;
    @Inject
    private BaseRoomValueFilteringService filteringService;
    @Inject
    private ChannelService channelService;

    @Override
    @Transactional
    public ResponseEntity<BaseRoomValue> create(@RequestBody BaseRoomValue entity) {
        ResponseEntity<BaseRoomValue> response = super.create(entity);
        updateChannels(Collections.singletonList(response.getContent()));
        return response;
    }

    @RequestMapping(value = "period", method = RequestMethod.POST)
    @Transactional
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public void createPeriod(@RequestBody @Valid PeriodDto dto) {
        List<BaseRoomValue> values = new ArrayList<>((Days.daysBetween(dto.getDateStart(), dto.getDateEnd()).getDays() + 1) * dto.getRooms().size());
        if (dto.getDateStart().isAfter(dto.getDateEnd())) {
            throw new VirtualRoomValueException(VirtualRoomValueException.INVALID_DATES, "dateEnd");
        }
        for (BaseRoom room : dto.getRooms()) {
            Map<LocalDate, BaseRoomValue> dateToValue = getExisting(dto.getDateStart(), dto.getDateEnd(), room);
            for (LocalDate date = dto.getDateStart(); !date.isAfter(dto.getDateEnd()); date = date.plusDays(1)) {
                BaseRoomValue value = dateToValue.containsKey(date) ? dateToValue.get(date) : new BaseRoomValue();
                BeanUtils.copyProperties(dto, value, "id");
                value.setDate(date);
                value.setRoom(room);
                values.add(repository.save(value));
            }
        }
        repository.flush();
        updateChannels(values);
    }

    private Map<LocalDate, BaseRoomValue> getExisting(LocalDate startDate, LocalDate endDate, BaseRoom room) {
        List<BaseRoomValue> existing = repository.findByRoomAndDates(startDate, endDate, room);
        Map<LocalDate, BaseRoomValue> res = new HashMap<>();
        for (BaseRoomValue value : existing) {
            res.put(value.getDate(), value);
        }
        return res;
    }

    @Override
    @Transactional
    public ResponseEntity<BaseRoomValue> update(@RequestBody BaseRoomValue entity, @PathVariable("id") Long id) {
        ResponseEntity<BaseRoomValue> response = super.update(entity, id);
        updateChannels(Collections.singletonList(response.getContent()));
        return response;
    }

    @Override
    @ResponseView(CalendarView.class)
    public ResponseEntity<List<BaseRoomValue>> listFiltered(@RequestParam(value = PAGE, defaultValue = "1") Integer page, @RequestParam(value = START, defaultValue = "0") Integer start, @RequestParam(value = LIMIT, defaultValue = DEFAULT_RECORDS_ON_PAGE) Integer limit, @RequestParam(value = SORT, required = false) String sortString, @RequestParam(value = CONNECTIVE, defaultValue = "and") String connective, HttpServletRequest request) throws Exception {
        return super.listFiltered(page, start, limit, sortString, connective, request);
    }

    @Override
    @Transactional
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        BaseRoomValue value = repository.findOne(id);
        super.delete(id);
        value.setClosed(OPEN);
        value.setClosedToDeparture(false);
        value.setMaxStay(0);
        value.setMinStay(0);
        value.setMinStayArrival(0);
        value.setOtaAllowed(true);
        updateChannels(Collections.singletonList(value));
        return new ResponseEntity<>();
    }

    private void updateChannels(List<BaseRoomValue> values) {
        if (SecurityUtils.isWubookConfigured() && SecurityUtils.getHotel().getImportStatus() == RESERVATIONS_IMPORTED && WubookImpl.ENABLED) {
            channelService.updateVirtualRoomValues(values, SecurityUtils.getWubookAccount());
        }
    }

    @Override
    protected FilteringService<BaseRoomValue> getFilteringService() {
        return filteringService;
    }

    @Override
    public BaseRepository<BaseRoomValue> getRepository() {
        return repository;
    }

    @Getter
    @Setter
    public static class PeriodDto extends WubookRoomValue {
        @NotNull
        private LocalDate dateStart;
        @NotNull
        private LocalDate dateEnd;
        @NotEmpty
        private List<BaseRoom> rooms;

    }
}
