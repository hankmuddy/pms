package com.idgindigo.pms.repository.extranet.service;

import com.idgindigo.pms.domain.extranet.service.Service;
import com.idgindigo.pms.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author valentyn_vakatsiienko
 * @since 12/4/13 5:15 PM
 */
public interface ServiceRepository extends BaseRepository<Service> {

    @Query("update #{#entityName} set deprecated = true where id = ?1")
    @Modifying
    @Transactional
    public void deprecate(Long id);
}
