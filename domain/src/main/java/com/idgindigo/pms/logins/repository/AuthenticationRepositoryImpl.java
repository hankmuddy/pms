package com.idgindigo.pms.logins.repository;

import com.idgindigo.pms.logins.domain.Authentication;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.filtering.AbstractFilteredRepository;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 7/11/14 2:25 PM
 */
public class AuthenticationRepositoryImpl extends AbstractFilteredRepository<Authentication> {
    @Inject
    private AuthenticationRepository repository;

    @Override
    public BaseRepository<Authentication> getRepository() {
        return repository;
    }
}
