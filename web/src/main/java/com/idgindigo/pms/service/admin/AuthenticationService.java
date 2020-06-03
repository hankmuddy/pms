package com.idgindigo.pms.service.admin;

import com.idgindigo.pms.domain.User;
import com.idgindigo.pms.logins.domain.Authentication;
import com.idgindigo.pms.logins.domain.Hotel;
import com.idgindigo.pms.logins.repository.AuthenticationRepository;
import com.idgindigo.pms.restutils.exception.AuthenticationException;
import com.idgindigo.pms.security.SecurityUtils;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

/**
 * @author valentyn_vakatsiienko
 * @since 12/20/13 5:23 PM
 */
@Service
public class AuthenticationService {
    @Inject
    private AuthenticationRepository authenticationRepository;
    @Inject
    private PasswordEncoder passwordEncoder;

    public boolean verify(Authentication authentication, String password) {
        return passwordEncoder.matches(password, authentication.getPassword());
    }

    public Authentication create(Authentication authentication, Hotel hotel) {
        authentication.setPassword(passwordEncoder.encode(authentication.getPassword()));

        authentication.setHotel(hotel);
        return authenticationRepository.save(authentication);
    }

    public void setPassword(Authentication authentication, PasswordDto dto) {
        if (!verify(authentication, dto.getOldPassword())) {
            throw new AuthenticationException(AuthenticationException.INVALID_OLD_PASSWORD);
        }

        String encoded = passwordEncoder.encode(dto.getNewPassword());
        authenticationRepository.setPassword(encoded, authentication.getId());

        authentication.setPassword(encoded);
        SecurityUtils.getUserDetails().setAuthentication(authentication);
    }

    public void setLang(User user, String lang) {
        Authentication authentication = authenticationRepository.findByUsernameAndHotelTenantId(user.getUsername(), SecurityUtils.getCurrentTenantId());
        authentication.setLanguage(lang);
        authenticationRepository.save(authentication);
    }

    @Getter
    public static class PasswordDto {
        @NotNull
        private String oldPassword;
        @NotNull
        private String newPassword;
    }
}