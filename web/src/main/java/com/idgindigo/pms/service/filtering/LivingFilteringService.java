package com.idgindigo.pms.service.filtering;

import com.idgindigo.pms.domain.extranet.service.Living;
import com.idgindigo.pms.repository.extranet.service.LivingRepository;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 1/27/14 12:37 PM
 */
@Service
public class LivingFilteringService extends GenericFilteringService<Living> {
    @Inject
    private LivingRepository repository;

    @Override
    public FilteredRepository<Living> getRepository() {
        return repository;
    }
}
