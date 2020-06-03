package com.idgindigo.pms.domain.extranet.plan;

import com.idgindigo.pms.domain.ApprovableEntityTest;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.extranet.plan.PlanProvider;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 1/15/14 5:01 PM
 */
public class PlanTest extends ApprovableEntityTest<Plan> {
    @Inject
    private PlanProvider provider;

    @Override
    protected EntityProvider<Plan> getProvider() {
        return provider;
    }
}
