package com.idgindigo.pms.web.controller;

import com.idgindigo.pms.domain.User;
import com.idgindigo.pms.logins.domain.Authentication;
import com.idgindigo.pms.logins.domain.Hotel;
import com.idgindigo.pms.logins.domain.HotelUser;
import com.idgindigo.pms.logins.repository.AuthenticationRepository;
import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.utils.AuthenticationProvider;
import com.idgindigo.pms.web.controller.pms.UserController;
import mockit.NonStrictExpectations;
import org.springframework.beans.BeanUtils;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static com.idgindigo.pms.web.controller.pms.UserController.UserDto;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

/**
 * @author vomel
 * @since 29.10.13 15:59
 */
public class UserControllerTest extends InMemoryDbWebTest<User> {
    public static final String LANG = "es";
    @Inject
    private AuthenticationProvider authenticationProvider;
    @Inject
    private UserController controller;
    @Inject
    private AuthenticationRepository authenticationRepository;

    @Test
    public void testCreate() {
        UserDto dto = getUserDto();
        User res = create(dto);

        assertEquals(res.getUsername(), dto.getAuthentication().getUsername());
    }

    @Test
    public void testUpdate() throws Exception {
        UserDto dto = getUserDto();
        User res = create(dto);
        assertNotEquals(dto.getAuthentication().getLanguage(), LANG);

        UserController.UserWithLang toUpdate = new UserController.UserWithLang();
        BeanUtils.copyProperties(res, toUpdate);
        toUpdate.setLanguage(LANG);
        controller.updateWithLang(toUpdate, res.getId());

        Authentication updated = authenticationRepository.findByUsernameAndHotelTenantId(res.getUsername(), dto.getAuthentication().getHotel().getTenantId());
        assertEquals(updated.getLanguage(), LANG);
    }

    public User create(UserDto dto) {
        initHotel(dto.getAuthentication().getHotel());
        return controller.create(dto).getContent();
    }

    public void initHotel(final Hotel hotel) {
        new NonStrictExpectations(SecurityUtils.class) {{
            SecurityUtils.getHotel();
            result = hotel;
            SecurityUtils.getCurrentTenantId();
            result = hotel.getTenantId();
            SecurityUtils.isRecognizedUser();
            result = false;
        }};
    }

    public UserDto getUserDto() {
        HotelUser authentication = authenticationProvider.hotelUser.getTransientEntity();
        User user = userProvider.getTransientEntity();
        UserDto dto = new UserDto();
        dto.setAuthentication(authentication);
        dto.setUser(user);
        return dto;
    }

    @Override
    protected String getUrl() {
        return UserController.URL + "/";
    }

}