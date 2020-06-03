package com.idgindigo.pms.restutils.exception;

/**
 * @author valentyn_vakatsiienko
 * @since 5/26/14 10:17 AM
 */
public class ApiException extends RestFriendlyException {
    private static final String API = "api.";

    public static final String PRICES_INVALID_PLAN = API + "prices.invalidPlan";
    public static final String AVAILABILITY_INVALID_DATE_FORMAT = API + "availability.invalidDateFormat";
    public static final String HOTEL_ID_REQUIRED = API + "protocol.hotelIdRequired";
    public static final String BOOKING_FAILURE = API + "booking.roomNotAvailable";
    public static final String BOOKING_INVALID_DATES = API + "booking.invalidDates";
    public static final String BOOKING_IN_PAST = API + "booking.dateInPast";
    public static final String DATE_SPAN_EXCEEDED = API + "availability.datePeriodTooLong";

    public ApiException(String code, String source) {
        super(code, source);
    }

    public ApiException(String code) {
        super(code);
    }
}
