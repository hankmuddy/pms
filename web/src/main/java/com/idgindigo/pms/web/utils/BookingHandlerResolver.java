package com.idgindigo.pms.web.utils;

import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.web.utils.bookinghandler.BookingHandler;
import com.idgindigo.pms.web.utils.bookinghandler.ExtranetBookingHandler;
import com.idgindigo.pms.web.utils.bookinghandler.PmsBookingHandler;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 6/12/14 1:52 PM
 */
@Component
public class BookingHandlerResolver {
    @Inject
    private PmsBookingHandler pmsBookingHandler;
    @Inject
    private ExtranetBookingHandler extranetBookingHandler;

    public BookingHandler resolve() {
        return SecurityUtils.isExtranet() ? extranetBookingHandler : pmsBookingHandler;
    }

}
