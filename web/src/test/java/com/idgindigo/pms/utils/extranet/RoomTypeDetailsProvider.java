package com.idgindigo.pms.utils.extranet;

import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeDetails;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.extranet.RoomTypeDetailsRepository;
import com.idgindigo.pms.utils.EntityProvider;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 3/21/14 3:20 PM
 */
@Component
public class RoomTypeDetailsProvider extends EntityProvider<RoomTypeDetails> {
    @Inject
    private RoomTypeDetailsRepository repository;

    @Override
    public RoomTypeDetails createAndFill() {
        return new RoomTypeDetails();
    }

    @Override
    public BaseRepository<RoomTypeDetails> getRepository() {
        return repository;
    }
}
