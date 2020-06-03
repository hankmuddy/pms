package com.idgindigo.pms.repository;

import com.idgindigo.pms.domain.User;

/**
 * @author vomel
 * @since 29.10.13 14:53
 */
public interface UserRepository extends BaseRepository<User> {

    User findByUsername(String username);

}
