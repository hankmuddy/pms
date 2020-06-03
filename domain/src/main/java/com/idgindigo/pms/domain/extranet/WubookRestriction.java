package com.idgindigo.pms.domain.extranet;

import com.idgindigo.pms.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;

/**
 * @author valentyn_vakatsiienko
 * @since 5/5/14 3:43 PM
 */
@Getter
@Setter
@MappedSuperclass
public class WubookRestriction extends BaseEntity {
    private Boolean closedToDeparture = false;
    private Integer minStay;
    private Integer minStayArrival;
    private Integer maxStay;
    //closed can be 0, 1 or 2. If 0, room is open. If 1, room is closed. If 2, room is closed to check-in (close to arrival)
    private RoomClosed closed = RoomClosed.OPEN;

}
