package com.idgindigo.pms.repository.extranet.plan;

import com.idgindigo.pms.domain.extranet.plan.CompactRestriction;
import com.idgindigo.pms.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 5/5/14 5:45 PM
 */
public interface CompactRestrictionRepository extends BaseRepository<CompactRestriction> {
    @Query("update #{#entityName} e set e.pid = ?1 where e = ?2")
    @Modifying
    @Transactional
    void setPid(long pid, CompactRestriction restriction);

    @Query("select e.pid from #{#entityName} e where e = ?1")
    Long getPid(CompactRestriction restriction);

    List<CompactRestriction> findByPidNull();
}
