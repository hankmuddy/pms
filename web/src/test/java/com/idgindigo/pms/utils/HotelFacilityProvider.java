package com.idgindigo.pms.utils;

import com.idgindigo.pms.logins.domain.HotelFacility;
import com.idgindigo.pms.logins.repository.HotelFacilityRepository;
import com.idgindigo.pms.repository.BaseRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.inject.Inject;


/**
 * @author vomel
 * @since 18.02.14 16:20
 */
@Component
public class HotelFacilityProvider extends EntityProvider<HotelFacility> {
    @Inject
    private HotelFacilityRepository repository;
    @Inject
    private JdbcTemplate jdbcTemplate;

    private static long id = 100;

    @Override
    public HotelFacility createAndFill() {
        String sql = " INSERT INTO facility (type, id, active, createddate, updateddate, name) VALUES " +
                "    ('HOTEL', " + ++id + ", TRUE, NULL, NULL, 'name: " + randomString() + "');";
        jdbcTemplate.execute(sql);
        return repository.findOne(id);
    }

    @Override
    public BaseRepository<HotelFacility> getRepository() {
        return repository;
    }
}
