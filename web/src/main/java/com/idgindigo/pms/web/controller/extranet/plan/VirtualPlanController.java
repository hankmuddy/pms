package com.idgindigo.pms.web.controller.extranet.plan;

import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.domain.extranet.plan.Plan;
import com.idgindigo.pms.domain.extranet.plan.VirtualPlan;
import com.idgindigo.pms.repository.extranet.plan.PlanRepository;
import com.idgindigo.pms.restutils.exception.RestFriendlyException;
import com.idgindigo.pms.service.approve.ApproveService;
import com.idgindigo.pms.service.approve.VirtualPlanApproveService;
import com.idgindigo.pms.web.controller.ApprovableController;
import com.idgindigo.pms.web.controller.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 5/5/14 2:56 PM
 */
@Controller
@RequestMapping(VirtualPlanController.URL)
public class VirtualPlanController extends ApprovableController<VirtualPlan> {
    public static final String URL = WebConfiguration.REST_URL_PREFIX + VirtualPlan.VIRTUAL_PLAN;

    @Inject
    private VirtualPlanApproveService approveService;
    @Inject
    private PlanRepository planRepository;

    @Override
    public ResponseEntity<VirtualPlan> create(@RequestBody VirtualPlan entity) {
        validate(entity);
        return super.create(entity);
    }

    private void validate(VirtualPlan entity) {
        if (!isApproved(entity.getPlan(), planRepository)) {
            throw new RestFriendlyException(RestFriendlyException.NON_APPROVED_DEPENDENCY, Plan.PLAN);
        }
    }

    @Override
    public ApproveService<VirtualPlan> getApproveService() {
        return approveService;
    }
}
