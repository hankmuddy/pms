package com.idgindigo.pms.logins.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.idgindigo.pms.domain.BaseIdentifiable;
import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeFacility;
import com.idgindigo.pms.utils.SmartToStringBuilder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.validation.constraints.NotNull;

/**
 * @author vomel
 * @since 18.02.14 15:56
 */
@Entity
@Getter
@Setter
@Inheritance
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = HotelFacility.class, name = Facility.HOTEL),
        @JsonSubTypes.Type(value = RoomTypeFacility.class, name = Facility.ROOM_TYPE)})
public abstract class Facility extends BaseIdentifiable {
    public static final String HOTEL = "HOTEL";
    public static final String ROOM_TYPE = "ROOM_TYPE";
    @NotNull
    @Column(unique = true, insertable = false, updatable = false, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", insertable = false, updatable = false, nullable = false)
    private FacilityType type;

    @JsonProperty("type")
    protected abstract FacilityType getType();

    public enum FacilityType {HOTEL, ROOM_TYPE}

    @Override
    public String toString() {
        return new SmartToStringBuilder(this)
                .appendSuper(super.toString())
                .append("name", name)
                .toString();
    }
}
