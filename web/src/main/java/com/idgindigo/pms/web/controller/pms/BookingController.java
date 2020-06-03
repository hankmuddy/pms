package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.channel.wubook.WubookException;
import com.idgindigo.pms.channel.wubook.WubookImpl;
import com.idgindigo.pms.domain.extranet.BaseGroupRoomUse;
import com.idgindigo.pms.domain.extranet.CustomerGroup;
import com.idgindigo.pms.domain.extranet.person.Adult;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.logins.domain.Hotel;
import com.idgindigo.pms.logins.repository.HotelRepository;
import com.idgindigo.pms.repository.pms.BankDetailsRepository;
import com.idgindigo.pms.restutils.exception.RefundException;
import com.idgindigo.pms.restutils.exception.RestFriendlyException;
import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.service.broadcast.BroadcastService;
import com.idgindigo.pms.service.channels.BookingService;
import com.idgindigo.pms.service.channels.ChannelService;
import com.idgindigo.pms.service.channels.WubookRoomUses;
import com.idgindigo.pms.service.extranet.MailService;
import com.idgindigo.pms.service.pms.RoomUseService;
import com.idgindigo.pms.utils.SmartToStringBuilder;
import com.idgindigo.pms.web.controller.ResponseEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import static com.idgindigo.pms.logins.domain.Hotel.WubookImportStatus.DATA_IMPORTED;
import static com.idgindigo.pms.logins.domain.Hotel.WubookImportStatus.RESERVATIONS_IMPORTED;
import static com.idgindigo.pms.service.pms.RoomUseService.GroupRoomUseDto;
import static com.idgindigo.pms.service.pms.RoomUseService.RoomUseWithOverrides;

/**
 * @author vomel
 * @since 29.01.14 16:49
 */
@Controller
@RequestMapping(BookingController.URL)
public class BookingController {
    public static final String URL = "/wubooking";
    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);
    public static final String NEW_RESERVATION = "%s:New reservation with ID=%s";
    public static final String CATCH = "catch";
    @Inject
    private BankDetailsRepository bankDetailsRepository;
    @Inject
    private HotelRepository hotelRepository;
    @Inject
    private RoomUseService roomUseService;
    @Inject
    private Environment env;
    private volatile int wubookBurstDuration;
    @Inject
    private ChannelService channelService;
    @Inject
    private BookingService bookingService;
    @Inject
    private MailService mailService;
    @Inject
    private BroadcastService broadcastService;
    //    private volatile boolean inProgress =false;
    private final Object lock = new Object();
    private ConcurrentHashMap<String, InputHolder> callBuffer = new ConcurrentHashMap<>();

    @Getter
    @Setter
    private static class InputHolder {

        private long lastCalled;
        private Set<String> rcodes = new CopyOnWriteArraySet<>();
        private Hotel hotel;

        @Override
        public String toString() {
            return new SmartToStringBuilder(this, SmartToStringBuilder.NO_FIELD_NAMES_STYLE)
                    .append("lastCalled", lastCalled)
                    .append("rcodes", rcodes, true)
                    .append("hotel", hotel)
                    .toString();
        }
    }

    @PostConstruct
    public void postConstruct() {
        wubookBurstDuration = env.getProperty("wubook.burst.duration", Integer.class);
    }

    @RequestMapping("cc")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> fetchCc(@RequestParam("id") Long roomUseId, @RequestParam("pwd") String pwd) {
        RoomUse roomUse = roomUseService.getRepository().findOne(roomUseId);
        if (roomUse == null) throw new EntityNotFoundException();
        if (roomUse.getRcode() == null) throw new RestFriendlyException(RestFriendlyException.INCORRECT_VALUE, "roomUseId");
        Map<String, Object> result = channelService.fetchCc(roomUse.getRcode(), pwd, SecurityUtils.getWubookAccount());
        return new ResponseEntity<>(result);
    }

    @RequestMapping(value = CATCH, method = RequestMethod.GET)
    public void pushBookingFake(HttpServletRequest request) {
        logger.warn("{}:Wrong request detected from IP: {} ", tenantId(), request.getRemoteAddr());
        logger.debug("{}:request params: {}", tenantId(), request.getParameterMap());
        throw new EntityNotFoundException();
    }

    @RequestMapping(value = CATCH, method = RequestMethod.POST)
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void pushBooking(@RequestParam(value = "rcode", required = false) String rcode, @RequestParam("lcode") String lcode) {
        callBuffer.putIfAbsent(lcode, new InputHolder());
        synchronized (lock) {
            InputHolder inputHolder = callBuffer.get(lcode);
            inputHolder.getRcodes().add(rcode);
            long now = System.currentTimeMillis();
            if (now - inputHolder.getLastCalled() < wubookBurstDuration) {
                logger.trace("{}:DDoS from WuBook detected for lcode='{}' with rcode='{}', skipping for this time", tenantId(), lcode, rcode);
                return;
            }
            inputHolder.setLastCalled(now);
        }
/*        synchronized (lock) {
            if (inProgress) {
                logger.debug("Pausing new push for lcode:{}" ,lcode);
                InputHolder inputHolder = callBuffer.get(lcode);
                long now = System.currentTimeMillis();
                if (now - inputHolder.getLastCalled() < wubookBurstDuration) {
                    logger.trace("DDoS from WuBook detected for lcode='{}' with rcode='{}', skipping for this time", lcode, rcode);
                    return;
                }
                inputHolder.getRcodes().add(rcode);
                inputHolder.setLastCalled(now);
                inProgress = false;
            } else {
                inProgress = true;
                logger.debug("Passing new push through for lcode:{}" ,lcode);
            }
        }
*/
        Hotel hotel = hotelRepository.findByLcode(lcode);
        if (hotel == null) {
            logger.error("{}:Could not find hotel for lcode: {}", tenantId(), lcode);
            callBuffer.remove(lcode);
            return;
        }
        if (logger.isDebugEnabled()) {
            logger.debug(String.format(NEW_RESERVATION, hotel.getTenantId(), rcode));
        }
        callBuffer.get(lcode).setHotel(hotel);

        synchronized (lock) {
            try {
                logger.debug("{}:Started collecting rcodes from WuBook for lcode: {}, wubookBurstDuration = {}", tenantId(), lcode, wubookBurstDuration);
                lock.wait(wubookBurstDuration);
            } catch (InterruptedException e) {
                logger.error("{}:Interrupted", tenantId(), e);
            }
            logger.debug("{}:Finished collecting rcodes from WuBook for lcode: {}, rcodes collected so far are: {}", tenantId(), lcode, callBuffer.get(lcode).getRcodes());
            LocalDate now = new LocalDate();
            processBookingsForHotel(callBuffer.get(lcode), now, now);
        }
    }

    @RequestMapping(value = "importBookings", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ResponseEntity<?> importBookings() {
        Hotel hotel = SecurityUtils.getHotel();
        if (WubookImpl.ENABLED && SecurityUtils.isWubookConfigured(hotel) && SecurityUtils.getHotel().getImportStatus() == DATA_IMPORTED) {
            LocalDate startDate = LocalDate.now();
            LocalDate endDate = startDate.plusYears(2);
            fetchBookings(startDate, endDate, hotel, Collections.<String>emptyList(), Boolean.FALSE);
            turnWubookSynchOn(hotel);
        } else throw new AccessDeniedException("Not supported");
        return new ResponseEntity<>();
    }

    @RequestMapping(value = "importBookingsForDates", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> importBookingsForDates(@RequestParam("dfrom") String dfrom, @RequestParam("dto") String dto) {
        Hotel hotel = SecurityUtils.getHotel();
        if (WubookImpl.ENABLED && SecurityUtils.isWubookConfigured(hotel) && SecurityUtils.getHotel().getImportStatus() == RESERVATIONS_IMPORTED) {
            LocalDate startDate = LocalDate.parse(dfrom, ChannelService.BOOKING_FORMATTER);
            LocalDate endDate = LocalDate.parse(dto, ChannelService.BOOKING_FORMATTER);
            fetchBookings(startDate, endDate, hotel, Collections.<String>emptyList(), Boolean.FALSE);
        } else throw new AccessDeniedException("Not supported");
        return new ResponseEntity<>();
    }

    //TODO Restrict to superadmin and disable upon move to production
    @RequestMapping(value = "attachPushUrlsToLcodes", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> attachPushUrlsToLcodes() {
        for (Hotel hotel : hotelRepository.findAll()) {
            if (WubookImpl.ENABLED && SecurityUtils.isWubookConfigured(hotel)) {
                logger.debug("Attaching push url {} to lcode for hotel {}", channelService.getApplicationUrl(), hotel);
                try {
                    channelService.attachPushUrlToLcode(SecurityUtils.getWubookAccount(hotel));
                } catch (WubookException e) {
                    logger.error("Failed to attach push url for hotel {}", hotel, e);
                }
            }
        }
        return new ResponseEntity<>();
    }

    public void turnWubookSynchOn(Hotel hotel) {
        logger.info("{}:Turning WuBook synch ON for hotel: {}", tenantId(), hotel);
        Hotel current = hotelRepository.findOne(hotel.getId());
        current.setImportStatus(RESERVATIONS_IMPORTED);
        hotelRepository.save(current);
        SecurityUtils.getUserDetails().getAuthentication().setHotel(current);
        logger.info("{}:Turned WuBook synch ON for hotel: {}", tenantId(), current);
    }

    public void processBookingsForHotel(InputHolder inputHolder, LocalDate dfrom, LocalDate dto) {
        logger.debug("{}:STARTED Processing dataset: {}", tenantId(), inputHolder);
        String lcode = inputHolder.getHotel().getLcode();
        Collection<String> rcodes = fetchBookings(dfrom, dto, inputHolder.getHotel(), new HashSet<>(inputHolder.getRcodes()), Boolean.TRUE);
        callBuffer.get(lcode).getRcodes().removeAll(rcodes);
        logger.debug("{}:FINISHED Processing dataset: {} ", tenantId(), inputHolder);
    }

    public Collection<String> fetchBookings(LocalDate dfrom, LocalDate dto, Hotel hotel, Collection<String> rcodes, Boolean oncreated) {
        WubookRoomUses roomUses = channelService.fetchBookings(dfrom, dto, oncreated, SecurityUtils.getWubookAccount(hotel));
        processBookings(hotel, roomUses);
        return rcodes;
    }

    private void processBookings(Hotel hotel, WubookRoomUses roomUses) {
        if (logger.isDebugEnabled()) {
            Collection<String> refusedRcodes = new ArrayList<>();
            for (Map<String, Object> o : roomUses.getRefused()) {
                refusedRcodes.add(String.valueOf(o.get("reservation_code")));
            }
            Collection<String> confirmedRcodes = new ArrayList<>();
            for (Map<String, Object> o : roomUses.getConfirmed()) {
                confirmedRcodes.add(String.valueOf(o.get("reservation_code")));
            }
            logger.debug("\n");
            logger.debug("Confirmed roomuses: {}", confirmedRcodes);
            logger.debug("Refused roomuses: {}", refusedRcodes);
        }

        //Sometimes one batch of bookings contain both new and refused reservations for the same code
        //We need to ignore them from being created
        Collection<String> refusedRcodes = new HashSet<>();
        for (Map<String, Object> refused : roomUses.getRefused()) {
            String rcode = String.valueOf(refused.get("reservation_code"));
            refusedRcodes.add(rcode);
            GroupRoomUseDto refusedDto = channelService.parseRefuse(rcode);
            if (refusedDto == null) {
                //rcode not found
                continue;
            }
            logger.debug("{}:processing refuse: {}", tenantId(), refusedDto);
            refuse(refusedDto, hotel);
        }
        Collection<Long> reservedRoomIds = new HashSet<>();
        for (Map<String, Object> confirmed : roomUses.getConfirmed()) {
            GroupRoomUseDto groupRoomUse = channelService.parseRoomUse(confirmed, reservedRoomIds);
            if (groupRoomUse == null || refusedRcodes.contains(groupRoomUse.getRcode())) {
                //Already refused
                continue;
            }
            if (groupRoomUse.getRoomUses().size() == 1) {
                createSingleRoomUse(groupRoomUse.getRcode(), hotel, groupRoomUse.getRoomUses().get(0));
            } else {
                createGroupRoomUse(groupRoomUse.getRcode(), hotel, groupRoomUse);
            }
        }
    }

    private void refuse(GroupRoomUseDto refused, Hotel hotel) {
        if (refused.getRoomUses().isEmpty()) {
            return;
        }
        try {
            refuse(refused.getGroup(), refused.getRoomUses(), roomUseService);
        } catch (RefundException e) {
            LocalDate[] minAndMaxDate = getMinAndMaxDate(refused.getRoomUses());
            StringBuilder message = new StringBuilder("Refused reservations(s) for period[");
            message.append(minAndMaxDate[0]).append(" - ").append(minAndMaxDate[1]);
            message.append("]:\n");
            for (RoomUseWithOverrides refuse : refused.getRoomUses()) {
                message.append("Reservation Code: ").append(refuse.getRoomUse().getRcode()).append(", ");
                message.append("Total: ").append(refuse.getRoomUse().getTotal()).append(", ");
                message.append("TotalPaid: ").append(refuse.getRoomUse().getTotalPaid()).append(", ");
                CustomerGroup customerGroup = refuse.getRoomUse().getCustomerGroup();
                message.append("Customer Group: ");

                Adult customer = customerGroup.getCustomer();
                if (customer != null) {
                    message.append(customer.getFirstName()).append(" ").append(customer.getLastName());
                } else {
                    message.append(customerGroup.getCompany().getName());
                }
                message.append("\n");
            }
            forcedRefuse(refused);
            logger.warn(message.toString());
            emailAdmins(message.toString());
            String email = hotel.getInfo().getEmail();
            if (StringUtils.isNotBlank(email)) {
                mailService.sendMail(email, "WARNING: Forcely refused booking with existing payments", message.toString(), true);
            }
        }
        List<RoomUse> toSend = new ArrayList<>(refused.getRoomUses().size());
        for (RoomUseWithOverrides withOverrides : refused.getRoomUses()) {
            toSend.add(withOverrides.getRoomUse());
        }
        broadcastService.broadcastRefuse(toSend);
    }

    private void forcedRefuse(GroupRoomUseDto refused) {
        for (RoomUseWithOverrides refuse : refused.getRoomUses()) {
            roomUseService.setNotArrived(refuse.getRoomUse());
        }
    }

    private List<RoomUseWithOverrides> refuse(CustomerGroup group, List<RoomUseWithOverrides> roomUses, RoomUseService service) {
        LocalDate minDate = getMinAndMaxDate(roomUses)[0];
        Collection<RoomUse> updated = service.refuse(group, minDate, true, bankDetailsRepository.getDefault());
        for (Iterator<RoomUseWithOverrides> iterator = roomUses.iterator(); iterator.hasNext(); ) {
            RoomUseWithOverrides ruwo = iterator.next();
            if (!updated.contains(ruwo.getRoomUse())) iterator.remove();
        }
        return roomUses;
    }

    private static LocalDate[] getMinAndMaxDate(List<RoomUseWithOverrides> roomUses) {
        LocalDate minDate = roomUses.get(0).getRoomUse().getStartDate();
        LocalDate maxDate = roomUses.get(0).getRoomUse().getEndDate();
        for (RoomUseWithOverrides roomUse : roomUses) {
            LocalDate candidateMin = roomUse.getRoomUse().getStartDate();
            if (candidateMin.isBefore(minDate)) {
                minDate = candidateMin;
            }
            LocalDate candidateMax = roomUse.getRoomUse().getEndDate();
            if (candidateMax.isAfter(maxDate)) {
                maxDate = candidateMax;
            }
        }
        return new LocalDate[]{minDate, maxDate};
    }

    private void createGroupRoomUse(String rcode, Hotel hotel, GroupRoomUseDto groupRoomUse) {
        try {
            if (groupRoomUse.getRcode().equals(rcode) && roomUseService.getRepository().existsByRcode(rcode)) {
                logger.info("{}:Skipping already existing booking for rcode: {}", tenantId(), rcode);
                return;
            }
            List<RoomUse> created = new ArrayList<>(groupRoomUse.getRoomUses().size());
            logger.debug("{}:Creating group room use: {}", tenantId(), created);
            List<RoomUseWithOverrides> toHandleWithMove = filterEmpty(groupRoomUse.getRoomUses());

            created.addAll(handleNonEmpty(groupRoomUse));
            CustomerGroup group = created.isEmpty() ? null : created.get(0).getCustomerGroup();
            created.addAll(handleEmpty(toHandleWithMove, group, hotel));

            broadcastService.broadcastBooking(created);
        } catch (Exception e) {
            String msg = String.format("%s:Could not create GroupRoomUse: %s", tenantId(), groupRoomUse);
            logger.error(msg, e);
            emailAdmins(msg);
            broadcastService.broadcastBookingFailed(groupRoomUse);
            throw e;
        }
    }

    private void emailAdmins(String msg) {
        mailService.sendMail("vomel@idg.net.ua", "Booking failed", msg, true);
        mailService.sendMail("valentin.vakatsiienko@idg.net.ua", "Booking failed", msg, true);
        mailService.sendMail("yaroslav@idg.net.ua", "Booking failed", msg, true);
    }

    private String tenantId() {
        String currentTenantId = SecurityUtils.getCurrentTenantId();
        return currentTenantId != null ? currentTenantId : "";
    }

    private List<RoomUseWithOverrides> filterEmpty(List<RoomUseWithOverrides> ruWithOverridesList) {
        List<RoomUseWithOverrides> toHandleWithMove = new ArrayList<>();
        Iterator<RoomUseWithOverrides> iterator = ruWithOverridesList.iterator();
        while (iterator.hasNext()) {
            RoomUseWithOverrides ruWithOverrides = iterator.next();
            if (ruWithOverrides.getRoomUse().getRoom() == null) {
                toHandleWithMove.add(ruWithOverrides);
                iterator.remove();
            }
        }
        return toHandleWithMove;
    }

    private Collection<RoomUse> handleNonEmpty(GroupRoomUseDto groupRoomUse) {
        List<RoomUse> created = new ArrayList<>(groupRoomUse.getRoomUses().size());
        if (!groupRoomUse.getRoomUses().isEmpty()) {
            created.addAll(roomUseService.create(groupRoomUse, false));
        }
        return created;
    }

    private Collection<RoomUse> handleEmpty(List<RoomUseWithOverrides> toHandleWithMove, CustomerGroup group, Hotel hotel) {
        List<RoomUse> created = new ArrayList<>(toHandleWithMove.size());
        for (RoomUseWithOverrides ruWithOverrides : toHandleWithMove) {
            if (group != null) {
                ruWithOverrides.getRoomUse().setCustomerGroup(group);
            }
            created.addAll(bookingService.handleWithoutRoom(ruWithOverrides, false));
            if (created.isEmpty()) {
                handleBookingFailed(ruWithOverrides.getRoomUse(), hotel, null);
            } else {
                group = created.get(0).getCustomerGroup();
            }
        }
        sendEmailWarning(hotel, getContent(created.toArray(new RoomUse[created.size()])));
        return created;
    }

    private void createSingleRoomUse(String rcode, Hotel hotel, RoomUseWithOverrides ruWithOverrides) {
        RoomUse roomUse = ruWithOverrides.getRoomUse();
        logger.debug("{}:creating single room use: {} ", tenantId(), ruWithOverrides);
        try {
            if (roomUse.getRcode().equals(rcode) && roomUseService.getRepository().existsByRcode(rcode)) {
                logger.info("{}:Skipping already existing booking for rcode: {}", tenantId(), rcode);
                return;
            }
            if (roomUse.getRoom() != null) {
                roomUse = roomUseService.create(roomUse, ruWithOverrides.getPrices(), false);
            } else {
                List<RoomUse> created = bookingService.handleWithoutRoom(ruWithOverrides, false);
                if (created.isEmpty()) {
                    handleBookingFailed(roomUse, hotel, null);
                    return;
                }
                sendEmailWarning(hotel, getContent(roomUse));
            }
            broadcastService.broadcastBooking(Collections.singletonList(roomUse));
        } catch (Exception e) {
            handleBookingFailed(roomUse, hotel, e);
        }
    }

    private void sendEmailWarning(Hotel hotel, String content) {
        String email = hotel.getInfo().getEmail();
        if (email != null) {
            mailService.sendMail(email, "Warning! Reservation with moves between rooms!", content, false);
        }
    }

    private static String getContent(RoomUse... roomUses) {
        StringBuilder result = new StringBuilder(roomUses.length * 5);
        for (RoomUse roomUse : roomUses) {
            Adult adult = roomUse.getCustomerGroup().getCustomer();
            String customer = adult != null ? adult.getFirstName() + " " + adult.getLastName() : roomUse.getCustomerGroup().getCompany().getName();
            result.append(roomUse.getStartDate()).append(" - ").append(roomUse.getStartDate()).append(" for ").append(customer).append("\n");
        }
        return result.toString();
    }

    private void handleBookingFailed(RoomUse roomUse, Hotel hotel, Exception e) {
        if (e == null) {
            String message = "%s:Cannot book room with RID '%s' rcode:'%s' - not enough free rooms found for dates: %s - %s";
            String msg = String.format(message, tenantId(), roomUse.getBaseRoom().getRid(), roomUse.getRcode(), roomUse.getStartDate(), roomUse.getEndDate());
            logger.error(msg);
            emailAdmins(msg);
            broadcastService.broadcastBookingFailed(Collections.<BaseGroupRoomUse>singletonList(roomUse));
        } else {
            String format = "%s:Could not create RoomUse: %s";
            String msg = String.format(format, tenantId(), roomUse);
            logger.error(msg, e);
            emailAdmins(msg);
            broadcastService.broadcastBookingFailed(Collections.<BaseGroupRoomUse>singletonList(roomUse));
        }
    }

}
