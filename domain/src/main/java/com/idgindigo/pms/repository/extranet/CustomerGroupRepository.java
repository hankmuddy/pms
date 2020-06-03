package com.idgindigo.pms.repository.extranet;

import com.idgindigo.pms.domain.extranet.CustomerGroup;
import com.idgindigo.pms.repository.ApprovableRepository;
import com.idgindigo.pms.repository.DiscountRepository;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author valentyn_vakatsiienko
 * @since 11/19/13 3:07 PM
 */
public interface CustomerGroupRepository extends ApprovableRepository<CustomerGroup>, DiscountRepository, FilteredRepository<CustomerGroup> {
    @Query("update #{#entityName} e set e.closed = true where e.id = ?1")
    @Modifying
    @Transactional
    void close(Long id);

    @Query("select closed from #{#entityName} where id = ?1")
    boolean isClosed(Long id);

    @Query("update #{#entityName} e set e.discount = ?1 where e = ?2")
    @Modifying
    @Transactional
    void setDiscount(Integer discount, CustomerGroup group);

    @Query("select e.total from #{#entityName} e where e = ?1")
    Long getTotal(CustomerGroup group);

    @Query("update #{#entityName} e set e.total = (select case when (sum(b.total) > 0) then sum(b.total) else 0 end from Bill b where b.group = ?1) where e = ?1")
    @Modifying
    @Transactional
    void updateTotal(CustomerGroup group);

    @Query("update #{#entityName} e set e.totalPaid = (select case when (sum(b.totalPaid) > 0) then sum(b.totalPaid) else 0 end from Bill b where b.group = ?1) where e = ?1")
    @Modifying
    @Transactional
    void updateTotalPaid(CustomerGroup group);

    @Query("update #{#entityName} e set e.total = ?1 where e = ?2")
    @Modifying
    @Transactional
    void setTotal(Long total, CustomerGroup group);

    @Query("select case when (count(b) > 0) then false else true end from Bill b join b.group cg where cg = ?1 and b.total > b.totalPaid")
    boolean isFullyPaid(CustomerGroup group);
}