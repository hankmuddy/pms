package com.idgindigo.pms.security;

import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.logins.domain.HotelUser;
import com.idgindigo.pms.logins.repository.AuthenticationRepository;
import com.idgindigo.pms.repository.UserRepository;
import org.joda.time.LocalDateTime;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @author valentyn_vakatsiienko
 * @since 11/6/13 5:23 PM
 */
public class TenantAwareAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Inject
    private UserRepository userRepository;
    @Inject
    private AuthenticationRepository authenticationRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomUserDetails userDetails = SecurityUtils.getUserDetails();
        if (userDetails.getAuthentication().getUserType().equals(HotelUser.USER)) {
            String username = userDetails.getUsername();
            String tenantId = userDetails.getAuthentication().getHotel().getTenantId();
            LockoutDetailsService.registerSuccessLogin(username, tenantId);
            userDetails.setUser(userRepository.findByUsername(username));
            SecurityUtils.authenticateUser(userDetails.getAuthentication(), userDetails.getUser());
            request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
            com.idgindigo.pms.logins.domain.Authentication user = authenticationRepository.findByUsernameAndHotelTenantId(username, tenantId);
            user.setLastLoggedIn(LocalDateTime.now());
            authenticationRepository.save(user);
            processSuccessfulLogin(request, response, WebConfiguration.APP_NAME);
        } else {
            LockoutDetailsService.registerSuccessLogin(userDetails.getUsername(), null);
            com.idgindigo.pms.logins.domain.Authentication user = authenticationRepository.findAdministrativeByUsername(userDetails.getUsername());
            user.setLastLoggedIn(LocalDateTime.now());
            authenticationRepository.save(user);
            processSuccessfulLogin(request, response, WebConfiguration.ADMIN_APP_NAME);
        }
    }

    private void processSuccessfulLogin(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
        if (!request.getParameterMap().containsKey("ajax_request")) {
            response.sendRedirect(url);
        }
    }
}
