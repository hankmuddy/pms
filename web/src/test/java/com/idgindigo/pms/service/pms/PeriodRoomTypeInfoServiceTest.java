package com.idgindigo.pms.service.pms;

import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.domain.pms.PeriodRoomTypeInfo;
import com.idgindigo.pms.service.ServiceTest;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.extranet.RoomTypeProvider;
import com.idgindigo.pms.utils.pms.PeriodRoomTypeInfoProvider;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static org.testng.Assert.assertEquals;

/**
 * @author valentyn_vakatsiienko
 * @since 2/25/14 6:07 PM
 */
public class PeriodRoomTypeInfoServiceTest extends ServiceTest {
    @Inject
    private PeriodRoomTypeInfoService periodRoomTypeInfoService;
    @Inject
    private PeriodRoomTypeInfoProvider periodRoomTypeInfoProvider;
    @Inject
    private RoomTypeProvider roomTypeProvider;

    @Test
    public void testLivingAmountResolving() {
        //resolve for room type without info
        final long defaultAmount = 100;
        final long overriddenAmount = 200;
        final LocalDate date = LocalDate.now(DateTimeZone.UTC);

        final RoomType roomType = createRoomType(defaultAmount);
        long actual = periodRoomTypeInfoService.getLivingAmount(roomType, date);
        assertEquals(actual, defaultAmount);

        //create info and resolve with it
        createInfo(overriddenAmount, date, roomType);

        actual = periodRoomTypeInfoService.getLivingAmount(roomType, date);
        assertEquals(actual, overriddenAmount);
    }

    @Test
    public void testProperInfoResolving() {
        final long defaultAmount = 100;
        final long firstOverriddenAmount = 200;
        final long secondOverriddenAmount = 300;
        final LocalDate first = LocalDate.now(DateTimeZone.UTC);
        final LocalDate second = first.plusDays(5);

        RoomType roomType = createRoomType(defaultAmount);
        createInfo(firstOverriddenAmount, first, roomType);
        createInfo(secondOverriddenAmount, second, roomType);

        long actual = periodRoomTypeInfoService.getLivingAmount(roomType, first);
        assertEquals(actual, firstOverriddenAmount);
        actual = periodRoomTypeInfoService.getLivingAmount(roomType, second);
        assertEquals(actual, secondOverriddenAmount);
    }

    private RoomType createRoomType(final long defaultAmount) {
        return roomTypeProvider.getPersistentEntity(new Visitor<RoomType>() {
            @Override
            public void visit(RoomType entity) {
                entity.setDefaultPrice(defaultAmount);
            }
        });
    }

    private void createInfo(final long overriddenAmount, final LocalDate date, final RoomType roomType) {
        periodRoomTypeInfoProvider.getPersistentEntity(new Visitor<PeriodRoomTypeInfo>() {
            @Override
            public void visit(PeriodRoomTypeInfo entity) {
                entity.setDateStart(date);
                entity.setRoomType(roomType);
                entity.setLivingPrice(overriddenAmount);
            }
        });
    }
}
