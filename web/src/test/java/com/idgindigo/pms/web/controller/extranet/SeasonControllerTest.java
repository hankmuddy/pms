package com.idgindigo.pms.web.controller.extranet;

import com.idgindigo.pms.domain.extranet.Season;
import com.idgindigo.pms.domain.extranet.plan.Plan;
import com.idgindigo.pms.repository.extranet.SeasonRepository;
import com.idgindigo.pms.repository.extranet.service.LivingRepository;
import com.idgindigo.pms.restutils.exception.SeasonException;
import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.extranet.SeasonProvider;
import com.idgindigo.pms.web.controller.ApprovableControllerTest;
import org.joda.time.LocalDate;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

/**
 * @author valentyn_vakatsiienko
 * @since 12/3/13 11:21 AM
 */
public class SeasonControllerTest extends ApprovableControllerTest<Season> {
    @Inject
    private SeasonProvider provider;
    @Inject
    private SeasonRepository repository;
    @Inject
    private LivingRepository livingRepository;

    @Test
    public void testDisabledSeasonOverlapping() throws Exception {
        Season season = provider.getPersistentEntity(new Visitor<Season>() {
            @Override
            public void visit(Season entity) {
                entity.setActive(false);
            }
        });
        Season overlap = createSeason(season.getStart(), season.getUntil(), season.getPlan());
        mvc.perform(preparePost(overlap)).andExpect(status().isCreated());
    }

    @Test
    public void testStartAfterUntil() throws Exception {
        final LocalDate start = LocalDate.now(SecurityUtils.getTimeZone());
        final LocalDate end = start.minusDays(1);
        Season season = provider.getTransientEntity(new Visitor<Season>() {
            @Override
            public void visit(Season entity) {
                entity.setStart(start);
                entity.setUntil(end);
            }
        });

        testBadRequest(preparePost(season), SeasonException.INVALID_DATES);

        season = provider.getPersistentEntity(new Visitor<Season>() {
            @Override
            public void visit(Season entity) {
                entity.setApproved(false);
                entity.setStart(start);
                entity.setUntil(start);
            }
        });
        season.setUntil(end);

        testBadRequest(preparePut(season), SeasonException.INVALID_DATES);
    }

    private Season createSeason(final LocalDate start, final LocalDate end, final Plan plan) {
        return provider.getTransientEntity(new Visitor<Season>() {
            @Override
            public void visit(Season entity) {
                entity.setStart(start);
                entity.setUntil(end);
                entity.setPlan(plan);
            }
        });
    }

    @Test
    public void testFullOverlapManagement() throws Exception {
        final LocalDate overlappedStart = LocalDate.now(SecurityUtils.getTimeZone());
        final LocalDate overlappedUntil = overlappedStart.plusDays(10);
        Season overlapped = provider.getPersistentEntity(new Visitor<Season>() {
            @Override
            public void visit(Season entity) {
                entity.setStart(overlappedStart);
                entity.setUntil(overlappedUntil);
            }
        });
        final Plan plan = overlapped.getPlan();

        Season overlapping = provider.getPersistentEntity(new Visitor<Season>() {
            @Override
            public void visit(Season entity) {
                entity.setStart(overlappedStart.plusDays(2));
                entity.setUntil(overlappedUntil.minusDays(2));
                entity.setApproved(false);
                entity.setPlan(plan);
            }
        });

        approve(overlapping);

        List<Season> seasons = repository.findByPlan(plan);
        assertEquals(seasons.size(), 3, "3 seasons should exist now: two previously created and one new - the overlapped duplicate, placed after overlapping");

        overlapped = repository.findOne(overlapped.getId());
        assertEquals(overlapped.getUntil(), overlapping.getStart().minusDays(1), "overlapped should`ve been shrunk to where overlapping starts");

        //Find created duplicate
        Season duplicate = null;
        for (Season season : seasons) {
            if (!season.equals(overlapped) && !season.equals(overlapping)) {
                duplicate = season;
            }
        }
        assertNotNull(duplicate);
        assertEquals(livingRepository.findBySeasonId(overlapped.getId()).size(), livingRepository.findBySeasonId(duplicate.getId()).size());
    }

    @Test
    public void testPartialOverlap() throws Exception {
        final LocalDate overlappingStart = LocalDate.now(SecurityUtils.getTimeZone());
        final LocalDate overlappingUntil = overlappingStart.plusDays(10);

        Season overlapping = provider.getPersistentEntity(new Visitor<Season>() {
            @Override
            public void visit(Season entity) {
                entity.setApproved(false);
                entity.setStart(overlappingStart);
                entity.setUntil(overlappingUntil);
            }
        });
        final Plan plan = overlapping.getPlan();
        Season overlappedFromLeft = createSeason(plan, overlappingStart.minusDays(5), overlappingStart.plusDays(1));
        Season overlappedFromRight = createSeason(plan, overlappingUntil.minusDays(1), overlappingUntil.plusDays(5));
        Season overlappedInCenter = createSeason(plan, overlappingStart.plusDays(3), overlappingUntil.minusDays(3));

        approve(overlapping);

        overlappedFromLeft = repository.findOne(overlappedFromLeft.getId());
        overlappedFromRight = repository.findOne(overlappedFromRight.getId());
        overlappedInCenter = repository.findOne(overlappedInCenter.getId());

        assertEquals(overlappedFromLeft.getUntil(), overlappingStart.minusDays(1));
        assertEquals(overlappedFromRight.getStart(), overlappingUntil.plusDays(1));
        assertFalse(overlappedInCenter.getActive());
    }

    private void approve(Season overlapping) throws Exception {
        mvc.perform(preparePut(overlapping.getId() + "/approved")).andExpect(status().isOk());
    }

    private Season createSeason(final Plan plan, final LocalDate start, final LocalDate until) {
        return provider.getPersistentEntity(new Visitor<Season>() {
            @Override
            public void visit(Season entity) {
                entity.setStart(start);
                entity.setUntil(until);
                entity.setPlan(plan);
            }
        });
    }

    @Override
    protected EntityProvider<Season> getProvider() {
        return provider;
    }

    @Override
    protected String getUrl() {
        return SeasonController.URL + "/";
    }
}
