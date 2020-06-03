package com.idgindigo.pms.domain.extranet.roomtype;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.idgindigo.pms.domain.pms.Room;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.REMOVE;

/**
 * @author vomel
 * @since 01.11.13 15:02
 */
@Entity
@DiscriminatorValue(RoomType.ROOM_TYPE)
@Getter
@Setter
@NoArgsConstructor
public class RoomType extends BaseRoom {
    public static final String ROOM_TYPE = "roomType";

    @Min(0)
    private Long adultBedPrice;
    @Min(0)
    private Long childBedPrice;
    @OneToMany(mappedBy = ROOM_TYPE)
    @JsonIgnore
    private Set<Room> rooms;
    @Column(updatable = false)
    @Min(0)
    @JsonIgnore
    @Getter(onMethod = @_({@JsonProperty}))
    @Setter(onMethod = @_({@JsonIgnore}))
    private Integer otaRooms = 0;//Quota
    private Integer area;
    @Enumerated(EnumType.STRING)
    private Smoking smoking;
    @OneToOne(fetch = FetchType.LAZY, cascade = ALL, orphanRemoval = true)
    @JsonView(DetailedView.class)
    private RoomTypeDetails details;

    @JsonView(TreeView.class)
    @JsonProperty("content")
    @OneToMany(mappedBy = "roomType", cascade = REMOVE)
    private List<VirtualRoom> virtualRooms;

    @Column(length = 1000)
    private String description;

    @JsonProperty("otaRoomsCount")
    @JsonView(TreeView.class)
    public Integer getOtaRoomsCount() {
        return getOtaRooms();
    }

    @JsonProperty("roomTypeId")
    @JsonView(TreeView.class)
    public Long getRoomTypeId() {
        return getId();
    }

    @Override
    public String getType() {
        return ROOM_TYPE;
    }

    @Override
    @JsonIgnore
    public RoomType roomType() {
        return this;
    }

    public RoomType(Long id) {
        setId(id);
    }

    private enum Smoking {
        SMOKING, NON_SMOKING, BOTH
    }

    public interface DetailedView extends SoloView {
    }

    public interface TreeView extends BaseView {
    }

}
