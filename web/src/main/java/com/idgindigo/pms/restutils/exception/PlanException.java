package com.idgindigo.pms.restutils.exception;

/**
 * @author valentyn_vakatsiienko
 * @since 6/6/14 11:41 AM
 */
public class PlanException extends RestFriendlyException {
    public static final String PLAN = "plan.";

    public static final String SET_DEFAULT_ONLY_FOR_APPROVED = PLAN + "setDefault.onlyForApproved";

    public PlanException(String code) {
        super(code);
    }

    public PlanException(String code, String source) {
        super(code, source);
    }
}
