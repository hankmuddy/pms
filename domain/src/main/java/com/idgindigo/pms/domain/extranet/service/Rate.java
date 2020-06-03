package com.idgindigo.pms.domain.extranet.service;

import com.idgindigo.pms.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

/**
 * @author valentyn_vakatsiienko
 * @since 11/26/13 11:14 AM
 */
//@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"date", "calendarService_id"})
})
@Getter
@Setter
public abstract class Rate<T extends CalendarService> extends BaseEntity {
    @Column(updatable = false)
    @NotNull
    private LocalDate date;
    private Long price;
    @ManyToOne
    @JoinColumn(updatable = false)
    @NotNull
    protected T calendarService;

    /*@JsonProperty("endDate")
    @JsonView(CalendarView.class)
    public LocalDate getEndDate() {
        return date.plusDays(1);
    }

    public interface CalendarView extends BaseView {
    }*/
}