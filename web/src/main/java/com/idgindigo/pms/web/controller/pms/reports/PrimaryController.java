package com.idgindigo.pms.web.controller.pms.reports;

import com.idgindigo.pms.domain.extranet.BaseGroupRoomUse;
import com.idgindigo.pms.domain.extranet.person.Adult;
import com.idgindigo.pms.domain.extranet.service.Service;
import com.idgindigo.pms.domain.pms.BankDetails;
import com.idgindigo.pms.domain.pms.BaseServiceUse;
import com.idgindigo.pms.domain.pms.LivingUse;
import com.idgindigo.pms.domain.pms.SimpleServiceUse;
import com.idgindigo.pms.domain.pms.Bill;
import com.idgindigo.pms.domain.pms.Company;
import com.idgindigo.pms.domain.pms.GroupMemberToRoomUse;
import com.idgindigo.pms.domain.pms.Payment;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.domain.pms.SimpleService;
import com.idgindigo.pms.logins.domain.HotelInfo;
import com.idgindigo.pms.restutils.exception.RestFriendlyException;
import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.service.channels.ChannelService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.sf.jasperreports.engine.JRException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by misha on 25.04.14.
 */
@Controller
public class PrimaryController extends ReportController {
    @RequestMapping("hotelBill")
    public void createHotelBill(@RequestParam("billId") final Long billId, @RequestParam(value = "bankDetailId", required = false) final Long bankDetailId, @RequestParam(value = "prePayment", required = false) final Integer prePayment, @RequestParam(value = "payerId", required = false) final Long payerId, HttpServletResponse response) throws JRException, IOException {
        new ReportCreator(response, getReport("hotelBill", "jasper"), getReport("hotelBill", "pdf")) {
            @Override
            void collectParameters(Map<String, Object> parameters) {
                hotelBill(billId, bankDetailId, prePayment, payerId, parameters);
            }
        }.createReport();
    }

    @RequestMapping("invoice")
    public void createInvoice(@RequestParam("billId") final Long billId, @RequestParam(value = "bankDetailId", required = false) final Long bankDetailId, @RequestParam(value = "prePayment", required = false) final Integer prePayment, @RequestParam(value = "payerId", required = false) final Long payerId, HttpServletResponse response) throws JRException, IOException {
        new ReportCreator(response, getReport("invoice", "jasper"), getReport("invoice", "pdf")) {
            @Override
            void collectParameters(Map<String, Object> parameters) {
                hotelBill(billId, bankDetailId, prePayment, payerId, parameters);
            }
        }.createReport();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HotelBillDto {
        private String name;
        private String dateSince;
        private String dateTo;
        private String qty;
        private String days;
        private String rate;
        private String total;
        private String num;
        private String item;
        private String rawTotal;
        private String taxPct;
        private String taxTotal;
    }

    public void hotelBill(Long billId, Long bankDetailId, Integer prePayment, Long payerId, Map<String, Object> parameters) {
        Bill bill = billRepository.findOne(billId);
        if (bill == null) throw new RestFriendlyException(RestFriendlyException.INCORRECT_VALUE, "billId");

        BankDetails bankDetails = bankDetailId != null ? bankDetailsRepository.findOne(bankDetailId) : bankDetailsRepository.getDefault();
        if (bankDetails == null) throw new RestFriendlyException(RestFriendlyException.INCORRECT_VALUE, "bankDetails");

        RoomUse roomUse = bill.getRoomUse();

        HotelInfo hotelInfo = hotelInfoRepository.findOne(SecurityUtils.getHotel().getInfo().getId());
        putOrEmpty(parameters, "vendorName", hotelInfo.getName());
        putOrEmpty(parameters, "vendorCity", hotelInfo.getCity());
        putOrEmpty(parameters, "vendorAddress", hotelInfo.getOfficialAddress());
        putOrEmpty(parameters, "vendorTourismTax", String.format("%.02f", hotelInfo.getTourismTax()));
        putOrEmpty(parameters, "vendorAccount", StringUtils.isEmpty(bankDetails.getAccount()) ? null : bankDetails.getAccount());
        putOrEmpty(parameters, "vendorMfo", bankDetails.getMfo() == null ? null : bankDetails.getMfo());
        putOrEmpty(parameters, "vendorOklo", null); // TODO ASK SLAVA, WHERE TO GET THIS FIELD
        putOrEmpty(parameters, "vendorInn", bankDetails.getEdrpou() == null ? null : bankDetails.getEdrpou());
        putOrEmpty(parameters, "vendorKpp", bankDetails.getMfo() == null ? null : bankDetails.getMfo());

        putOrEmpty(parameters, "billId", bill.getId());
        putOrEmpty(parameters, "billDate", bill.getCreatedDate().toString(ChannelService.FORMATTER));
        putOrEmpty(parameters, "billRawTotal", String.format("%.02f", (float) bill.getRawTotal() / 100));
        putOrEmpty(parameters, "billTotal", String.format("%.02f", (float) bill.getTotal() / 100));
        putOrEmpty(parameters, "billDiscount", bill.getDiscount() == 0L ? "0" : bill.getDiscount());
//      putOrEmpty(parameters, "billReporter", null); // TODO ???
        putOrEmpty(parameters, "billMoneyType", lang("paymentType." + bankDetails.getPaymentType().name()));

        Float tourTax = hotelInfo.getTourismTax();
        Float incTourTax = 0F;

        Long bookingId = roomUse == null ? bill.getGroup().getId() : bill.getRoomUse().getId();
        putOrEmpty(parameters, "bookingId", bookingId);

        if (roomUse != null) {
            putOrEmpty(parameters, "payerRoom", roomUse.getRoom().getNumber());
            putOrEmpty(parameters, "payerCheckInTime", roomUse.getCheckInTime() == null ? null : roomUse.getCheckInTime().toString("dd/MM/yyyy HH:mm"));
            putOrEmpty(parameters, "accommodation", roomUse.getRoom().getAccommodation().getId());
        }

        Company company = null;
        Adult adult = null;

        if (payerId != null) {
            adult = adultRepository.findOne(payerId);
        } else {
            company = roomUse != null ? roomUse.getCustomerGroup().getCompany() : bill.getGroup().getCompany();
            adult = roomUse != null ? roomUse.getCustomerGroup().getCustomer() : bill.getGroup().getCustomer();
        }

        if (company != null) {
            putOrEmpty(parameters, "payerName", company.getName());
            putOrEmpty(parameters, "payerEdrpou", null);
            putOrEmpty(parameters, "payerKpp", null);
            putOrEmpty(parameters, "payerInn", company.getInn() == null ? null : company.getInn());
            putOrEmpty(parameters, "payerAccount", company.getAccountNumber() == null ? null : company.getAccountNumber());
            putOrEmpty(parameters, "payerMfo", company.getBankMfo() == null ? null : company.getBankMfo());
        } else if (adult != null) {
            putOrEmpty(parameters, "payerName", adult.getLastName() + " " + adult.getFirstName());
            putOrEmpty(parameters, "payerEdrpou", null);
            putOrEmpty(parameters, "payerKpp", null);
            putOrEmpty(parameters, "payerInn", null);
            putOrEmpty(parameters, "payerAccount", null);
            putOrEmpty(parameters, "payerMfo", null);
        } else throw new RestFriendlyException(RestFriendlyException.INCORRECT_VALUE, "payer.id");

        List<BaseServiceUse> serviceUses = baseServiceUseRepository.findByBill(bill);
        List<HotelBillDto> billDtos = new ArrayList<>(serviceUses.size());
        Integer num = 0;

        Collections.sort(serviceUses, new Comparator<BaseServiceUse>() {
            public int compare(BaseServiceUse o1, BaseServiceUse o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });

        for (BaseServiceUse serviceUse : serviceUses) {
            num = num + 1;
            String serviceTitle = lang("LIVING");
            String item = lang("day");
            Service service = serviceUse.getService();

            String serviceDateStart = serviceUse.getDate().toString(ChannelService.FORMATTER);
            String serviceDateEnd = serviceUse.getDate().plusDays(1).toString(ChannelService.FORMATTER);

            if (serviceUse.isRefunded() != true) {
                if (service instanceof SimpleService) {
                    serviceTitle = ((SimpleService) service).getTitle();
                    item = lang("item");
                    serviceDateEnd = serviceDateStart;
                } else {
                    incTourTax += (float) serviceUse.getRawTotal() / 100 * tourTax / 100;
                }
                billDtos.add(new HotelBillDto(
                        lang(serviceTitle),
                        serviceDateStart,
                        serviceDateEnd,
                        String.valueOf(serviceUse.getQuantity()),
                        "1",
                        String.format("%.02f", (float) serviceUse.getTotal() / serviceUse.getQuantity() / 100),
                        String.format("%.02f", (float) serviceUse.getTotal() / 100),
                        String.valueOf(num),
                        item,
                        String.format("%.02f", (float) serviceUse.getTotal() / 100),
                        "",
                        ""
                ));
            }
        }
        parameters.put("incTourismTax", String.format("%.02f", incTourTax));
        parameters.put("serviceCollection", billDtos);
        Double prePaymentValue = 0.00;
        if (prePayment != null) {
            prePaymentValue = prePayment == 0 ? 0.00 : ((double) bill.getTotal() / 100) * prePayment / 100;
        } else {
            prePayment = 0;
        }
        putOrEmpty(parameters, "prePaymentPercent", prePayment);
        putOrEmpty(parameters, "prePaymentValue", String.format("%.02f", prePaymentValue));
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PersonName {
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ServiceItem {
        private String name;
        private String date;
        private String total;
        private String paid;
    }

    @RequestMapping("folio") // All services of current roomUse
    public void createFolio(@RequestParam("roomUseId") final Long roomUseId,/* @RequestParam("groupId") final Long groupId,*/ HttpServletResponse response) throws JRException, IOException {
        new ReportCreator(response, getReport("folio", "jasper"), getReport("folio", "pdf")) {
            @Override
            void collectParameters(Map<String, Object> parameters) {
                putOrEmpty(parameters, "bookingId", roomUseId);
                putOrEmpty(parameters, "hotelName", SecurityUtils.getHotel().getInfo().getName());
                putOrEmpty(parameters, "hotelAddress", SecurityUtils.getHotel().getInfo().getOfficialAddress());
                putOrEmpty(parameters, "hotelContact", SecurityUtils.getHotel().getInfo().getBookPhone());

                DateFormat folioDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date folioDate = new Date();
                putOrEmpty(parameters, "folioDate", folioDateFormat.format(folioDate));

                RoomUse roomUse = roomUseRepository.findOne(roomUseId);
                if (roomUse == null) throw new RestFriendlyException(RestFriendlyException.INCORRECT_VALUE, "roomUse.id");

                Integer hotelCheckOut = settingsService.getHotelInfo().getCheckOut();

                putOrEmpty(parameters, "adults", roomUse.getBaseRoom().getAdults());
                putOrEmpty(parameters, "children", roomUse.getBaseRoom().getChildren());
                putOrEmpty(parameters, "additional", roomUse.getBaseRoom().getAdditional());
                putOrEmpty(parameters, "room", roomUse.getRoom().getNumber());
                putOrEmpty(parameters, "roomType", roomUse.getRoom().getRoomType().getName());
                putOrEmpty(parameters, "roomTypeRate", String.format("%.02f", (float) roomUse.getRoom().getRoomType().getDefaultPrice() / 100));
                putOrEmpty(parameters, "startDate", roomUse.getCheckInTime() != null ? roomUse.getCheckInTime().toString("dd/MM/yyy HH:mm") : null);
                putOrEmpty(parameters, "endDate", roomUse.getStatus() == RoomUse.Status.OUTGO ? roomUse.getCheckOutTime().toString("dd/MM/yyy HH:mm") : roomUse.getEndDate().toDateTimeAtStartOfDay().plusMinutes(hotelCheckOut).toString("dd/MM/yyy HH:mm"));
                putOrEmpty(parameters, "total", String.format("%.02f", (float) roomUse.getTotal() / 100));
                putOrEmpty(parameters, "totalPaid", String.format("%.02f", (float) roomUse.getTotalPaid() / 100));
                putOrEmpty(parameters, "checkedOut", String.format("%.02f", ((float) roomUse.getTotal() - (float) roomUse.getTotalPaid()) / 100));

                List<GroupMemberToRoomUse> groupMembers = groupMemberToRoomUseRepository.findByRoomUse(roomUse);
                if (!groupMembers.isEmpty()) {
                    List<PersonName> persons = new ArrayList<>(groupMembers.size());
                    for (GroupMemberToRoomUse groupMember : groupMembers) {
                        persons.add(new PersonName(groupMember.getGroupMember().getPerson().getLastName() + " " + groupMember.getGroupMember().getPerson().getFirstName()));
                    }
                    parameters.put("customerCollection", persons);
                }

                List<BaseServiceUse> serviceUses = baseServiceUseRepository.findByBillRoomUse(roomUse);
                if (serviceUses.isEmpty()) throw new RestFriendlyException(RestFriendlyException.INCORRECT_VALUE, "BaseServiceUse.roomUse");

                Collections.sort(serviceUses, new Comparator<BaseServiceUse>() {
                    public int compare(BaseServiceUse o1, BaseServiceUse o2) {
                        return o1.getDate().compareTo(o2.getDate());
                    }
                });

                List<ServiceItem> services = new ArrayList<>(serviceUses.size());
                for (BaseServiceUse serviceUse : serviceUses) {

                    String serviceTitle = lang("LIVING");
                    Service service = serviceUse.getService();
                    if (service instanceof SimpleService) serviceTitle = ((SimpleService) service).getTitle();

                    if (serviceUse.isRefunded() != true) {
                        services.add(new ServiceItem(
                                lang(serviceTitle),
                                serviceUse.getDate().toString(ChannelService.FORMATTER),
                                String.format("%.02f", (float) serviceUse.getTotal() / 100),
                                ""
                        ));
                    }
                }
                parameters.put("serviceCollection", services);
            }
        }.createReport();
    }

    @RequestMapping("serviceRequestDetails") // All services of current group
    public void createServiceRequestDetails(@RequestParam("roomUseId") final Long roomUseId, HttpServletResponse response) throws JRException, IOException {
        new ReportCreator(response, getReport("serviceRequestDetails", "jasper"), getReport("serviceRequestDetails", "pdf")) {
            @Override
            void collectParameters(Map<String, Object> parameters) {
                putOrEmpty(parameters, "bookingId", roomUseId);

                DateFormat folioDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                Date folioDate = new Date();
                putOrEmpty(parameters, "folioDate", folioDateFormat.format(folioDate));
//                putOrEmpty(parameters, "folioClerk", null);

                // TODO folioClerk  // current user with appropriate role (e.g. Администратор Алексеев А. А. / Кассир Константинов К. К.)

                RoomUse roomUse = roomUseRepository.findOne(roomUseId);
                if (roomUse == null) throw new RestFriendlyException(RestFriendlyException.INCORRECT_VALUE, "roomUse.id");

                putOrEmpty(parameters, "roomUseAdults", roomUse.getBaseRoom().getAdults());
                putOrEmpty(parameters, "roomUseChildren", roomUse.getBaseRoom().getChildren());
                putOrEmpty(parameters, "roomUseAdditional", roomUse.getBaseRoom().getAdditional());
                putOrEmpty(parameters, "roomUseRooms", roomUse.getRoom().getNumber());
                putOrEmpty(parameters, "roomUseDateSince", roomUse.getStartDate() == null ? "" : roomUse.getStartDate().toString(ChannelService.FORMATTER));
                putOrEmpty(parameters, "roomUseDateTo", roomUse.getEndDate() == null ? "" : roomUse.getEndDate().toString(ChannelService.FORMATTER));
                putOrEmpty(parameters, "roomUseTotal", String.format("%.02f", (float) roomUse.getTotal() / 100));
                putOrEmpty(parameters, "roomUseTotalPaid", String.format("%.02f", (float) roomUse.getTotalPaid() / 100));

                List<GroupMemberToRoomUse> groupMembers = groupMemberToRoomUseRepository.findByRoomUse(roomUse);
                if (!groupMembers.isEmpty()) {//throw new RestFriendlyException(RestFriendlyException.INCORRECT_VALUE, "GroupMemberToRoomUse.groupMember");

                    List<PersonName> persons = new ArrayList<>(groupMembers.size());
                    for (GroupMemberToRoomUse groupMember : groupMembers) {
                        persons.add(new PersonName(groupMember.getGroupMember().getPerson().getLastName() + " " + groupMember.getGroupMember().getPerson().getFirstName()));
                    }
                    parameters.put("roomUseCustomerGroup", persons);
                }

                List<BaseServiceUse> serviceUses = baseServiceUseRepository.findByBillRoomUse(roomUse);
                if (serviceUses.isEmpty()) throw new RestFriendlyException(RestFriendlyException.INCORRECT_VALUE, "BaseServiceUse.roomUse");

                Collections.sort(serviceUses, new Comparator<BaseServiceUse>() {
                    public int compare(BaseServiceUse o1, BaseServiceUse o2) {
                        return o1.getDate().compareTo(o2.getDate());
                    }
                });

                List<ServiceItem> services = new ArrayList<>(serviceUses.size());
                for (BaseServiceUse serviceUse : serviceUses) {

                    String serviceTitle = lang("LIVING");
                    Service service = serviceUse.getService();
                    if (service instanceof SimpleService) serviceTitle = ((SimpleService) service).getTitle();

                    if (serviceUse.isRefunded() != true) {
                        services.add(new ServiceItem(
                                lang(serviceTitle),
                                serviceUse.getDate().toString(ChannelService.FORMATTER),
                                String.format("%.02f", (float) serviceUse.getTotal() / 100),
                                "-"
                        ));
                    }
                }
                parameters.put("serviceCollection", services);
            }
        }.createReport();
    }

    @RequestMapping("creditAccountOrder") // Same as payment, is printed on paymentPrintButton click
    public void createCreditAccountOrder(@RequestParam("paymentId") final Long paymentId, HttpServletResponse response) throws JRException, IOException {
        new ReportCreator(response, getReport("creditAccountOrder", "jasper"), getReport("creditAccountOrder", "pdf")) {
            @Override
            void collectParameters(Map<String, Object> parameters) {
                creditAccountOrderParameters(paymentId, parameters);
            }
        }.createReport();
    }

    public void creditAccountOrderParameters(Long paymentId, Map<String, Object> parameters) {
        Payment payment = paymentRepository.findOne(paymentId);
        if (payment == null)
            throw new RestFriendlyException(RestFriendlyException.INCORRECT_VALUE, "payment.id");
        putOrEmpty(parameters, "hotelName", payment.getBankDetails().getName());
        putOrEmpty(parameters, "hotelEdrpou", payment.getBankDetails().getEdrpou());
        putOrEmpty(parameters, "paymentId", payment.getId());
        putOrEmpty(parameters, "paymentTotal", String.format("%.02f", (float) payment.getTotal() / 100));
        putOrEmpty(parameters, "paymentDate", payment.getDate().toString(ChannelService.FORMATTER));

        String paymentIssuer;
        if (payment.getBill().getRoomUse() != null) {
            if (payment.getBill().getRoomUse().getCustomerGroup().getCompany() != null)
                paymentIssuer = payment.getBill().getRoomUse().getCustomerGroup().getCompany().getName(); // group master company
            else
                paymentIssuer = payment.getBill().getRoomUse().getCustomerGroup().getCustomer().getLastName() + " " + payment.getBill().getRoomUse().getCustomerGroup().getCustomer().getFirstName(); // group master person
        } else {
            if (payment.getBill().getGroup().getCompany() != null)
                paymentIssuer = payment.getBill().getGroup().getCompany().getName(); // group master company
            else
                paymentIssuer = payment.getBill().getGroup().getCustomer().getLastName() + " " + payment.getBill().getGroup().getCustomer().getFirstName(); // group master person
        }
        putOrEmpty(parameters, "paymentIssuer", paymentIssuer);
        putOrEmpty(parameters, "paymentPurpose", "Оплата счета №" + payment.getBill().getId()); // payment.bill.id (e.g. Оплата счета №billId)
        putOrEmpty(parameters, "paymentTotalLiteral", String.format("%.02f", (float) payment.getTotal() / 100)); // amount of money printed in words (e.g. "Одна тисяча ...")
    }

    @RequestMapping("debitAccountOrder") // not available at the moment, for Finances module
    public void createDebitAccountOrder(HttpServletResponse response) throws JRException, IOException {
        new ReportCreator(response, getReport("debitAccountOrder", "jasper"), getReport("debitAccountOrder", "pdf")) {
            @Override
            void collectParameters(Map<String, Object> parameters) {

//                orderId
//                orderDate
//                orderSum
//                organizationName // from bankDetails.holder selected for current Payment
//                organizationEdrpou // from bankDetails.edrpou selected for current Payment
//                orderDate
//                orderRecipient
//                orderPurpose
//                orderSumLiteral // amount of money printed in words (e.g. "Одна тисяча ...")
//                orderRecipientDocument // current recipient identity document data

            }
        }.createReport();
    }
}
