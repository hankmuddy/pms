package com.idgindigo.pms.domain.extranet;

import com.idgindigo.pms.domain.BasePersistenceTest;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.extranet.ExtranetRoomUseProvider;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 6/19/14 4:31 PM
 */
public class ExtranetRoomUseTest extends BasePersistenceTest<ExtranetRoomUse> {
    @Inject
    private ExtranetRoomUseProvider provider;

    @Override
    protected EntityProvider<ExtranetRoomUse> getProvider() {
        return provider;
    }
}
