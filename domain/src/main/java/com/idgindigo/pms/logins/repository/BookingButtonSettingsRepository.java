package com.idgindigo.pms.logins.repository;

import com.idgindigo.pms.logins.domain.BookingButtonSettings;
import com.idgindigo.pms.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author vomel
 * @since 27.05.14 14:50
 */
public interface BookingButtonSettingsRepository extends BaseRepository<BookingButtonSettings> {

    @Query("DELETE BookingButtonSettings WHERE  hotel.id = ?1")
    @Modifying
    @Transactional
    void deleteByHotelId(Long hotelId);

}
