package com.idgindigo.pms.domain.extranet;

import com.idgindigo.pms.domain.ApprovableEntityTest;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.extranet.CustomerGroupProvider;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 11/19/13 4:50 PM
 */
public class CustomerGroupTest extends ApprovableEntityTest<CustomerGroup> {
    @Inject
    private CustomerGroupProvider provider;

    @Override
    protected EntityProvider<CustomerGroup> getProvider() {
        return provider;
    }
}