package com.idgindigo.pms.domain.extranet.roomtype;

import com.idgindigo.pms.domain.BaseIdentifiable;
import com.idgindigo.pms.utils.SmartToStringBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @author vomel
 * @since 19.02.14 00:00
 */
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"roomType_id", "facility_id"}))
public class RoomTypeToFacility extends BaseIdentifiable {
    @ManyToOne(optional = false)
    private RoomType roomType;
    @ManyToOne(optional = false)
    private RoomTypeFacility facility;

    @Override
    public String toString() {
        return new SmartToStringBuilder(this)
                .append("roomType", roomType.getId())
                .append("facility", facility.getId())
                .toString();
    }
}
