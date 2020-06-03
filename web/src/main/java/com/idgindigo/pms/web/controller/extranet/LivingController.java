package com.idgindigo.pms.web.controller.extranet;

import com.idgindigo.pms.channel.wubook.WubookImpl;
import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.domain.extranet.Season;
import com.idgindigo.pms.domain.extranet.service.Living;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.extranet.SeasonRepository;
import com.idgindigo.pms.repository.extranet.plan.PlanRepository;
import com.idgindigo.pms.repository.extranet.service.LivingRepository;
import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.service.channels.ChannelService;
import com.idgindigo.pms.service.filtering.FilteringService;
import com.idgindigo.pms.service.filtering.LivingFilteringService;
import com.idgindigo.pms.web.controller.BaseCrudController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

import static com.idgindigo.pms.logins.domain.Hotel.WubookImportStatus.RESERVATIONS_IMPORTED;

/**
 * @author valentyn_vakatsiienko
 * @since 11/11/13 6:19 PM
 */
@Controller
@RequestMapping(LivingController.URL)
public class LivingController extends BaseCrudController<Living> {
    public static final String URL = WebConfiguration.REST_URL_PREFIX + Living.LIVING;
    @Inject
    private LivingRepository repository;
    @Inject
    private LivingFilteringService filteringService;
    @Inject
    private PlanRepository planRepository;
    @Inject
    private SeasonRepository seasonRepository;
    @Inject
    private ChannelService channelService;

    @Override
    public BaseRepository<Living> getRepository() {
        return repository;
    }

    @RequestMapping(value = "byPlan/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public void updateByPlan(@RequestBody List<Living> livings, @PathVariable("id") long id) {
        livings = repository.save(livings);
        if (WubookImpl.ENABLED && SecurityUtils.isWubookConfigured() && SecurityUtils.getHotel().getImportStatus() == RESERVATIONS_IMPORTED) {
            channelService.updatePlanRack(planRepository.findOne(id).getPid(), livings, SecurityUtils.getWubookAccount());
        }
    }

    @RequestMapping(value = "bySeason/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public void updateBySeason(@RequestBody List<Living> livings, @PathVariable("id") long id) {
        repository.save(livings);//TODO handle multiple seasons update
        repository.flush();
        if (WubookImpl.ENABLED && SecurityUtils.isWubookConfigured() && SecurityUtils.getHotel().getImportStatus() == RESERVATIONS_IMPORTED) {
            Season season = seasonRepository.findOne(id);
            channelService.updatePlanPeriods(season.getPlan(), Collections.singletonList(season), SecurityUtils.getWubookAccount());
        }
    }

    @Override
    protected FilteringService<Living> getFilteringService() {
        return filteringService;
    }
}
