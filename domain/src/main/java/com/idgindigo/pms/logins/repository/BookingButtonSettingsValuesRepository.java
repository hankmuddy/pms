package com.idgindigo.pms.logins.repository;

import com.idgindigo.pms.logins.domain.BookingButtonSettingsValues;
import com.idgindigo.pms.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author vomel
 * @since 05.06.14 13:14
 */
public interface BookingButtonSettingsValuesRepository extends BaseRepository<BookingButtonSettingsValues> {

    @Query("DELETE BookingButtonSettingsValues bv WHERE bv.bbs.id IN (SELECT bbs.id FROM BookingButtonSettings bbs WHERE bbs.hotel.id = ?1)")
    @Modifying
    @Transactional("loginsTransactionManager")
    void deleteByHotelId(Long hotelId);

    @Query("DELETE BookingButtonSettingsValues bv WHERE bv.bbs.id = ?1")
    @Modifying
    @Transactional("loginsTransactionManager")
    void deleteByBbsId(Long bbsId);

}
