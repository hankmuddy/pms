package com.idgindigo.pms.domain.pms;

import com.idgindigo.pms.domain.ApprovableEntityTest;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.pms.BillProvider;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 11/22/13 11:46 AM
 */
public class BillTest extends ApprovableEntityTest<Bill> {
    @Inject
    private BillProvider provider;

    @Override
    protected EntityProvider<Bill> getProvider() {
        return provider;
    }
}
