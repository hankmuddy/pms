package com.idgindigo.pms.utils.extranet;

import com.idgindigo.pms.domain.extranet.BaseGroupRoomUse;
import com.idgindigo.pms.domain.extranet.service.Living;
import com.idgindigo.pms.utils.EntityProvider;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 6/19/14 4:54 PM
 */
public abstract class GenericRoomUseProvider<T extends BaseGroupRoomUse> extends EntityProvider<T> {
    @Inject
    protected LivingProvider livingProvider;
    @Inject
    protected CustomerGroupProvider customerGroupProvider;

    @Override
    public T createAndFill() {
        T roomUse = create();
        roomUse.setStartDate(LocalDate.now(DateTimeZone.forID("UTC")));
        roomUse.setEndDate(roomUse.getStartDate().plusDays(1));
        final Living living = livingProvider.getPersistentEntity();
        roomUse.setBaseRoom(living.getRoom());
        roomUse.setPlan(living.getPlan());
        roomUse.setCustomerGroup(customerGroupProvider.getPersistentEntity());
        roomUse.setStatus(BaseGroupRoomUse.Status.BOOKING_FREE);
        return roomUse;
    }

    protected abstract T create();
}
