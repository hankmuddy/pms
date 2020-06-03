package com.idgindigo.pms.repository.pms;

import com.idgindigo.pms.domain.pms.BankDetails;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author valentyn_vakatsiienko
 * @since 12/30/13 12:53 PM
 */
public interface BankDetailsRepository extends BaseRepository<BankDetails>, FilteredRepository<BankDetails> {

    @Query("update #{#entityName} e set e.defaultDetails = true where e.id = ?1")
    @Modifying
    @Transactional
    void setDefault(Long detailsId);

    @Query("update #{#entityName} e set e.defaultDetails = false where e.defaultDetails = true")
    @Modifying
    @Transactional
    void eraseDefaults();

    @Query("from #{#entityName} e where e.defaultDetails = true")
    BankDetails getDefault();

    @Query("select case when e.defaultDetails = true then true else false end from #{#entityName} e where e.id = ?1")
    boolean isDefault(Long id);

    @Query("update #{#entityName} e set e.blocked = ?2 where e.id = ?1")
    @Modifying
    @Transactional
    void setBlocked(Long id, Boolean blocked);

    @Query("select case when e.blocked = true then true else false end from #{#entityName} e where e.id = ?1")
    boolean isBlocked(Long id);

}
