package com.idgindigo.pms.utils.extranet;

import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeFacility;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.extranet.RoomTypeFacilityRepository;
import com.idgindigo.pms.utils.EntityProvider;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author vomel
 * @since 19.02.14 00:10
 */
@Component
public class RoomTypeFacilityProvider extends EntityProvider<RoomTypeFacility> {
    @Inject
    private RoomTypeFacilityRepository repository;

    @Inject
    private JdbcTemplate jdbcTemplate;

    private static long id = 2000;

    @Override
    public RoomTypeFacility createAndFill() {
        String sql = " INSERT INTO facility (type, id, active, createddate, updateddate, name) VALUES " +
                "    ('ROOM_TYPE', " + ++id + ", TRUE, NULL, NULL, 'name: " + randomString() + "');";
        jdbcTemplate.execute(sql);
        return repository.findOne(id);
    }

    @Override
    public BaseRepository<RoomTypeFacility> getRepository() {
        return repository;
    }
}
