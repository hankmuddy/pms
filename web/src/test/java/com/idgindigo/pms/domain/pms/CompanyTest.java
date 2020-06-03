package com.idgindigo.pms.domain.pms;

import com.idgindigo.pms.domain.BasePersistenceTest;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.pms.CompanyProvider;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 12/24/13 6:50 PM
 */
public class CompanyTest extends BasePersistenceTest<Company> {
    @Inject
    private CompanyProvider provider;

    @Override
    protected EntityProvider<Company> getProvider() {
        return provider;
    }
}
