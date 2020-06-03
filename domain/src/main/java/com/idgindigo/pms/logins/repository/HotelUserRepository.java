package com.idgindigo.pms.logins.repository;

import com.idgindigo.pms.logins.domain.HotelUser;
import com.idgindigo.pms.repository.BaseRepository;

/**
 * @author valentyn_vakatsiienko
 * @since 11/19/13 12:01 PM
 */
public interface HotelUserRepository extends BaseRepository<HotelUser> {
    HotelUser findByUsername(String username);
}
