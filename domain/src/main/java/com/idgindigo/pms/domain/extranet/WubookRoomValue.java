package com.idgindigo.pms.domain.extranet;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;

/**
 * @author valentyn_vakatsiienko
 * @since 4/9/14 11:15 AM
 */
@Getter
@Setter
@MappedSuperclass
public class WubookRoomValue extends WubookRestriction {
    private Boolean otaAllowed = true;
}
