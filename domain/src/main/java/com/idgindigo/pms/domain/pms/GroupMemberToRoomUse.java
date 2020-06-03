package com.idgindigo.pms.domain.pms;

import com.idgindigo.pms.domain.BaseEntity;
import com.idgindigo.pms.domain.extranet.GroupMember;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * @author valentyn_vakatsiienko
 * @since 3/11/14 11:34 AM
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupMemberToRoomUse extends BaseEntity {
    public static final String GROUP_MEMBER_TO_ROOM_USE = "groupMemberToRoomUse";

    @NotNull
    @ManyToOne
    @JoinColumn(updatable = false)
    private GroupMember groupMember;
    @NotNull
    @ManyToOne
    @JoinColumn(updatable = false)
    private RoomUse roomUse;
}
