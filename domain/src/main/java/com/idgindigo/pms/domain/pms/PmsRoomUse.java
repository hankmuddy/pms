package com.idgindigo.pms.domain.pms;

import org.joda.time.LocalDate;

/**
 * @author valentyn_vakatsiienko
 * @since 3/17/14 12:20 PM
 */
public interface PmsRoomUse {
    Room getRoom();

    LocalDate getStartDate();

    LocalDate getEndDate();

}
