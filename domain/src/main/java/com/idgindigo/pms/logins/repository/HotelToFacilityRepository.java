package com.idgindigo.pms.logins.repository;

import com.idgindigo.pms.logins.domain.HotelFacility;
import com.idgindigo.pms.logins.domain.HotelFacilityDto;
import com.idgindigo.pms.logins.domain.HotelToFacility;
import com.idgindigo.pms.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author vomel
 * @since 21.02.14 16:58
 */
public interface HotelToFacilityRepository extends BaseRepository<HotelToFacility> {
    @Query("SELECT htf.facility FROM HotelToFacility htf WHERE htf.hotel.id=?1")
    List<HotelFacility> findFacilitiesByHotel(Long hotelId);

    @Query("SELECT new com.idgindigo.pms.logins.domain.HotelFacilityDto( htf.facility,htf.chargeFree) FROM HotelToFacility htf WHERE htf.hotel.id=?1")
    List<HotelFacilityDto> findDtosByHotel(Long hotelId);

    @Query("DELETE HotelToFacility htf WHERE htf.hotel.id =?1 AND htf.facility IN (?2)")
    @Modifying
    @Transactional
    void removeSelected(Long hotelId, List<HotelFacility> facilities);

    @Query("DELETE HotelToFacility htf WHERE htf.hotel.id =?1")
    @Modifying
    @Transactional
    void deleteByHotelId(Long hotelId);
}
