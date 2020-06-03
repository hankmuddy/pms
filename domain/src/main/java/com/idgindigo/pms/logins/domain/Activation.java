package com.idgindigo.pms.logins.domain;

import com.idgindigo.pms.domain.BaseIdentifiable;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

/**
 * @author valentyn_vakatsiienko
 * @since 11/19/13 10:40 AM
 */
@Entity
@Getter
@Setter
public class Activation extends BaseIdentifiable {
    @OneToOne
    @NotNull
    private HotelUser authentication;

    private String email;
    @NotNull
    private String phone;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    private String position;

    @NotNull
    @Column(name = "activationKey", unique = true, updatable = false)
    private String key;
}
