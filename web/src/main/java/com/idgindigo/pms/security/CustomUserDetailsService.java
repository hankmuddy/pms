package com.idgindigo.pms.security;

import com.idgindigo.pms.configuration.SecurityConfiguration;
import com.idgindigo.pms.logins.domain.Authentication;
import com.idgindigo.pms.logins.domain.HotelUser;
import com.idgindigo.pms.logins.repository.AuthenticationRepository;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.regex.Pattern;

/**
 * @author valentyn_vakatsiienko
 * @since 10/31/13 9:51 AM
 */
@Component
public class CustomUserDetailsService implements UserDetailsService {
    static final Pattern SEPARATE = Pattern.compile(SecurityConfiguration.SEPARATOR);
    public static final String TOO_MANY_FAILED_LOGIN_ATTEMPTS = "auth.toManyFailedLoginAttempts";
    @Inject
    private AuthenticationRepository repository;

    @Override
    public UserDetails loadUserByUsername(String principal) {
        String[] username = SEPARATE.split(principal);
        Authentication authentication = null;
        if (username.length == 1) {
            if (LockoutDetailsService.isLockedOut(username[0], null)) throw new DisabledException(TOO_MANY_FAILED_LOGIN_ATTEMPTS);
            authentication = findAdministrative(username[0]);
        } else if (username.length == 2) {
            if (LockoutDetailsService.isLockedOut(username[0], username[1])) throw new DisabledException(TOO_MANY_FAILED_LOGIN_ATTEMPTS);
            authentication = repository.findByUsernameAndHotelTenantId(username[0], username[1]);
            if (authentication == null) {
                authentication = repository.findByUsernameAndHotelTenantId(username[0], null);
            }
        }
        validate(authentication);
        return new CustomUserDetails(authentication);
    }

    public void validate(Authentication authentication) {
        if (null == authentication) {
            throw new UsernameNotFoundException("Not found");
        } else if (isBlocked(authentication)) {
            throw new LockedException(SecurityConfiguration.DISABLED);
        }
    }

    private Authentication findAdministrative(String username) {
        Iterable<Authentication> byUsername = repository.findByUsername(username);
        for (Authentication authentication : byUsername) {
            if (!authentication.getUserType().equals(HotelUser.USER)) {
                return authentication;
            }
        }
        return null;
    }

    private static boolean isBlocked(Authentication authentication) {
        if (!authentication.getUserType().equals(HotelUser.USER)) {
            return false;
        }
        HotelUser user = (HotelUser) authentication;
        return user.getStatus() == HotelUser.Status.BLOCKED || user.getHotel().isBlocked();
    }
}
