package com.idgindigo.pms.service.admin;

import com.idgindigo.pms.logins.domain.Hotel;
import com.idgindigo.pms.logins.domain.HotelInfo;
import com.idgindigo.pms.logins.repository.HotelRepository;
import com.idgindigo.pms.security.SecurityUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 1/31/14 11:57 AM
 */
@Service
public class SettingsService {
    @Inject
    private HotelRepository hotelRepository;

    public HotelInfo getHotelInfo() {
        return getHotel() != null ? getHotel().getInfo() : null;
    }

    public boolean isTourismTaxFromFullPrice() {
        return getHotelInfo() != null && getHotelInfo().isTourismTaxFromFullPrice();
    }

    public DateTimeZone getTimeZone() {
        return getHotel() != null ? DateTimeZone.forID(getHotel().getInfo().getTimeZone()) : DateTimeZone.forID("UTC");
    }

    public LocalDate getHotelDate() {
        return LocalDate.now(getTimeZone());
    }

    public LocalDateTime getHotelTime() {
        return LocalDateTime.now(getTimeZone());
    }

    public float getTourismTax() {
        return getHotel() != null ? getHotel().getInfo().getTourismTax() : 0.0F;
    }

    public Hotel getHotel() {
        String tenantId = SecurityUtils.getCurrentTenantId();
        return tenantId != null ? hotelRepository.findByTenantId(tenantId) : null;
    }

    public boolean isMaxRoomsLimited() {
        Hotel hotel = getHotel();
        return hotel != null && hotel.getMaxRooms() != null;
    }

    public int getMaxRooms() {
        return isMaxRoomsLimited() ? getHotel().getMaxRooms() : Integer.MAX_VALUE;
    }


}
