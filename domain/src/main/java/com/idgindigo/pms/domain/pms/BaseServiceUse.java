package com.idgindigo.pms.domain.pms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.idgindigo.pms.domain.ApprovableEntity;
import com.idgindigo.pms.domain.extranet.service.Service;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * @author valentyn_vakatsiienko
 * @since 1/8/14 3:33 PM
 */
@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = LivingUse.class, name = LivingUse.LIVING_USE),
        @JsonSubTypes.Type(value = SimpleServiceUse.class, name = SimpleServiceUse.SIMPLE_SERVICE_USE)})
public abstract class BaseServiceUse<T extends Service> extends ApprovableEntity {
    public static final String BASE_SERVICE_USE = "baseServiceUse";

    @ManyToOne
    @JoinColumn(updatable = false, nullable = false)
    @JsonView(ListView.class)
    private Bill bill;
    @ManyToOne
    @JoinColumn(updatable = false, nullable = false)
    private RoomUse roomUse;
    @NotNull
    private LocalDate date;
    @NotNull
    @ManyToOne(targetEntity = Service.class)
    @JoinColumn(updatable = false)
    private T service;
    @NotNull
    private Integer quantity;
    @Column(nullable = false)
    private Long total;//total with taxes and discount applied
    @Column(nullable = false)
    private Long rawTotal;//total without any taxes and discount
    private String description;
    @ManyToOne
    @JsonIgnore
//    @Getter(onMethod = @_({@JsonProperty}))
//    @Setter(onMethod = @_({@JsonIgnore}))
    private Refund refund;

    @JsonProperty("type")
    protected abstract String getType();

    @JsonProperty("refund")
    public boolean isRefunded() {
        return refund != null;
    }
}
