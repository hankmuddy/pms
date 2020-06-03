package com.idgindigo.pms.service.channels;

import com.idgindigo.pms.utils.SmartToStringBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author vomel
 * @since 01.02.14 23:32
 */
@Getter
@AllArgsConstructor
public class WubookRoomUses {
    private List<Map<String, Object>> confirmed = new ArrayList<>();
    private List<Map<String, Object>> refused = new ArrayList<>();

    @Override
    public String toString() {
        return new SmartToStringBuilder(this, SmartToStringBuilder.NO_FIELD_NAMES_STYLE)
                .append("refused", refused, true)
                .append("confirmed", confirmed, true)
                .toString();
    }
}
