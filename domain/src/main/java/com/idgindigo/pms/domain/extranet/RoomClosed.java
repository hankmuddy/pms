package com.idgindigo.pms.domain.extranet;

/**
 * @author valentyn_vakatsiienko
 * @since 5/5/14 5:36 PM
 */
public enum RoomClosed {
    OPEN(0), CLOSED(1), CLOSED_TO_CHECKIN(2);
    private final int value;

    RoomClosed(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
