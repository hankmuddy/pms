package com.idgindigo.pms.web.controller.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.idgindigo.pms.domain.User;
import com.idgindigo.pms.logins.domain.Activation;
import com.idgindigo.pms.logins.domain.HotelUser;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @author valentyn_vakatsiienko
 * @since 1/29/14 1:02 PM
 */
@Getter
@Setter
public class HotelUserDto {

    private HotelUser authentication;
    private String email;
    @NotNull
    private String phone;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    private String position;

    @JsonIgnore
    public Activation getActivation() {
        Activation activation = new Activation();
        activation.setEmail(email);
        activation.setFirstName(firstName);
        activation.setLastName(lastName);
        activation.setPhone(phone);
        activation.setPosition(position);
        return activation;
    }

    @JsonIgnore
    public User getUser() {
        User user = new User();
        user.setUsername(authentication.getUsername());
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhone(phone);
        user.setPosition(position);
        return user;
    }

    public static HotelUserDto fromActivation(Activation activation) {
        HotelUserDto dto = new HotelUserDto();
        dto.setAuthentication(activation.getAuthentication());
        dto.setEmail(activation.getEmail());
        dto.setFirstName(activation.getFirstName());
        dto.setLastName(activation.getLastName());
        dto.setPhone(activation.getPhone());
        dto.setPosition(activation.getPosition());
        return dto;
    }
}
