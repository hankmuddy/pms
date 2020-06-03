package com.idgindigo.pms.web.utils.bookinghandler;

import com.idgindigo.pms.domain.extranet.BaseGroupRoomUse;
import com.idgindigo.pms.domain.extranet.ExtranetRoomUse;
import com.idgindigo.pms.service.extranet.ExtranetRoomUseService;
import org.joda.time.LocalDate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 6/12/14 3:00 PM
 */
@Component
public class ExtranetBookingHandler implements BookingHandler {
    @Inject
    private ExtranetRoomUseService eruService;

    @Override
    public List<ExtranetRoomUse> handleCreate(BaseGroupRoomUse booking) {
        ExtranetRoomUse roomUse = new ExtranetRoomUse();
        BeanUtils.copyProperties(booking, roomUse);

        return Collections.singletonList(eruService.create(roomUse, Collections.<LocalDate, Long>emptyMap()));
    }

    @Override
    public List<? extends BaseGroupRoomUse> handleRefuse(BaseGroupRoomUse booking) {
        return Collections.singletonList(eruService.refuse(getRoomUse(booking)));
    }

    @Override
    public List<? extends BaseGroupRoomUse> handleRefuse(List<? extends BaseGroupRoomUse> bookings) {
        List<BaseGroupRoomUse> refused = new ArrayList<>();
        for (BaseGroupRoomUse roomUse : bookings) {
            refused.addAll(handleRefuse(roomUse));
        }
        return refused;
    }

    private ExtranetRoomUse getRoomUse(BaseGroupRoomUse booking) {
        if (booking instanceof ExtranetRoomUse) {
            return (ExtranetRoomUse) booking;
        } else {
            throw new IllegalArgumentException("Can not handle booking of type: " + booking.getClass());
        }
    }
}
