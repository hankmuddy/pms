package com.idgindigo.pms.domain.pms;

import com.idgindigo.pms.domain.BasePersistenceTest;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.pms.ChildProvider;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 12/27/13 12:00 PM
 */
public class ChildTest extends BasePersistenceTest<Child> {
    @Inject
    private ChildProvider provider;

    @Override
    protected EntityProvider<Child> getProvider() {
        return provider;
    }
}
