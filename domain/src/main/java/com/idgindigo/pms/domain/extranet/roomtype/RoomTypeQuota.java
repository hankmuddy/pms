package com.idgindigo.pms.domain.extranet.roomtype;

import com.idgindigo.pms.utils.SmartToStringBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author vomel
 * @since 13.03.14 13:27
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomTypeQuota {
    private RoomType roomType;
    private int quota;

    @Override
    public String toString() {
        return new SmartToStringBuilder(this)
                .append("roomType", roomType)
                .append("quota", quota)
                .toString();
    }
}
