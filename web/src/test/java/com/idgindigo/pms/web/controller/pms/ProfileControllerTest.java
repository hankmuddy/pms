package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.domain.User;
import com.idgindigo.pms.logins.domain.Authentication;
import com.idgindigo.pms.logins.domain.HotelUser;
import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.utils.AuthenticationProvider;
import com.idgindigo.pms.utils.UserProvider;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.web.controller.InMemoryDbWebTest;
import org.springframework.beans.BeanUtils;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static com.idgindigo.pms.web.controller.pms.ProfileController.Profile;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

/**
 * @author valentyn_vakatsiienko
 * @since 2/10/14 2:48 PM
 */
public class ProfileControllerTest extends InMemoryDbWebTest {
    @Inject
    private AuthenticationProvider provider;
    @Inject
    private UserProvider userProvider;

    @Test
    public void testProfileUpdate() throws Exception {
        final HotelUser authentication = provider.hotelUser.getPersistentEntity();
        final User user = userProvider.getPersistentEntity(new Visitor<User>() {
            @Override
            public void visit(User entity) {
                entity.setUsername(authentication.getUsername());
            }
        });
        loginAs(user);

        String newEmail = "newEmail";
        String newLanguage = "uk";
        assertNotEquals(SecurityUtils.getCurrentUser().getEmail(), newEmail);
        assertNotEquals(SecurityUtils.getUserDetails().getAuthentication().getLanguage(), newLanguage);
        user.setEmail(newEmail);
        Profile profile = new Profile();
        BeanUtils.copyProperties(user, profile);
        profile.setLanguage(newLanguage);

        mvc.perform(preparePut(profile, "")).andExpect(status().isOk());
        assertEquals(SecurityUtils.getCurrentUser().getEmail(), newEmail);
        assertEquals(SecurityUtils.getUserDetails().getAuthentication().getLanguage(), newLanguage);
    }

    @Inject
    private ProfileController controller;

    @Test
    public void testGetProfile() {
        final Authentication authentication = provider.hotelUser.getPersistentEntity();
        User user = userProvider.getPersistentEntity(new Visitor<User>() {
            @Override
            public void visit(User entity) {
                entity.setUsername(authentication.getUsername());
            }
        });

        loginAs(user);
        Profile profile = controller.profile().getContent();

        assertEquals(profile.getUsername(), authentication.getUsername());
        assertEquals(profile.getUsername(), user.getUsername());
        assertEquals(profile.getLanguage(), authentication.getLanguage());
    }

    @Override
    protected String getUrl() {
        return ProfileController.URL + "/";
    }
}