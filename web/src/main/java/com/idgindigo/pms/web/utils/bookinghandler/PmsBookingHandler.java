package com.idgindigo.pms.web.utils.bookinghandler;

import com.idgindigo.pms.domain.extranet.BaseGroupRoomUse;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.repository.pms.BankDetailsRepository;
import com.idgindigo.pms.service.channels.BookingService;
import com.idgindigo.pms.service.pms.RoomUseService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 6/12/14 2:58 PM
 */
@Component
public class PmsBookingHandler implements BookingHandler {
    @Inject
    private BookingService bookingService;
    @Inject
    private RoomUseService service;
    @Inject
    private BankDetailsRepository bankDetailsRepository;
    @Inject
    private RoomUseService roomUseService;

    @Override
    public List<RoomUse> handleCreate(BaseGroupRoomUse booking) {
        RoomUse roomUse = new RoomUse();
        BeanUtils.copyProperties(booking, roomUse);
        roomUse.setCustomerPays(true);

        return bookingService.handleWithoutRoom(new RoomUseService.RoomUseWithOverrides(roomUse), true);
    }

    @Override
    public List<? extends BaseGroupRoomUse> handleRefuse(BaseGroupRoomUse booking) {
        RoomUse roomUse = getRoomUse(booking);
        return service.refuse(roomUse, roomUse.getStartDate(), true, bankDetailsRepository.getDefault());
    }

    @Override
    public List<? extends BaseGroupRoomUse> handleRefuse(List<? extends BaseGroupRoomUse> bookings) {
        List<RoomUse> refused = new ArrayList<>(bookings.size());
        for (BaseGroupRoomUse booking : bookings) {
            RoomUse roomUse = getRoomUse(booking);
            if (!roomUse.isMoved()) {
                roomUseService.refuse(roomUse, roomUse.getStartDate(), true, bankDetailsRepository.getDefault());//TODO Need bank details resolving logic
                refused.add(roomUse);
            }
        }
        return refused;
    }

    private RoomUse getRoomUse(BaseGroupRoomUse booking) {
        if (booking instanceof RoomUse) {
            return (RoomUse) booking;
        } else {
            throw new IllegalArgumentException("Can not handle booking of type: " + booking.getClass());
        }
    }
}
