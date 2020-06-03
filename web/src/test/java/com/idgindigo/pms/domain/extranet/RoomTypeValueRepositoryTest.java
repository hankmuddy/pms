package com.idgindigo.pms.domain.extranet;

import com.idgindigo.pms.domain.JpaTests;
import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeQuota;
import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeValue;
import com.idgindigo.pms.repository.extranet.rate.RoomTypeValueRepository;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.extranet.RoomTypeValueProvider;
import org.joda.time.LocalDate;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author valentyn_vakatsiienko
 * @since 6/18/14 6:01 PM
 */
public class RoomTypeValueRepositoryTest extends JpaTests {
    @Inject
    private RoomTypeValueRepository repository;
    @Inject
    private RoomTypeValueProvider provider;

    @Test
    public void testGetQuota() {
        final LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(10);
        final Map<RoomType, Integer> byRoomType = getRoomTypes(start);
        List<RoomTypeQuota> res = repository.getRoomTypeQuota(start, end);
        for (Map.Entry<RoomType, Integer> entry : byRoomType.entrySet()) {
            int found = 0;
            for (RoomTypeQuota quota : res) {
                if (quota.getRoomType().equals(entry.getKey())) {
                    Assert.assertEquals(quota.getQuota(), entry.getValue().intValue());
                    found++;
                }
            }
            Assert.assertEquals(found, 1);
        }
    }

    private Map<RoomType, Integer> getRoomTypes(final LocalDate start) {
        Map<RoomType, Integer> res = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            final int min = EntityProvider.randomPositiveInt();
            final RoomTypeValue rtv = provider.getPersistentEntity(new Visitor<RoomTypeValue>() {
                @Override
                public void visit(RoomTypeValue entity) {
                    entity.setRoomsAvailable(min);
                    entity.setDate(start);
                }
            });
            final RoomType roomType = rtv.getRoomType();
            for (int j = min; j < min + 5; j++) {
                final int roomsAvailable = j;
                provider.getPersistentEntity(new Visitor<RoomTypeValue>() {
                    @Override
                    public void visit(RoomTypeValue entity) {
                        entity.setRoomType(roomType);
                        entity.setRoomsAvailable(roomsAvailable);
                        entity.setDate(start.plusDays(roomsAvailable));
                    }
                });
            }
            res.put(roomType, min);
        }
        return res;
    }
}
