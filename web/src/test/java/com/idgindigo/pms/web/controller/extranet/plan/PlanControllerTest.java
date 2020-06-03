package com.idgindigo.pms.web.controller.extranet.plan;

import com.idgindigo.pms.domain.extranet.plan.Plan;
import com.idgindigo.pms.repository.extranet.plan.PlanRepository;
import com.idgindigo.pms.service.extranet.LivingService;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.extranet.plan.PlanProvider;
import com.idgindigo.pms.web.controller.ApprovableControllerTest;
import mockit.Deencapsulation;
import mockit.Mocked;
import mockit.Verifications;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author valentyn_vakatsiienko
 * @since 1/15/14 5:25 PM
 */
public class PlanControllerTest extends ApprovableControllerTest<Plan> {
    @Inject
    private PlanProvider provider;
    @Inject
    private PlanController planController;
    @Mocked
    private LivingService livingService;
    @Inject
    private PlanRepository repository;

    @Test
    public void testApproveHandling() throws Exception {
        LivingService original = Deencapsulation.getField(planController, "livingService");
        Deencapsulation.setField(planController, "livingService", livingService);

        final Plan plan = provider.getPersistentEntity(new Visitor<Plan>() {
            @Override
            public void visit(Plan entity) {
                entity.setApproved(false);
            }
        });

        mvc.perform(preparePut(plan.getId().toString() + "/approved")).andExpect(status().isOk());

        new Verifications() {{
            livingService.handleCreate(plan);
        }};

        Deencapsulation.setField(planController, "livingService", original);
    }

    @Test
    public void testUniqueDefaultPlan() throws Exception {
        Plan one = provider.getPersistentEntity();
        Plan two = provider.getPersistentEntity();
        Assert.assertNotEquals(repository.getDefault(), one);
        Assert.assertNotEquals(repository.getDefault(), two);

        planController.setDefault(one.getId());
        Assert.assertEquals(repository.getDefault(), one);
        Assert.assertNotEquals(repository.getDefault(), two);

        planController.setDefault(two.getId());
        Assert.assertNotEquals(repository.getDefault(), one);
        Assert.assertEquals(repository.getDefault(), two);
    }

    @Override
    protected EntityProvider<Plan> getProvider() {
        return provider;
    }

    @Override
    protected String getUrl() {
        return PlanController.URL + "/";
    }

    @Override
    protected boolean isModificationAllowed() {
        return true;
    }
}
