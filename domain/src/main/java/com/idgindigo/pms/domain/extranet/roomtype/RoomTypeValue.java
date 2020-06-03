package com.idgindigo.pms.domain.extranet.roomtype;

import com.idgindigo.pms.domain.BaseEntity;
import com.idgindigo.pms.utils.SmartToStringBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Check;
import org.joda.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.util.Comparator;

/**
 * @author valentyn_vakatsiienko
 * @since 11/11/13 4:32 PM
 * <p/>
 * This entity corresponds to Rate_Calendar table in rate_calendar.jpg
 */
@Entity
@Getter
@Setter
@Check(constraints = "roomsAvailable >= 0")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"roomType_id", "date"}))
public class RoomTypeValue extends BaseEntity {
    public static final Comparator<RoomTypeValue> DATE_COMPARATOR = new Comparator<RoomTypeValue>() {

        @Override
        public int compare(RoomTypeValue o1, RoomTypeValue o2) {
            return o1.getDate().isAfter(o2.getDate()) ? 1 : o1.getDate().isBefore(o2.getDate()) ? -1 : 0;
        }
    };
    public static final String ROOM_TYPE_VALUE = "roomTypeValue";

    @ManyToOne
    @JoinColumn(updatable = false)
    @NotNull
    private RoomType roomType;
    @Column(updatable = false)
    @NotNull
    private LocalDate date;
    @NotNull
    private Integer roomsAvailable;
    private Long adultBedPrice;
    private Long childBedPrice;

    @Override
    public String toString() {
        return new SmartToStringBuilder(this)
                .append("roomType", roomType)
                .append("date", date)
                .append("roomsAvailable", roomsAvailable)
                .toString();
    }
}