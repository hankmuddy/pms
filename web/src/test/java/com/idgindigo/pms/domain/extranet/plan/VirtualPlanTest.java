package com.idgindigo.pms.domain.extranet.plan;

import com.idgindigo.pms.domain.ApprovableEntityTest;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.extranet.plan.VirtualPlanProvider;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 5/5/14 2:35 PM
 */
public class VirtualPlanTest extends ApprovableEntityTest<VirtualPlan> {
    @Inject
    private VirtualPlanProvider provider;

    @Override
    protected EntityProvider<VirtualPlan> getProvider() {
        return provider;
    }
}
