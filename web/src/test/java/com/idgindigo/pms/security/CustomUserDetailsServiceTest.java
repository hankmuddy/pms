package com.idgindigo.pms.security;

import com.idgindigo.pms.configuration.SecurityConfiguration;
import com.idgindigo.pms.logins.domain.Authentication;
import com.idgindigo.pms.logins.domain.HotelUser;
import com.idgindigo.pms.logins.repository.HotelRepository;
import com.idgindigo.pms.service.ServiceTest;
import com.idgindigo.pms.utils.AuthenticationProvider;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static junit.framework.Assert.fail;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

/**
 * @author valentyn_vakatsiienko
 * @since 5/7/14 4:21 PM
 */
public class CustomUserDetailsServiceTest extends ServiceTest {
    @Inject
    private AuthenticationProvider authenticationProvider;
    @Inject
    private HotelRepository hotelRepository;

    @Inject
    private CustomUserDetailsService customUserDetailsService;

    @Test
    public void testBlocked() {
        final HotelUser user = authenticationProvider.hotelUser.getPersistentEntity();
        assertFalse(user.getStatus() == HotelUser.Status.BLOCKED);
        assertFalse(user.getHotel().isBlocked());

        String principal = user.getUsername() + CustomUserDetailsService.SEPARATE + user.getHotel().getTenantId();

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(principal);
        assertEquals(userDetails.getUsername(), user.getUsername());

        setStatus(user, HotelUser.Status.BLOCKED);
        confirmBlocked(principal);

        setStatus(user, HotelUser.Status.ACTIVE);
        hotelRepository.setBlocked(user.getHotel().getId(), true);
        confirmBlocked(principal);

        //Admin can be bound to blocked hotel
        Authentication admin = authenticationProvider.getPersistentEntity();
        hotelRepository.setBlocked(admin.getHotel().getId(), true);
        userDetails = customUserDetailsService.loadUserByUsername(admin.getUsername());
        assertEquals(userDetails.getUsername(), admin.getUsername());

    }

    public void confirmBlocked(String principal) {
        try {
            customUserDetailsService.loadUserByUsername(principal);
            fail();
        } catch (LockedException e) {
            assertEquals(e.getMessage(), SecurityConfiguration.DISABLED);
        }
    }

    public void setStatus(HotelUser user, HotelUser.Status active) {
        user.setStatus(active);
        authenticationProvider.hotelUser.getRepository().save(user);
    }
}
