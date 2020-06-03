package com.idgindigo.pms.utils.pms;

import com.idgindigo.pms.domain.pms.Accommodation;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.pms.AccommodationRepository;
import com.idgindigo.pms.utils.ApprovableEntityProvider;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author vomel
 * @since 31.10.13 17:33
 */
@Component
public class AccommodationProvider extends ApprovableEntityProvider<Accommodation> {
    @Inject
    private AccommodationRepository repository;

    @Override
    public Accommodation createAndFill() {
        Accommodation accommodation = new Accommodation();
        accommodation.setName(randomString());
        accommodation.setShortName(randomAlphabeticString());
        return accommodation;
    }

    @Override
    public BaseRepository<Accommodation> getRepository() {
        return repository;
    }

}
