package com.idgindigo.pms.repository.extranet.plan;

import com.idgindigo.pms.domain.extranet.plan.VirtualPlan;
import com.idgindigo.pms.repository.ApprovableRepository;

/**
 * @author valentyn_vakatsiienko
 * @since 5/5/14 2:31 PM
 */
public interface VirtualPlanRepository extends WubookPlanRepository<VirtualPlan>, ApprovableRepository<VirtualPlan> {

}