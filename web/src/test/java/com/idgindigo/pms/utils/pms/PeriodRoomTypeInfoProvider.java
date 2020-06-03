package com.idgindigo.pms.utils.pms;

import com.idgindigo.pms.domain.pms.PeriodRoomTypeInfo;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.pms.PeriodRoomTypeInfoRepository;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.extranet.RoomTypeProvider;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 12/27/13 6:17 PM
 */
@Component
public class PeriodRoomTypeInfoProvider extends EntityProvider<PeriodRoomTypeInfo> {
    @Inject
    private PeriodRoomTypeInfoRepository repository;
    @Inject
    private RoomTypeProvider roomTypeProvider;

    @Override
    public PeriodRoomTypeInfo createAndFill() {
        PeriodRoomTypeInfo info = new PeriodRoomTypeInfo();
        info.setDateStart(LocalDate.now(DateTimeZone.UTC));
        info.setRoomType(roomTypeProvider.getPersistentEntity());
        info.setLivingPrice(randomPositiveLong());
        return info;
    }

    @Override
    public BaseRepository<PeriodRoomTypeInfo> getRepository() {
        return repository;
    }
}
