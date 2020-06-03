package com.idgindigo.pms.service.pms;


import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeValue;
import com.idgindigo.pms.domain.extranet.service.Living;
import com.idgindigo.pms.domain.pms.SimpleService;
import com.idgindigo.pms.repository.extranet.rate.RoomTypeValueRepository;
import com.idgindigo.pms.repository.extranet.service.SimpleServiceRepository;
import org.joda.time.LocalDate;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 12/17/13 11:03 AM
 */
@org.springframework.stereotype.Service
public class SystemServiceService {
    public static final String ADULT_BED_SERVICE = "service.adultBed";
    public static final String CHILD_BED_SERVICE = "service.childBed";
    public static final String EARLY_CHECK_IN = "service.earlyCheckIn";
    public static final String LATE_CHECK_OUT = "service.lateCheckOut";

    @Inject
    private SimpleServiceRepository serviceRepository;
    @Inject
    private RoomTypeValueRepository roomTypeValueRepository;

    public SimpleService getAdultBedService() {
        return serviceRepository.findByTitle(ADULT_BED_SERVICE);
    }

    public long getAdultBedPrice(Living living, LocalDate date) {
        RoomTypeValue rate = roomTypeValueRepository.findByRoomTypeAndDate(living.getRoom().roomType(), date);
        Long price = rate != null ? rate.getAdultBedPrice() : null;
        if (price == null) {
            price = living.getRoom().roomType().getAdultBedPrice();
        }
        if (price == null) {
            price = getAdultBedService().getPrice();
        }
        return price != null ? price : 0L;
    }

    public SimpleService getChildBedService() {
        return serviceRepository.findByTitle(CHILD_BED_SERVICE);
    }

    public long getChildBedPrice(Living living, LocalDate date) {
        RoomTypeValue rate = roomTypeValueRepository.findByRoomTypeAndDate(living.getRoom().roomType(), date);
        Long price = rate != null ? rate.getChildBedPrice() : null;
        if (price == null) {
            price = living.getRoom().roomType().getChildBedPrice();
        }
        if (price == null) {
            price = getChildBedService().getPrice();
        }
        return price != null ? price : 0L;
    }
}
