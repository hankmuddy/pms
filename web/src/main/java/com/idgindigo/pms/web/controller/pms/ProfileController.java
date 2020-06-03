package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.domain.User;
import com.idgindigo.pms.logins.domain.Authentication;
import com.idgindigo.pms.logins.repository.AuthenticationRepository;
import com.idgindigo.pms.repository.UserRepository;
import com.idgindigo.pms.security.CustomUserDetails;
import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.service.admin.AuthenticationService;
import com.idgindigo.pms.web.controller.ResponseEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

/**
 * @author valentyn_vakatsiienko
 * @since 2/10/14 1:11 PM
 */
@Controller
@RequestMapping(ProfileController.URL)
public class ProfileController {
    public static final String URL = WebConfiguration.REST_URL_PREFIX + "profile";
    @Inject
    private UserRepository repository;
    @Inject
    private AuthenticationRepository authenticationRepository;
    @Inject
    private AuthenticationService authenticationService;

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Profile> update(@RequestBody Profile profile) {
        verifyIsTenantUser();
        User current = SecurityUtils.getCurrentUser();
        User updated = new User();
        BeanUtils.copyProperties(profile, updated);
        updated.setId(current.getId());
        updated = repository.save(updated);

        CustomUserDetails userDetails = SecurityUtils.getUserDetails();
        userDetails.setUser(updated);

        Authentication authentication = userDetails.getAuthentication();
        authentication.setLanguage(profile.language);
        authentication = authenticationRepository.save(authentication);
        userDetails.setAuthentication(authentication);
        return new ResponseEntity<>(profile);
    }

    @RequestMapping
    @ResponseBody
    public ResponseEntity<Profile> profile() {
        verifyIsTenantUser();
        User current = SecurityUtils.getCurrentUser();
        Profile profile = new Profile();
        BeanUtils.copyProperties(current, profile);
        profile.setLanguage(SecurityUtils.getUserDetails().getAuthentication().getLanguage());
        return new ResponseEntity<>(profile);
    }

    @RequestMapping(value = "password", method = RequestMethod.PUT)
    @ResponseBody
    public void setPassword(@RequestBody @Valid AuthenticationService.PasswordDto dto) {
        verifyIsTenantUser();
        authenticationService.setPassword(authenticationRepository.findByUsernameAndHotelTenantId(SecurityUtils.getCurrentUser().getUsername(), SecurityUtils.getCurrentTenantId()), dto);
    }

    public void verifyIsTenantUser() {
        if (!SecurityUtils.isTenantUser()) {
            throw new EntityNotFoundException();
        }
    }

    @Getter
    @Setter
    public static class Profile extends User {
        private String language;//TODO validate ISO lang
    }

}