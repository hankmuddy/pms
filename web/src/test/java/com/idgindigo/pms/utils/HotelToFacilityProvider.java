package com.idgindigo.pms.utils;

import com.idgindigo.pms.logins.domain.HotelToFacility;
import com.idgindigo.pms.logins.repository.HotelToFacilityRepository;
import com.idgindigo.pms.repository.BaseRepository;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

import static com.idgindigo.pms.logins.domain.HotelToFacility.ChargeFree;

/**
 * @author vomel
 * @since 21.02.14 17:36
 */
@Component
public class HotelToFacilityProvider extends EntityProvider<HotelToFacility> {
    @Inject
    private HotelToFacilityRepository repository;
    @Inject
    private HotelFacilityProvider hfProvider;
    @Inject
    private HotelProvider hProvider;

    @Override
    public HotelToFacility createAndFill() {
        HotelToFacility htf = new HotelToFacility(hProvider.getFirst(), hfProvider.getPersistentEntity(), ChargeFree.FREE);
        return htf;
    }

    @Override
    public void updateEntity(HotelToFacility entity) {
        entity.setFacility(hfProvider.getByIndex(1));
    }

    @Override
    public BaseRepository<HotelToFacility> getRepository() {
        return repository;
    }
}
