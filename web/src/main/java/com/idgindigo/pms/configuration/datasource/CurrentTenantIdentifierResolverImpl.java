package com.idgindigo.pms.configuration.datasource;

import com.idgindigo.pms.logins.domain.Hotel;
import com.idgindigo.pms.security.CustomUserDetails;
import com.idgindigo.pms.security.SecurityUtils;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

/**
 * @author valentyn_vakatsiienko
 * @since 11/6/13 12:54 PM
 */
public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {

    public static final String TEST = "test";

    @Override
    public String resolveCurrentTenantIdentifier() {
        if (SecurityUtils.isRecognizedUser()) {
            CustomUserDetails userDetails = SecurityUtils.getUserDetails();
            Hotel hotel = userDetails.getAuthentication().getHotel();
            return hotel != null ? hotel.getTenantId() : TEST;
        } else {
            return TEST;
        }
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
