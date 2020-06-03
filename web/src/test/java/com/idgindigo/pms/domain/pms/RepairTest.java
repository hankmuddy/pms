package com.idgindigo.pms.domain.pms;

import com.idgindigo.pms.domain.BasePersistenceTest;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.pms.RepairProvider;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 11/27/13 2:17 PM
 */
public class RepairTest extends BasePersistenceTest<Repair> {
    @Inject
    private RepairProvider provider;

    @Override
    protected EntityProvider<Repair> getProvider() {
        return provider;
    }
}
