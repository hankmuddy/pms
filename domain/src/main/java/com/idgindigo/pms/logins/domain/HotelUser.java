package com.idgindigo.pms.logins.domain;

import com.idgindigo.pms.utils.SmartToStringBuilder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;

/**
 * @author valentyn_vakatsiienko
 * @since 11/18/13 11:24 AM
 */
@Entity
@DiscriminatorValue(HotelUser.USER)
@Setter
public class HotelUser extends Authentication {
    public static final String USER = "user";
    @NotNull
    @Getter
    @Enumerated(EnumType.STRING)
    private Status status = Status.NON_CONFIRMED;

    public enum Status {
        NON_CONFIRMED, CONFIRMED, ACTIVE, BLOCKED
    }

    @NotNull
    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    @Override
    public String getUserType() {
        return USER;
    }

    @Override
    public String toString() {
        return new SmartToStringBuilder(this)
                .appendSuper(super.toString())
                .append("status", status)
                .toString();
    }
}
