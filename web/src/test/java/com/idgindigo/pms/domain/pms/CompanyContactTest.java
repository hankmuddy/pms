package com.idgindigo.pms.domain.pms;

import com.idgindigo.pms.domain.BasePersistenceTest;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.pms.CompanyContactProvider;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 1/10/14 1:32 PM
 */
public class CompanyContactTest extends BasePersistenceTest<CompanyContact> {
    @Inject
    private CompanyContactProvider provider;

    @Override
    protected EntityProvider<CompanyContact> getProvider() {
        return provider;
    }
}
