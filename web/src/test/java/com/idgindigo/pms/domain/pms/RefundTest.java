package com.idgindigo.pms.domain.pms;

import com.idgindigo.pms.domain.BasePersistenceTest;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.pms.RefundProvider;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 1/10/14 2:00 PM
 */
public class RefundTest extends BasePersistenceTest<Refund> {
    @Inject
    private RefundProvider provider;

    @Override
    protected EntityProvider<Refund> getProvider() {
        return provider;
    }
}
