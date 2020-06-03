package com.idgindigo.pms.service.filtering;

import com.idgindigo.pms.domain.extranet.person.Person;
import com.idgindigo.pms.repository.extranet.person.PersonRepository;
import com.idgindigo.pms.repository.filtering.FilteredRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 1/9/14 2:05 PM
 */
@Service
public class PersonFilteringService extends GenericFilteringService<Person> {
    @Inject
    private PersonRepository repository;

    @Override
    public FilteredRepository<Person> getRepository() {
        return repository;
    }
}
