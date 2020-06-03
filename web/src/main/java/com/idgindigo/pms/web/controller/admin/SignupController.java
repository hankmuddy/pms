package com.idgindigo.pms.web.controller.admin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.idgindigo.pms.domain.Role;
import com.idgindigo.pms.logins.domain.Activation;
import com.idgindigo.pms.logins.domain.Hotel;
import com.idgindigo.pms.logins.domain.HotelUser;
import com.idgindigo.pms.logins.repository.ActivationRepository;
import com.idgindigo.pms.logins.repository.AuthenticationRepository;
import com.idgindigo.pms.logins.repository.HotelRepository;
import com.idgindigo.pms.service.admin.AuthenticationService;
import com.idgindigo.pms.service.admin.HotelService;
import com.idgindigo.pms.service.extranet.MailService;
import com.idgindigo.pms.service.multitenancy.TenantService;
import com.idgindigo.pms.web.utils.MailHelper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.LocalDate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Collections;

/**
 * @since 11/14/13 12:35 PM
 */
@Controller
public class SignupController {
    @Inject
    private AuthenticationService authenticationService;
    @Inject
    private TenantService tenantService;
    @Inject
    private HotelService hotelService;
    @Inject
    private ActivationRepository activationRepository;
    @Inject
    private SecureRandom secureRandom;
    @Inject
    private MailService mailService;
    @Inject
    private HotelRepository hotelRepository;
    @Inject
    private AuthenticationRepository authenticationRepository;
    @Inject
    private MailHelper mailHelper;

    @RequestMapping(value = "signup", method = RequestMethod.POST)
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@RequestBody @Valid UserSignupDto dto, HttpServletRequest request) {
        final HotelUser authentication = dto.user.getAuthentication();
        dto.hotel.setActive(false);
        authentication.setActive(false);
        Hotel hotel = hotelService.save(dto.hotel);

        authenticationService.create(authentication, hotel);

        Activation activation = dto.getUser().getActivation();
        activation.setAuthentication(authentication);
        do {
            activation.setKey(new BigInteger(260, secureRandom).toString(32));
        } while (activationRepository.findByKey(activation.getKey()) != null);
        activationRepository.save(activation);

        mailService.sendMail("support@pmscloud.com", "Новая заявка на подключение системы", mailHelper.getActivationMailBody(hotel, getUrlPrefix(request) + "/activate/" + activation.getKey(), getUrlPrefix(request) + "/admin#hotel/view/" + hotel.getId()), true);
    }

    private String getUrlPrefix(HttpServletRequest request) {
        return request.getRequestURL().toString().replace(request.getRequestURI(), "") + request.getContextPath();
    }

    @RequestMapping("activate/{key}")
    @Transactional
    @ResponseBody
    public void activate(@PathVariable("key") String key) {
        Activation activation = activationRepository.findByKey(key);
        if (activation == null) {
            throw new EntityNotFoundException();
        }
        HotelUser authentication = activation.getAuthentication();

        authentication.setActive(true);
        authentication.getHotel().setActive(true);
        authenticationRepository.save(authentication);
        hotelRepository.save(authentication.getHotel());
        hotelRepository.setPaidUntil(authentication.getHotel().getId(), LocalDate.now().plusDays(15));

        tenantService.initTenant(authentication.getHotel().getTenantId());
        hotelService.initHotel(HotelUserDto.fromActivation(activation), Collections.<Role>emptyList());

        activationRepository.delete(activation);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserSignupDto {

        @NotNull
        private HotelUserDto user;
        @NotNull
        private Hotel hotel;

    }
}
