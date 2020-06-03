package com.idgindigo.pms.restutils.exception;

import com.idgindigo.pms.domain.extranet.CustomerGroup;

/**
 * @author valentyn_vakatsiienko
 * @since 1/17/14 11:07 AM
 */
public class CustomerGroupException extends RestFriendlyException {
    private static final String GROUP = CustomerGroup.GROUP + ".";

    public static final String CUSTOMER_AND_COMPANY_NULL = GROUP + "mayNotBeNull";
    public static final String CLOSE_ONLY_FOR_FULLY_PAID = GROUP + "close.groupNotFullyPaid";
    public static final String CLOSE_ROOM_USE_NOT_CHECKED_OUT = GROUP + "close.roomUseNotCheckedOut";
    public static final String INCLUDE_NULL_CUSTOMER = GROUP + "create.includeNullCustomer";


    public CustomerGroupException(String code, String source) {
        super(code, source);
    }
}
