package com.idgindigo.pms.service.pms;

import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.repository.pms.PeriodRoomTypeInfoRepository;
import org.joda.time.LocalDate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 1/8/14 12:57 PM
 */
@Service
public class PeriodRoomTypeInfoService {
    private static final Pageable PAGE = new PageRequest(0, 1);
    @Inject
    private PeriodRoomTypeInfoRepository seasonRoomInfoRepository;

    public long getLivingAmount(RoomType roomType, LocalDate date) {
        List<Long> prices = seasonRoomInfoRepository.getLivingPrices(roomType, date, PAGE);
        long livingAmount;
        if (!prices.isEmpty()) {
            livingAmount = prices.get(0);
        } else {
            livingAmount = roomType.getDefaultPrice();
        }
        return livingAmount;
    }
}
