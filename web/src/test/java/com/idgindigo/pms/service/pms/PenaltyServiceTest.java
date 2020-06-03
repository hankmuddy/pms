package com.idgindigo.pms.service.pms;

import com.idgindigo.pms.domain.extranet.CustomerGroup;
import com.idgindigo.pms.domain.pms.BaseServiceUse;
import com.idgindigo.pms.domain.pms.Bill;
import com.idgindigo.pms.domain.pms.LivingUse;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.domain.pms.SimpleService;
import com.idgindigo.pms.logins.domain.HotelInfo;
import com.idgindigo.pms.repository.extranet.service.SimpleServiceRepository;
import com.idgindigo.pms.repository.pms.BaseServiceUseRepository;
import com.idgindigo.pms.repository.pms.BillRepository;
import com.idgindigo.pms.repository.pms.LivingUseRepository;
import com.idgindigo.pms.service.ServiceTest;
import com.idgindigo.pms.service.admin.SettingsService;
import com.idgindigo.pms.utils.HotelInfoProvider;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.pms.BillProvider;
import com.idgindigo.pms.utils.pms.LivingUseProvider;
import com.idgindigo.pms.utils.pms.RoomUseProvider;
import mockit.NonStrictExpectations;
import org.joda.time.LocalDateTime;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static com.idgindigo.pms.service.pms.PenaltyService.HandlingType;
import static com.idgindigo.pms.service.pms.PenaltyService.HandlingType.EARLY;
import static com.idgindigo.pms.service.pms.PenaltyService.HandlingType.LATE;
import static mockit.Deencapsulation.invoke;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

/**
 * @author valentyn_vakatsiienko
 * @since 1/24/14 12:50 PM
 */
public class PenaltyServiceTest extends ServiceTest {
    public static final long PENALTY_AMOUNT = 100L;
    public static final int TWO = 840;
    public static final int TWELVE = 720;
    public static final int TEN = 600;
    private static final int EIGHT = 480;
    private static final int SIX = 360;
    @Inject
    private PenaltyService penaltyService;
    @Inject
    private LivingUseRepository livingUseRepository;
    @Inject
    private SimpleServiceRepository simpleServiceRepository;
    @Inject
    private BaseServiceUseRepository baseServiceUseRepository;
    @Inject
    private HotelInfoProvider hotelInfoProvider;
    @Inject
    private RoomUseProvider roomUseProvider;
    @Inject
    private LivingUseProvider livingUseProvider;
    @Inject
    private BillProvider billProvider;
    @Inject
    private BillRepository billRepository;
    @Inject
    private SettingsService settingsService;

    @Test(dataProvider = "testPenaltyHandling")
    public void testPenaltyWithinPeriodHandling(final RoomUse roomUse, final HotelInfo info, final HandlingType handlingType, final boolean canHandle) {
        final LivingUse livingUse = livingUseProvider.getPersistentEntity(new Visitor<LivingUse>() {
            @Override
            public void visit(final LivingUse livingUse) {
                livingUse.setBill(billProvider.getPersistentEntity(new Visitor<Bill>() {
                    @Override
                    public void visit(Bill bill) {
                        bill.setRoomUse(roomUse);
                        bill.setTotal(livingUse.getTotal());
                    }
                }));
            }
        });

        initMocks(info, handlingType, canHandle, livingUse, "canHandleWithinPeriod");

        Long billTotalBefore = livingUse.getBill().getTotal();
        penaltyService.handleWithType(roomUse, settingsService.getHotelTime(), handlingType);

        BaseServiceUse penalty = getServiceUseByServiceName(roomUse.getCustomerGroup(), handlingType.getServiceName());
        if (!canHandle || info.getMultiplier() == null) {
            assertNull(penalty);
        } else {
            SimpleService service = (SimpleService) penalty.getService();
            assertEquals(service.getTitle(), handlingType.getServiceName());
            assertEquals(penalty.getTotal().longValue(), PENALTY_AMOUNT);
            Long billTotalAfter = billRepository.findOne(livingUse.getBill().getId()).getTotal();
            assertEquals(billTotalAfter.longValue(), billTotalBefore + PENALTY_AMOUNT);
        }
    }

    @DataProvider(name = "testPenaltyHandling")
    public Object[][] getData_testPenaltyHandling() {
        List<Object[]> result = new ArrayList<Object[]>(8);
        HotelInfo info = hotelInfoProvider.getPersistentEntity();
        result.add(new Object[]{roomUseProvider.getPersistentEntity(), info, EARLY, true});
        info = hotelInfoProvider.getPersistentEntity(new Visitor<HotelInfo>() {
            @Override
            public void visit(HotelInfo entity) {
                entity.setMultiplier(50);
            }
        });
        result.add(new Object[]{roomUseProvider.getPersistentEntity(), info, EARLY, true});
        result.add(new Object[]{roomUseProvider.getPersistentEntity(), info, EARLY, false});

        info = hotelInfoProvider.getPersistentEntity();
        result.add(new Object[]{roomUseProvider.getPersistentEntity(), info, LATE, true});
        info = hotelInfoProvider.getPersistentEntity(new Visitor<HotelInfo>() {
            @Override
            public void visit(HotelInfo entity) {
                entity.setMultiplier(50);
            }
        });
        result.add(new Object[]{roomUseProvider.getPersistentEntity(), info, LATE, true});
        result.add(new Object[]{roomUseProvider.getPersistentEntity(), info, LATE, false});

        return result.toArray(new Object[result.size()][]);
    }

    @Test(dataProvider = "testPenaltyHandling")
    public void testPenaltyOffPeriodHandling(final RoomUse roomUse, final HotelInfo info, final HandlingType handlingType, final boolean canHandle) {
        final LivingUse livingUse = livingUseProvider.getPersistentEntity(new Visitor<LivingUse>() {
            @Override
            public void visit(final LivingUse livingUse) {
                livingUse.setBill(billProvider.getPersistentEntity(new Visitor<Bill>() {
                    @Override
                    public void visit(Bill bill) {
                        bill.setRoomUse(roomUse);
                        bill.setTotal(livingUse.getTotal());
                    }
                }));
            }
        });
        initMocks(info, handlingType, canHandle, livingUse, "canHandleOffPeriod");
        List<LivingUse> before = livingUseRepository.findByRoomUse(roomUse);
        penaltyService.handleWithType(roomUse, settingsService.getHotelTime(), handlingType);
        List<LivingUse> after = livingUseRepository.findByRoomUse(roomUse);
        assertEquals(after.size(), before.size() + (canHandle ? 1 : 0));
    }

    private void initMocks(final HotelInfo info, final HandlingType handlingType, final boolean canHandle, final LivingUse livingUse, final String methodName) {
        new NonStrictExpectations(penaltyService) {{
            invoke(penaltyService, "getTargetLivingUse", withAny(RoomUse.class), withAny(HandlingType.class));
            result = livingUse;
            invoke(penaltyService, "getPenaltyAmount", withAny(Integer.class), withAny(LivingUse.class));
            result = PENALTY_AMOUNT;

        }};
        new NonStrictExpectations(handlingType) {{
            invoke(handlingType, methodName, withAny(LocalDateTime.class), withAny(SettingsService.class));
            result = canHandle;

        }};
        new NonStrictExpectations(settingsService) {{
            invoke(settingsService, "getHotelInfo");
            result = info;
        }};
    }

    @Test(dataProvider = "testCanHandle")
    public void testCanHandle(final HotelInfo info, LocalDateTime time, HandlingType handlingType, final boolean canHandle) {
        new NonStrictExpectations(settingsService) {{
            invoke(settingsService, "getHotelInfo");
            result = info;
        }};
        assertEquals(handlingType.canHandleWithinPeriod(time, settingsService), canHandle);
    }

    @DataProvider(name = "testCanHandle")
    public Object[][] getData_testIsEarlyCheckIn() {
        List<Object[]> result = new ArrayList<>(8);
        HotelInfo info = hotelInfoProvider.getPersistentEntity(new Visitor<HotelInfo>() {
            @Override
            public void visit(HotelInfo entity) {
                entity.setCheckIn(TWO);
                entity.setEarlyCheckInStart(TEN);
                entity.setEarlyCheckInEnd(TWELVE);
                entity.setCheckOut(SIX);
                entity.setLateCheckOutStart(EIGHT);
                entity.setLateCheckOutEnd(TEN);
            }
        });
        result.add(new Object[]{info, getMinutes(TEN), EARLY, true});
        result.add(new Object[]{info, getMinutes(TEN - 1), EARLY, false});
        result.add(new Object[]{info, getMinutes(TWELVE - 1), EARLY, true});
        result.add(new Object[]{info, getMinutes(TWELVE), EARLY, false});

        result.add(new Object[]{info, getMinutes(EIGHT), LATE, true});
        result.add(new Object[]{info, getMinutes(EIGHT - 1), LATE, false});
        result.add(new Object[]{info, getMinutes(TEN - 1), LATE, true});
        result.add(new Object[]{info, getMinutes(TEN), LATE, false});
        return result.toArray(new Object[result.size()][]);
    }

    private LocalDateTime getMinutes(int minutes) {
        return getTimeAtStartOfDay().plusMinutes(minutes);
    }

    private LocalDateTime getTimeAtStartOfDay() {
        return settingsService.getHotelDate().toDateTimeAtStartOfDay(settingsService.getTimeZone()).toLocalDateTime();
    }

    private BaseServiceUse getServiceUseByServiceName(CustomerGroup group, String name) {
        SimpleService service = simpleServiceRepository.findByTitle(name);
        if (service == null) {
            return null;
        }

        List<BaseServiceUse> serviceUses = baseServiceUseRepository.findByServiceAndBillRoomUseCustomerGroup(service, group);
        return serviceUses.size() == 1 ? serviceUses.get(0) : null;
    }
}
