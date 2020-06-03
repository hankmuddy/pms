package com.idgindigo.pms.domain.extranet.roomtype;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.idgindigo.pms.domain.ApprovableEntity;
import com.idgindigo.pms.domain.extranet.WubookRoom;
import com.idgindigo.pms.utils.SmartToStringBuilder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author valentyn_vakatsiienko
 * @since 1/15/14 10:58 AM
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = RoomType.class, name = RoomType.ROOM_TYPE),
        @JsonSubTypes.Type(value = VirtualRoom.class, name = VirtualRoom.VIRTUAL_ROOM)})
@Getter
@Setter
public abstract class BaseRoom extends ApprovableEntity implements WubookRoom {
    public static final String BASE_ROOM = "baseRoom";

    @NotNull
    private String name;//for wubook
    @Column(unique = true, nullable = false, length = 4)
    private String shortname;//for wubook
    @Column(unique = true)
    private Long rid;//for wubook
    @NotNull
    @Min(value = 0, message = CONSTRAINT_VALUE_IS_LESS_THAN_MIN)
    private Integer adults = 0;
    @Min(value = 0, message = CONSTRAINT_VALUE_IS_LESS_THAN_MIN)
    private Integer children = 0;
    @Min(value = 0, message = CONSTRAINT_VALUE_IS_LESS_THAN_MIN)
    private Integer additional = 0;
    @NotNull
    @Min(value = 6, message = CONSTRAINT_VALUE_IS_LESS_THAN_MIN)//wubook accepts values from 6
    private Long defaultPrice;

    @Column(updatable = false)
    @JsonIgnore
    @Getter(onMethod = @_({@JsonProperty}))
    @Setter(onMethod = @_({@JsonIgnore}))
    private Boolean defaultBaseRoom = Boolean.FALSE;

    @Column(name = "type", insertable = false, updatable = false)
    @JsonProperty("type")
    private String type;

    public abstract RoomType roomType();

    @Override
    public String toString() {
        return new SmartToStringBuilder(this)
                .appendSuper(super.toString())
                .append("name", name)
                .append("shortname", shortname)
                .toString();
    }
}