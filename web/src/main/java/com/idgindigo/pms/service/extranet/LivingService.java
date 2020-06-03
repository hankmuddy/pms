package com.idgindigo.pms.service.extranet;

import com.idgindigo.pms.domain.extranet.Season;
import com.idgindigo.pms.domain.extranet.plan.Plan;
import com.idgindigo.pms.domain.extranet.roomtype.BaseRoom;
import com.idgindigo.pms.domain.extranet.service.Living;
import com.idgindigo.pms.repository.extranet.BaseRoomRepository;
import com.idgindigo.pms.repository.extranet.SeasonRepository;
import com.idgindigo.pms.repository.extranet.plan.PlanRepository;
import com.idgindigo.pms.repository.extranet.service.LivingRepository;
import com.idgindigo.pms.service.admin.SettingsService;
import org.joda.time.LocalDate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 1/16/14 4:07 PM
 */
@Service
public class LivingService {
    public static final String LIVING_NOT_FOUND = "living not found for vr=%s, plan=%s, date=%s ";

    @Inject
    private LivingRepository repository;
    @Inject
    private BaseRoomRepository baseRoomRepository;
    @Inject
    private SeasonRepository seasonRepository;
    @Inject
    private PlanRepository planRepository;
    @Inject
    private SettingsService settingsService;

    public void handleCreate(Season season) {
        List<Living> byPlan = repository.findByPlanId(season.getPlan().getId());
        List<Living> toSave = new ArrayList<>(byPlan.size());
        for (Living source : byPlan) {
            Living target = new Living();
            BeanUtils.copyProperties(source, target, "id", "plan");
            target.setSeason(season);

            toSave.add(target);
        }
        repository.save(toSave);
    }

    public void handleCreate(Plan plan) {
        List<BaseRoom> rooms = baseRoomRepository.findByApprovedTrue();
        Collection<Living> livings = new ArrayList<>(rooms.size());
        for (BaseRoom room : rooms) {
            livings.add(getLiving(plan, null, room));
        }
        repository.save(livings);
    }

    public void handleCreate(BaseRoom room) {
        List<Plan> plans = planRepository.findAll();
        Collection<Living> livings = new ArrayList<>(plans.size());
        for (Plan plan : plans) {
            livings.add(getLiving(plan, null, room));
        }
        List<Season> seasons = seasonRepository.findByUntilNotBefore(settingsService.getHotelDate(), true);
        for (Season season : seasons) {
            livings.add(getLiving(null, season, room));
        }
        repository.save(livings);
    }

    private static Living getLiving(Plan plan, Season season, BaseRoom room) {
        Living living = new Living();
        living.setPlan(plan);
        living.setRoom(room);
        living.setSeason(season);
        living.setSystem(true);
        living.setPrices(room.getDefaultPrice());
        return living;
    }

    public Living get(BaseRoom baseRoom, Plan plan, LocalDate date) {
        Season season = seasonRepository.getForDateAndPlan(date, plan);
        Living result;
        if (season != null) {
            result = repository.findByRoomAndSeason(baseRoom, season);
        } else {
            result = repository.findByRoomAndPlan(baseRoom, plan);
        }
        Assert.notNull(result, String.format(LIVING_NOT_FOUND, baseRoom, plan, date));
        return result;
    }
}
