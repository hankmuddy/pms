package com.idgindigo.pms.repository.extranet;

import com.idgindigo.pms.domain.extranet.Season;
import com.idgindigo.pms.domain.extranet.plan.Plan;
import com.idgindigo.pms.repository.ApprovableRepository;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import org.joda.time.LocalDate;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author vomel
 * @since 01.11.13 12:32
 */
public interface SeasonRepository extends ApprovableRepository<Season>, FilteredRepository<Season> {
    List<Season> findByPlan(Plan plan);

    @Query("SELECT e FROM #{#entityName} e WHERE e.start = ?1 AND e.until = ?2 AND e.plan.id = ?3 AND e.approved = TRUE")
    Season findByStartAndEndAndPlanId(LocalDate start, LocalDate end, Long planId);

    @Query("SELECT e FROM #{#entityName} e WHERE ?1 >= e.start AND ?1 <= e.until AND e.plan = ?2 AND e.approved = TRUE")
    Season getForDateAndPlan(LocalDate date, Plan plan);

    @Query("SELECT e FROM #{#entityName} e WHERE e.until >= ?1 AND e.approved = ?2")
    List<Season> findByUntilNotBefore(LocalDate date, boolean approved);

    @Query("FROM #{#entityName} WHERE start >= ?1 AND until <= ?2 AND id <> ?4 AND plan = ?3 AND approved = TRUE")
    List<Season> getByDatesBetweenExceptId(LocalDate start, LocalDate end, Plan plan, Long id);

    @Query("FROM #{#entityName} WHERE start < ?1 AND until > ?2 AND plan = ?3 AND approved = TRUE")
    Season getFullOverlap(LocalDate start, LocalDate until, Plan plan);

    @Query("FROM #{#entityName} WHERE start < ?1 AND until >= ?1 AND until <= ?2 AND plan = ?3 AND approved = TRUE")
    Season findOverlappingFromLeft(LocalDate start, LocalDate until, Plan plan);

    @Query("FROM #{#entityName} WHERE start >= ?1 AND start <= ?2 AND until > ?2 AND plan = ?3 AND approved = TRUE")
    Season findOverlappingFromRight(LocalDate start, LocalDate until, Plan plan);
}
