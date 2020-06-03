package com.idgindigo.pms.domain.extranet.plan;

import com.idgindigo.pms.domain.extranet.RoomClosed;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author valentyn_vakatsiienko
 * @since 5/5/14 5:33 PM
 */
@Entity
@DiscriminatorValue(CompactRestriction.COMPACT_RESTRICTION)
@Getter
@Setter
public class CompactRestriction extends PlanRestriction {
    public static final String COMPACT_RESTRICTION = "compactRestriction";

    private Boolean closedToDeparture = false;
    private Integer minStay;
    private Integer minStayArrival;
    private Integer maxStay;
    //closed can be 0, 1 or 2. If 0, room is open. If 1, room is closed. If 2, room is closed to check-in (close to arrival)
    private RoomClosed closed = RoomClosed.OPEN;
}
