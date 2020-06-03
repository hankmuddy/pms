package com.idgindigo.pms.restutils.exception;

import com.idgindigo.pms.domain.pms.BankDetails;

/**
 * @author valentyn_vakatsiienko
 * @since 3/11/14 10:32 AM
 */
public class BankDetailsException extends RestFriendlyException {
    public static final String DETAILS = BankDetails.BANK_DETAILS + ".";

    public static final String BLOCK_DEFAULT_DETAILS = DETAILS + "block.detailsAreDefault";

    public BankDetailsException(String code, String source) {
        super(code, source);
    }
}
