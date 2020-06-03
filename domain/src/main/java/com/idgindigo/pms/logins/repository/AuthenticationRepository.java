package com.idgindigo.pms.logins.repository;

import com.idgindigo.pms.logins.domain.Authentication;
import com.idgindigo.pms.logins.domain.Hotel;
import com.idgindigo.pms.logins.domain.HotelUser;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 11/6/13 5:28 PM
 */
public interface AuthenticationRepository extends BaseRepository<Authentication>, FilteredRepository<Authentication> {
    List<Authentication> findByUsername(String username);

    @Query("from #{#entityName} e where e.username = ?1 and e.userType != '" + HotelUser.USER + "'")
    Authentication findAdministrativeByUsername(String username);

    Authentication findByUsernameAndHotelTenantId(String username, String tenantId);

    List<Authentication> findByHotelId(Long hotelId);

    @Query("DELETE #{#entityName} e WHERE e.hotel.id = ?1 AND e.userType = '" + HotelUser.USER + "'")
    @Modifying
    @Transactional
    void deleteUsersByHotelId(Long hotelId);

    @Query("update #{#entityName} set password = ?1 where id = ?2")
    @Modifying
    @Transactional
    void setPassword(String pass, Long id);

    @Query("update #{#entityName} set language = ?1 where id = ?2")
    @Modifying
    @Transactional
    void setLang(String lang, Long id);

    @Query("select e.language from #{#entityName} e where e.username = ?1 and e.hotel = ?2")
    String getLang(String username, Hotel hotel);
}
