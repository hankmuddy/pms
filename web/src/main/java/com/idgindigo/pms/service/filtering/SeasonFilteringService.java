package com.idgindigo.pms.service.filtering;

import com.idgindigo.pms.domain.extranet.Season;
import com.idgindigo.pms.repository.extranet.SeasonRepository;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 4/10/14 1:00 PM
 */
@Service
public class SeasonFilteringService extends GenericFilteringService<Season> {
    @Inject
    private SeasonRepository repository;

    @Override
    public FilteredRepository<Season> getRepository() {
        return repository;
    }
}
