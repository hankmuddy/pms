package com.idgindigo.pms.domain.extranet.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.idgindigo.pms.domain.BaseEntity;
import com.idgindigo.pms.domain.pms.SimpleService;
import com.idgindigo.pms.price.PriceResolver;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * @author valentyn_vakatsiienko
 * @since 11/26/13 10:35 AM
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Living.class, name = Living.LIVING),
        @JsonSubTypes.Type(value = SimpleService.class, name = SimpleService.SIMPLE_SERVICE)})
@Getter
@Setter
public abstract class Service extends BaseEntity {
    public static final String SERVICE = "service";

    private boolean deprecated;
    @Column(updatable = false)
    @JsonIgnore
    @Getter(onMethod = @_({@JsonProperty}))
    @Setter(onMethod = @_({@JsonIgnore}))
    private boolean system;

    @JsonProperty("type")
    protected abstract String getType();

    @JsonIgnore
    public abstract Class<? extends PriceResolver> getPriceResolver();

}
