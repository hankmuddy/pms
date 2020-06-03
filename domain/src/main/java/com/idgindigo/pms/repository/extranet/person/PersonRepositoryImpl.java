package com.idgindigo.pms.repository.extranet.person;

import com.idgindigo.pms.domain.extranet.person.Person;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.filtering.AbstractFilteredRepository;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 1/9/14 2:07 PM
 */
public class PersonRepositoryImpl extends AbstractFilteredRepository<Person> {
    @Inject
    private PersonRepository repository;

    @Override
    public BaseRepository<Person> getRepository() {
        return repository;
    }
}
