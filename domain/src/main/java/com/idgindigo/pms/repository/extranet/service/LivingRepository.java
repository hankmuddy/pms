package com.idgindigo.pms.repository.extranet.service;

import com.idgindigo.pms.domain.extranet.Season;
import com.idgindigo.pms.domain.extranet.plan.Plan;
import com.idgindigo.pms.domain.extranet.roomtype.BaseRoom;
import com.idgindigo.pms.domain.extranet.service.Living;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 11/11/13 4:41 PM
 */
public interface LivingRepository extends BaseRepository<Living>, FilteredRepository<Living> {
    List<Living> findByPlanId(Long planId);

    List<Living> findBySeasonId(Long seasonId);

    List<Living> findByRoomId(Long roomId);

    Living findByRoomAndPlan(BaseRoom room, Plan plan);

    Living findByRoomAndSeason(BaseRoom room, Season season);

    Living findByRoomAndPlanAndSeason(BaseRoom room, Plan plan, Season season);

    @Query("update #{#entityName} set deprecated = true where season in ?1")
    @Modifying
    @Transactional
    public void deprecateBySeasonIn(Iterable<Season> seasons);

}
