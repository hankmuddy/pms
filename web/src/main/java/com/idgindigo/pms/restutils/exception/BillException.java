package com.idgindigo.pms.restutils.exception;

import com.idgindigo.pms.domain.pms.Bill;

/**
 * @author valentyn_vakatsiienko
 * @since 3/5/14 5:18 PM
 */
public class BillException extends RestFriendlyException {
    public static final String BILL = Bill.BILL + ".";
    public static final String FOR_CUSTOMER = "forCustomer.";

    public static final String ROOM_USE_CHECKED_OUT = BILL + FOR_CUSTOMER + "roomUseCheckedOut";
    public static final String FOR_CUSTOMER_INVALID_DATA = BILL + FOR_CUSTOMER + "invalidData";

    public BillException(String code, String source) {
        super(code, source);
    }
}
