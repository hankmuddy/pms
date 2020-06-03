package com.idgindigo.pms.web.websocket;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author valentyn_vakatsiienko
 * @since 2/4/14 3:01 PM
 */
@Getter
@AllArgsConstructor
public class WebSocketMessage {
    private static final String CHANNELS = "channels.";

    public static final String NEW_BOOKING = CHANNELS + "newBooking";
    public static final String NEW_GROUP_BOOKING = CHANNELS + "newGroupBooking";
    public static final String NOT_ENOUGH_ROOMS_SINGLE = CHANNELS + "error.newBooking.notEnoughRoomsSingle";
    public static final String NOT_ENOUGH_ROOMS_GROUP = CHANNELS + "error.newBooking.notEnoughRoomsGroup";
    public static final String REFUSE = CHANNELS + "refuse";
    public static final String GROUP_REFUSE = CHANNELS + "groupRefuse";
    public static final String SYSTEM_ALERT = "systemAlert";

    private String code;
    private Object data;

}
