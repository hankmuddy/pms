package com.idgindigo.pms.domain.extranet.plan;

import com.idgindigo.pms.domain.BasePersistenceTest;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.extranet.plan.CompactRestrictionProvider;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 5/5/14 5:46 PM
 */
public class CompactRestrictionTest extends BasePersistenceTest<CompactRestriction> {
    @Inject
    private CompactRestrictionProvider provider;

    @Override
    protected EntityProvider<CompactRestriction> getProvider() {
        return provider;
    }
}
