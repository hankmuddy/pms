package com.idgindigo.pms.domain.extranet;

import com.idgindigo.pms.domain.BaseEntity;
import com.idgindigo.pms.utils.SmartToStringBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Check;
import org.joda.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.NotNull;

/**
 * @author valentyn_vakatsiienko
 * @since 11/28/13 2:52 PM
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@Check(constraints = "(startDate < endDate) AND " +
        "(type != 'ROOM_USE' OR source IN ('FRONT_DESK', 'BOOKING_BUTTON') OR rcode IS NOT NULL) AND" +
        "(type != 'ROOM_USE' OR source NOT IN ('BOOKING_BUTTON') OR acode IS NOT NULL)"
)
@Getter
@Setter
public abstract class BaseRoomUse extends BaseEntity {
    public static final String BASE_ROOM_USE = "baseRoomUse";

    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", insertable = false, updatable = false)
    private UseType type;

    public enum UseType {
        REPAIR, ROOM_USE, EXTRANET_ROOM_USE
    }

    @Override
    public String toString() {
        return new SmartToStringBuilder(this)
                .append("startDate", startDate)
                .append("endDate", endDate)
                .toString();
    }
}
