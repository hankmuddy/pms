package com.idgindigo.pms.domain.pms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.idgindigo.pms.domain.User;
import com.idgindigo.pms.domain.extranet.BaseGroupRoomUse;
import com.idgindigo.pms.domain.extranet.BaseRoomUse;
import com.idgindigo.pms.utils.SmartToStringBuilder;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Status.BOOKING_FREE;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Status.BOOKING_WARRANTY;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Status.NOT_ARRIVED;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Status.REFUSE;

/**
 * @author valentyn_vakatsiienko
 * @since 11/19/13 1:01 PM
 */
@Entity
@DiscriminatorValue("ROOM_USE")
@Getter
@Setter
public class RoomUse extends BaseGroupRoomUse implements PmsRoomUse {
    public static final String ROOM_USE = "roomUse";
    @NotNull
    @ManyToOne
    @JoinColumn(updatable = false)
    private Room room;
    @Transient
    @JsonIgnore
    @Getter(onMethod = @_({@JsonIgnore}))
    @Setter(onMethod = @_({@JsonProperty}))
    private boolean customerPays;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = false)
    @JsonIgnore
    private RoomUse movedFrom;
    @Column(updatable = false)
    private LocalDateTime checkInTime;
    @Column(updatable = false)
    private LocalDateTime checkOutTime;
    @JsonIgnore
    @Getter(onMethod = @_({@JsonProperty}))
    @Setter(onMethod = @_({@JsonIgnore}))
    private long totalPaid;

    @JsonProperty
    public Long getMovedFromId() {
        return isMoved() ? movedFrom.getId() : null;
    }

    public boolean isMoved() {
        return movedFrom != null;
    }

    public boolean isBooking() {
        return status == BOOKING_FREE || status == BOOKING_WARRANTY;
    }

    public boolean isRefused() {
        return status == NOT_ARRIVED || status == REFUSE;
    }

    @Override
    public BaseRoomUse.UseType getType() {
        return BaseRoomUse.UseType.ROOM_USE;
    }

    @Override
    public String toString() {
        return new SmartToStringBuilder(this, SmartToStringBuilder.SHORT_PREFIX_STYLE)
                .appendSuper(super.toString())
                .append("rcode", rcode)
                .append("group", customerGroup)
                .append("vRId", baseRoom.getId())
                .append("roomId", getRoom() != null ? getRoom().getId() : null)
                .append("status", status)
                .append("source", source)
                .toString();
    }

    @Override
    @JsonView(SoloView.class)
    public User getCreatedBy() {
        return super.getCreatedBy();
    }

    @Override
    @JsonView(SoloView.class)
    public User getUpdatedBy() {
        return super.getUpdatedBy();
    }
}
