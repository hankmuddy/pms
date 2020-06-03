package com.idgindigo.pms.utils.extranet;

import com.idgindigo.pms.domain.extranet.Season;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.extranet.SeasonRepository;
import com.idgindigo.pms.utils.ApprovableEntityProvider;
import com.idgindigo.pms.utils.extranet.plan.PlanProvider;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author vomel
 * @since 01.11.13 12:33
 */
@Component
public class SeasonProvider extends ApprovableEntityProvider<Season> {
    @Inject
    private SeasonRepository repository;
    @Inject
    private PlanProvider planProvider;

    @Override
    public Season createAndFill() {
        Season season = new Season();
        season.setStart(LocalDate.now(DateTimeZone.forID("UTC")));
        season.setUntil(season.getStart().plusDays(5));
        season.setPlan(planProvider.getPersistentEntity());
        return season;
    }

    @Override
    public BaseRepository<Season> getRepository() {
        return repository;
    }
}
