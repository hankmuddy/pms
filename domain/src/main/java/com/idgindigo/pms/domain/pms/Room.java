package com.idgindigo.pms.domain.pms;

import com.idgindigo.pms.domain.ApprovableEntity;
import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.utils.SmartToStringBuilder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import static com.idgindigo.pms.domain.extranet.roomtype.RoomType.ROOM_TYPE;
import static com.idgindigo.pms.domain.pms.Accommodation.ACCOMMODATION;

/**
 * @author vomel
 * @since 01.11.13 17:01
 */
@Entity
@Setter
@Getter
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"number", "floor", "accommodation_id"}))
public class Room extends ApprovableEntity {
    public static final String ROOM = "room";

    private String number;
    private Integer floor;
    @ManyToOne(optional = false)
    private Accommodation accommodation;
    @ManyToOne(optional = false)
    private RoomType roomType;
    @Column(updatable = false)
    private int position;

    @Override
    public String toString() {
        return new SmartToStringBuilder(this)
                .appendSuper(super.toString())
                .append("number", number)
                .append(ROOM_TYPE, roomType)
                .append(ACCOMMODATION, accommodation)
                .toString();
    }

}
