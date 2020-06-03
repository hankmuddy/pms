package com.idgindigo.pms.repository.extranet.service;

import com.idgindigo.pms.domain.pms.SimpleService;
import com.idgindigo.pms.repository.BaseRepository;

/**
 * @author valentyn_vakatsiienko
 * @since 1/9/14 12:13 PM
 */
public interface SimpleServiceRepository extends BaseRepository<SimpleService> {
    SimpleService findByTitle(String title);
}
