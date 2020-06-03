package com.idgindigo.pms.service.approve;

import com.idgindigo.pms.channel.wubook.WubookImpl;
import com.idgindigo.pms.domain.extranet.plan.Plan;
import com.idgindigo.pms.repository.ApprovableRepository;
import com.idgindigo.pms.repository.extranet.plan.PlanRepository;
import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.service.channels.ChannelService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import static com.idgindigo.pms.logins.domain.Hotel.WubookImportStatus.RESERVATIONS_IMPORTED;

/**
 * @author valentyn_vakatsiienko
 * @since 1/21/14 2:46 PM
 */
@Service
public class PlanApproveService extends GenericApproveService<Plan> {
    @Inject
    private PlanRepository repository;
    @Inject
    private ChannelService channelService;

    @Override
    public Plan approve(Long id) {
        Plan plan = super.approve(id);
        if (SecurityUtils.isWubookConfigured() && SecurityUtils.getHotel().getImportStatus() == RESERVATIONS_IMPORTED && WubookImpl.ENABLED) {
            Long pid = channelService.addPlan(plan.getName(), SecurityUtils.getWubookAccount());
            plan.setPid(pid);
            plan = repository.save(plan);
        }
        return plan;
    }

    @Override
    public ApprovableRepository<Plan> getApproveRepository() {
        return repository;
    }
}
