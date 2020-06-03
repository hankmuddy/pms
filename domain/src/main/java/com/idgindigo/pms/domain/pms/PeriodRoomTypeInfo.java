package com.idgindigo.pms.domain.pms;

import com.idgindigo.pms.domain.BaseEntity;
import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

/**
 * @author valentyn_vakatsiienko
 * @since 12/27/13 3:22 PM
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"dateStart", "roomType_id"}))
@Getter
@Setter
public class PeriodRoomTypeInfo extends BaseEntity {
    public static final String PERIOD_ROOM_TYPE_INFO = "periodRoomTypeInfo";

    @NotNull
    private LocalDate dateStart;
    @ManyToOne
    @JoinColumn(updatable = false)
    @NotNull
    private RoomType roomType;
    @NotNull
    private long livingPrice;
}