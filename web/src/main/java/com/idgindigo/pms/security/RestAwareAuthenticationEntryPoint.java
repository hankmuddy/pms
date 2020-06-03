package com.idgindigo.pms.security;

import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.web.controller.pms.ApiController;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author vomel
 * @since 29.10.13 13:52
 */
public class RestAwareAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {
    public static final String UNAUTHORIZED_MESSAGE_CODE = "security.authentication_required";

    public RestAwareAuthenticationEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if (isAjax(request)) {
            response.getWriter().write(UNAUTHORIZED_MESSAGE_CODE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            super.commence(request, response, authException);
        }
    }

    private boolean isAjax(HttpServletRequest request) {
        return hasPrefix(request, WebConfiguration.REST_URL_PREFIX)
                || hasPrefix(request, WebConfiguration.ADMIN_URL_PREFIX)
                || hasPrefix(request, ApiController.URL);
    }

    private boolean hasPrefix(HttpServletRequest request, String prefix) {
        return request.getRequestURI().startsWith(request.getContextPath() + prefix);
    }
}
