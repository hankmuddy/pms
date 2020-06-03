package com.idgindigo.pms.domain.extranet.roomtype;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * @author valentyn_vakatsiienko
 * @since 3/19/14 10:17 AM
 */
@Entity
@DiscriminatorValue(VirtualRoom.VIRTUAL_ROOM)
@Getter
@Setter
public class VirtualRoom extends BaseRoom {
    public static final String VIRTUAL_ROOM = "virtualRoom";

    @ManyToOne
    @NotNull
    @JsonView({SoloView.class, ListView.class})
    private RoomType roomType;

    /*
     *This is required for Extjs tree to be able to build... No other usage of it is intended
     */
    //Start
    @JsonProperty("leaf")
    @JsonView(RoomType.TreeView.class)
    public boolean isLeaf() {
        return true;
    }

    @JsonProperty("roomTypeId")
    @JsonView(RoomType.TreeView.class)
    public Long getRoomTypeId() {
        return roomType.getId();
    }
    //End

    @Override
    public String getType() {
        return VIRTUAL_ROOM;
    }

    @Override
    public RoomType roomType() {
        return roomType;
    }
}
