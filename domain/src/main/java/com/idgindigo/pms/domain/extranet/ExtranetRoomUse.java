package com.idgindigo.pms.domain.extranet;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author valentyn_vakatsiienko
 * @since 5/28/14 12:58 PM
 */
@Entity
@DiscriminatorValue("EXTRANET_ROOM_USE")
@Getter
@Setter
public class ExtranetRoomUse extends BaseGroupRoomUse {
    public static final String EXTRANET_ROOM_USE = "extranetRoomUse";

    @Override
    public UseType getType() {
        return UseType.EXTRANET_ROOM_USE;
    }
}
