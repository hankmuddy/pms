package com.idgindigo.pms.logins.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author vomel
 * @since 18.02.14 16:15
 */
@Getter
@Setter
@Entity
@DiscriminatorValue(Facility.HOTEL)
public class HotelFacility extends Facility {

    @Override
    public FacilityType getType() {
        return FacilityType.HOTEL;
    }

}
