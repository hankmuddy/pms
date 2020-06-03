package com.idgindigo.pms.web.controller.extranet.plan;

import com.idgindigo.pms.domain.extranet.plan.Plan;
import com.idgindigo.pms.domain.extranet.plan.VirtualPlan;
import com.idgindigo.pms.restutils.exception.RestFriendlyException;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.extranet.plan.PlanProvider;
import com.idgindigo.pms.utils.extranet.plan.VirtualPlanProvider;
import com.idgindigo.pms.web.controller.ApprovableControllerTest;
import junit.framework.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static org.testng.Assert.assertEquals;

/**
 * @author valentyn_vakatsiienko
 * @since 5/5/14 3:11 PM
 */
public class VirtualPlanControllerTest extends ApprovableControllerTest<VirtualPlan> {
    @Inject
    private VirtualPlanProvider provider;
    @Inject
    private PlanProvider planProvider;
    @Inject
    private VirtualPlanController controller;

    @Test
    public void testNonApprovedPlan() {
        VirtualPlan toSave = provider.getTransientEntity(new Visitor<VirtualPlan>() {
            @Override
            public void visit(VirtualPlan entity) {
                entity.setPlan(planProvider.getPersistentEntity(new Visitor<Plan>() {
                    @Override
                    public void visit(Plan entity) {
                        entity.setApproved(false);
                    }
                }));
            }
        });

        try {
            controller.create(toSave);
            Assert.fail();
        } catch (RestFriendlyException ex) {
            assertEquals(ex.getMessage(), RestFriendlyException.NON_APPROVED_DEPENDENCY);
            assertEquals(ex.getSource(), Plan.PLAN);
        }
    }

    @Override
    protected EntityProvider<VirtualPlan> getProvider() {
        return provider;
    }

    @Override
    protected String getUrl() {
        return VirtualPlanController.URL + "/";
    }
}
