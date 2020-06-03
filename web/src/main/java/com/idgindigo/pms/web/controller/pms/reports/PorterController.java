package com.idgindigo.pms.web.controller.pms.reports;

import com.idgindigo.pms.domain.extranet.BaseGroupRoomUse;
import com.idgindigo.pms.domain.extranet.CustomerGroup;
import com.idgindigo.pms.domain.extranet.person.Adult;
import com.idgindigo.pms.domain.extranet.person.Person;
import com.idgindigo.pms.domain.extranet.plan.Plan;
import com.idgindigo.pms.domain.pms.BankDetails;
import com.idgindigo.pms.domain.pms.Bill;
import com.idgindigo.pms.domain.pms.Child;
import com.idgindigo.pms.domain.pms.Company;
import com.idgindigo.pms.domain.pms.GroupMemberToRoomUse;
import com.idgindigo.pms.domain.pms.Payment;
import com.idgindigo.pms.domain.pms.Refund;
import com.idgindigo.pms.domain.pms.Room;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.service.channels.ChannelService;
import com.idgindigo.pms.service.pms.PenaltyService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.sf.jasperreports.engine.JRException;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.idgindigo.pms.domain.extranet.plan.Plan.Board;


/**
 * Created by misha on 25.04.14.
 */
@Controller
public class PorterController extends ReportController {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoomUseItem {
        private String room;
        private String roomType;
        private String firstName;
        private String lastName;
        private String patronymic;
        private String qty;
        private String startDate;
        private String endDate;
        private String earlyCheckIn;
        private String checkInTime;
        private String checkOutTime;
        private String lateCheckOut;
    }

    @RequestMapping("hotelLoad")
    public void createHotelLoad(@RequestParam(value = "reportDate", required = false) final Long reportDate, HttpServletResponse response) throws JRException, IOException {
        final LocalDate date = reportDate == null ? settingsService.getHotelDate() : new LocalDate(reportDate * 1000, DateTimeZone.UTC);
        new ReportCreator(response, getReport("hotelLoad", "jasper"), getReport("hotelLoad", "pdf")) {
            @Override
            void collectParameters(Map<String, Object> parameters) {

                final LocalDateTime now = settingsService.getHotelTime();

                List<RoomUse> roomUses = roomUseRepository.findAll(new Specification<RoomUse>() {
                    @Override
                    public Predicate toPredicate(Root<RoomUse> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                        Predicate startDate = cb.lessThanOrEqualTo(root.<LocalDate>get("startDate"), date);
                        Predicate endDate = cb.greaterThanOrEqualTo(root.<LocalDate>get("endDate"), date);

                        return cb.and(startDate, endDate);
                    }
                });

                Integer totalLivingGuest = 0;
                Integer totalLivingRoom = 0;
                Integer totalOutgoGuest = 0;
                Integer totalOutgoRoom = 0;
                Integer totalIncomeGuest = 0;
                Integer totalIncomeRoom = 0;
                Integer totalLateCheckOutRoom = 0;
//                Integer totalShortTimeGuest = 0;
//                Integer totalShortTimeRoom = 0;
                List<RoomUseItem> livingCollection = new ArrayList<>();
                List<RoomUseItem> outgoCollection = new ArrayList<>();
                List<RoomUseItem> incomeCollection = new ArrayList<>();

                for (RoomUse roomUse : roomUses) {
                    LocalDate startDate = roomUse.getStartDate();
                    LocalDate endDate = roomUse.getEndDate();
                    RoomUse.Status status = roomUse.getStatus();

                    Company company = roomUse.getCustomerGroup().getCompany();
                    Adult customer = roomUse.getCustomerGroup().getCustomer();

                    List<GroupMemberToRoomUse> groupMembers = groupMemberToRoomUseRepository.findByRoomUse(roomUse);

                    String firstName = "";
                    String lastName = "";
                    String patronymic = "";

                    if (customer != null) {
                        firstName = customer.getFirstName();
                        lastName = customer.getLastName();
                        if (customer.getPatronymic() != null) patronymic = customer.getPatronymic();
                    } else if (groupMembers != null) {
                        Adult firstCustomer = null;
                        Iterator<GroupMemberToRoomUse> iterator = groupMembers.iterator();
                        while (firstCustomer == null) {
                            Person mayBeAdult = iterator.next().getGroupMember().getPerson();
                            if (mayBeAdult instanceof Adult) firstCustomer = (Adult) mayBeAdult;
                        }
                        firstName = firstCustomer.getFirstName();
                        lastName = firstCustomer.getLastName();
                        if (firstCustomer.getPatronymic() != null) patronymic = firstCustomer.getPatronymic();
                    } else if (company != null) {
                        firstName = company.getName();
                    }

                    Integer qty = groupMembers == null ? 1 : groupMembers.size();

                    String earlyCheckIn = (roomUse.getCheckInTime() != null && PenaltyService.HandlingType.EARLY.canHandleWithinPeriod(roomUse.getCheckInTime(), settingsService)) ? "1" : "";
                    String lateCheckOut = (roomUse.getCheckOutTime() != null && PenaltyService.HandlingType.LATE.canHandleWithinPeriod(roomUse.getCheckOutTime(), settingsService)) ? "1" : "";

                    if (status == BaseGroupRoomUse.Status.LIVING && date.compareTo(endDate) < 0) {
                        totalLivingGuest += qty;
                        totalLivingRoom++;
                        livingCollection.add(new RoomUseItem(
                                roomUse.getRoom().getNumber(),
                                roomUse.getBaseRoom().getShortname(),
                                firstName,
                                lastName,
                                patronymic,
                                String.valueOf(qty),
                                startDate.toString(ChannelService.FORMATTER),
                                endDate.toString(ChannelService.FORMATTER),
                                "",
                                "",
                                "",
                                ""
                        ));
                    } else if (status == BaseGroupRoomUse.Status.LIVING && date.compareTo(endDate) == 0) {
                        totalOutgoGuest += qty;
                        totalOutgoRoom++;
                        if (roomUse.getCheckOutTime() != null && PenaltyService.HandlingType.LATE.canHandleWithinPeriod(roomUse.getCheckOutTime(), settingsService))
                            totalLateCheckOutRoom++;
                        outgoCollection.add(new RoomUseItem(
                                roomUse.getRoom().getNumber(),
                                roomUse.getBaseRoom().getShortname(),
                                firstName,
                                lastName,
                                patronymic,
                                String.valueOf(qty),
                                startDate.toString(ChannelService.FORMATTER),
                                "",
                                "",
                                "",
                                roomUse.getCheckOutTime() == null ? "" : roomUse.getCheckOutTime().toString("HH:mm"),
                                lateCheckOut
                        ));
                    } else if ((status == BaseGroupRoomUse.Status.BOOKING_FREE || status == BaseGroupRoomUse.Status.BOOKING_WARRANTY) && date.compareTo(startDate) == 0) {
                        totalIncomeGuest += qty;
                        totalIncomeRoom++;
                        incomeCollection.add(new RoomUseItem(
                                roomUse.getRoom().getNumber(),
                                roomUse.getBaseRoom().getShortname(),
                                firstName,
                                lastName,
                                patronymic,
                                String.valueOf(qty),
                                "",
                                endDate.toString(ChannelService.FORMATTER),
                                earlyCheckIn,
                                roomUse.getCheckInTime() == null ? "" : roomUse.getCheckInTime().toString("HH:mm"),
                                "",
                                ""
                        ));
                    }
                }
                parameters.put("livingCollection", livingCollection);
                parameters.put("outgoCollection", outgoCollection);
                parameters.put("incomeCollection", incomeCollection);

                parameters.put("reportDate", date.toString(ChannelService.FORMATTER));

                parameters.put("currentDate", now.toString(ChannelService.FORMATTER));
                parameters.put("currentTime", now.toString("HH:mm"));

                parameters.put("totalLivingGuest", String.valueOf(totalLivingGuest));
                parameters.put("totalLivingRoom", String.valueOf(totalLivingRoom));
                parameters.put("totalIncomeGuest", String.valueOf(totalIncomeGuest));
                parameters.put("totalIncomeRoom", String.valueOf(totalIncomeRoom));
                parameters.put("totalOutgoGuest", String.valueOf(totalOutgoGuest));
                parameters.put("totalOutgoRoom", String.valueOf(totalOutgoRoom));
                parameters.put("totalShortTimeGuest", "-"); // TODO ASK SLAVA
                parameters.put("totalShortTimeRoom", "-"); // TODO ASK SLAVA
                List<Room> rooms = roomRepository.findAll(new Specification<Room>() {
                    @Override
                    public Predicate toPredicate(Root<Room> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                        Predicate approved = cb.equal(root.<Boolean>get("approved"), true);
                        return cb.and(approved);
                    }
                });
                Integer totalRoomQty = rooms.size();
                parameters.put("totalRooms", String.valueOf(totalRoomQty));
                Double totalLoad = (totalLivingRoom + totalIncomeRoom + totalLateCheckOutRoom) / (double) totalRoomQty * 100;
                parameters.put("totalLoad", String.format("%.02f", totalLoad));
            }
        }.createReport();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BreakfastRoomUse {
        private String roomNumber;
        private String fullName;
        private String adults;
        private String children;
        private String additional;
        private String checkInTime;
        private String checkOutTime;
        private String earlyCheckIn;
    }

    @RequestMapping("breakfast")
    public void createBreakfast(@RequestParam(value = "reportDate", required = false) final Long reportDate, HttpServletResponse response) throws JRException, IOException {
        final LocalDate date = reportDate == null ? settingsService.getHotelDate() : new LocalDate(reportDate * 1000, DateTimeZone.UTC);
        final LocalDate nextDay = date.plusDays(1);

        new ReportCreator(response, getReport("breakfast", "jasper"), getReport("breakfast", "pdf")) {
            @Override
            void collectParameters(Map<String, Object> parameters) {
                List<RoomUse> roomUses = new ArrayList<>();
                List<RoomUse> roomUsesNextDay = new ArrayList<>();
                processRoomUses(roomUses, roomUsesNextDay);

                putOrEmpty(parameters, "date", date.toString(ChannelService.FORMATTER));
                putOrEmpty(parameters, "nextDayDate", nextDay.toString(ChannelService.FORMATTER));

                Integer breakfastAdults = 0;
                Integer breakfastChildren = 0;
                Integer breakfastAdditional = 0;

                Integer lunchAdults = 0;
                Integer lunchChildren = 0;
                Integer lunchAdditional = 0;

                Integer dinnerAdults = 0;
                Integer dinnerChildren = 0;
                Integer dinnerAdditional = 0;

                List<BreakfastRoomUse> breakfastRoomUses = new ArrayList<>();
                List<BreakfastRoomUse> lunchRoomUses = new ArrayList<>();
                List<BreakfastRoomUse> dinnerRoomUses = new ArrayList<>();
                for (RoomUse roomUse : roomUses) {
                    Board board = roomUse.getPlan().getBoard();
                    String firstName = "";
                    String lastName = "";
                    String patronymic = "";

                    Integer adults = 0;
                    Integer children = 0;
                    Integer additional = 0;
                    Integer roomAdults = roomUse.getBaseRoom().getAdults();
                    Integer roomChildren = roomUse.getBaseRoom().getChildren();
                    Integer roomAdditional = roomUse.getBaseRoom().getAdditional();

                    List<GroupMemberToRoomUse> groupMembers = groupMemberToRoomUseRepository.findByRoomUse(roomUse);

                    Adult customer = roomUse.getCustomerGroup().getCustomer();
                    if (customer != null) {
                        firstName = customer.getFirstName();
                        lastName = customer.getLastName();
                        if (customer.getPatronymic() != null) patronymic = customer.getPatronymic();
                    }
                    if (groupMembers != null) {
                        for (GroupMemberToRoomUse groupMember : groupMembers) {
                            Person person = groupMember.getGroupMember().getPerson();
                            if (person instanceof Adult) {
                                if (StringUtils.isEmpty(firstName)) firstName = person.getFirstName();
                                if (StringUtils.isEmpty(lastName)) lastName = person.getLastName();
                                if (StringUtils.isEmpty(patronymic) && person.getPatronymic() != null) patronymic = person.getPatronymic();
                                adults++;
                            } else if (person instanceof Child) {
                                children++;
                            } else {
                                throw new IllegalArgumentException("Unsupported subclass of Person: " + person);
                            }
                        }
                        int total = roomAdults + roomChildren;
                        if (total < groupMembers.size()) {
                            additional = groupMembers.size() - total;
                        }
                    }

                    String fullName = firstName + " " + lastName + " " + patronymic;

                    String earlyCheckIn = (roomUse.getCheckInTime() != null && PenaltyService.HandlingType.EARLY.canHandleWithinPeriod(roomUse.getCheckInTime(), settingsService)) ? "1" : "";

                    breakfastRoomUses.add(new BreakfastRoomUse(
                            roomUse.getRoom().getNumber(),
                            fullName,
                            String.valueOf(adults),
                            String.valueOf(children),
                            String.valueOf(additional),
                            roomUse.getStartDate().toString(ChannelService.FORMATTER),
                            roomUse.getEndDate().toString(ChannelService.FORMATTER),
                            earlyCheckIn
                    ));

                    breakfastAdults += adults;
                    breakfastChildren += children;
                    breakfastAdditional += additional;

                    if(board == Board.HB || board == Board.FB || board == Board.AI) {
                        lunchRoomUses.add(new BreakfastRoomUse(
                                roomUse.getRoom().getNumber(),
                                fullName,
                                String.valueOf(adults),
                                String.valueOf(children),
                                String.valueOf(additional),
                                roomUse.getStartDate().toString(ChannelService.FORMATTER),
                                roomUse.getEndDate().toString(ChannelService.FORMATTER),
                                earlyCheckIn
                        ));

                        lunchAdults += adults;
                        lunchChildren += children;
                        lunchAdditional += additional;
                    }

                    if(board == Board.FB || board == Board.AI) {
                        dinnerRoomUses.add(new BreakfastRoomUse(
                                roomUse.getRoom().getNumber(),
                                fullName,
                                String.valueOf(adults),
                                String.valueOf(children),
                                String.valueOf(additional),
                                roomUse.getStartDate().toString(ChannelService.FORMATTER),
                                roomUse.getEndDate().toString(ChannelService.FORMATTER),
                                earlyCheckIn
                        ));

                        dinnerAdults += adults;
                        dinnerChildren += children;
                        dinnerAdditional += additional;
                    }
                }
                parameters.put("breakfastCollection", breakfastRoomUses);
                parameters.put("lunchCollection", lunchRoomUses);
                parameters.put("dinnerCollection", dinnerRoomUses);

                Integer nextDayBreakfastAdults = 0;
                Integer nextDayBreakfastChildren = 0;
                Integer nextDayBreakfastAdditional = 0;

                Integer nextDayLunchAdults = 0;
                Integer nextDayLunchChildren = 0;
                Integer nextDayLunchAdditional = 0;

                Integer nextDayDinnerAdults = 0;
                Integer nextDayDinnerChildren = 0;
                Integer nextDayDinnerAdditional = 0;

                for (RoomUse roomUse : roomUsesNextDay) {
                    Board board = roomUse.getPlan().getBoard();
                    Integer adults = 0;
                    Integer children = 0;
                    Integer additional = 0;
                    Integer roomAdults = roomUse.getBaseRoom().getAdults();
                    Integer roomChildren = roomUse.getBaseRoom().getChildren();
                    Integer roomAdditional = roomUse.getBaseRoom().getAdditional();

                    List<GroupMemberToRoomUse> groupMembers = groupMemberToRoomUseRepository.findByRoomUse(roomUse);

                    if (groupMembers != null) {
                        for (GroupMemberToRoomUse groupMember : groupMembers) {
                            Person person = groupMember.getGroupMember().getPerson();
                            if (person instanceof Adult) {
                                adults++;
                            } else if (person instanceof Child) {
                                children++;
                            } else {
                                throw new IllegalArgumentException("Unsupported subclass of Person: " + person);
                            }
                        }
                        int total = roomAdults + roomChildren;
                        if (total < groupMembers.size()) {
                            additional = groupMembers.size() - total;
                        }
                    }

                    nextDayBreakfastAdults += adults;
                    nextDayBreakfastChildren += children;
                    nextDayBreakfastAdditional += additional;

                    if(board == Board.HB || board == Board.FB || board == Board.AI) {
                        nextDayLunchAdults += adults;
                        nextDayLunchChildren += children;
                        nextDayLunchAdditional += additional;
                    }

                    if(board == Board.FB || board == Board.AI) {
                        nextDayDinnerAdults += adults;
                        nextDayDinnerChildren += children;
                        nextDayDinnerAdditional += additional;
                    }
                }

                putOrEmpty(parameters, "breakfastAdults", breakfastAdults);
                putOrEmpty(parameters, "breakfastChildren", breakfastChildren);
                putOrEmpty(parameters, "breakfastAdditional", breakfastAdditional);

                putOrEmpty(parameters, "nextDayBreakfastAdults", nextDayBreakfastAdults);
                putOrEmpty(parameters, "nextDayBreakfastChildren", nextDayBreakfastChildren);
                putOrEmpty(parameters, "nextDayBreakfastAdditional", nextDayBreakfastAdditional);

                putOrEmpty(parameters, "lunchAdults", lunchAdults);
                putOrEmpty(parameters, "lunchChildren", lunchChildren);
                putOrEmpty(parameters, "lunchAdditional", lunchAdditional);

                putOrEmpty(parameters, "nextDayLunchAdults", nextDayLunchAdults);
                putOrEmpty(parameters, "nextDayLunchChildren", nextDayLunchChildren);
                putOrEmpty(parameters, "nextDayLunchAdditional", nextDayLunchAdditional);

                putOrEmpty(parameters, "dinnerAdults", dinnerAdults);
                putOrEmpty(parameters, "dinnerChildren", dinnerChildren);
                putOrEmpty(parameters, "dinnerAdditional", dinnerAdditional);

                putOrEmpty(parameters, "nextDayDinnerAdults", nextDayDinnerAdults);
                putOrEmpty(parameters, "nextDayDinnerChildren", nextDayDinnerChildren);
                putOrEmpty(parameters, "nextDayDinnerAdditional", nextDayDinnerAdditional);
            }

            private void processRoomUses(List<RoomUse> roomUses, List<RoomUse> roomUsesNextDay) {
                List<RoomUse> toProcess = roomUseRepository.findAll(new Specification<RoomUse>() {
                    @Override
                    public Predicate toPredicate(Root<RoomUse> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                        Board[] boardsWithBreakfast = {Board.AI, Board.BB, Board.FB, Board.HB};
                        Predicate board = root.<RoomUse, Plan>join("plan").<Board>get("board").in(boardsWithBreakfast);
                        Predicate startDate = cb.lessThanOrEqualTo(root.<LocalDate>get("startDate"), date);
                        Predicate endDate = cb.greaterThanOrEqualTo(root.<LocalDate>get("endDate"), date);

                        return cb.and(startDate, endDate, board);
                    }
                });
                for (RoomUse roomUse : toProcess) {
                    handleForDate(roomUse, date, roomUses);
                    handleForDate(roomUse, nextDay, roomUsesNextDay);
                }
            }

            private void handleForDate(RoomUse roomUse, LocalDate date, List<RoomUse> roomUses) {
                LocalDate startDate = roomUse.getStartDate();
                LocalDate endDate = roomUse.getEndDate();
                if (startDate.equals(date)) {
                    if (!settingsService.getHotelInfo().isEarlyCheckInNoBreakfast()
                            && roomUse.getStatus() == BaseGroupRoomUse.Status.LIVING
                            && PenaltyService.HandlingType.EARLY.canHandleWithinPeriod(roomUse.getCheckInTime(), settingsService)) {
                        roomUses.add(roomUse);
                    }
                } else if (startDate.isBefore(date) && !endDate.isBefore(date)) {
                    roomUses.add(roomUse);
                }
            }
        }.createReport();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BillPaymentItem {
        private String billId;
        private String billDate;
        private String rooms;
        private String bookingId;
        private String customer;
        private String dateSince;
        private String dateTo;
        private String cashTotal;
        private String cardTotal;
        private String nonCashTotal;
        private String total;
        private String notPaidTotal;
    }

    @RequestMapping("receptionCash")
    public void createReceptionCash(@RequestParam(value = "periodStart", required = false) final Long periodStart, @RequestParam(value = "periodEnd", required = false) final Long periodEnd, HttpServletResponse response) throws JRException, IOException {
        final LocalDateTime startDate = periodStart == null ? settingsService.getHotelTime() : new LocalDateTime(periodStart * 1000, DateTimeZone.UTC);
        final LocalDateTime endDate = periodEnd == null ? startDate.plusDays(1) : new LocalDateTime(periodEnd * 1000, DateTimeZone.UTC);
        new ReportCreator(response, getReport("receptionCash", "jasper"), getReport("receptionCash", "pdf")) {
            @Override
            void collectParameters(Map<String, Object> parameters) {
                putOrEmpty(parameters, "startDate", startDate.toString(ChannelService.FORMATTER));
                putOrEmpty(parameters, "endDate", endDate.toString(ChannelService.FORMATTER));

                List<Payment> payments = paymentRepository.findAll(new Specification<Payment>() {
                    @Override
                    public Predicate toPredicate(Root<Payment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                        Predicate end = cb.greaterThanOrEqualTo(root.<LocalDateTime>get("date"), startDate);
                        Predicate start = cb.lessThanOrEqualTo(root.<LocalDateTime>get("date"), endDate);
                        return cb.and(start, end);
                    }
                });

                List<Refund> refunds = refundRepository.findAll(new Specification<Refund>() {
                    @Override
                    public Predicate toPredicate(Root<Refund> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                        Predicate end = cb.greaterThanOrEqualTo(root.<LocalDateTime>get("createdDate"), startDate);
                        Predicate start = cb.lessThanOrEqualTo(root.<LocalDateTime>get("createdDate"), endDate);
                        Predicate total = cb.greaterThan(root.<Long>get("total"), 0L);
                        return cb.and(start, end, total);
                    }
                });

                Double billCashTotalUah = 0.00;
                Double billCardTotalUah = 0.00;
                Double billNoncashTotalUah = 0.00;
                Double refundTotalUah = 0.00;
                Double totalUah = 0.00;

                List<BillPaymentItem> cashCardCollection = new ArrayList<>();
                List<BillPaymentItem> nonCashCollection = new ArrayList<>();
                List<BillPaymentItem> refundCollection = new ArrayList<>();

                for (final Payment payment : payments) {
                    Bill bill = payment.getBill();
                    BankDetails.PaymentType paymentType = payment.getBankDetails().getPaymentType();
                    RoomUse roomUse = bill.getRoomUse();

                    Company company = roomUse != null ? roomUse.getCustomerGroup().getCompany() : payment.getBill().getGroup().getCompany();
                    Adult customer = roomUse != null ? roomUse.getCustomerGroup().getCustomer() : payment.getBill().getGroup().getCustomer();
                    List<GroupMemberToRoomUse> groupMembers = groupMemberToRoomUseRepository.findByRoomUse(roomUse);

                    String roomNumbers = "";
                    if (payment.getBill().getGroup() != null) {
                        List<RoomUse> groupRoomUses = roomUseRepository.findAll(new Specification<RoomUse>() {
                            @Override
                            public Predicate toPredicate(Root<RoomUse> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                                Predicate group = cb.equal(root.<CustomerGroup>get("customerGroup"), payment.getBill().getGroup());
                                return cb.and(group);
                            }
                        });
                        if (groupRoomUses.size() > 0) {
                            for (RoomUse groupRoomUse : groupRoomUses) {
                                roomNumbers += roomNumbers.isEmpty() ? String.valueOf(groupRoomUse.getRoom().getNumber()) : "; " + String.valueOf(groupRoomUse.getRoom().getNumber());
                            }
                        }
                    }

                    String firstName = "";
                    String lastName = "";
                    String patronymic = "";

                    if (customer != null) {
                        firstName = customer.getFirstName();
                        lastName = customer.getLastName();
                        if (customer.getPatronymic() != null) patronymic = customer.getPatronymic();
                    } else if (groupMembers != null) {
                        Adult firstCustomer = null;
                        Iterator<GroupMemberToRoomUse> iterator = groupMembers.iterator();
                        while (firstCustomer == null && iterator.hasNext()) {
                            Person mayBeAdult = iterator.next().getGroupMember().getPerson();
                            if (mayBeAdult instanceof Adult) {
                                firstCustomer = (Adult) mayBeAdult;
                            }
                        }
                        if (firstCustomer == null) {
                            throw new IllegalStateException("No customers in the booking");
                        }
                        firstName = firstCustomer.getFirstName();
                        lastName = firstCustomer.getLastName();
                        if (customer.getPatronymic() != null) {
                            patronymic = firstCustomer.getPatronymic();
                        }
                    } else if (company != null) firstName = company.getName();

                    String fullName = firstName + " " + lastName + " " + patronymic;

                    if (paymentType == BankDetails.PaymentType.CASH || paymentType == BankDetails.PaymentType.CARD) {
                        if (paymentType == BankDetails.PaymentType.CASH) billCashTotalUah += /*bill.getTotal() == 0 ? 0 : */(double) payment.getTotal() / 100;
                        if (paymentType == BankDetails.PaymentType.CARD) billCardTotalUah += /*bill.getTotal() == 0 ? 0 : */(double) payment.getTotal() / 100;
                        cashCardCollection.add(new BillPaymentItem(
                                String.valueOf(bill.getId()), // billId
                                bill.getCreatedDate().toString("dd/MM/yyyy HH:mm"), // billDate
                                roomUse == null ? roomNumbers : roomUse.getRoom().getNumber(), // rooms
                                roomUse == null ? String.valueOf(bill.getGroup().getId()) : String.valueOf(roomUse.getId()), // bookingId
                                fullName, // customer
                                roomUse == null ? "" : roomUse.getStartDate().toString(ChannelService.FORMATTER), // dateSince
                                roomUse == null ? "" : roomUse.getEndDate().toString(ChannelService.FORMATTER), // dateTo
                                paymentType == BankDetails.PaymentType.CASH /*&& bill.getTotal() != 0 */ ? String.format("%.02f", (double) payment.getTotal() / 100) : "0", // cashTotal
                                paymentType == BankDetails.PaymentType.CARD /*&& bill.getTotal() != 0 */ ? String.format("%.02f", (double) payment.getTotal() / 100) : "0", // cardTotal
                                "", // nonCashTotal
                                String.format("%.02f", (double) bill.getTotal() / 100), // total
                                "" // notPaidTotal
                        ));
                    } else {
                        billNoncashTotalUah += /*bill.getTotal() == 0 ? 0 : */(double) payment.getTotal() / 100;
                        nonCashCollection.add(new BillPaymentItem(
                                String.valueOf(bill.getId()), // billId
                                bill.getCreatedDate().toString("dd/MM/yyyy HH:mm"), // billDate
                                roomUse == null ? roomNumbers : roomUse.getRoom().getNumber(), // rooms
                                roomUse == null ? String.valueOf(bill.getGroup().getId()) : String.valueOf(roomUse.getId()), // bookingId
                                fullName, // customer
                                roomUse == null ? "" : roomUse.getStartDate().toString(ChannelService.FORMATTER), // dateSince
                                roomUse == null ? "" : roomUse.getEndDate().toString(ChannelService.FORMATTER), // dateTo
                                "", // cashTotal
                                "", // cardTotal
                                /*bill.getTotal() != 0 ? */String.format("%.02f", (double) payment.getTotal() / 100)/* : "0"*/, // total
                                String.format("%.02f", (double) bill.getTotal() / 100), // nonCashTotal
                                "" // notPaidTotal
                        ));
                    }
                }

                for (final Refund refund : refunds) {
                    RoomUse roomUse = refund.getRoomUse();

                    Company company = roomUse != null ? roomUse.getCustomerGroup().getCompany() : refund.getGroup().getCompany();
                    Adult customer = roomUse != null ? roomUse.getCustomerGroup().getCustomer() : refund.getGroup().getCustomer();
                    List<GroupMemberToRoomUse> groupMembers = groupMemberToRoomUseRepository.findByRoomUse(roomUse);

                    String firstName = "";
                    String lastName = "";
                    String patronymic = "";

                    if (customer != null) {
                        firstName = customer.getFirstName();
                        lastName = customer.getLastName();
                        if (customer.getPatronymic() != null) patronymic = customer.getPatronymic();
                    } else if (groupMembers != null) {
                        Adult firstCustomer = null;
                        Iterator<GroupMemberToRoomUse> iterator = groupMembers.iterator();
                        while (firstCustomer == null && iterator.hasNext()) {
                            Person mayBeAdult = iterator.next().getGroupMember().getPerson();
                            if (mayBeAdult instanceof Adult) {
                                firstCustomer = (Adult) mayBeAdult;
                            }
                        }
                        if (firstCustomer == null) {
                            throw new IllegalStateException("No customers in the booking");
                        }
                        firstName = firstCustomer.getFirstName();
                        lastName = firstCustomer.getLastName();
                        if (customer.getPatronymic() != null) {
                            patronymic = firstCustomer.getPatronymic();
                        }
                    } else if (company != null) firstName = company.getName();

                    String fullName = firstName + " " + lastName + " " + patronymic;

                    refundTotalUah += refund.getTotal() / 100;

                    String roomNumbers = "";
                    if (refund.getGroup() != null) {
                        List<RoomUse> groupRoomUses = roomUseRepository.findAll(new Specification<RoomUse>() {
                            @Override
                            public Predicate toPredicate(Root<RoomUse> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                                Predicate group = cb.equal(root.<CustomerGroup>get("customerGroup"), refund.getGroup());
                                return cb.and(group);
                            }
                        });
                        if (groupRoomUses.size() > 0) {
                            for (RoomUse groupRoomUse : groupRoomUses) {
                                roomNumbers += roomNumbers.isEmpty() ? String.valueOf(groupRoomUse.getRoom().getNumber()) : "; " + String.valueOf(groupRoomUse.getRoom().getNumber());
                            }
                        }
                    }

                    refundCollection.add(new BillPaymentItem(
                            String.valueOf(refund.getId()), // billId
                            refund.getCreatedDate().toString("dd/MM/yyyy HH:mm"), // billDate
                            roomUse == null ? roomNumbers : roomUse.getRoom().getNumber(), // rooms
                            roomUse == null ? String.valueOf(refund.getGroup().getId()) : String.valueOf(roomUse.getId()), // bookingId
                            fullName, // customer
                            roomUse == null ? "" : roomUse.getStartDate().toString(ChannelService.FORMATTER), // dateSince
                            roomUse == null ? "" : roomUse.getEndDate().toString(ChannelService.FORMATTER), // dateTo
                            "", // cashTotal
                            "", // cardTotal
                            "", // nonCashTotal
                            String.format("%.02f", (double) refund.getTotal() / 100), // total
                            "" // notPaidTotal
                    ));
                }

                totalUah = billCashTotalUah + billCardTotalUah + billNoncashTotalUah - refundTotalUah;

                parameters.put("cashCardCollection", cashCardCollection);
                parameters.put("nonCashCollection", nonCashCollection);
                parameters.put("refundCollection", refundCollection);

//                Double usd = 11.00;
//                Double eur = 15.00;
//                Double rur = 0.30;

                putOrEmpty(parameters, "billCashTotalUah", String.format("%.02f", billCashTotalUah));
//                putOrEmpty(parameters, "billCashTotalUsd", String.format("%.02f", billCashTotalUah / usd));
//                putOrEmpty(parameters, "billCashTotalEur", String.format("%.02f", billCashTotalUah / eur));
//                putOrEmpty(parameters, "billCashTotalRur", String.format("%.02f", billCashTotalUah / rur));

                putOrEmpty(parameters, "billCardTotalUah", String.format("%.02f", billCardTotalUah));
//                putOrEmpty(parameters, "billCardTotalUsd", String.format("%.02f", billCardTotalUah / usd));
//                putOrEmpty(parameters, "billCardTotalEur", String.format("%.02f", billCardTotalUah / eur));
//                putOrEmpty(parameters, "billCardTotalRur", String.format("%.02f", billCardTotalUah / rur));

                putOrEmpty(parameters, "billNoncashTotalUah", String.format("%.02f", billNoncashTotalUah));
//                putOrEmpty(parameters, "billNoncashTotalUsd", String.format("%.02f", billNoncashTotalUah / usd));
//                putOrEmpty(parameters, "billNoncashTotalEur", String.format("%.02f", billNoncashTotalUah / eur));
//                putOrEmpty(parameters, "billNoncashTotalRur", String.format("%.02f", billNoncashTotalUah / rur));

                putOrEmpty(parameters, "refundTotalUah", String.format("%.02f", refundTotalUah));
//                putOrEmpty(parameters, "refundTotalUsd", String.format("%.02f", refundTotalUah / usd));
//                putOrEmpty(parameters, "refundTotalEur", String.format("%.02f", refundTotalUah / eur));
//                putOrEmpty(parameters, "refundTotalRur", String.format("%.02f", refundTotalUah / rur));

                putOrEmpty(parameters, "totalUah", String.format("%.02f", totalUah));
//                putOrEmpty(parameters, "totalUsd", String.format("%.02f", totalUah / usd));
//                putOrEmpty(parameters, "totalEur", String.format("%.02f", totalUah / eur));
//                putOrEmpty(parameters, "totalRur", String.format("%.02f", totalUah / rur));

//                spendItem
//                correspondent
//                total

//                restStartUah
//                restStartUsd
//                restStartEur
//                restStartRur

//                serviceTotalUah
//                serviceTotalUsd
//                serviceTotalEur
//                serviceTotalRur

//                depositTotalUah
//                depositTotalUsd
//                depositTotalEur
//                depositTotalRur

//                otherTotalUah
//                otherTotalUsd
//                otherTotalEur
//                otherTotalRur

//                restEndUah
//                restEndUsd
//                restEndEur
//                restEndRur
            }
        }.createReport();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoomItem {
        private String room;
        private String qty;
        private String checkInTime;
        private String endDate;
        private String totalPaid;
        private String lastName;
        private String firstName;
        private String patronymic;
        private String company;
        private String note;
    }

    @RequestMapping("roomLoadMap")
    public void createRoomLoadMap(@RequestParam(value = "reportDate", required = false) final Long reportDate, HttpServletResponse response) throws JRException, IOException {
        final LocalDate date = reportDate == null ? settingsService.getHotelDate() : new LocalDate(reportDate * 1000, DateTimeZone.UTC);
        new ReportCreator(response, getReport("roomLoadMap", "jasper"), getReport("roomLoadMap", "pdf")) {
            @Override
            void collectParameters(Map<String, Object> parameters) {
                putOrEmpty(parameters, "reportDate", date.toString(ChannelService.FORMATTER));

                List<RoomUse> roomUses = roomUseRepository.findAll(new Specification<RoomUse>() {
                    @Override
                    public Predicate toPredicate(Root<RoomUse> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                        RoomUse.Status[] statuses = {BaseGroupRoomUse.Status.BOOKING_FREE, BaseGroupRoomUse.Status.BOOKING_WARRANTY, BaseGroupRoomUse.Status.LIVING};
                        Predicate status = root.<RoomUse.Status>get("status").in(statuses);
                        Predicate startDate = cb.lessThanOrEqualTo(root.<LocalDate>get("startDate"), date);
                        Predicate endDate = cb.greaterThanOrEqualTo(root.<LocalDate>get("endDate"), date);
                        return cb.and(status, startDate, endDate);
                    }
                });

                List<RoomItem> dataCollection = new ArrayList<>();

                for (RoomUse roomUse : roomUses) {
                    List<GroupMemberToRoomUse> groupMembers = groupMemberToRoomUseRepository.findByRoomUse(roomUse);

                    Integer qty = groupMembers == null ? 1 : groupMembers.size();

                    String firstName = "";
                    String lastName = "";
                    String patronymic = "";
                    Adult customer = roomUse.getCustomerGroup().getCustomer();
                    if (customer != null) {
                        firstName = customer.getFirstName();
                        lastName = customer.getLastName();
                        if (customer.getPatronymic() != null) patronymic = customer.getPatronymic();
                    } else if (groupMembers != null) {
                        Adult firstCustomer = null;
                        Iterator<GroupMemberToRoomUse> iterator = groupMembers.iterator();
                        while (firstCustomer == null) {
                            Person mayBeAdult = iterator.next().getGroupMember().getPerson();
                            if (mayBeAdult instanceof Adult) firstCustomer = (Adult) mayBeAdult;
                        }
                        firstName = firstCustomer.getFirstName();
                        lastName = firstCustomer.getLastName();
                        if (firstCustomer.getPatronymic() != null) patronymic = firstCustomer.getPatronymic();
                    }
                    String company = roomUse.getCustomerGroup().getCompany() == null ? "" : roomUse.getCustomerGroup().getCompany().getName();

                    String checkInTime = roomUse.getCheckInTime() == null ? roomUse.getStartDate().toString(ChannelService.FORMATTER) : roomUse.getCheckInTime().toString(ChannelService.FORMATTER);

                    dataCollection.add(new RoomItem(
                            roomUse.getRoom().getNumber(),
                            String.valueOf(qty),
                            checkInTime,
                            roomUse.getEndDate().toString(ChannelService.FORMATTER),
                            String.format("%.02f", (float) roomUse.getTotalPaid() / 100),
                            firstName,
                            lastName,
                            patronymic,
                            company,
                            roomUse.getDescription()
                    ));
                }

                parameters.put("dataCollection", dataCollection);
            }
        }.createReport();
    }
}
