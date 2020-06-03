package com.idgindigo.pms.repository.extranet.service;

import com.idgindigo.pms.domain.extranet.service.FixedService;
import com.idgindigo.pms.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author valentyn_vakatsiienko
 * @since 1/16/14 7:09 PM
 */
public interface FixedServiceRepository extends BaseRepository<FixedService> {

    @Query("select case when (count (e) > 0) then true else false end from #{#entityName} e where e.title = ?1")
    boolean existsByTitle(String title);

}
