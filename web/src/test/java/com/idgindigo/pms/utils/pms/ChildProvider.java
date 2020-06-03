package com.idgindigo.pms.utils.pms;

import com.idgindigo.pms.domain.pms.Child;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.pms.ChildRepository;
import com.idgindigo.pms.utils.EntityProvider;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 12/26/13 4:15 PM
 */
@Service
public class ChildProvider extends EntityProvider<Child> {
    @Inject
    private ChildRepository repository;

    @Override
    public Child createAndFill() {
        Child child = new Child();
        child.setFirstName(randomAlphabeticString());
        child.setLastName(randomAlphabeticString());
        return child;
    }

    @Override
    public BaseRepository<Child> getRepository() {
        return repository;
    }
}
