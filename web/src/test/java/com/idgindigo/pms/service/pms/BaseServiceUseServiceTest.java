package com.idgindigo.pms.service.pms;

import com.idgindigo.pms.domain.pms.BaseServiceUse;
import com.idgindigo.pms.domain.pms.Bill;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.domain.pms.SimpleService;
import com.idgindigo.pms.domain.pms.SimpleServiceUse;
import com.idgindigo.pms.repository.pms.BaseServiceUseRepository;
import com.idgindigo.pms.service.ServiceTest;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.pms.BillProvider;
import com.idgindigo.pms.utils.pms.RoomUseProvider;
import com.idgindigo.pms.utils.pms.SimpleServiceProvider;
import com.idgindigo.pms.utils.pms.SimpleServiceUseProvider;
import lombok.AllArgsConstructor;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * @author valentyn_vakatsiienko
 * @since 3/24/14 5:10 PM
 */
public class BaseServiceUseServiceTest extends ServiceTest {
    public static final int DAYS = 5;
    public static final LocalDate SINCE_DATE = LocalDate.now(DateTimeZone.UTC).plusDays(DAYS - 2);
    @Inject
    private BaseServiceUseService service;
    @Inject
    private RoomUseProvider roomUseProvider;
    @Inject
    private SimpleServiceUseProvider serviceUseProvider;
    @Inject
    private BillProvider billProvider;
    @Inject
    private BaseServiceUseRepository baseServiceUseRepository;
    @Inject
    private SimpleServiceProvider simpleServiceProvider;
    @Inject
    private SystemServiceService systemServiceService;

    @Test(dataProvider = "testMoveServiceUses")
    public void testMoveServiceUses(boolean createAdditionalBeds, boolean moveAdditionalBeds) {
        RoomUsePair pair = createRoomUses(createAdditionalBeds);
        RoomUse from = pair.from;
        RoomUse to = pair.to;

        List<BaseServiceUse> toMove = baseServiceUseRepository.findByRoomUseAndAfterDateIncluding(from, SINCE_DATE);

        service.move(from, to, SINCE_DATE, moveAdditionalBeds, null);

        List<BaseServiceUse> moved = baseServiceUseRepository.findByRoomUseAndAfterDateIncluding(to, SINCE_DATE);
        if (!createAdditionalBeds || moveAdditionalBeds) {
            assertEquals(toMove, moved);
        } else {
            assertTrue(moved.isEmpty());
        }
    }

    @DataProvider(name = "testMoveServiceUses")
    public Object[][] getData_testMoveServiceUses() {
        List<Object[]> result = new ArrayList<>(3);
        result.add(new Object[]{false, false});
        result.add(new Object[]{true, true});
        result.add(new Object[]{true, false});
        return result.toArray(new Object[result.size()][]);
    }

    public RoomUsePair createRoomUses(boolean additionalBeds) {
        RoomUse from = roomUseProvider.getPersistentEntity();
        RoomUse to = roomUseProvider.getPersistentEntity();

        createBill(from, true, additionalBeds);
        createBill(from, false, additionalBeds);
        return new RoomUsePair(from, to);
    }

    public void createBill(final RoomUse roomUse, final boolean forCustomer, boolean additionalBeds) {
        final Bill bill = billProvider.getPersistentEntity(new Visitor<Bill>() {
            @Override
            public void visit(Bill entity) {
                if (forCustomer) {
                    entity.setGroup(roomUse.getCustomerGroup());
                    entity.setRoomUse(null);
                } else {
                    entity.setRoomUse(roomUse);
                    entity.setGroup(null);
                }
            }
        });
        createServiceUses(bill, additionalBeds ? systemServiceService.getAdultBedService() : simpleServiceProvider.getPersistentEntity());
    }

    public void createServiceUses(final Bill forCustomer, final SimpleService service) {
        LocalDate now = LocalDate.now(DateTimeZone.UTC);
        for (LocalDate date = now; !date.isAfter(now.plusDays(DAYS)); date = date.plusDays(1)) {
            final LocalDate useDate = date;
            serviceUseProvider.getPersistentEntity(new Visitor<SimpleServiceUse>() {
                @Override
                public void visit(SimpleServiceUse entity) {
                    entity.setDate(useDate);
                    entity.setBill(forCustomer);
                    entity.setService(service);
                }
            });
            date = date.plusDays(1);
        }
    }

    @AllArgsConstructor
    public static class RoomUsePair {
        private RoomUse from;
        private RoomUse to;
    }
}
