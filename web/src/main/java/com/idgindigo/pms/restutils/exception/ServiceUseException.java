package com.idgindigo.pms.restutils.exception;

/**
 * @author valentyn_vakatsiienko
 * @since 12/13/13 3:34 PM
 */
public class ServiceUseException extends RestFriendlyException {
    public static final String LIVING_NOT_DELETABLE = "serviceUse.livingNotDeletable";
    public static final String LIVING_NOT_PERMITTED = "serviceUse.livingNotPermitted";
    public static final String INVALID_BILL = "serviceUse.invalidBill";

    public ServiceUseException(String code) {
        super(code);
    }

    public ServiceUseException(String code, String source) {
        super(code, source);
    }
}
