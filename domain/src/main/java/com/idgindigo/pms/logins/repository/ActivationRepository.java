package com.idgindigo.pms.logins.repository;

import com.idgindigo.pms.logins.domain.Activation;
import com.idgindigo.pms.logins.domain.Authentication;
import com.idgindigo.pms.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author valentyn_vakatsiienko
 * @since 11/19/13 10:44 AM
 */
public interface ActivationRepository extends BaseRepository<Activation> {
    Activation findByKey(String key);

    @Modifying
    @Transactional
    @Query("delete Activation a where a.authentication = ?1")
    void deleteKeysForAuthentication(Authentication authentication);

    @Modifying
    @Transactional
    @Query("delete Activation a where a in (select da from Activation da join da.authentication au join au.hotel h where h.tenantId = ?1)")
    void deleteByHotelId(String tenantId);


}
