package com.idgindigo.pms.web.controller.pms.reports;


import com.idgindigo.pms.domain.extranet.BaseGroupRoomUse;
import com.idgindigo.pms.domain.extranet.person.Adult;
import com.idgindigo.pms.domain.extranet.person.Person;
import com.idgindigo.pms.domain.pms.BankDetails;
import com.idgindigo.pms.domain.pms.Bill;
import com.idgindigo.pms.domain.pms.Company;
import com.idgindigo.pms.domain.pms.GroupMemberToRoomUse;
import com.idgindigo.pms.domain.pms.Payment;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.restutils.exception.RestFriendlyException;
import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.service.channels.ChannelService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.sf.jasperreports.engine.JRException;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.LocalDate;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by misha on 25.04.14.
 */
@Controller
public class ApprovedController extends ReportController {

    @RequestMapping("guestForm")
    public void createGuestForm(@RequestParam("customerId") final Long customerId, HttpServletResponse response) throws JRException, IOException {
        new ReportCreator(response, getReport("guestForm", "jasper"), getReport("guestForm", "pdf")) {
            @Override
            void collectParameters(Map<String, Object> parameters) {
                getFormData(customerId, parameters);
            }
        }.createReport();
    }

    @RequestMapping("guestCard")
    public void createGuestCard(@RequestParam("customerId") final Long customerId, HttpServletResponse response) throws JRException, IOException {
        new ReportCreator(response, getReport("guestCard", "jasper"), getReport("guestCard", "pdf")) {
            @Override
            void collectParameters(Map<String, Object> parameters) {
                getFormData(customerId, parameters);
            }
        }.createReport();
    }

    @RequestMapping("hotelCard")
    public void createHotelCard(@RequestParam("customerId") final Long customerId, HttpServletResponse response) throws JRException, IOException {
        new ReportCreator(response, getReport("hotelCard", "jasper"), getReport("hotelCard", "pdf")) {
            @Override
            void collectParameters(Map<String, Object> parameters) {
                getFormData(customerId, parameters);
            }
        }.createReport();
    }

    public void getFormData(Long customerId, Map<String, Object> parameters) {
        Adult adult = adultRepository.findOne(customerId);
        if (adult == null) throw new RestFriendlyException(RestFriendlyException.INCORRECT_VALUE, "customerId");
        putOrEmpty(parameters, "hotelName", SecurityUtils.getHotel().getInfo().getName());
        putOrEmpty(parameters, "hotelAddress", SecurityUtils.getHotel().getInfo().getOfficialAddress());
        putOrEmpty(parameters, "hotelCity", SecurityUtils.getHotel().getInfo().getCity());
        putOrEmpty(parameters, "hotelPhone", SecurityUtils.getHotel().getInfo().getInfoPhone());

        BankDetails bankDetails = bankDetailsRepository.getDefault();
//                if (bankDetails == null) throw new RestFriendlyException(RestFriendlyException.INCORRECT_VALUE, "bankDetails");
        putOrEmpty(parameters, "hotelEdrpou", bankDetails == null ? null : bankDetails.getEdrpou());
        putOrEmpty(parameters, "hotelDkud", null);

        List<GroupMemberToRoomUse> roomUses = groupMemberToRoomUseRepository.findByPersonId(customerId);
        if (roomUses.isEmpty()) throw new RestFriendlyException(RestFriendlyException.INCORRECT_VALUE, "customer.rooms");
        GroupMemberToRoomUse gmtru = roomUses.get(0);
        RoomUse roomUse = gmtru.getRoomUse();
        putOrEmpty(parameters, "bookingId", roomUse.getId());
        putOrEmpty(parameters, "room", roomUse.getRoom().getNumber());
        putOrEmpty(parameters, "startDate", roomUse.getStartDate().toString(ChannelService.FORMATTER));
        putOrEmpty(parameters, "endDate", roomUse.getEndDate().toString(ChannelService.FORMATTER));
        putOrEmpty(parameters, "guestId", String.valueOf(customerId));
        putOrEmpty(parameters, "firstName", adult.getFirstName());
        putOrEmpty(parameters, "lastName", adult.getLastName());
        putOrEmpty(parameters, "patronymic", adult.getPatronymic());
        putOrEmpty(parameters, "dob", adult.getDob() == null ? null : adult.getDob().toString(ChannelService.FORMATTER));
        putOrEmpty(parameters, "passport", adult.getPassportNumber());
        putOrEmpty(parameters, "address", adult.getAddress());
        putOrEmpty(parameters, "visaNumber", null);//TODO ADD THIS FIELD
        putOrEmpty(parameters, "visaValidUntil", null);//TODO ADD THIS FIELD
        putOrEmpty(parameters, "durationOfStay", String.valueOf(Days.daysBetween(roomUse.getStartDate(), roomUse.getEndDate()).getDays()));
        putOrEmpty(parameters, "pov", lang(String.valueOf(gmtru.getGroupMember().getCustomerGroup().getPov())));
        putOrEmpty(parameters, "registrationNumber", null);//TODO ADD THIS FIELD
        putOrEmpty(parameters, "country", adult.getCountry() == null ? "" : new Locale("", adult.getCountry()).getDisplayCountry(UserLocale));
        putOrEmpty(parameters, "city", adult.getCity());
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoomItem {
        private String bookingId;
        private String name;
        private String paymentType;
        private String citizenship;
        private String roomType;
        private String qty;
        private String startDate;
        private String endDate;
        private String description;
    }

    @RequestMapping("bookingRegBook")
    public void createBookingRegBook(@RequestParam(value = "periodStart", required = false) final Long periodStart, @RequestParam(value = "periodEnd", required = false) final Long periodEnd, HttpServletResponse response) throws JRException, IOException {
        final LocalDate startDate = periodStart == null ? settingsService.getHotelDate() : new LocalDate(periodStart * 1000, DateTimeZone.UTC);
        final LocalDate endDate = periodEnd == null ? startDate.plusDays(1) : new LocalDate(periodEnd * 1000, DateTimeZone.UTC);
        new ReportCreator(response, getReport("bookingRegBook", "jasper"), getReport("bookingRegBook", "pdf")) {
            @Override
            void collectParameters(Map<String, Object> parameters) {
                putOrEmpty(parameters, "startDate", startDate.toString(ChannelService.FORMATTER));
                putOrEmpty(parameters, "endDate", endDate.toString(ChannelService.FORMATTER));

                putOrEmpty(parameters, "hotelName", SecurityUtils.getHotel().getInfo().getName());
                BankDetails bankDetails = bankDetailsRepository.getDefault();
//                if (bankDetails == null) throw new RestFriendlyException(RestFriendlyException.INCORRECT_VALUE, "bankDetails");
                putOrEmpty(parameters, "hotelEdrpou", bankDetails == null ? null : bankDetails.getEdrpou());
                putOrEmpty(parameters, "hotelDkud", null);

                List<RoomUse> roomUses = roomUseRepository.findAll(new Specification<RoomUse>() {
                    @Override
                    public Predicate toPredicate(Root<RoomUse> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                        RoomUse.Status[] statuses = {BaseGroupRoomUse.Status.BOOKING_FREE, BaseGroupRoomUse.Status.BOOKING_WARRANTY};
                        Predicate status = root.<RoomUse.Status>get("status").in(statuses);
                        Predicate sDate = cb.lessThanOrEqualTo(root.<LocalDate>get("startDate"), endDate);
                        Predicate eDate = cb.greaterThanOrEqualTo(root.<LocalDate>get("endDate"), startDate);
                        return cb.and(status, sDate, eDate);
                    }
                });

                List<RoomItem> dataCollection = new ArrayList<>();

                for (RoomUse roomUse : roomUses) {
                    List<GroupMemberToRoomUse> groupMembers = groupMemberToRoomUseRepository.findByRoomUse(roomUse);

                    Integer qty = groupMembers == null ? 1 : groupMembers.size();

                    Company company = roomUse.getCustomerGroup().getCompany();
                    Adult customer = roomUse.getCustomerGroup().getCustomer();
                    String name = "";
                    String citizenship = "";
                    if (company != null) {
                        name = company.getName();
                        citizenship = company.getCountry();
                    } else if (customer != null) {
                        name = customer.getLastName() + " " + customer.getFirstName();
                        name += customer.getPatronymic() == null ? "" : " " + customer.getPatronymic();
                        citizenship = customer.getCountry();
                    } else if (groupMembers != null) {
                        Adult firstCustomer = null;
                        Iterator<GroupMemberToRoomUse> iterator = groupMembers.iterator();
                        while (firstCustomer == null) {
                            Person mayBeAdult = iterator.next().getGroupMember().getPerson();
                            if (mayBeAdult instanceof Adult) {
                                firstCustomer = (Adult) mayBeAdult;
                                name = firstCustomer.getLastName() + " " + firstCustomer.getFirstName();
                                name += firstCustomer.getPatronymic() == null ? "" : " " + firstCustomer.getPatronymic();
                                citizenship = firstCustomer.getCountry();
                            }
                        }
                    }

                    final List bills = billRepository.findByRoomUse(roomUse);
                    List<Payment> payments = bills.isEmpty() ? Collections.<Payment>emptyList() : paymentRepository.findAll(new Specification<Payment>() {
                        @Override
                        public Predicate toPredicate(Root<Payment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                            Predicate b = root.<Bill>get("bill").in(bills);
                            return cb.and(b);
                        }
                    });

                    String paymentType = "";
                    Map<String, String> paymentTypes = new HashMap<>();
                    for (Payment item : payments) {
                        String key = String.valueOf(item.getBankDetails().getPaymentType());
                        if (paymentTypes.get(key) == null) {
                            paymentTypes.put(key, lang("paymentType." + String.valueOf(item.getBankDetails().getPaymentType())));
                            paymentType += paymentType.isEmpty() ? paymentTypes.get(key) : ", " + paymentTypes.get(key);
                        }
                    }

                    dataCollection.add(new RoomItem(
                            String.valueOf(roomUse.getId()),
                            name,
                            paymentType,
                            StringUtils.isEmpty(citizenship) ? "" : new Locale("", citizenship).getDisplayCountry(UserLocale),
                            roomUse.getBaseRoom().getName(),
                            String.valueOf(1),
                            roomUse.getStartDate().toString(ChannelService.FORMATTER),
                            roomUse.getEndDate().toString(ChannelService.FORMATTER),
                            roomUse.getDescription()
                    ));
                }

                parameters.put("dataCollection", dataCollection);

            }
        }.createReport();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class guestBookItem {
        private String bookingId;
        private String name;
        private String startDate;
        private String room;
        private String paid;
        private String paymentType;
        private String endDate;
        private String days;
    }

    @RequestMapping("guestBook")
    public void createGuestBook(@RequestParam(value = "periodStart", required = false) final Long periodStart, @RequestParam(value = "periodEnd", required = false) final Long periodEnd, HttpServletResponse response) throws JRException, IOException {
        final LocalDate startDate = periodStart == null ? settingsService.getHotelDate() : new LocalDate(periodStart * 1000, DateTimeZone.UTC);
        final LocalDate endDate = periodEnd == null ? startDate.plusDays(1) : new LocalDate(periodEnd * 1000, DateTimeZone.UTC);
        new ReportCreator(response, getReport("guestBook", "jasper"), getReport("guestBook", "pdf")) {
            @Override
            void collectParameters(Map<String, Object> parameters) {
                putOrEmpty(parameters, "startDate", startDate.toString(ChannelService.FORMATTER));
                putOrEmpty(parameters, "endDate", endDate.toString(ChannelService.FORMATTER));

                putOrEmpty(parameters, "hotelName", SecurityUtils.getHotel().getInfo().getName());
                BankDetails bankDetails = bankDetailsRepository.getDefault();
//                if (bankDetails == null) throw new RestFriendlyException(RestFriendlyException.INCORRECT_VALUE, "bankDetails");
                putOrEmpty(parameters, "hotelEdrpou", bankDetails == null ? null : bankDetails.getEdrpou());
                putOrEmpty(parameters, "hotelDkud", null);

                List<RoomUse> roomUses = roomUseRepository.findAll(new Specification<RoomUse>() {
                    @Override
                    public Predicate toPredicate(Root<RoomUse> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                        RoomUse.Status[] statuses = {BaseGroupRoomUse.Status.OUTGO, BaseGroupRoomUse.Status.LIVING};
                        Predicate status = root.<RoomUse.Status>get("status").in(statuses);
                        Predicate sDate = cb.lessThanOrEqualTo(root.<LocalDate>get("startDate"), endDate);
                        Predicate eDate = cb.greaterThanOrEqualTo(root.<LocalDate>get("endDate"), startDate);
                        return cb.and(status, sDate, eDate);
                    }
                });

                Collections.sort(roomUses, new Comparator<RoomUse>() {
                    public int compare(RoomUse o1, RoomUse o2) {
                        return o1.getStartDate().compareTo(o2.getStartDate());
                    }
                });

                Integer n = 1;
                List<guestBookItem> dataCollection = new ArrayList<>();

                for (RoomUse roomUse : roomUses) {
                    Integer hotelCheckOut = settingsService.getHotelInfo().getCheckOut();
                    String checkInTime = roomUse.getCheckInTime().toString("dd/MM/yyy HH:mm");
                    String checkOutTime = roomUse.getStatus() == BaseGroupRoomUse.Status.OUTGO ? roomUse.getCheckOutTime().toString("dd/MM/yyy HH:mm") : roomUse.getEndDate().toDateTimeAtStartOfDay().plusMinutes(hotelCheckOut).toString("dd/MM/yyy HH:mm");
                    Long paid = roomUse.getTotalPaid();
                    Integer days = Days.daysBetween(roomUse.getStartDate(), roomUse.getEndDate()).getDays();
                    String room = roomUse.getRoom().getNumber();

                    final List bills = billRepository.findByRoomUse(roomUse);
                    List<Payment> payments = bills.isEmpty() ? Collections.<Payment>emptyList() : paymentRepository.findAll(new Specification<Payment>() {
                        @Override
                        public Predicate toPredicate(Root<Payment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                            Predicate b = root.<Bill>get("bill").in(bills);
                            return cb.and(b);
                        }
                    });

                    String paymentType = "";
                    Map<String, String> paymentTypes = new HashMap<String, String>();
                    for (Payment item : payments) {
                        String key = String.valueOf(item.getBankDetails().getPaymentType());
                        if (paymentTypes.get(key) == null) {
                            paymentTypes.put(key, lang("paymentType." + String.valueOf(item.getBankDetails().getPaymentType())));
                            paymentType += paymentType.isEmpty() ? paymentTypes.get(key) : ", " + paymentTypes.get(key);
                        }
                    }

                    List<GroupMemberToRoomUse> groupMembers = groupMemberToRoomUseRepository.findByRoomUse(roomUse);
                    for (GroupMemberToRoomUse member : groupMembers) {
                        Adult adult = member.getGroupMember().getCustomerGroup().getCustomer();
                        Person person = member.getGroupMember().getPerson();
                        String name = person.getLastName() + " " + person.getFirstName();
                        name += person.getPatronymic() == null ? "" : " " + person.getPatronymic();

                        if (adult != null && (adult.getCountry() == null || adult.getCountry().equals(getHotelCountry()))) {
                            dataCollection.add(new guestBookItem(
                                    String.valueOf(n),
                                    name,
                                    checkInTime,
                                    room,
                                    String.format("%.02f", (float) paid / 100),
                                    paymentType,
                                    checkOutTime,
                                    String.valueOf(days)
                            ));
                            n += 1;
                        }
                    }
                }

                parameters.put("dataCollection", dataCollection);

            }
        }.createReport();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class foreignGuestBookItem {
        private String bookingId;
        private String startDate;
        private String name;
        private String dob;
        private String passportVisa;
        private String country;
        private String entryDateKpp;
        private String company;
        private String pov;
        private String room;
        private String regExpiresPerson;
        private String endDate;
    }

    @RequestMapping("foreignGuestBook")
    public void createForeignGuestBook(@RequestParam(value = "periodStart", required = false) final Long periodStart, @RequestParam(value = "periodEnd", required = false) final Long periodEnd, HttpServletResponse response) throws JRException, IOException {
        final LocalDate startDate = periodStart == null ? settingsService.getHotelDate() : new LocalDate(periodStart * 1000, DateTimeZone.UTC);
        final LocalDate endDate = periodEnd == null ? startDate.plusDays(1) : new LocalDate(periodEnd * 1000, DateTimeZone.UTC);
        new ReportCreator(response, getReport("foreignGuestBook", "jasper"), getReport("foreignGuestBook", "pdf")) {
            @Override
            void collectParameters(Map<String, Object> parameters) {
                putOrEmpty(parameters, "startDate", startDate.toString(ChannelService.FORMATTER));
                putOrEmpty(parameters, "endDate", endDate.toString(ChannelService.FORMATTER));

                putOrEmpty(parameters, "hotelName", SecurityUtils.getHotel().getInfo().getName());
                BankDetails bankDetails = bankDetailsRepository.getDefault();
//                if (bankDetails == null) throw new RestFriendlyException(RestFriendlyException.INCORRECT_VALUE, "bankDetails");
                putOrEmpty(parameters, "hotelEdrpou", bankDetails == null ? null : bankDetails.getEdrpou());
                putOrEmpty(parameters, "hotelDkud", null);

                List<RoomUse> roomUses = roomUseRepository.findAll(new Specification<RoomUse>() {
                    @Override
                    public Predicate toPredicate(Root<RoomUse> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                        RoomUse.Status[] statuses = {BaseGroupRoomUse.Status.OUTGO, BaseGroupRoomUse.Status.LIVING};
                        Predicate status = root.<RoomUse.Status>get("status").in(statuses);
                        Predicate sDate = cb.lessThanOrEqualTo(root.<LocalDate>get("startDate"), endDate);
                        Predicate eDate = cb.greaterThanOrEqualTo(root.<LocalDate>get("endDate"), startDate);
                        return cb.and(status, sDate, eDate);
                    }
                });

                Collections.sort(roomUses, new Comparator<RoomUse>() {
                    public int compare(RoomUse o1, RoomUse o2) {
                        return o1.getStartDate().compareTo(o2.getStartDate());
                    }
                });

                Integer n = 1;

                List<foreignGuestBookItem> dataCollection = new ArrayList<>();

                for (RoomUse roomUse : roomUses) {
                    final List bills = billRepository.findByRoomUse(roomUse);
                    List<Payment> payments = bills.isEmpty() ? Collections.<Payment>emptyList() : paymentRepository.findAll(new Specification<Payment>() {
                        @Override
                        public Predicate toPredicate(Root<Payment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                            Predicate b = root.<Bill>get("bill").in(bills);
                            return cb.and(b);
                        }
                    });

                    String paymentType = "";
                    Map<String, String> paymentTypes = new HashMap<String, String>();
                    for (Payment item : payments) {
                        String key = String.valueOf(item.getBankDetails().getPaymentType());
                        if (paymentTypes.get(key) == null) {
                            paymentTypes.put(key, lang("paymentType." + String.valueOf(item.getBankDetails().getPaymentType())));
                            paymentType += paymentType.isEmpty() ? paymentTypes.get(key) : ", " + paymentTypes.get(key);
                        }
                    }

                    List<GroupMemberToRoomUse> groupMembers = groupMemberToRoomUseRepository.findByRoomUse(roomUse);
                    for (GroupMemberToRoomUse member : groupMembers) {
                        Adult adult = member.getGroupMember().getCustomerGroup().getCustomer();
                        Person person = member.getGroupMember().getPerson();
                        String name = person.getLastName() + " " + person.getFirstName();
                        name += person.getPatronymic() == null ? "" : " " + person.getPatronymic();

                        if (adult != null && adult.getCountry() != null && adult.getCountry() != getHotelCountry()) {
                            String passportVisa = "";
                            String entryDateKpp = "";
                            String regExpiresPerson = adult.getEntryValidFrom() == null || adult.getEntryValidTill() == null ? "" : lang("visaValidity") + " " + adult.getEntryValidFrom() + " - " + adult.getEntryValidTill();
//                            next 'if' is always true at this point!!!!
//                            if (adult != null) {
                            passportVisa += StringUtils.isBlank(adult.getPassportNumber()) ? "" : adult.getPassportNumber();
                            passportVisa += StringUtils.isBlank(adult.getPassportValidTill()) ? "" : ", " + adult.getPassportValidTill();
                            passportVisa += StringUtils.isBlank(adult.getPassportIssued()) ? "" : ", " + adult.getPassportIssued();
                            passportVisa += adult.getVisaType() == null ? "" : ", " + lang("visaType." + adult.getVisaType());

                            entryDateKpp += adult.getCio() == null ? "" : adult.getCio();

                            if (!StringUtils.isBlank(adult.getEntryNumber())) {
                                regExpiresPerson += StringUtils.isBlank(regExpiresPerson) ? lang("entryNumber") + ": " + adult.getEntryNumber() : ", " + lang("entryNumber") + ": " + adult.getEntryNumber();
                            }
//                            } else if (person instanceof Child) {
//                                passportVisa += person.getIdentity() == null ? "" : person.getIdentity();
//                            }

                            dataCollection.add(new foreignGuestBookItem(
                                    String.valueOf(n),
                                    roomUse.getStartDate().toString(ChannelService.FORMATTER),
                                    name,
                                    person.getDob() == null ? "" : person.getDob().toString(ChannelService.FORMATTER),
                                    passportVisa,
                                    adult == null || adult.getCountry() == null ? "" : new Locale("", adult.getCountry()).getDisplayCountry(UserLocale),
                                    entryDateKpp,
                                    roomUse.getCustomerGroup().getCompany() == null ? "" : roomUse.getCustomerGroup().getCompany().getName(),
                                    roomUse.getCustomerGroup().getPov() == null ? "" : lang("pov." + roomUse.getCustomerGroup().getPov()),
                                    roomUse.getRoom().getNumber(),
                                    regExpiresPerson,
                                    roomUse.getEndDate().toString(ChannelService.FORMATTER)
                            ));
                            n += 1;
                        }
                    }
                }

                parameters.put("dataCollection", dataCollection);

            }
        }.createReport();
    }
}
