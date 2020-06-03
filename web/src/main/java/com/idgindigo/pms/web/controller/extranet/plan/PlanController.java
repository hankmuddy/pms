package com.idgindigo.pms.web.controller.extranet.plan;

import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.domain.BaseEntity;
import com.idgindigo.pms.domain.extranet.plan.Plan;
import com.idgindigo.pms.repository.ApprovableRepository;
import com.idgindigo.pms.repository.extranet.plan.PlanRepository;
import com.idgindigo.pms.restutils.exception.PlanException;
import com.idgindigo.pms.restutils.view.ResponseView;
import com.idgindigo.pms.service.approve.ApproveService;
import com.idgindigo.pms.service.approve.PlanApproveService;
import com.idgindigo.pms.service.extranet.LivingService;
import com.idgindigo.pms.service.filtering.FilteringService;
import com.idgindigo.pms.service.filtering.PlanFilteringService;
import com.idgindigo.pms.web.controller.ApprovableController;
import com.idgindigo.pms.web.controller.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;

/**
 * @author valentyn_vakatsiienko
 * @since 1/15/14 4:49 PM
 */
@Controller
@RequestMapping(PlanController.URL)
public class PlanController extends ApprovableController<Plan> {
    public static final String URL = WebConfiguration.REST_URL_PREFIX + Plan.PLAN;
    @Inject
    private PlanFilteringService filteringService;
    @Inject
    private PlanApproveService approveService;
    @Inject
    private LivingService livingService;
    @Inject
    private PlanRepository repository;

    @Override
    @Transactional
    public void approve(@PathVariable("id") Long... ids) {
        ApprovableRepository<Plan> repository = getApproveService().getApproveRepository();
        for (Long id : ids) {
            if (!repository.isApproved(id)) {
                super.approve(id);
                livingService.handleCreate(getRepository().findOne(id));
            }
        }
    }

    @RequestMapping(value = "{id}/default", method = RequestMethod.PUT)
    @ResponseBody
    @Transactional
    public void setDefault(@PathVariable("id") long id) {
        if (!repository.isApproved(id)) {
            throw new PlanException(PlanException.SET_DEFAULT_ONLY_FOR_APPROVED, "approved");
        }
        repository.eraseDefaults();
        repository.setDefault(id);
    }

    @RequestMapping("default")
    @ResponseBody
    @Transactional
    @ResponseView(BaseEntity.SoloView.class)
    public ResponseEntity<Plan> getDefault() {
        Plan plan = repository.getDefault();
        if (plan == null) {
            throw new EntityNotFoundException();
        }
        return new ResponseEntity<>(plan);
    }


    @Override
    protected void applyModifications(Plan toUpdate, Plan updated) {
        toUpdate.setBoard(updated.getBoard());
    }

    @Override
    public ApproveService<Plan> getApproveService() {
        return approveService;
    }

    @Override
    protected FilteringService<Plan> getFilteringService() {
        return filteringService;
    }
}
