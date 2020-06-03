package com.idgindigo.pms.utils.extranet;

import com.idgindigo.pms.domain.extranet.ExtranetRoomUse;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.extranet.roomuse.ExtranetRoomUseRepository;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 6/19/14 4:52 PM
 */
@Component
public class ExtranetRoomUseProvider extends GenericRoomUseProvider<ExtranetRoomUse> {
    @Inject
    private ExtranetRoomUseRepository repository;

    @Override
    protected ExtranetRoomUse create() {
        return new ExtranetRoomUse();
    }

    @Override
    public BaseRepository<ExtranetRoomUse> getRepository() {
        return repository;
    }
}
