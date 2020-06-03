package com.idgindigo.pms.restutils.exception;

import com.idgindigo.pms.logins.domain.Authentication;

/**
 * @author valentyn_vakatsiienko
 * @since 4/14/14 2:31 PM
 */
public class AuthenticationException extends RestFriendlyException {
    public static final String AUTHENTICATION = Authentication.AUTHENTICATION + ".";

    public static final String HOTEL_USER_CREATION_FORBIDDEN = AUTHENTICATION + "create.hotelUserIsForbidden";
    public static final String INVALID_OLD_PASSWORD = AUTHENTICATION + "changePassword.invalidOldPassword";

    public AuthenticationException(String code, String source) {
        super(code, source);
    }

    public AuthenticationException(String code) {
        super(code);
    }
}
