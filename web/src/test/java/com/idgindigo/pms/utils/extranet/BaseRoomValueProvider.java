package com.idgindigo.pms.utils.extranet;

import com.idgindigo.pms.domain.extranet.roomtype.BaseRoomValue;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.extranet.BaseRoomValueRepository;
import com.idgindigo.pms.utils.EntityProvider;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 2/20/14 11:20 AM
 */
@Component
public class BaseRoomValueProvider extends EntityProvider<BaseRoomValue> {
    @Inject
    private BaseRoomValueRepository repository;
    @Inject
    private VirtualRoomProvider virtualRoomProvider;

    @Override
    public BaseRoomValue createAndFill() {
        BaseRoomValue value = new BaseRoomValue();
        value.setRoom(virtualRoomProvider.getPersistentEntity());
        value.setDate(LocalDate.now(DateTimeZone.UTC));
        return value;
    }

    @Override
    public BaseRepository<BaseRoomValue> getRepository() {
        return repository;
    }
}
