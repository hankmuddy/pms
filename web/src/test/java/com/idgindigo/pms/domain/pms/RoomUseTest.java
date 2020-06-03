package com.idgindigo.pms.domain.pms;

import com.idgindigo.pms.domain.BasePersistenceTest;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.pms.RoomUseProvider;
import org.testng.annotations.Test;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 11/19/13 5:31 PM
 */
public class RoomUseTest extends BasePersistenceTest<RoomUse> {
    @Inject
    private RoomUseProvider provider;

    @Test(enabled = false)
    public void testInvalidDates() {
        RoomUse roomUse = provider.getPersistentEntity(new Visitor<RoomUse>() {
            @Override
            public void visit(RoomUse entity) {
                entity.setEndDate(entity.getStartDate().minusDays(1));
            }
        });
    }

    @Override
    protected EntityProvider<RoomUse> getProvider() {
        return provider;
    }
}
