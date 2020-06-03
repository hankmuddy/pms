package com.idgindigo.pms.utils;

import com.idgindigo.pms.logins.domain.HotelInfo;
import com.idgindigo.pms.logins.repository.HotelInfoRepository;
import com.idgindigo.pms.repository.BaseRepository;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 1/23/14 6:15 PM
 */
@Component
public class HotelInfoProvider extends EntityProvider<HotelInfo> {
    @Inject
    private HotelInfoRepository repository;

    @Override
    public HotelInfo createAndFill() {
        HotelInfo info = new HotelInfo();
        info.setCheckIn(720);
        info.setCheckOut(540);
        info.setCountry("UA");
        info.setCity(randomAlphabeticString());
        info.setName(randomAlphabeticString());
        info.setTimeZone("UTC");
        info.setOfficialAddress(randomAlphabeticString());
        info.setCurrency("UAH");
        return info;
    }

    @Override
    public BaseRepository<HotelInfo> getRepository() {
        return repository;
    }
}
