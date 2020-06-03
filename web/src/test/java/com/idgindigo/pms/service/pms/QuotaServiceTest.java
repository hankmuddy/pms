package com.idgindigo.pms.service.pms;

import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeValue;
import com.idgindigo.pms.repository.extranet.rate.RoomTypeValueRepository;
import com.idgindigo.pms.service.ServiceTest;
import com.idgindigo.pms.service.admin.SettingsService;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.extranet.RoomTypeProvider;
import com.idgindigo.pms.utils.extranet.RoomTypeValueProvider;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.List;


/**
 * @author valentyn_vakatsiienko
 * @since 1/29/14 10:53 AM
 */
public class QuotaServiceTest extends ServiceTest {

    @Inject
    private QuotaService service;
    @Inject
    private RoomTypeValueRepository roomTypeValueRepository;
    @Inject
    private RoomTypeValueProvider roomTypeValueProvider;
    @Inject
    private RoomTypeProvider roomTypeProvider;
    @Inject
    private SettingsService settingsService;

    @Test
    public void testQuotaUpdate() {
        RoomType roomType = roomTypeProvider.getPersistentEntity();
        LocalDate start = settingsService.getHotelDate();
        LocalDate end = start.plusDays(5);

        updateQuotaAndCheckValues(roomType, start, end);
    }

    @Test
    public void testRoomValuesCreation() {
        RoomType roomType = roomTypeProvider.getPersistentEntity();
        LocalDate start = settingsService.getHotelDate();
        LocalDate end = start.plusDays(10);

        createRoomTypeValue(roomType, start);
        createRoomTypeValue(roomType, start.plusDays(5));
        createRoomTypeValue(roomType, start.plusDays(6));

        updateQuotaAndCheckValues(roomType, start, end);
    }

    private void updateQuotaAndCheckValues(RoomType roomType, LocalDate start, LocalDate end) {
        service.updateQuota(start, end, roomType, -1, false);

        List<RoomTypeValue> values = roomTypeValueRepository.findByRoomTypeAndDateBetween(roomType, start, end);
        Assert.assertEquals(values.size(), Days.daysBetween(start, end).getDays());
        RoomTypeValue entity = values.get(0);
        entity.setActive(Boolean.FALSE);
        roomTypeValueRepository.saveAndFlush(entity);
        Assert.assertEquals(roomTypeValueRepository.findByRoomTypeAndDateBetween(roomType, start, end).size(), values.size() - 1);
        for (RoomTypeValue value : values) {
            int expectedQuota = roomType.getOtaRooms() - 1;
            Assert.assertEquals(value.getRoomsAvailable().intValue(), expectedQuota);
        }
    }

    private void createRoomTypeValue(final RoomType roomType, final LocalDate start) {
        roomTypeValueProvider.getPersistentEntity(new Visitor<RoomTypeValue>() {
            @Override
            public void visit(RoomTypeValue entity) {
                entity.setRoomType(roomType);
                entity.setRoomsAvailable(roomType.getOtaRooms());
                entity.setDate(start);
            }
        });
    }
}
