package com.idgindigo.pms.restutils.exception;

import com.idgindigo.pms.domain.extranet.plan.Plan;
import com.idgindigo.pms.domain.extranet.roomtype.BaseRoom;

/**
 * @author valentyn_vakatsiienko
 * @since 12/2/13 4:05 PM
 */
public class RoomUseException extends RestFriendlyException {
    public static final String ROOM_USE = "roomUse.";

    public static final String BILL_IS_ALREADY_APPROVED = ROOM_USE + "billIsAlreadyApproved";
    public static final String NEW_END_DATE_BEFORE_OLD = ROOM_USE + "newEndDateBeforeOld";
    public static final String BILL_UPDATE_INVALID_GROUP = ROOM_USE + "invalidCustomerGroup";
    public static final String ROOM_NOT_APPROVED = ROOM_USE + "room.notApproved";
    public static final String PLAN_NOT_APPROVED = ROOM_USE + "plan.notApproved";
    public static final String INCOME_ONLY_AFTER_BOOKING = ROOM_USE + "income.allowedOnlyFromBooking";
    public static final String REFUSE_INVALID_DATE = ROOM_USE + "refuse.invalidDates";
    public static final String OUTGO_ONLY_AFTER_LIVING = ROOM_USE + "outgo.allowedOnlyAfterLiving";
    public static final String INVALID_MOVE_DATE = ROOM_USE + "move.invalidSinceDate";
    public static final String MOVE_PLAN_NOT_SET = ROOM_USE + "move." + Plan.PLAN + "NotSet";
    public static final String REFUSE_INVALID_STATUS = ROOM_USE + "refuse.invalidStatus";
    public static final String UPDATE_INVALID_STATUS = ROOM_USE + "move.invalidStatus";
    public static final String INCOME_TOO_EARLY = ROOM_USE + "income.startDateInFuture";
    public static final String INVALID_DATES = ROOM_USE + "booking.invalidDates";
    public static final String BASE_ROOM_NOT_APPROVED = ROOM_USE + BaseRoom.BASE_ROOM + ".notApproved";
    public static final String INVALID_VIRTUAL_ROOM = ROOM_USE + BaseRoom.BASE_ROOM + ".invalid";
    public static final String OUTGO_ONLY_ON_END_DATE = ROOM_USE + "outgo.todayIsNotEndDate";
    public static final String CLOSED_GROUP = ROOM_USE + "roomUse.closed";
    public static final String REFUSE_CHANNEL_BOOKING = ROOM_USE + "refuse.channelBooking";
    public static final String NOT_ARRIVED_ONLY_FOR_CHANNEL = ROOM_USE + "notArrived.invalidSource";
    public static final String NOT_ARRIVED_ONLY_FOR_BOOKING = ROOM_USE + "notArrived.invalidStatus";
    public static final String CONFIRM_ONLY_FOR_BOOKING_FREE = ROOM_USE + "confirm.invalidStatus";
    public static final String WIDEN_MOVED_ROOM_USE = ROOM_USE + "widen.roomUseMoved";
    public static final String OUTGO_FOR_FULLY_PAID = ROOM_USE + "outgo.notFullyPaid";
    public static final String CHECK_IN_EMPTY_ROOM_USE = ROOM_USE + "checkIn.roomUseEmpty";
    public static final String FORBIDDEN_SOURCE = ROOM_USE + "forbiddenSource";
    public static final String MOVE_CHANNEL_BOOKING_WRONG_ROOM_TYPE = ROOM_USE + "moveChannelBooking.cantChangeRoomType";

    public RoomUseException(String code, String source) {
        super(code, source);
    }
}
