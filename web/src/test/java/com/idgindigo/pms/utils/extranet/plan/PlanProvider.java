package com.idgindigo.pms.utils.extranet.plan;

import com.idgindigo.pms.domain.extranet.plan.Plan;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.extranet.plan.PlanRepository;
import com.idgindigo.pms.utils.ApprovableEntityProvider;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 1/15/14 4:51 PM
 */
@Component
public class PlanProvider extends ApprovableEntityProvider<Plan> {
    @Inject
    private PlanRepository repository;

    @Override
    public Plan createAndFill() {
        Plan plan = new Plan();
        do {
            plan.setName(randomAlphabeticString());
        } while (repository.existsByName(plan.getName()));
        plan.setBoard(Plan.Board.BB);
        return plan;
    }

    @Override
    public BaseRepository<Plan> getRepository() {
        return repository;
    }
}
