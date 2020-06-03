package com.idgindigo.pms.service.broadcast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idgindigo.pms.domain.extranet.BaseGroupRoomUse;
import com.idgindigo.pms.service.pms.RoomUseService;
import com.idgindigo.pms.web.websocket.WebSocketEndpoint;
import com.idgindigo.pms.web.websocket.WebSocketMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.idgindigo.pms.security.SecurityUtils.getCurrentTenantId;

/**
 * @author valentyn_vakatsiienko
 * @since 6/3/14 5:36 PM
 */
@Service
public class BroadcastService {

    //TODO Handle moved bookings
    public void broadcastRefuse(List<? extends BaseGroupRoomUse> refused) {
        if (refused.isEmpty()) {
            return;
        }

        if (refused.size() == 1) {
            WebSocketEndpoint.broadcast(refused.get(0), WebSocketMessage.REFUSE, getCurrentTenantId());
        } else {
            List<BaseGroupRoomUse> toSend = new ArrayList<>(refused.size());
            for (BaseGroupRoomUse ru : refused) {
                toSend.add(ru);
            }
            WebSocketEndpoint.broadcast(toSend, WebSocketMessage.GROUP_REFUSE, getCurrentTenantId());
        }
    }

    //TODO Handle moved bookings
    public void broadcastBooking(List<? extends BaseGroupRoomUse> bookings) {
        if (bookings.isEmpty()) {
            return;
        }

        if (bookings.size() == 1) {
            WebSocketEndpoint.broadcast(bookings.get(0), WebSocketMessage.NEW_BOOKING, getCurrentTenantId());
        } else {
            WebSocketEndpoint.broadcast(bookings, WebSocketMessage.NEW_GROUP_BOOKING, getCurrentTenantId());
        }
    }

    public void broadcastBookingFailed(List<BaseGroupRoomUse> roomUses) {
        if (roomUses.isEmpty()) {
            return;
        }

        if (roomUses.size() == 1) {
            WebSocketEndpoint.broadcast(roomUses.get(0), WebSocketMessage.NOT_ENOUGH_ROOMS_SINGLE, getCurrentTenantId());
        } else {
            WebSocketEndpoint.broadcast(roomUses, WebSocketMessage.NOT_ENOUGH_ROOMS_GROUP, getCurrentTenantId());
        }
    }

    public void broadcastBookingFailed(RoomUseService.GroupRoomUseDto dto) {
        List<BaseGroupRoomUse> failed = new ArrayList<>(dto.getRoomUses().size());
        for (RoomUseService.RoomUseWithOverrides roomUseWithOverrides : dto.getRoomUses()) {
            failed.add(roomUseWithOverrides.getRoomUse());
        }
        broadcastBookingFailed(failed);
    }

    public void pushMailSent(String tenantId, String username, String email) {
        pushMailResult(tenantId, username, email, "mailSent");
    }

    public void pushMailFailed(String tenantId, String username, String email) {
        pushMailResult(tenantId, username, email, "mailFailed");
    }

    private void pushMailResult(String tenantId, String username, String email, String code) {
        String notification;
        try {
            notification = new ObjectMapper().writeValueAsString(new WebSocketMessage(code, email));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        WebSocketEndpoint.push(notification, username, tenantId);
    }

}
