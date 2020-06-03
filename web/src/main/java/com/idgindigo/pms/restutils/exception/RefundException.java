package com.idgindigo.pms.restutils.exception;

import com.idgindigo.pms.domain.pms.Refund;

/**
 * @author valentyn_vakatsiienko
 * @since 1/10/14 1:17 PM
 */
public class RefundException extends RestFriendlyException {
    public static final String REFUND = Refund.REFUND + ".";
    public static final String SERVICE_USES_EMPTY = REFUND + "serviceUses.mayNotBeEmpty";
    public static final String INVALID_ROOM_USE = REFUND + "serviceUses.differentRoomUses";
    public static final String INVALID_GROUP = REFUND + "serviceUses.differentGroups";
    public static final String BANK_DETAILS_REQUIRED = REFUND + "bankDetails.required";

    public RefundException(String code, String source) {
        super(code, source);
    }

    public RefundException(String code) {
        super(code);
    }
}
