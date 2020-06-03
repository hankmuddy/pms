package com.idgindigo.pms.repository.extranet.plan;

import com.idgindigo.pms.domain.extranet.plan.Plan;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.filtering.AbstractFilteredRepository;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 1/15/14 4:46 PM
 */
public class PlanRepositoryImpl extends AbstractFilteredRepository<Plan> {
    @Inject
    private PlanRepository repository;

    @Override
    public BaseRepository<Plan> getRepository() {
        return repository;
    }
}
