package com.idgindigo.pms.restutils.exception;

/**
 * @author valentyn_vakatsiienko
 * @since 12/2/13 11:59 AM
 */
public class PaymentException extends RestFriendlyException {
    public static final String PAYMENT = "payment.";
    public static final String PAYMENT_TOTAL_EXCEEDS_BILL = PAYMENT + "totalExceedsBill";
    public static final String PAYMENT_TOTAL_EXCEEDS_NON_PAID_PART = PAYMENT + "totalExceedsNonPaidPart";
    public static final String BANK_DETAILS_BLOCKED = PAYMENT + ".bankDetailsBlocked";
    public static final String PAYMENT_DATE_BEFORE_BILL_DATE = PAYMENT + "dateBeforeBillDate";

    public PaymentException(String code, String source) {
        super(code, source);
    }
}
