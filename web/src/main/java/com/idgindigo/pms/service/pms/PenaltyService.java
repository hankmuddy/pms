package com.idgindigo.pms.service.pms;

import com.idgindigo.pms.domain.pms.Bill;
import com.idgindigo.pms.domain.pms.LivingUse;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.domain.pms.SimpleServiceUse;
import com.idgindigo.pms.logins.domain.HotelInfo;
import com.idgindigo.pms.repository.extranet.service.SimpleServiceRepository;
import com.idgindigo.pms.repository.pms.LivingUseRepository;
import com.idgindigo.pms.repository.pms.SimpleServiceUseRepository;
import com.idgindigo.pms.service.admin.SettingsService;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

import static com.idgindigo.pms.service.pms.PenaltyService.HandlingType.EARLY;
import static com.idgindigo.pms.service.pms.PenaltyService.HandlingType.LATE;

/**
 * @author valentyn_vakatsiienko
 * @since 1/23/14 6:47 PM
 */
@Service
public class PenaltyService {
    @Inject
    private LivingUseRepository livingUseRepository;
    @Inject
    private BillService billService;
    @Inject
    private SimpleServiceUseRepository simpleServiceUseRepository;
    @Inject
    private SimpleServiceRepository simpleServiceRepository;
    @Inject
    private RoomUseService roomUseService;
    @Inject
    private SettingsService settingsService;

    /**
     * @param roomUse
     * @param time
     * @return whether handling actually occurred
     */
    public boolean handleEarlyCheckIn(RoomUse roomUse, LocalDateTime time) {
        return handleWithType(roomUse, time, EARLY);
    }

    /**
     * @param roomUse
     * @param time
     * @return whether handling actually occurred
     */
    public boolean handleLateCheckOut(RoomUse roomUse, LocalDateTime time) {
        return handleWithType(roomUse, time, LATE);
    }

    public boolean handleWithType(RoomUse roomUse, LocalDateTime time, HandlingType handlingType) {
        Bill bill = null;
        if (handlingType.canHandleWithinPeriod(time, settingsService)) {
            bill = performHandlingWithinPeriod(roomUse, handlingType);
        } else if (handlingType.canHandleOffPeriod(time, settingsService)) {
            bill = performHandlingOffPeriod(roomUse, handlingType);
        }
        if (bill != null) {
            billService.updateTotal(bill, null);
            return true;
        }
        return false;
    }

    private Bill performHandlingOffPeriod(RoomUse roomUse, HandlingType handlingType) {
        LivingUse targetLivingUse = getTargetLivingUse(roomUse, handlingType);
        Bill bill = targetLivingUse.getBill();

        LocalDate targetDate = targetLivingUse.getDate().plusDays(handlingType == EARLY ? -1 : 1);

        roomUseService.generateServiceUses(targetDate, targetDate.plusDays(1), roomUse, bill, Collections.<LocalDate, Long>emptyMap());
        return bill;
    }

    private Bill performHandlingWithinPeriod(RoomUse roomUse, HandlingType handlingType) {
        Integer multiplier = settingsService.getHotelInfo().getMultiplier();
        if (multiplier == null) {
            return null;
        }
        LivingUse targetLivingUse = getTargetLivingUse(roomUse, handlingType);
        Long value = getPenaltyAmount(multiplier, targetLivingUse);

        SimpleServiceUse penalty = new SimpleServiceUse();
        penalty.setService(simpleServiceRepository.findByTitle(handlingType.getServiceName()));
        penalty.setRoomUse(roomUse);
        penalty.setBill(targetLivingUse.getBill());
        penalty.setDate(targetLivingUse.getDate());
        penalty.setQuantity(1);
        penalty.setRawTotal(value);
        penalty.setTotal(value);
        penalty.setApproved(true);
        simpleServiceUseRepository.save(penalty);
        return targetLivingUse.getBill();
    }


    private LivingUse getTargetLivingUse(RoomUse roomUse, HandlingType handlingType) {
        List<LivingUse> livingUses = livingUseRepository.findByRoomUse(roomUse);
        if (livingUses.isEmpty()) {
            throw new IllegalArgumentException("living uses can not be empty");
        }
        LocalDate targetDate = handlingType == EARLY ? roomUse.getStartDate() : roomUse.getEndDate().minusDays(1);
        for (LivingUse livingUse : livingUses) {
            if (livingUse.getDate().equals(targetDate)) {
                return livingUse;
            }
        }
        return null;
    }

    private long getPenaltyAmount(Integer multiplier, LivingUse serviceUse) {
        return (long) Math.ceil(serviceUse.getTotal() * multiplier / 100.0);
    }


    public enum HandlingType {
        EARLY {
            @Override
            public boolean canHandleWithinPeriod(LocalDateTime time, SettingsService settingsService) {
                Period period = getPeriod(settingsService, time.toLocalDate());
                return period != null && !time.isBefore(period.start) && time.isBefore(period.end);
            }

            @Override
            public boolean canHandleOffPeriod(LocalDateTime time, SettingsService settingsService) {
                Period period = getPeriod(settingsService, time.toLocalDate());
                return period != null && time.isBefore(period.start);
            }

            @Override
            public Period getPeriod(SettingsService settingsService, LocalDate date) {
                HotelInfo hotelInfo = settingsService.getHotelInfo();
                if (hotelInfo == null || hotelInfo.getEarlyCheckInStart() == null || hotelInfo.getEarlyCheckInEnd() == null) {
                    return null;
                }
                LocalDateTime timeAtStartOfDay = date.toDateTimeAtStartOfDay(settingsService.getTimeZone()).toLocalDateTime();
                LocalDateTime earlyCheckInStart = timeAtStartOfDay.plusMinutes(hotelInfo.getEarlyCheckInStart());
                LocalDateTime earlyCheckInEnd = timeAtStartOfDay.plusMinutes(hotelInfo.getEarlyCheckInEnd());
                return new Period(earlyCheckInStart, earlyCheckInEnd);
            }

            @Override
            public String getServiceName() {
                return SystemServiceService.EARLY_CHECK_IN;
            }
        },
        LATE {
            @Override
            public boolean canHandleWithinPeriod(LocalDateTime time, SettingsService settingsService) {
                Period period = getPeriod(settingsService, time.toLocalDate());
                return period != null && !time.isBefore(period.start) && time.isBefore(period.end);
            }

            @Override
            public boolean canHandleOffPeriod(LocalDateTime time, SettingsService settingsService) {
                Period period = getPeriod(settingsService, time.toLocalDate());
                return period != null && time.isAfter(period.end);
            }

            @Override
            public Period getPeriod(SettingsService settingsService, LocalDate date) {
                HotelInfo hotelInfo = settingsService.getHotelInfo();
                if (hotelInfo == null || hotelInfo.getLateCheckOutStart() == null) {
                    return null;
                }
                LocalDateTime timeAtStartOfDay = date.toDateTimeAtStartOfDay(settingsService.getTimeZone()).toLocalDateTime();
                LocalDateTime lateCheckOutStart = timeAtStartOfDay.plusMinutes(hotelInfo.getLateCheckOutStart());
                LocalDateTime lateCheckOutEnd = timeAtStartOfDay.plusMinutes(hotelInfo.getLateCheckOutEnd());
                return new Period(lateCheckOutStart, lateCheckOutEnd);
            }

            @Override
            public String getServiceName() {
                return SystemServiceService.LATE_CHECK_OUT;
            }
        };

        public abstract boolean canHandleWithinPeriod(LocalDateTime time, SettingsService settingsService);

        public abstract boolean canHandleOffPeriod(LocalDateTime time, SettingsService settingsService);

        public abstract Period getPeriod(SettingsService settingsService, LocalDate date);

        public abstract String getServiceName();

        private static class Period {
            private LocalDateTime start;
            private LocalDateTime end;

            public Period(LocalDateTime start, LocalDateTime end) {
                this.start = start;
                this.end = end;
            }
        }

    }
}