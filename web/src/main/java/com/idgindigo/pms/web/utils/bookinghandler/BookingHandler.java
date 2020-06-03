package com.idgindigo.pms.web.utils.bookinghandler;

import com.idgindigo.pms.domain.extranet.BaseGroupRoomUse;

import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 6/12/14 1:52 PM
 */
public interface BookingHandler {
    List<? extends BaseGroupRoomUse> handleCreate(BaseGroupRoomUse booking);

    List<? extends BaseGroupRoomUse> handleRefuse(BaseGroupRoomUse booking);

    List<? extends BaseGroupRoomUse> handleRefuse(List<? extends BaseGroupRoomUse> bookings);
}
