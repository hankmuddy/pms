package com.idgindigo.pms.utils.extranet.plan;

import com.idgindigo.pms.domain.extranet.plan.VirtualPlan;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.extranet.plan.VirtualPlanRepository;
import com.idgindigo.pms.utils.EntityProvider;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 5/5/14 2:36 PM
 */
@Component
public class VirtualPlanProvider extends EntityProvider<VirtualPlan> {
    @Inject
    private VirtualPlanRepository repository;
    @Inject
    private PlanProvider planProvider;

    @Override
    public VirtualPlan createAndFill() {
        VirtualPlan plan = new VirtualPlan();
        plan.setPlan(planProvider.getPersistentEntity());
        plan.setName(randomAlphabeticString());
        plan.setValue(randomPositiveLong());
        plan.setVariation(VirtualPlan.Variation.FIXED_DISCOUNT);
        return plan;
    }

    @Override
    public BaseRepository<VirtualPlan> getRepository() {
        return repository;
    }
}
