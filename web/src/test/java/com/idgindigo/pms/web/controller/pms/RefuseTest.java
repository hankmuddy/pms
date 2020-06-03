package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.domain.extranet.BaseGroupRoomUse;
import com.idgindigo.pms.domain.pms.BaseServiceUse;
import com.idgindigo.pms.domain.pms.Bill;
import com.idgindigo.pms.domain.pms.Refund;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.repository.pms.BaseServiceUseRepository;
import com.idgindigo.pms.repository.pms.BillRepository;
import com.idgindigo.pms.repository.pms.RoomUseRepository;
import com.idgindigo.pms.restutils.exception.RoomUseException;
import com.idgindigo.pms.service.admin.SettingsService;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.RoomUseUtils;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.pms.BankDetailsProvider;
import com.idgindigo.pms.utils.pms.RoomUseProvider;
import com.idgindigo.pms.web.controller.InMemoryDbWebTest;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Source.BOOKING;
import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Status.LIVING;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * @author valentyn_vakatsiienko
 * @since 12/11/13 10:30 AM
 */
public class RefuseTest extends InMemoryDbWebTest {
    public static final int TOTAL_DAYS = 5;
    public static final int DAYS_TO_ADD = TOTAL_DAYS - 1;
    public static final LocalDate NOW = new LocalDate(DateTimeZone.UTC);

    @Inject
    private RoomUseProvider provider;
    @Inject
    private BillRepository billRepository;
    @Inject
    private RoomUseRepository roomUseRepository;
    @Inject
    private BaseServiceUseRepository baseServiceUseRepository;
    @Inject
    private SettingsService settingsService;
    @Inject
    private RoomUseUtils roomUseUtils;
    @Inject
    private BankDetailsProvider bankDetailsProvider;


    @Test(dataProvider = "testRefuseBooking")
    public void testRefuseBooking(RoomUse roomUse, final Bill billBeforeRefuse, final Long totalPaid) throws Exception {
        billBeforeRefuse.setTotalPaid(totalPaid);
        billRepository.updateTotalPaid(billBeforeRefuse, totalPaid);
        roomUseUtils.populate(roomUse.getCustomerGroup(), roomUse);
        mvc.perform(preparePost(roomUse.getId() + "/confirmed")).andExpect(status().isOk());

        refuse(roomUse, roomUse.getStartDate());
        //Service uses are not removed
        List<BaseServiceUse> serviceUses = baseServiceUseRepository.findByBillRoomUse(roomUse);
        assertTrue(serviceUses.size() == DAYS_TO_ADD);
        //Proper refund is created and set to every refunded service use
        Refund refund = serviceUses.get(0).getRefund();
        assertTrue(refund.getTotal().equals(totalPaid));
        for (BaseServiceUse serviceUse : serviceUses) {
            assertTrue(serviceUse.getRefund().equals(refund));
            assertTrue(serviceUse.getBill().equals(billBeforeRefuse));
        }
        //Bill becomes fully paid and its total becomes 0
        Bill billAfterRefuse = billRepository.findByRoomUse(roomUse).get(0);
        assertTrue(billAfterRefuse.isFullyPaid());
        assertTrue(billAfterRefuse.getTotal() == 0);
    }

    @DataProvider(name = "testRefuseBooking")
    public Object[][] getData_testRefuseBooking() throws Exception {
        List<Object[]> result = new ArrayList<Object[]>(2);

        RoomUse roomUse = create();
        Bill billBeforeRefuse = billRepository.findByRoomUse(roomUse).get(0);
        Long totalPaid = billBeforeRefuse.getTotal();
        result.add(new Object[]{roomUse, billBeforeRefuse, totalPaid});

        roomUse = create();
        billBeforeRefuse = billRepository.findByRoomUse(roomUse).get(0);
        totalPaid = billBeforeRefuse.getTotal() / 2;
        result.add(new Object[]{roomUse, billBeforeRefuse, totalPaid});

        return result.toArray(new Object[result.size()][]);
    }

    @Test(dataProvider = "testRefuseLiving")
    public void testRefuseLiving(RoomUse roomUse, final Bill billBeforeRefuse, final Long totalPaid, final int daysAfterCheckIn) throws Exception {
        //Perform payment with provided amount
        billBeforeRefuse.setTotalPaid(totalPaid);
        billRepository.updateTotalPaid(billBeforeRefuse, totalPaid);

        //Confirm and check in the roomUse
        roomUseUtils.populate(roomUse.getCustomerGroup(), roomUse);
        mvc.perform(preparePost(roomUse.getId() + "/confirmed")).andExpect(status().isOk());
        RoomUseController.CheckDto dto = new RoomUseController.CheckDto(settingsService.getHotelTime());
        mvc.perform(preparePost(dto, roomUse.getId() + "/checkedIn")).andExpect(status().isOk());
        roomUse = refuse(roomUse, NOW.plusDays(daysAfterCheckIn));

        //Find the created refuse
        List<RoomUse> roomUses = roomUseRepository.findByCustomerGroup(roomUse.getCustomerGroup());
        RoomUse refuse = roomUses.get(0).getStatus() == BaseGroupRoomUse.Status.REFUSE ? roomUses.get(0) : roomUses.get(1);

        //If the roomUse has been used for at least one day, divide it properly
        //Not refunded service uses stay in the room use. Others are transferred to refuse
        long totalUsed = 0;
        if (daysAfterCheckIn > 0) {
            assertTrue(roomUse.getEndDate().equals(roomUse.getStartDate().plusDays(daysAfterCheckIn)));
            assertTrue(refuse.getStartDate().equals(roomUse.getEndDate()));

            List<BaseServiceUse> notRefundedServiceUses = baseServiceUseRepository.findByBillRoomUse(roomUse);
            for (BaseServiceUse serviceUse : notRefundedServiceUses) {
                totalUsed += serviceUse.getTotal();
            }
            assertTrue(notRefundedServiceUses.size() == daysAfterCheckIn);
        } else if (daysAfterCheckIn == 0) {
            assertTrue(roomUse.getEndDate().equals(roomUse.getStartDate().plusDays(DAYS_TO_ADD)));
        }

        List<BaseServiceUse> refundedServiceUses = baseServiceUseRepository.findByBillRoomUse(refuse);
        assertTrue(refundedServiceUses.size() == DAYS_TO_ADD - daysAfterCheckIn);
        //Proper refund is created and set to every refunded service use
        Refund refund = refundedServiceUses.get(0).getRefund();
        assertTrue(refund.getTotal().equals(totalPaid - totalUsed));
        for (BaseServiceUse serviceUse : refundedServiceUses) {
            assertTrue(serviceUse.getRefund().equals(refund));
//            assertTrue(serviceUse.getBill().equals(billBeforeRefuse));
        }
        //If remaining total paid covers bill total becomes fully paid and its total becomes 0
        Bill billAfterRefuse = billRepository.findByRoomUse(roomUse).get(0);
        if (totalPaid >= billAfterRefuse.getTotal()) {
            assertTrue(billAfterRefuse.isFullyPaid());
        } else {
            assertFalse(billAfterRefuse.isFullyPaid());
        }
    }

    @DataProvider(name = "testRefuseLiving")
    public Object[][] getData_testRefuseLiving() throws Exception {
        List<Object[]> result = new ArrayList<Object[]>(2);

        RoomUse roomUse = create();
        int daysAfterCheckIn = 2;
        Bill billBeforeRefuse = billRepository.findByRoomUse(roomUse).get(0);
        Long totalPaid = billBeforeRefuse.getTotal();
        result.add(new Object[]{roomUse, billBeforeRefuse, totalPaid, daysAfterCheckIn});

        roomUse = create();
        daysAfterCheckIn = 0;
        billBeforeRefuse = billRepository.findByRoomUse(roomUse).get(0);
        totalPaid = billBeforeRefuse.getTotal() / 2;
        result.add(new Object[]{roomUse, billBeforeRefuse, totalPaid, daysAfterCheckIn});

        return result.toArray(new Object[result.size()][]);
    }

    @Test
    public void testRefuseChannelBooking() throws Exception {
        RoomUse channelBooking = provider.getPersistentEntity(new Visitor<RoomUse>() {
            @Override
            public void visit(RoomUse entity) {
                entity.setSource(BOOKING);
                entity.setRcode(EntityProvider.randomAlphabeticString());
            }
        });
        RoomUseController.RefuseDto dto = new RoomUseController.RefuseDto();
        dto.setDate(channelBooking.getStartDate());

        testBadRequest(preparePost(dto, channelBooking.getId() + "/refused"), RoomUseException.REFUSE_CHANNEL_BOOKING);
    }

    @Test
    public void testRefuseChannelLiving() throws Exception {
        RoomUse channelLiving = provider.getPersistentEntity(new Visitor<RoomUse>() {
            @Override
            public void visit(RoomUse entity) {
                entity.setStatus(LIVING);
                entity.setSource(BOOKING);
                entity.setRcode(EntityProvider.randomAlphabeticString());
            }
        });
        RoomUseController.RefuseDto dto = new RoomUseController.RefuseDto();
        dto.setDate(channelLiving.getStartDate());

        mvc.perform(preparePost(dto, channelLiving.getId() + "/refused")).andExpect(status().isOk());
    }

    private RoomUse create() throws Exception {
        RoomUse roomUse = provider.getTransientEntity(new Visitor<RoomUse>() {
            @Override
            public void visit(RoomUse entity) {
                entity.setStartDate(settingsService.getHotelDate());
                entity.setEndDate(entity.getStartDate().plusDays(DAYS_TO_ADD));
            }
        });
        roomUse = convertResponseWithSingleObject(objectMapper, mvc.perform(preparePost(roomUse)).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString(), RoomUse.class);
        return roomUse;
    }

    private RoomUse refuse(RoomUse roomUse, LocalDate date) throws Exception {
        RoomUseController.RefuseDto dto = new RoomUseController.RefuseDto();
        dto.setDate(date);
        dto.setBankDetails(bankDetailsProvider.getPersistentEntity());
        mvc.perform(preparePost(dto, roomUse.getId() + "/refused")).andExpect(status().isOk());
        return roomUseRepository.findOne(roomUse.getId());
    }

    @Override
    protected String getUrl() {
        return RoomUseController.URL + "/";
    }
}
