package com.idgindigo.pms.repository.extranet.person;

import com.idgindigo.pms.domain.extranet.person.Person;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.filtering.FilteredRepository;

/**
 * @author valentyn_vakatsiienko
 * @since 12/26/13 3:30 PM
 */
public interface PersonRepository extends BaseRepository<Person>, FilteredRepository<Person> {
}
