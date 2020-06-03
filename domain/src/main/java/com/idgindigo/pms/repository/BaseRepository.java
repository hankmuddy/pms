package com.idgindigo.pms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author vomel
 * @since 29.10.13 14:52
 */
@NoRepositoryBean
public interface BaseRepository<T> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {
    @Query("update #{#entityName} e set active = false where e = ?1")
    @Modifying
    @Transactional
    public void deactivate(T entity);

    @Query("update #{#entityName} e set active = false where e in ?1")
    @Modifying
    @Transactional
    public void deactivate(Iterable<T> entity);
}