package com.idgindigo.pms.logins.domain;

import com.idgindigo.pms.domain.BaseIdentifiable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @author vomel
 * @since 21.02.14 17:01
 */
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"hotel_id", "facility_id"}))
public class HotelToFacility extends BaseIdentifiable {
    @ManyToOne(optional = false)
    private Hotel hotel;
    @ManyToOne(optional = false)
    private HotelFacility facility;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ChargeFree chargeFree;

    public enum ChargeFree {
        FREE, CHARGED, N_A
    }

}
