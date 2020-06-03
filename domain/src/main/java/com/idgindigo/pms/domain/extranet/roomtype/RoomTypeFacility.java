package com.idgindigo.pms.domain.extranet.roomtype;

import com.idgindigo.pms.logins.domain.Facility;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author vomel
 * @since 18.02.14 16:06
 */
@Entity
@DiscriminatorValue(Facility.ROOM_TYPE)
public class RoomTypeFacility extends Facility {
    @Override
    public FacilityType getType() {
        return FacilityType.ROOM_TYPE;
    }
}
