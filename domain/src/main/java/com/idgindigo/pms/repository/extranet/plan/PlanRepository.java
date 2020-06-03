package com.idgindigo.pms.repository.extranet.plan;

import com.idgindigo.pms.domain.extranet.plan.Plan;
import com.idgindigo.pms.repository.ApprovableRepository;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author valentyn_vakatsiienko
 * @since 1/15/14 4:46 PM
 */
public interface PlanRepository extends WubookPlanRepository<Plan>, ApprovableRepository<Plan>, FilteredRepository<Plan> {

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN TRUE ELSE FALSE END FROM #{#entityName} e WHERE e.name = ?1")
    boolean existsByName(String name);

    @Query("update #{#entityName} e set e.defaultPlan = true where e.id = ?1")
    @Modifying
    @Transactional
    void setDefault(Long planId);

    @Query("update #{#entityName} e set e.defaultPlan = false where e.defaultPlan = true")
    @Modifying
    @Transactional
    void eraseDefaults();

    @Query("from #{#entityName} e where e.defaultPlan = true")
    Plan getDefault();

    @Query("select case when e.defaultPlan = true then true else false end from #{#entityName} e where e.id = ?1")
    boolean isDefault(Long id);

}
