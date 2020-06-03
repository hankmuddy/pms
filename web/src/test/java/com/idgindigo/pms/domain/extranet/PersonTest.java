package com.idgindigo.pms.domain.extranet;

import com.idgindigo.pms.domain.BasePersistenceTest;
import com.idgindigo.pms.domain.extranet.person.Adult;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.extranet.AdultProvider;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 11/19/13 5:30 PM
 */
public class PersonTest extends BasePersistenceTest<Adult> {
    @Inject
    private AdultProvider provider;

    @Override
    protected EntityProvider<Adult> getProvider() {
        return provider;
    }
}
