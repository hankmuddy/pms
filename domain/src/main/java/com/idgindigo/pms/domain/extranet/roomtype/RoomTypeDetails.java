package com.idgindigo.pms.domain.extranet.roomtype;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.idgindigo.pms.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

/**
 * @author valentyn_vakatsiienko
 * @since 3/21/14 1:47 PM
 */
@Entity
@Getter
@Setter
public class RoomTypeDetails extends BaseEntity {
    public static final String ROOM_TYPE_DETAILS = "roomTypeDetails";
    public static final String X_LARGE_DOUBLE = "xLargeDouble";

    private int single;
    private int standardDouble;
    private int largeDouble;
    @JsonIgnore
    @Getter(onMethod = @_({@JsonProperty(X_LARGE_DOUBLE)}))
    @Setter(onMethod = @_({@JsonProperty(X_LARGE_DOUBLE)}))
    private int xLargeDouble;
    private int bunk;
    private int sofaBed;
    private int futon;
}
