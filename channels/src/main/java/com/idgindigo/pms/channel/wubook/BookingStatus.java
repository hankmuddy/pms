package com.idgindigo.pms.channel.wubook;

/**
 * @author vomel
 * @since 30.01.14 14:18
 */
public interface BookingStatus {
    int CONFIRMED = 1;//	 Confirmed
    int WAITING = 2;//	 Waiting for approval
    int REFUSED = 3;//	 Refused
    int ACCEPTED = 4;//	 Accepted
    int DELETED = 5;//	 Deleted
    int DELETED_PENALTY = 6;//	 Deleted with penalty
}
