package com.idgindigo.pms.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idgindigo.pms.configuration.security.CrudSecurityManager;
import com.idgindigo.pms.domain.Permission;
import com.idgindigo.pms.logins.domain.Admin;
import com.idgindigo.pms.logins.domain.Manager;
import com.idgindigo.pms.logins.domain.ManagerSupervisor;
import com.idgindigo.pms.restutils.exception.handler.ExceptionMessage;
import com.idgindigo.pms.security.CustomUserDetailsService;
import com.idgindigo.pms.security.RestAwareAuthenticationEntryPoint;
import com.idgindigo.pms.security.TenantAwareAuthenticationSuccessHandler;
import com.idgindigo.pms.web.controller.admin.HotelController;
import com.idgindigo.pms.web.controller.admin.SettingsController;
import com.idgindigo.pms.web.controller.pms.ApiController;
import com.idgindigo.pms.web.controller.pms.MerchantController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import static com.idgindigo.pms.security.CustomUserDetailsService.TOO_MANY_FAILED_LOGIN_ATTEMPTS;


/**
 * @author valentyn_vakatsiienko
 * @since 10/31/13 9:47 AM
 */
@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = "com.idgindigo.pms.security")
@PropertySource("classpath:META-INF/security.properties")
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    public static final String SEPARATOR = "%%%";
    public static final String DISABLED = "auth.disabled";
    public static final String TOO_MANY_FAILED_LOGINS = "disabled";

    @Inject
    private Environment environment;
    @Inject
    private CustomUserDetailsService customUserDetailsService;

    @Override
    public void configure(WebSecurity webSecurity) {
        webSecurity
                .ignoring()
                .antMatchers(
                        "/client/**",
                        "/styles/**",
                        "/pbb/**",
                        "/themes/**",
                        "/confirmationletter/**",
                        "/wubooking/catch",
                        ApiController.URL + "/**",
                        MerchantController.URL + "/**",
                        "/mockwubook/xrws");
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry urlRegistry = httpSecurity
                .headers().disable()
                .csrf().disable()
                        //Url based authorization
                .formLogin()
                .successHandler(successHandler())
                .failureHandler(failureHandler())
                .loginProcessingUrl(environment.getProperty("security.loginProcessingUrl"))
                .usernameParameter(environment.getProperty("security.usernameParam"))
                .passwordParameter(environment.getProperty("security.passwordParam"))
                .loginPage(environment.getProperty("security.welcomeUrl"))
                        //Rest- aware authentication
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())
                .authenticationEntryPoint(new RestAwareAuthenticationEntryPoint(environment.getProperty("security.welcomeUrl")))
                        //Url security
                .and()
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers("/form").permitAll()
                .antMatchers("/button").permitAll();

        //CRUD Permissions
        urlRegistry = CrudSecurityManager.secureCruds(urlRegistry);

        urlRegistry = urlRegistry
                .antMatchers(HttpMethod.PUT, SettingsController.URL).hasAnyAuthority(Admin.ADMIN, ManagerSupervisor.MANAGER_SUPERVISOR, Manager.MANAGER, Permission.SETTINGS_SAVE.name())
                .antMatchers(HttpMethod.POST, SettingsController.URL + "/exportToWubook").hasAnyAuthority(Admin.ADMIN, ManagerSupervisor.MANAGER_SUPERVISOR, Manager.MANAGER, Permission.EXPORT_TO_WUBOOK.name())
                .antMatchers(HttpMethod.POST, SettingsController.URL + "/broadcast").hasAnyAuthority(Admin.ADMIN)
                .antMatchers(HttpMethod.POST, HotelController.URL + "/dropTenant").hasAnyAuthority(Admin.ADMIN)
                .antMatchers(HttpMethod.PUT, HotelController.URL + "/*/supervisor/*").hasAnyAuthority(Admin.ADMIN)
        ;

        urlRegistry.antMatchers(HttpMethod.GET, "/**").authenticated()
                .antMatchers(HttpMethod.POST, "/**").authenticated()
                .antMatchers(HttpMethod.PUT, "/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/**").authenticated();

//                .and()
//                .logout()
//                .logoutRequestMatcher(new AntPathRequestMatcher(environment.getProperty("security.logoutUrl"), "GET"))
//                .logoutSuccessUrl(environment.getProperty("security.welcomeUrl") + "?logout")

    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new TenantAwareAuthenticationSuccessHandler();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean()
            throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(customUserDetailsService)
                .passwordEncoder(getPasswordEncoder());
    }

    @Bean
    public SecureRandom getRandomGenerator() throws NoSuchAlgorithmException {
        return SecureRandom.getInstance("SHA1PRNG");
    }

    @Bean
    protected PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private AuthenticationFailureHandler failureHandler() {
        return new AuthenticationFailureHandler() {
            private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
            private final AuthenticationFailureHandler fallback = new SimpleUrlAuthenticationFailureHandler(environment.getProperty("security.loginErrorUrl"));
            private final Map<String, String> codeToUrl = new HashMap<String, String>() {
                {
                    put(DISABLED, environment.getProperty("security.credentialsBlockedUrl"));
                    put(TOO_MANY_FAILED_LOGIN_ATTEMPTS, environment.getProperty("security.loginErrorUrlOuttried"));
                }
            };

            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                if (request.getParameterMap().containsKey("ajax_request")) {
                    handleAjax(request, response, exception);
                } else {
                    handleForm(request, response, exception);
                }
            }

            public void handleForm(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                String code = exception.getMessage();
                if (codeToUrl.containsKey(code)) {
                    redirectStrategy.sendRedirect(request, response, codeToUrl.get(code));
                } else {
                    fallback.onAuthenticationFailure(request, response, exception);
                }
            }

            private void handleAjax(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getOutputStream().write(new ObjectMapper().writeValueAsBytes(new ExceptionMessage(exception.getMessage())));
            }
        };
    }

    private AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandler() {
            private final byte[] accessDeniedMessageBytes;

            {
                try {
                    accessDeniedMessageBytes = new ObjectMapper().writeValueAsBytes(new ExceptionMessage("security.accessDenied"));
                } catch (JsonProcessingException e) {
                    throw new IllegalStateException("Could not create access denied message bytes");
                }

            }

            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                response.getOutputStream().write(accessDeniedMessageBytes);
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        };
    }
}
