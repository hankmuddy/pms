package com.idgindigo.pms.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author valentyn_vakatsiienko
 * @since 1/11/14 12:42 PM
 */
public interface DiscountRepository {

    @Query("update #{#entityName} set discount = ?2 where id = ?1")
    @Modifying
    @Transactional
    void setDiscount(Long id, Integer discount);
}
