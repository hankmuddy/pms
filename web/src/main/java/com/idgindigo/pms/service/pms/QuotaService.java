package com.idgindigo.pms.service.pms;

import com.idgindigo.pms.channel.wubook.WubookImpl;
import com.idgindigo.pms.domain.extranet.BaseRoomUse;
import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeQuota;
import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeValue;
import com.idgindigo.pms.repository.extranet.RoomTypeRepository;
import com.idgindigo.pms.repository.extranet.rate.RoomTypeValueRepository;
import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.service.admin.SettingsService;
import com.idgindigo.pms.service.channels.ChannelService;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.idgindigo.pms.logins.domain.Hotel.WubookImportStatus.RESERVATIONS_IMPORTED;

/**
 * @author valentyn_vakatsiienko
 * @since 11/28/13 5:50 PM
 */
@Service
public class QuotaService {
    @Inject
    private RoomTypeValueRepository roomTypeValueRepository;
    @Inject
    private RoomTypeRepository roomTypeRepository;
    @Inject
    private ChannelService channelService;
    @Inject
    private SettingsService settingsService;

    public void manageQuota(BaseRoomUse o, BaseRoomUse n, RoomType roomType) {
        LocalDate oldStartDate = o.getStartDate();
        LocalDate oldEndDate = o.getEndDate();
        LocalDate newStartDate = n.getStartDate();
        LocalDate newEndDate = n.getEndDate();

        if (newStartDate.isBefore(oldStartDate)) {
            updateQuota(newStartDate, oldStartDate, roomType, -1, true);
        } else if (newStartDate.isAfter(oldEndDate)) {
            updateQuota(oldStartDate, newStartDate, roomType, 1, true);
        }
        if (newEndDate.isBefore(oldEndDate)) {
            updateQuota(newEndDate, oldEndDate, roomType, 1, true);
        } else if (newEndDate.isAfter(oldEndDate)) {
            updateQuota(oldEndDate, newEndDate, roomType, -1, true);
        }
    }

    /*
    Updates quota for given roomType excluding last day
     */
    public void updateQuota(LocalDate start, LocalDate dateEnd, RoomType roomType, int delta, boolean updateChannels) {
        LocalDate hotelDate = settingsService.getHotelDate();
        LocalDate end = dateEnd.minusDays(1);
        if (end.isBefore(hotelDate)) {
            return;
        }
        if (start.isBefore(hotelDate)) {
            start = hotelDate;
        }
        List<RoomTypeValue> values = roomTypeValueRepository.findByRoomTypeAndDateBetween(roomType, start, end);
        if (values.size() < Days.daysBetween(start, dateEnd).getDays()) {
            List<RoomTypeValue> rtValues = fillBlanks(roomType, values, start, end);
            roomTypeValueRepository.save(rtValues);
        }
        roomTypeValueRepository.flush();//need to flush as update clears EntityManager's cache
        roomTypeValueRepository.updateQuota(start, end, roomType, delta);
        if (updateChannels && SecurityUtils.isWubookConfigured() && SecurityUtils.getHotel().getImportStatus() == RESERVATIONS_IMPORTED && WubookImpl.ENABLED) {
            channelService.updateRoomValues(roomTypeValueRepository.findByRoomTypeAndDateBetween(roomType, start, end), start, SecurityUtils.getWubookAccount());
        }
    }

    public List<RoomTypeValue> fillBlanks(RoomType roomType, List<RoomTypeValue> values, LocalDate start, LocalDate end) {
        List<RoomTypeValue> rtValues = new ArrayList<>();
        roomType = roomTypeRepository.findOne(roomType.getId());
        Collections.sort(values, RoomTypeValue.DATE_COMPARATOR);
        LocalDate current = start;
        for (RoomTypeValue value : values) {
            while (current.isBefore(value.getDate())) {
                rtValues.add(createRoomValue(current, roomType));
                current = current.plusDays(1);
            }
            current = current.plusDays(1);
        }
        while (!current.isAfter(end)) {
            rtValues.add(createRoomValue(current, roomType));
            current = current.plusDays(1);
        }
        return rtValues;
    }

    private RoomTypeValue createRoomValue(LocalDate date, RoomType roomType) {
        RoomTypeValue newValue = new RoomTypeValue();
        newValue.setRoomType(roomType);
        newValue.setDate(date);
        newValue.setRoomsAvailable(roomType.getOtaRooms());
        return newValue;
    }

    public void updateQuota(LocalDate start, RoomType roomType, int delta) {
        roomTypeValueRepository.flush();//need to flush as update clears EntityManager's cache
        roomTypeValueRepository.updateQuota(start, roomType, delta);
        if (SecurityUtils.isWubookConfigured() && SecurityUtils.getHotel().getImportStatus() == RESERVATIONS_IMPORTED && WubookImpl.ENABLED) {
            List<RoomTypeValue> values = roomTypeValueRepository.findByRoomTypeAndDateNotBefore(roomType, start);
            if (!values.isEmpty()) {
                channelService.updateSparseRoomValues(values, SecurityUtils.getWubookAccount());
            }
        }
    }

    public List<RoomTypeQuota> getRoomTypeQuota(LocalDate start, LocalDate end) {
        return roomTypeValueRepository.getRoomTypeQuota(start, end);
    }
}
