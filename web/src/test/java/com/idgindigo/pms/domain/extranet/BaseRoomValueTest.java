package com.idgindigo.pms.domain.extranet;

import com.idgindigo.pms.domain.BasePersistenceTest;
import com.idgindigo.pms.domain.extranet.roomtype.BaseRoomValue;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.extranet.BaseRoomValueProvider;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 2/20/14 12:11 PM
 */
public class BaseRoomValueTest extends BasePersistenceTest<BaseRoomValue> {
    @Inject
    private BaseRoomValueProvider provider;

    @Override
    protected EntityProvider<BaseRoomValue> getProvider() {
        return provider;
    }
}
