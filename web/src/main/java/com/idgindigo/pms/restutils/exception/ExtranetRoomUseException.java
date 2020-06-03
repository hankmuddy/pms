package com.idgindigo.pms.restutils.exception;

/**
 * @author valentyn_vakatsiienko
 * @since 6/18/14 2:54 PM
 */
public class ExtranetRoomUseException extends RestFriendlyException {

    public static final String QUOTA_EXCEEDED = "extranetRoomUse.quotaExceeded";

    public ExtranetRoomUseException(String code) {
        super(code);
    }

    public ExtranetRoomUseException(String code, String source) {
        super(code, source);
    }
}
