package com.idgindigo.pms.utils;

import com.idgindigo.pms.logins.domain.Hotel;
import com.idgindigo.pms.logins.repository.HotelRepository;
import com.idgindigo.pms.repository.BaseRepository;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 11/18/13 12:12 PM
 */
@Component
public class HotelProvider extends EntityProvider<Hotel> {
    @Inject
    private HotelRepository repository;
    @Inject
    private HotelInfoProvider hotelInfoProvider;

    @Override
    public Hotel createAndFill() {
        Hotel hotel = new Hotel();
        hotel.setTenantId(randomAlphabeticString(10));
        hotel.setInfo(hotelInfoProvider.getTransientEntity());
        return hotel;
    }

    @Override
    public BaseRepository<Hotel> getRepository() {
        return repository;
    }
}