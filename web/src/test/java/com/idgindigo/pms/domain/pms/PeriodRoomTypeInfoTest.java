package com.idgindigo.pms.domain.pms;

import com.idgindigo.pms.domain.BasePersistenceTest;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.pms.PeriodRoomTypeInfoProvider;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 12/27/13 6:26 PM
 */
public class PeriodRoomTypeInfoTest extends BasePersistenceTest<PeriodRoomTypeInfo> {
    @Inject
    private PeriodRoomTypeInfoProvider provider;

    @Override
    protected EntityProvider<PeriodRoomTypeInfo> getProvider() {
        return provider;
    }
}
