package com.idgindigo.pms.web.controller.extranet;

import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.domain.extranet.Season;
import com.idgindigo.pms.repository.extranet.SeasonRepository;
import com.idgindigo.pms.restutils.exception.SeasonException;
import com.idgindigo.pms.service.approve.ApproveService;
import com.idgindigo.pms.service.approve.SeasonApproveService;
import com.idgindigo.pms.service.filtering.FilteringService;
import com.idgindigo.pms.service.filtering.SeasonFilteringService;
import com.idgindigo.pms.web.controller.ApprovableController;
import com.idgindigo.pms.web.controller.ResponseEntity;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;
import javax.validation.Valid;

/**
 * @author vomel
 * @since 01.11.13 13:32
 */
@Controller
@RequestMapping(SeasonController.URL)
public class SeasonController extends ApprovableController<Season> {
    public static final String URL = WebConfiguration.REST_URL_PREFIX + Season.SEASON;
    @Inject
    private SeasonRepository repository;
    @Inject
    private SeasonApproveService approveService;
    @Inject
    private SeasonFilteringService filteringService;

    @Override
    @Transactional
    public ResponseEntity<Season> create(@RequestBody @Valid Season entity) {
        validateSeasonDates(entity.getStart(), entity.getUntil());
        return super.create(entity);
    }

    @Override
    @Transactional
    public void approve(@PathVariable("id") Long... ids) {
        for (Long id : ids) {
            if (repository.isApproved(id)) {
                logger.warn("Could not approve already approved season with id: {}", id);
                continue;
            }
            super.approve(id);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Season> update(@RequestBody @Valid Season entity, @PathVariable("id") Long id) {
        validateSeasonDates(entity.getStart(), entity.getUntil());
        return super.update(entity, id);
    }

    private void validateSeasonDates(LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            throw new SeasonException(SeasonException.INVALID_DATES);
        }
    }

    @Override
    protected FilteringService<Season> getFilteringService() {
        return filteringService;
    }

    @Override
    public ApproveService<Season> getApproveService() {
        return approveService;
    }
}