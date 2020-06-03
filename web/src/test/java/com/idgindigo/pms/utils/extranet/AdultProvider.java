package com.idgindigo.pms.utils.extranet;

import com.idgindigo.pms.domain.extranet.person.Adult;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.extranet.person.AdultRepository;
import com.idgindigo.pms.utils.EntityProvider;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 11/19/13 4:02 PM
 */
@Component
@DependsOn("adultRepository")
public class AdultProvider extends EntityProvider<Adult> {
    @Inject
    private AdultRepository repository;

    @Override
    public Adult createAndFill() {
        Adult person = new Adult();
        person.setFirstName(randomAlphabeticString());
        person.setLastName(randomAlphabeticString());
        return person;
    }

    @Override
    public BaseRepository<Adult> getRepository() {
        return repository;
    }
}