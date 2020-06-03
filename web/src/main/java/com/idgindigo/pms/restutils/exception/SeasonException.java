package com.idgindigo.pms.restutils.exception;

/**
 * @author valentyn_vakatsiienko
 * @since 12/24/13 12:32 PM
 */
public class SeasonException extends RestFriendlyException {
    public static final String INVALID_DATES = "season.startAfterEnd";

    public SeasonException(String code) {
        super(code);
    }

    public SeasonException(String code, String source) {
        super(code, source);
    }
}
