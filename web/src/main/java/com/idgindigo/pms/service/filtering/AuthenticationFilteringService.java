package com.idgindigo.pms.service.filtering;

import com.idgindigo.pms.logins.domain.Authentication;
import com.idgindigo.pms.logins.repository.AuthenticationRepository;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 7/11/14 2:24 PM
 */
@Component
public class AuthenticationFilteringService extends GenericFilteringService<Authentication> {
    @Inject
    private AuthenticationRepository repository;

    @Override
    public FilteredRepository<Authentication> getRepository() {
        return repository;
    }
}
