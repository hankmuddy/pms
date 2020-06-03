package com.idgindigo.pms.service.approve;

import com.idgindigo.pms.channel.wubook.WubookImpl;
import com.idgindigo.pms.domain.extranet.plan.VirtualPlan;
import com.idgindigo.pms.repository.ApprovableRepository;
import com.idgindigo.pms.repository.extranet.plan.VirtualPlanRepository;
import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.service.channels.ChannelService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import static com.idgindigo.pms.logins.domain.Hotel.WubookImportStatus.DATA_IMPORTED;

/**
 * @author valentyn_vakatsiienko
 * @since 5/5/14 3:00 PM
 */
@Service
public class VirtualPlanApproveService extends GenericApproveService<VirtualPlan> {
    @Inject
    private VirtualPlanRepository repository;
    @Inject
    private ChannelService channelService;

    @Override
    public VirtualPlan approve(Long id) {
        VirtualPlan approved = super.approve(id);
        if (SecurityUtils.isWubookConfigured() && SecurityUtils.getHotel().getImportStatus() == DATA_IMPORTED && WubookImpl.ENABLED) {
            approved = channelService.createVirtualPlan(approved, SecurityUtils.getWubookAccount());
        }
        return approved;
    }

    @Override
    public ApprovableRepository<VirtualPlan> getApproveRepository() {
        return repository;
    }
}
