package com.idgindigo.pms.logins.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.idgindigo.pms.constraint.Language;
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
import org.joda.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

/**
 * @author valentyn_vakatsiienko
 * @since 11/6/13 5:25 PM
 */
@Entity
@Setter
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "userType", discriminatorType = DiscriminatorType.STRING)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "userType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = HotelUser.class, name = HotelUser.USER),
        @JsonSubTypes.Type(value = Admin.class, name = Admin.ADMIN),
        @JsonSubTypes.Type(value = Manager.class, name = Manager.MANAGER),
        @JsonSubTypes.Type(value = ManagerSupervisor.class, name = ManagerSupervisor.MANAGER_SUPERVISOR),
})
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"username", "hotel_id"}))
@FilterDefs({
        @FilterDef(name = Authentication.FILTER_BY_SUPERVISOR, parameters = @ParamDef(name = BaseIdentifiable.ID, type = "long")),
        @FilterDef(name = Authentication.FILTER_BY_MANAGER, parameters = @ParamDef(name = BaseIdentifiable.ID, type = "long")),
})
@Filters({
        @Filter(name = Authentication.FILTER_BY_SUPERVISOR,
                condition = "" +
                        " userType = '" + Manager.MANAGER + "' AND supervisor_id = :id " +
                        " OR " +
                        " userType = '" + HotelUser.USER + "' AND id IN (SELECT a.id FROM logins." + Authentication.AUTHENTICATION + " a INNER JOIN logins." + Hotel.HOTEL + " h ON a.hotel_id = h.id WHERE h.supervisor_id = :id)"),
        @Filter(name = Authentication.FILTER_BY_MANAGER,
                condition = "" +
                        " userType = '" + HotelUser.USER + "' AND id IN (SELECT a.id FROM logins." + Authentication.AUTHENTICATION + " a INNER JOIN logins." + Hotel.HOTEL + " h ON a.hotel_id = h.id WHERE h.manager_id = :id)"),
})
public abstract class Authentication extends BaseIdentifiable {
    public static final String AUTHENTICATION = "authentication";
    public static final String FILTER_BY_SUPERVISOR = AUTHENTICATION + "BySupervisor";
    public static final String FILTER_BY_MANAGER = AUTHENTICATION + "ByManager";

    @NotNull
    private String username;
    @Column(updatable = false)
    @NotNull
    @JsonIgnore
    @Getter(onMethod = @_({@JsonIgnore}))
    @Setter(onMethod = @_({@JsonProperty}))
    private String password;
    @ManyToOne
    @JsonView(AuthView.class)
    protected Hotel hotel;
    @Language
    private String language;

    @Column(name = "userType", insertable = false, updatable = false)
    @JsonProperty("userType")
    private String userType;

    @Transient
    @JsonProperty("online")
    private boolean online;

    private LocalDateTime lastLoggedIn;

    public abstract String getUserType();

    public interface AuthView extends BaseEntity.BaseView {
    }

    @Override
    public String toString() {
        return new SmartToStringBuilder(this)
                .appendSuper(super.toString())
                .append("username", getUsername())
                .append("userType", getUserType())
                .toString();
    }
}
