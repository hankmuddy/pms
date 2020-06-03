package com.idgindigo.pms.service.approve;

import com.idgindigo.pms.channel.wubook.WubookImpl;
import com.idgindigo.pms.domain.extranet.Season;
import com.idgindigo.pms.domain.extranet.plan.Plan;
import com.idgindigo.pms.domain.extranet.service.Living;
import com.idgindigo.pms.repository.ApprovableRepository;
import com.idgindigo.pms.repository.extranet.SeasonRepository;
import com.idgindigo.pms.repository.extranet.plan.PlanRepository;
import com.idgindigo.pms.repository.extranet.service.LivingRepository;
import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.service.channels.ChannelService;
import com.idgindigo.pms.service.extranet.LivingService;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static com.idgindigo.pms.logins.domain.Hotel.WubookImportStatus.RESERVATIONS_IMPORTED;

/**
 * @author valentyn_vakatsiienko
 * @since 12/26/13 6:14 PM
 */
@Service
public class SeasonApproveService extends GenericApproveService<Season> {
    private static final Long BY_PASS_ID = 0L;
    @Inject
    private SeasonRepository repository;
    @Inject
    private PlanRepository planRepository;
    @Inject
    private LivingRepository livingRepository;
    @Inject
    private LivingService livingService;
    @Inject
    private ChannelService channelService;

    @Override
    public Season approve(Long id) {
        List<Season> modified = new ArrayList<>();
        Season season = super.approve(id);
        modified.add(season);

        LocalDate start = season.getStart();
        LocalDate until = season.getUntil();
        Plan plan = season.getPlan();
        Season fullOverlap = repository.getFullOverlap(start, until, plan);
        if (fullOverlap != null) {
            Season duplicate = createDuplicate(until.plusDays(1), fullOverlap.getUntil(), fullOverlap);
            modified.add(duplicate);
            fullOverlap.setUntil(start.minusDays(1));
            fullOverlap = repository.save(fullOverlap);
            modified.add(fullOverlap);
        } else {
            //Delete all seasons that are fully covered be the new one
            List<Season> toDelete = repository.getByDatesBetweenExceptId(start, until, plan, id);
            if (!toDelete.isEmpty()) {
                livingRepository.deprecateBySeasonIn(toDelete);
                repository.deactivate(toDelete);
            }
            //Shrink others
            Season overlappingFromLeft = repository.findOverlappingFromLeft(start, until, plan);
            if (overlappingFromLeft != null) {
                overlappingFromLeft.setUntil(start.minusDays(1));
                overlappingFromLeft = repository.save(overlappingFromLeft);
                modified.add(overlappingFromLeft);
            }
            Season overlappingFromRight = repository.findOverlappingFromRight(start, until, plan);
            if (overlappingFromRight != null) {
                overlappingFromRight.setStart(until.plusDays(1));
                overlappingFromRight = repository.save(overlappingFromRight);
                modified.add(overlappingFromRight);
            }
        }

        //Manage channel sync
        LocalDate updateStart = start;
        LocalDate updateEnd = until;
        for (Season mod : modified) {
            if (mod.getStart().isBefore(updateStart)) {
                updateStart = mod.getStart();
            }
            if (mod.getUntil().isAfter(updateEnd)) {
                updateEnd = mod.getUntil();
            }
        }
        livingService.handleCreate(repository.findOne(id));
        repository.flush();
        if (SecurityUtils.isWubookConfigured() && SecurityUtils.getHotel().getImportStatus() == RESERVATIONS_IMPORTED && WubookImpl.ENABLED) {
            channelService.updatePlanPeriods(plan, repository.getByDatesBetweenExceptId(updateStart, updateEnd, plan, BY_PASS_ID), SecurityUtils.getWubookAccount());
        }
        return season;
    }

    private Season createDuplicate(LocalDate start, LocalDate until, Season fullOverlap) {
        Season season = new Season();
        season.setApproved(true);
        season.setStart(start);
        season.setUntil(until);
        season.setPlan(fullOverlap.getPlan());
        season = repository.save(season);

        List<Living> livings = new ArrayList<>();
        for (Living living : fullOverlap.getLivings()) {
            Living copy = new Living();
            copy.setSeason(season);
            copy.setRoom(living.getRoom());
            copy.setMon(living.getMon());
            copy.setTue(living.getTue());
            copy.setWed(living.getWed());
            copy.setThu(living.getThu());
            copy.setFri(living.getFri());
            copy.setSat(living.getSat());
            copy.setSun(living.getSun());
            livings.add(copy);
        }
        livingRepository.save(livings);
        season.setLivings(livings);
        return season;
    }

    @Override
    protected List<String> checkDependenciesApproved(Season entity) {
        List<String> sources = super.checkDependenciesApproved(entity);
        if (!planRepository.isApproved(entity.getPlan().getId())) {
            sources.add(Plan.PLAN);
        }
        return sources;
    }

    @Override
    public ApprovableRepository<Season> getApproveRepository() {
        return repository;
    }
}
