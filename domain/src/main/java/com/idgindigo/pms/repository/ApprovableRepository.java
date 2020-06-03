package com.idgindigo.pms.repository;

import com.idgindigo.pms.domain.Approvable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 11/26/13 6:30 PM
 */
@NoRepositoryBean
public interface ApprovableRepository<T extends Approvable> extends BaseRepository<T> {

    @Query("UPDATE #{#entityName} e SET e.approved = TRUE WHERE e.id = ?1")
    @Modifying
    @Transactional
    void approve(Long id);

    @Query("SELECT CASE WHEN (COUNT(e) > 0) THEN TRUE ELSE FALSE END " +
            " FROM #{#entityName} e WHERE e.id = ?1 AND e.approved = TRUE")
    boolean isApproved(Long id);

    List<T> findByApprovedTrue();

    @Query("SELECT count(e) FROM #{#entityName} e WHERE e.approved = true")
    Long countApproved();
}
