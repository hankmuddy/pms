package com.idgindigo.pms.utils.extranet.plan;

import com.idgindigo.pms.domain.extranet.plan.CompactRestriction;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.extranet.plan.CompactRestrictionRepository;
import com.idgindigo.pms.utils.EntityProvider;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 5/5/14 5:47 PM
 */
@Component
public class CompactRestrictionProvider extends EntityProvider<CompactRestriction> {
    @Inject
    private CompactRestrictionRepository repository;

    @Override
    public CompactRestriction createAndFill() {
        CompactRestriction restriction = new CompactRestriction();
        restriction.setName(randomAlphabeticString());
        return restriction;
    }

    @Override
    public BaseRepository<CompactRestriction> getRepository() {
        return repository;
    }
}
