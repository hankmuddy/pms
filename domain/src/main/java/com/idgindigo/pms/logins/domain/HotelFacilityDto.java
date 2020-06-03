package com.idgindigo.pms.logins.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.idgindigo.pms.logins.domain.HotelToFacility.ChargeFree;

/**
 * @author vomel
 * @since 25.02.14 13:49
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HotelFacilityDto {
    private HotelFacility facility;
    private ChargeFree chargeFree;
}
