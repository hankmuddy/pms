package com.idgindigo.pms.restutils.exception;

import com.idgindigo.pms.domain.pms.PmsRoomUse;

/**
 * @author valentyn_vakatsiienko
 * @since 11/20/13 10:45 AM
 */
public class RoomNotAvailableException extends RuntimeException {
    public static final String MESSAGE = "Couldn`t perform booking of %s. No sufficient rooms for dates %s to %s";
    public static final String FORMAT = "yyyy-MM-dd";
    public static final String CODE = "error.booking.roomNotAvailable";

    public RoomNotAvailableException(PmsRoomUse roomUse) {
        super(String.format(MESSAGE, roomUse.getRoom(), roomUse.getStartDate().toString(FORMAT), roomUse.getEndDate().toString(FORMAT)));
    }
}
