package com.idgindigo.pms.service.filtering;

import com.idgindigo.pms.domain.extranet.plan.Plan;
import com.idgindigo.pms.repository.extranet.plan.PlanRepository;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 1/15/14 4:48 PM
 */
@Service
public class PlanFilteringService extends GenericFilteringService<Plan> {
    @Inject
    private PlanRepository repository;

    @Override
    public FilteredRepository<Plan> getRepository() {
        return repository;
    }
}
