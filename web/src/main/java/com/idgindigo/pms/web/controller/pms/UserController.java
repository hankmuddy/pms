package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.domain.BaseEntity;
import com.idgindigo.pms.domain.User;
import com.idgindigo.pms.logins.domain.Authentication;
import com.idgindigo.pms.logins.domain.Hotel;
import com.idgindigo.pms.logins.domain.HotelUser;
import com.idgindigo.pms.logins.repository.AuthenticationRepository;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.UserRepository;
import com.idgindigo.pms.restutils.view.ResponseView;
import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.service.admin.AuthenticationService;
import com.idgindigo.pms.service.admin.SettingsService;
import com.idgindigo.pms.web.controller.BaseCrudController;
import com.idgindigo.pms.web.controller.ResponseEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author vomel
 * @since 29.10.13 15:51
 */
@Controller
@RequestMapping(UserController.URL)
public class UserController extends BaseCrudController<User> {
    public static final String URL = WebConfiguration.REST_URL_PREFIX + "user";

    @Inject
    private UserRepository repository;
    @Inject
    private AuthenticationService authenticationService;
    @Inject
    private AuthenticationRepository authenticationRepository;
    @Inject
    private SettingsService settingsService;

    @Override
    @RequestMapping("null")
    public ResponseEntity<User> create(@RequestBody User entity) {
        throw new EntityNotFoundException();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<User> create(@RequestBody @Valid UserDto dto) {
        Hotel hotel = SecurityUtils.getHotel();
        Authentication authentication = authenticationService.create(dto.getAuthentication(), hotel);
        User user = dto.getUser();
        user.setUsername(authentication.getUsername());
        return super.create(user);
    }

    @Override
    @RequestMapping("null2")
    public ResponseEntity<User> getById(@PathVariable("id") Long id) {
        throw new EntityNotFoundException();
    }

    @RequestMapping(ID)
    @ResponseBody
    @ResponseView(BaseEntity.SoloView.class)
    public ResponseEntity<UserWithLang> getWithLang(@PathVariable("id") Long id) {
        User user = super.getById(id).getContent();
        UserWithLang res = new UserWithLang();
        BeanUtils.copyProperties(user, res);
        res.setLanguage(authenticationRepository.getLang(user.getUsername(), SecurityUtils.getHotel()));
        return new ResponseEntity<>(res);
    }

    @Override
    @RequestMapping("null1")
    public ResponseEntity<User> update(@RequestBody User entity, @PathVariable("id") Long id) {
        throw new EntityNotFoundException();
    }

    @RequestMapping(value = ID, method = RequestMethod.PUT)
    @ResponseBody
    @ResponseView(BaseEntity.SoloView.class)
    public ResponseEntity<User> updateWithLang(@RequestBody UserWithLang entity, @PathVariable("id") Long id) {
        User toUpdate = repository.findOne(id);
        BeanUtils.copyProperties(entity, toUpdate);
        ResponseEntity<User> update = super.update(toUpdate, id);
        User saved = update.getContent();
        if (SecurityUtils.isTenantUser() && SecurityUtils.getCurrentUser().getUsername().equals(saved.getUsername())) {
            SecurityUtils.getUserDetails().setUser(saved);
            SecurityUtils.getUserDetails().getAuthentication().setLanguage(entity.getLanguage());
        }
        authenticationService.setLang(saved, entity.getLanguage());
        return update;
    }

//    @RequestMapping(value = ID + "/password", method = RequestMethod.PUT)
//    @ResponseBody
//    public void changePassword(@RequestBody @Valid PasswordDto dto, @PathVariable("id") Long id) {
//        authenticationService.setPassword(repository.findOne(id), dto.getValue());
//    }

    @Override
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        throw new EntityNotFoundException();
    }

    @Override
    public BaseRepository<User> getRepository() {
        return repository;
    }

    @Getter
    @Setter
    public static class UserDto {
        @NotNull
        private User user;
        @NotNull
        private HotelUser authentication;
    }

    @Getter
    private class PasswordDto {
        private String value;
    }

    @Getter
    @Setter
    public static class UserWithLang extends User {
        private String language;
    }
}