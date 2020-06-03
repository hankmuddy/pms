package com.idgindigo.pms.web.intercenptor;

import com.idgindigo.pms.logins.domain.Authentication;
import com.idgindigo.pms.logins.domain.Hotel;
import com.idgindigo.pms.logins.repository.HotelRepository;
import com.idgindigo.pms.restutils.exception.ApiException;
import com.idgindigo.pms.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author valentyn_vakatsiienko
 * @since 5/26/14 11:44 AM
 */
public class ApiInterceptor extends HandlerInterceptorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ApiInterceptor.class);

    @Inject
    private HotelRepository hotelRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        validate(request);
        authenticate(request);
        return true;
    }

    public void authenticate(HttpServletRequest request) {
        String hotelId = request.getParameter("hotelId");
        Hotel hotel = hotelRepository.findByTenantId(hotelId);
        if (hotel == null) {
            logger.error("Could not find hotel for id: {}", hotelId);
            throw new AccessDeniedException("!");
        }
        SecurityUtils.authenticateNonTenantUser(new ApiUser(hotel));
    }

    public void validate(HttpServletRequest request) {
        if (request.getParameter("hotelId") == null) {
            throw new ApiException(ApiException.HOTEL_ID_REQUIRED, "hotelId");
        }
    }

    private static class ApiUser extends Authentication {

        ApiUser(Hotel hotel) {
            this.hotel = hotel;
        }

        @Override
        public String getUserType() {
            return "api";
        }
    }
}
