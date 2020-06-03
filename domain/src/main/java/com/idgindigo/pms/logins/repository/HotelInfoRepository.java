package com.idgindigo.pms.logins.repository;

import com.idgindigo.pms.logins.domain.HotelInfo;
import com.idgindigo.pms.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author valentyn_vakatsiienko
 * @since 1/23/14 6:16 PM
 */
public interface HotelInfoRepository extends BaseRepository<HotelInfo> {

    @Query("UPDATE #{#entityName} e SET e.paymentInfo = ?1 WHERE e.id = ?2")
    @Modifying
    @Transactional
    void setPaymentInfo(String value, Long id);

    @Query("UPDATE #{#entityName} e SET e.importantInfo = ?1 WHERE e.id = ?2")
    @Modifying
    @Transactional
    void setImportantInfo(String value, Long id);

    @Query("UPDATE #{#entityName} e SET e.mainPhoto = ?1 WHERE e.id = ?2")
    @Modifying
    @Transactional
    void setMainPhoto(String value, Long id);

    @Query("DELETE #{#entityName} e  WHERE e.id = (SELECT h.info.id FROM Hotel h WHERE h.id=?1)")
    @Modifying
    @Transactional
    void deleteForHotelId(Long hotelId);
}
