package com.idgindigo.pms.domain.extranet.roomtype;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.idgindigo.pms.domain.BaseEntity;
import com.idgindigo.pms.domain.extranet.WubookRoomValue;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

/**
 * @author valentyn_vakatsiienko
 * @since 2/20/14 9:58 AM
 */
@Entity
@Getter
@Setter
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"room_id", "date"}))
public class BaseRoomValue extends WubookRoomValue {
    public static final String BASE_ROOM_VALUE = "baseRoomValue";

    @ManyToOne
    @JoinColumn(updatable = false)
    @NotNull
    private BaseRoom room;
    @Column(updatable = false)
    @NotNull
    private LocalDate date;

    @JsonProperty("endDate")
    @JsonView(CalendarView.class)
    public LocalDate getEndDate() {
        return date.plusDays(1);
    }

    public interface CalendarView extends BaseEntity.BaseView {
    }
}
