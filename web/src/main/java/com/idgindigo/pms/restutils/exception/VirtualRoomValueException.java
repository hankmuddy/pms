package com.idgindigo.pms.restutils.exception;

import com.idgindigo.pms.domain.extranet.roomtype.BaseRoomValue;

/**
 * @author ExtravaganZza
 * @since 27.02.14 9:27
 */
public class VirtualRoomValueException extends RestFriendlyException {
    private static final String VALUE = BaseRoomValue.BASE_ROOM_VALUE + ".";
    public static final String INVALID_DATES = VALUE + "dateStartAfterDateEnd";

    public VirtualRoomValueException(String code, String source) {
        super(code, source);
    }
}
