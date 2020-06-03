package com.idgindigo.pms.logins.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.idgindigo.pms.domain.BaseEntity;
import com.idgindigo.pms.domain.BaseIdentifiable;
import com.idgindigo.pms.utils.SmartToStringBuilder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;
import org.joda.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author valentyn_vakatsiienko
 * @since 11/18/13 12:01 PM
 */
@Entity
@Getter
@Setter
@FilterDefs({
        @FilterDef(name = Hotel.FILTER_BY_SUPERVISOR, parameters = @ParamDef(name = BaseIdentifiable.ID, type = "long")),
        @FilterDef(name = Hotel.FILTER_BY_MANAGER, parameters = @ParamDef(name = BaseIdentifiable.ID, type = "long")),
})
@Filters({
        @Filter(name = Hotel.FILTER_BY_SUPERVISOR,
                condition = "supervisor_id = :id"),
        @Filter(name = Hotel.FILTER_BY_MANAGER,
                condition = "manager_id = :id"),
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Hotel extends BaseIdentifiable {
    public static final String HOTEL = "hotel";
    public static final String FILTER_BY_SUPERVISOR = HOTEL + "bySupervisor";
    public static final String FILTER_BY_MANAGER = HOTEL + "byManager";

    @Column(updatable = false)
    @NotNull
//    @Getter(onMethod = @_({@JsonProperty("hotelId")}))
//    @Setter(onMethod = @_({@JsonIgnore}))
//    @JsonIgnore
    @JsonProperty("hotelId")
    private String tenantId;

    @Column(unique = true)
    @Pattern(regexp = "[0-9]{10}")
    private String lcode;
    private String wuName;
    private String wuPass;
    @Column(nullable = false, columnDefinition = "varchar(32) DEFAULT 'INIT'")
    @Enumerated(EnumType.STRING)
    private WubookImportStatus importStatus = WubookImportStatus.INIT;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(updatable = false)
    @NotNull
    private HotelInfo info;

    @ManyToOne
    @JsonView(HotelView.class)
    private Manager manager;
    @ManyToOne
    @JsonView(HotelView.class)
    private ManagerSupervisor supervisor;

    private Integer maxRooms;
    @Column(updatable = false)
    private boolean blocked;
    @Column(updatable = false)
    private LocalDate paidUntil;

    public interface HotelView extends BaseEntity.BaseView {
    }

    @Column(updatable = false)
    @JsonIgnore
    @Getter(onMethod = @_({@JsonIgnore}))
    @Setter(onMethod = @_({@JsonProperty}))
    private boolean extranet;

    public enum WubookImportStatus {
        INIT, DATA_IMPORTED, RESERVATIONS_IMPORTED
    }

    @Override
    public String toString() {
        return new SmartToStringBuilder(this, SmartToStringBuilder.SIMPLE_STYLE)
                .append("id", getId())
                .append("info", info)
                .append("tenantId", tenantId)
                .append("lcode", lcode)
                .append("importStatus", importStatus)
                .toString();
    }
}