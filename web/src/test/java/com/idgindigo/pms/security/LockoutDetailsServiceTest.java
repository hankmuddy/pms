package com.idgindigo.pms.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.idgindigo.pms.domain.User;
import com.idgindigo.pms.logins.domain.Admin;
import com.idgindigo.pms.logins.domain.Hotel;
import com.idgindigo.pms.logins.domain.HotelUser;
import com.idgindigo.pms.logins.repository.AuthenticationRepository;
import com.idgindigo.pms.utils.AuthenticationProvider;
import com.idgindigo.pms.utils.HotelProvider;
import com.idgindigo.pms.utils.UserProvider;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.web.controller.InMemoryDbWebTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;

/**
 * @author vomel
 * @since 28.04.14 21:12
 */
public class LockoutDetailsServiceTest extends InMemoryDbWebTest<User> {
    @Inject
    private UserProvider userProvider;
    @Inject
    private AuthenticationProvider authenticationProvider;
    @Inject
    private HotelProvider hotelProvider;
    @Inject
    private AuthenticationRepository authenticationRepository;

    @Test
    public void testLogin() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = preparePost("")
                .param("username", "test3%%%solovki")
                .param("login", "test3")
                .param("password", "t3pwd")
                .param("hotel", "solovki");
        MockHttpServletResponse response = securedMvc.perform(requestBuilder).andReturn().getResponse();
        Assert.assertEquals(response.getStatus(), HttpStatus.FOUND.value());
        Assert.assertEquals(response.getHeader("Location"), "/login?error=true");
        HotelUser hotelUser = new HotelUser();
        Hotel hotel = hotelProvider.getPersistentEntity(new Visitor<Hotel>() {
            @Override
            public void visit(Hotel entity) {
                entity.setTenantId("solovki");
            }
        });
        hotelUser.setHotel(hotel);
        hotelUser.setUsername("test3");
        hotelUser.setPassword("$2a$10$tZypAlnZp/zIcjnJ7t6m3.oz5Shdo6tuvR9je1qJjvE0ELCzCFEXa");
        authenticationRepository.saveAndFlush(hotelUser);
        userProvider.getPersistentEntity(new Visitor<User>() {
            @Override
            public void visit(User entity) {
                entity.setUsername("test3");
            }
        });

        response = securedMvc.perform(requestBuilder).andReturn().getResponse();
        Assert.assertEquals(response.getStatus(), HttpStatus.FOUND.value());
        Assert.assertEquals(response.getHeader("Location"), "home");
        Assert.assertNotNull(authenticationRepository.findByUsernameAndHotelTenantId("test3", "solovki").getLastLoggedIn());
    }

    @Test
    public void testLockBruteForce() throws Exception {
        HotelUser hotelUser = new HotelUser();
        Hotel hotel = hotelProvider.getPersistentEntity(new Visitor<Hotel>() {
            @Override
            public void visit(Hotel entity) {
                entity.setTenantId("magadan");
            }
        });
        hotelUser.setHotel(hotel);
        hotelUser.setUsername("test4");
        hotelUser.setPassword("$2a$10$ukTAuij0RyDJRVYgO7a9W.pMv6nBqHac63WdgSw95ybtpMZS9NzWi");// t4pwd
        authenticationRepository.saveAndFlush(hotelUser);
        userProvider.getPersistentEntity(new Visitor<User>() {
            @Override
            public void visit(User entity) {
                entity.setUsername("test4");
            }
        });
        MockHttpServletRequestBuilder requestBuilder = createLoginRequest("t5pwd");
        Assert.assertEquals(LockoutDetailsService.isLockedOut("test4", "magadan"), false);
        for (int i = 1; i < LockoutDetailsService.DEFAULT_MAX_ATTEMPTS + 5; i++) {
            MockHttpServletResponse response = securedMvc.perform(requestBuilder).andReturn().getResponse();
            Assert.assertEquals(LockoutDetailsService.isLockedOut("test4", "magadan"), i >= LockoutDetailsService.DEFAULT_MAX_ATTEMPTS, "i=" + i);
            Assert.assertEquals(response.getStatus(), HttpStatus.FOUND.value(), "i=" + i);
            Assert.assertEquals(response.getHeader("Location"), i >= 1 + LockoutDetailsService.DEFAULT_MAX_ATTEMPTS ? "/login?error=too_many_attempts" : "/login?error=true", "i=" + i);
        }
        Assert.assertEquals(LockoutDetailsService.isLockedOut("test4", "magadan"), true);
        requestBuilder = createLoginRequest("t4pwd");
        Assert.assertEquals(LockoutDetailsService.isLockedOut("test4", "magadan"), true);
        for (int i = 1; i <= LockoutDetailsService.DEFAULT_MAX_ATTEMPTS; i++) {
            Assert.assertEquals(LockoutDetailsService.isLockedOut("test4", "magadan"), true, "i=" + i);
            MockHttpServletResponse response = securedMvc.perform(requestBuilder).andReturn().getResponse();
            Assert.assertEquals(LockoutDetailsService.isLockedOut("test4", "magadan"), true, "i=" + i);
            Assert.assertEquals(response.getStatus(), HttpStatus.FOUND.value(), "i=" + i);
            Assert.assertEquals(response.getHeader("Location"), "/login?error=too_many_attempts", "i=" + i);
        }
        Thread.sleep(201L);
        Assert.assertEquals(LockoutDetailsService.isLockedOut("test4", "magadan"), false);
        MockHttpServletResponse response = securedMvc.perform(requestBuilder).andReturn().getResponse();
        Assert.assertEquals(response.getStatus(), HttpStatus.FOUND.value());
        Assert.assertEquals(response.getHeader("Location"), "home");
        response = securedMvc.perform(requestBuilder).andReturn().getResponse();
        Assert.assertEquals(response.getStatus(), HttpStatus.FOUND.value());
        Assert.assertEquals(response.getHeader("Location"), "home");
        response = securedMvc.perform(requestBuilder).andReturn().getResponse();
        Assert.assertEquals(response.getStatus(), HttpStatus.FOUND.value());
        Assert.assertEquals(response.getHeader("Location"), "home");
    }

    @Test
    public void testLockAdminBruteForce() throws Exception {
        Admin admin = new Admin();
        admin.setUsername("admin4");
        admin.setPassword("$2a$10$ukTAuij0RyDJRVYgO7a9W.pMv6nBqHac63WdgSw95ybtpMZS9NzWi");// t4pwd
        authenticationRepository.saveAndFlush(admin);
        MockHttpServletRequestBuilder requestBuilder = createAdminLoginRequest("t5pwd");
        Assert.assertEquals(LockoutDetailsService.isLockedOut("admin4", null), false);
        for (int i = 1; i < LockoutDetailsService.DEFAULT_MAX_ATTEMPTS + 5; i++) {
            MockHttpServletResponse response = securedMvc.perform(requestBuilder).andReturn().getResponse();
            Assert.assertEquals(LockoutDetailsService.isLockedOut("admin4", null), i >= LockoutDetailsService.DEFAULT_MAX_ATTEMPTS, "i=" + i);
            Assert.assertEquals(response.getStatus(), HttpStatus.FOUND.value(), "i=" + i);
            Assert.assertEquals(response.getHeader("Location"), i >= 1 + LockoutDetailsService.DEFAULT_MAX_ATTEMPTS ? "/login?error=too_many_attempts" : "/login?error=true", "i=" + i);
        }
        Assert.assertEquals(LockoutDetailsService.isLockedOut("admin4", null), true);
        requestBuilder = createAdminLoginRequest("t4pwd");
        Assert.assertEquals(LockoutDetailsService.isLockedOut("admin4", null), true);
        for (int i = 1; i <= LockoutDetailsService.DEFAULT_MAX_ATTEMPTS; i++) {
            Assert.assertEquals(LockoutDetailsService.isLockedOut("admin4", null), true, "i=" + i);
            MockHttpServletResponse response = securedMvc.perform(requestBuilder).andReturn().getResponse();
            Assert.assertEquals(LockoutDetailsService.isLockedOut("admin4", null), true, "i=" + i);
            Assert.assertEquals(response.getStatus(), HttpStatus.FOUND.value(), "i=" + i);
            Assert.assertEquals(response.getHeader("Location"), "/login?error=too_many_attempts", "i=" + i);
        }
        Thread.sleep(201L);
        Assert.assertEquals(LockoutDetailsService.isLockedOut("admin4", null), false);
        MockHttpServletResponse response = securedMvc.perform(requestBuilder).andReturn().getResponse();
        Assert.assertEquals(response.getStatus(), HttpStatus.FOUND.value(), response.getStatus());
        Assert.assertEquals(response.getHeader("Location"), "admin", response.getHeader("Location"));
        response = securedMvc.perform(requestBuilder).andReturn().getResponse();
        Assert.assertEquals(response.getStatus(), HttpStatus.FOUND.value(), response.getStatus());
        Assert.assertEquals(response.getHeader("Location"), "admin", response.getHeader("Location"));
        response = securedMvc.perform(requestBuilder).andReturn().getResponse();
        Assert.assertEquals(response.getStatus(), HttpStatus.FOUND.value(), response.getStatus());
        Assert.assertEquals(response.getHeader("Location"), "admin", response.getHeader("Location"));
    }

    private MockHttpServletRequestBuilder createLoginRequest(String pwd) throws JsonProcessingException {
        return createRequest("test4", pwd, "magadan");
    }

    private MockHttpServletRequestBuilder createAdminLoginRequest(String pwd) throws JsonProcessingException {
        return createRequest("admin4", pwd, "");
    }

    private MockHttpServletRequestBuilder createRequest(String user, String pwd, String hotelId) throws JsonProcessingException {
        return preparePost("")
                .param("username", user + "%%%" + hotelId)
                .param("login", user)
                .param("password", pwd)
                .param("hotel", hotelId);
    }

    @Override
    protected String getUrl() {
        return "/login";
    }
}
