package com.idgindigo.pms.web.utils;

import com.idgindigo.pms.domain.extranet.BaseGroupRoomUse;
import com.idgindigo.pms.domain.extranet.CodeDto;
import com.idgindigo.pms.domain.extranet.CustomerGroup;
import com.idgindigo.pms.domain.extranet.Document;
import com.idgindigo.pms.domain.extranet.GroupMember;
import com.idgindigo.pms.domain.extranet.person.Adult;
import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeDetails;
import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeFacility;
import com.idgindigo.pms.domain.pms.Child;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.logins.domain.Hotel;
import com.idgindigo.pms.logins.domain.HotelInfo;
import com.idgindigo.pms.repository.extranet.CustomerGroupRepository;
import com.idgindigo.pms.repository.extranet.DocumentRepository;
import com.idgindigo.pms.repository.extranet.RoomTypeRepository;
import com.idgindigo.pms.repository.extranet.RoomTypeToFacilityRepository;
import com.idgindigo.pms.service.admin.SettingsService;
import com.idgindigo.pms.web.controller.extranet.DocumentController;
import lombok.Getter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author valentyn_vakatsiienko
 * @since 5/30/14 5:26 PM
 */
@Component
public class MailHelper {
    @Inject
    private MessageSource messageSource;
    @Inject
    private SettingsService settingsService;
    @Inject
    private HttpServletRequest request;
    @Inject
    private RoomTypeToFacilityRepository roomTypeToFacilityRepository;
    @Inject
    private DocumentRepository documentRepository;
    @Inject
    private CustomerGroupRepository customerGroupRepository;
    @Inject
    private RoomTypeRepository roomTypeRepository;
    private static Pattern LABEL_PATTERN = Pattern.compile("l\\{([^>]*?)\\}");
    private String multiRoomBookingString;
    private String singleRoomBookingString;
    private String multiBookingBookingPartial;
    private String roomBookingPartial;
    private String activationTpl;
    private String composed;
    @Inject
    private DateUtils dateUtils;

    @PostConstruct
    public void init() {
        multiRoomBookingString = loadTemplate("multiRoomBookingInfo");
        singleRoomBookingString = loadTemplate("1roomBookingInfo");
        multiBookingBookingPartial = loadTemplate("bookingInfo");
        roomBookingPartial = loadTemplate("bookingInfoPartial");
        composed = loadTemplate("composed");
        activationTpl = loadTemplate("activation");
    }

    private String loadTemplate(String name) {
        String prefix = "letters/";
        String suffix = ".html";
        try {
            return IOUtils.toString(new ClassPathResource(prefix + name + suffix).getInputStream(), "UTF-8");
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not open template file with name: " + name);
        }
    }

    public String getActivationMailBody(Hotel hotel, String activationUrl, String viewUrl) {
        HotelInfo info = hotel.getInfo();
        return new StringWithPlaceholders(activationTpl, null)
                .replace("name", info.getName())
                .replace("country", new Locale("", info.getCountry()).getDisplayCountry(new Locale("ru")))
                .replace("address", info.getOfficialAddress())
                .replace("activate", activationUrl)
                .replace("view", viewUrl)
                .getString();
    }

    public String getConfirmationMailBody(LocalDate start, LocalDate end, List<BaseGroupRoomUse> roomUses, Locale locale) {
        List<BaseGroupRoomUse> parsed = parse(roomUses);
        BaseGroupRoomUse roomUse = parsed.get(0);
        CustomerGroup group = roomUse.getCustomerGroup();
        Adult customer = roomUse.getCustomerGroup().getCustomer();
        HotelInfo info = settingsService.getHotelInfo();
        Long total = customerGroupRepository.getTotal(group);
        return getHtmlString(parsed, locale)
                .replace("hotel.phoneHtml", getHotelPhoneHtml(locale))
                .replace("hotel.logo", info.getLogo() != null ? DocumentController.getImagesUrlPrefix() + info.getLogo() : getImagesPrefix() + "/themes/default/images/empty-photo.jpg")
                .replace("hotelPhotoHtml", getHotelPhotoHtml())
                .replace("customer.name", customer.getFirstName())
                .replace("booking.id", roomUse.getAcode())
                .replace("hotel.bookingPhoneHtml", getHotelBookingPhoneHtml(locale))
                .replace("booking.arrival", getDateString(start, locale))
                .replace("booking.departure", getDateString(end, locale))
                .replace("booking.nights", String.valueOf(Days.daysBetween(start, end).getDays()))
                .replace("booking.adults", getPersonQuantity(roomUse, Adult.ADULT).toString())
                .replace("booking.children", getPersonQuantity(roomUse, Child.CHILD).toString())
                .replace("booking.room.quantity", getRoomQuantity(parsed, locale))
                .replace("booking.total", getDecimalFormat().format(total / 100.00))
                .replace("booking.vatHtml", getVatHtml(total, locale))
                .replace("hotel.name", getOrEmpty(info.getName(), null, "<br>"))
                .replace("hotel.address", getOrEmpty(info.getOfficialAddress(), null, "<br>"))
                .replace("hotel.infoPhone", getOrEmpty(info.getInfoPhone(), getLabel("confirmation.infoPhone", locale) + " ", "<br>"))
                .replace("hotel.email", getOrEmpty(info.getEmail(), "E-mail: ", "<br>"))
                .replace("hotel.site", getHotelSiteHtml(info, locale))
                .replace("image.planet.png", getImage("planet.png"))
                .replace("image.pms.jpg", getImage("pms.jpg"))
                .replace("image.face.jpg", getImage("face.jpg"))
                .replace("image.tw.jpg", getImage("tw.jpg"))
                .replace("hotel.color", info.getColor() != null ? info.getColor() : "#00B1FA")
                .getString();
    }

    private String getDateString(LocalDate date, Locale locale) {
        return String.format("%s, %s %s %s", dateUtils.getDayOfWeek(date, locale), date.getDayOfMonth(), dateUtils.getOfMonth(date, locale), date.getYear());
    }

    private String getImage(String name) {
        return getImagesPrefix() + "/confirmationletter/images/" + name;
    }

    private String getHotelSiteHtml(HotelInfo info, Locale locale) {
        String prepend = "l{hotelAdditionalInfoPrefix} <a style=\"color: ${hotel.color};\" href=\"";
        String append = "\">l{hotelAdditionalInfoSuffix}</a>";
        return new StringWithPlaceholders(getOrEmpty(info.getWebSite(), prepend, append), locale).getString();
    }

    private String getOrEmpty(String s, String prepend, String append) {
        String res = s;
        if (s != null && !s.isEmpty()) {
            if (prepend != null) {
                res = prepend + res;
            }
            if (append != null) {
                res = res + append;
            }
            return res;
        } else {
            return "";
        }
    }

    private String getRoomQuantity(List<BaseGroupRoomUse> parsed, Locale locale) {
        int rooms = parsed.size();
        if (rooms == 1) {
            return rooms + " " + getLabel("confirmation.1roomLabel", locale);
        } else if (rooms < 5) {
            return rooms + " " + getLabel("confirmation.multiRoomLabel1", locale);
        } else {
            return rooms + " " + getLabel("confirmation.multiRoomLabel2", locale);
        }//TODO handle 22 rooms?
    }

    private String getVatHtml(Long total, Locale locale) {
        float vat = settingsService.getHotelInfo().getVat();
        if (vat > 0) {
            String html = "          <tr>\n" +
                    "                    <td style=\"color: #999;\">l{vat} (${hotel.vat}%)</td>\n" +
                    "                    <td>${booking.vat}</td>\n" +
                    "                </tr>";
            return new StringWithPlaceholders(html, locale)
                    .replace("hotel.vat", String.valueOf(vat))
                    .replace("booking.vat", getDecimalFormat().format(calcVat(total, vat)))
                    .getString();
        } else {
            return "";
        }
    }

    private DecimalFormat getDecimalFormat() {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);
        df.setRoundingMode(RoundingMode.UP);
        return df;
    }

    private double calcVat(Long total, float vat) {
        double vatPercentage = 100 + vat;
        double t = total / 100.00;
        return t - (100 * t / vatPercentage);
    }

    private String getHotelPhotoHtml() {
        List<CodeDto> photos = documentRepository.findPageByType(Document.DocType.PHOTO, new PageRequest(0, 1));
        if (!photos.isEmpty()) {
            String prefix = "<tr>\n" +
                    "<td colspan=\"5\"><img width=\"638\" src=\"";
            String suffix = "\"></td>" +
                    "</tr>";
            return prefix + DocumentController.getImagesUrlPrefix() + photos.get(0).getCode() + suffix;
        } else {
            return "";
        }
    }

    private List<BaseGroupRoomUse> parse(List<BaseGroupRoomUse> roomUses) {
        List<BaseGroupRoomUse> parsed = new ArrayList<>();
        for (BaseGroupRoomUse bgru : roomUses) {
            if (!(bgru instanceof RoomUse)) {
                parsed.add(bgru);
                continue;
            }
            RoomUse roomUse = (RoomUse) bgru;
            if (roomUse.isMoved()) {
                RoomUse start = roomUse;
                while (start.isMoved()) {
                    start = start.getMovedFrom();
                }
                start.setEndDate(roomUse.getEndDate());
            } else {
                parsed.add(roomUse);
            }
        }
        return parsed;
    }

    private Integer getPersonQuantity(BaseGroupRoomUse roomUse, String type) {
        List<GroupMember> groupMembers = roomUse.getCustomerGroup().getGroupMembers();
        int res = 0;
        for (GroupMember groupMember : groupMembers) {
            if (groupMember.getPerson().getType().equals(type)) {
                res++;
            }
        }
        return res;
    }

    private String getImagesPrefix() {
        return request.getRequestURL().toString().replace(request.getRequestURI(), "") + request.getContextPath();
    }

    private String getHotelPhoneHtml(Locale locale) {
        String bookPhone = settingsService.getHotelInfo().getBookPhone();
        if (bookPhone != null && !bookPhone.isEmpty()) {
            String s = "l{optionalContactInfo}: <span style=\"color: #333;\">${hotel.phone}</span>";
            StringWithPlaceholders res = new StringWithPlaceholders(s, locale);
            return res.replace("hotel.phone", bookPhone).getString();
        } else {
            return "";
        }
    }

    private String getHotelBookingPhoneHtml(Locale locale) {
        String bookPhone = settingsService.getHotelInfo().getBookPhone();
        if (bookPhone != null && !bookPhone.isEmpty()) {
            String s = "<span style=\"color: #999;\"> l{optionalBookingPhoneInfo}</span><br>${hotel.bookingPhone}";
            StringWithPlaceholders res = new StringWithPlaceholders(s, locale);
            return res.replace("hotel.bookingPhone", bookPhone).getString();
        } else {
            return "";
        }
    }

    private String getLabel(String code, Locale locale) {
        return messageSource.getMessage(code, null, code, locale);
    }

    private StringWithPlaceholders getHtmlString(List<BaseGroupRoomUse> roomUses, Locale locale) {
        StringWithPlaceholders res = new StringWithPlaceholders(composed, locale);
        res.replace("bookingDetailsHtml", roomUses.size() > 1 ? getMultiRoomHtmlString(roomUses, locale) : getSingleRoomHtmlString(roomUses.get(0), locale));
        return res;
    }

    private String getSingleRoomHtmlString(BaseGroupRoomUse roomUse, Locale locale) {
        StringWithPlaceholders res = new StringWithPlaceholders(singleRoomBookingString, locale);
        return res
                .replace("room.name", roomUse.getBaseRoom().roomType().getName())
                .replace("room.facilities", getFacilities(roomUse.getBaseRoom().roomType().getId(), locale))
                .replace("bookingInfoPartial", getBookingInfoPartial(roomUse, locale))
                .getString();
    }

    private String getMultiRoomHtmlString(List<BaseGroupRoomUse> roomUses, Locale locale) {
        StringWithPlaceholders res = new StringWithPlaceholders(multiRoomBookingString, locale);
        String bookingInfo = "";
        for (int i = 0; i < roomUses.size(); i++) {
            bookingInfo += getBookingInfo(roomUses.get(i), locale, i + 1);
        }
        return res
                .replace("room.quantity", String.valueOf(roomUses.size()))
                .replace("bookingInfoHtml", bookingInfo)
                .getString();
    }

    private String getBookingInfo(BaseGroupRoomUse roomUse, Locale locale, int index) {
        StringWithPlaceholders res = new StringWithPlaceholders(multiBookingBookingPartial, locale);

        return res
                .replace("room.facilities", getFacilities(roomUse.getBaseRoom().roomType().getId(), locale))
                .replace("booking.number", String.valueOf(index))
                .replace("bookingInfoPartial", getBookingInfoPartial(roomUse, locale))
                .getString();
    }

    private String getFacilities(Long roomTypeId, Locale locale) {
        RoomType roomType = roomTypeRepository.findOne(roomTypeId);
        List<RoomTypeFacility> facilities = roomTypeToFacilityRepository.findByRoomType(roomTypeId);
        List<String> res = new ArrayList<>(facilities.size());
        if (roomType.getArea() != null) {
            res.add(roomType.getArea() + " " + getLabel("confirmation.metersSquared", locale));
        }
        addBedsInfo(roomType.getDetails(), res, locale);
        for (RoomTypeFacility facility : facilities) {
            res.add(getLabel(facility.getName(), locale));
        }
        return StringUtils.join(res, ", ");
    }

    private void addBedsInfo(RoomTypeDetails details, List<String> res, Locale locale) {
        if (details != null) {
            addExisting(details.getSingle(), res, getLabel("roomType.details.single", locale));
            addExisting(details.getStandardDouble(), res, getLabel("roomType.details.standardDouble", locale));
            addExisting(details.getLargeDouble(), res, getLabel("roomType.details.largeDouble", locale));
            addExisting(details.getXLargeDouble(), res, getLabel("roomType.details.xLargeDouble", locale));
            addExisting(details.getBunk(), res, getLabel("roomType.details.bunk", locale));
            addExisting(details.getSofaBed(), res, getLabel("roomType.details.sofaBed", locale));
            addExisting(details.getFuton(), res, getLabel("roomType.details.futon", locale));
        }
    }

    private void addExisting(int quantity, List<String> res, String label) {
        if (quantity > 0) {
            res.add(label + ": " + quantity);
        }
    }

    private String getBookingInfoPartial(BaseGroupRoomUse roomUse, Locale locale) {
        StringWithPlaceholders res = new StringWithPlaceholders(roomBookingPartial, locale);
        return res
                .replace("plan.board", getLabel("boardType." + roomUse.getPlan().getBoard().name(), locale))
                .getString();

    }

    private class StringWithPlaceholders {
        @Getter
        private String string;
        private Locale locale;

        public StringWithPlaceholders(String s, Locale locale) {
            this.string = s;
            this.locale = locale;

            Matcher matcher = LABEL_PATTERN.matcher(s);
            while (matcher.find()) {
                setLabel(matcher.group(1));
            }
        }

        public StringWithPlaceholders setLabel(String label) {
            string = string.replace("l{" + label + "}", getLabel("confirmation." + label, locale));
            return this;
        }

        private String getLabel(String code, Locale locale) {
            return messageSource.getMessage(code, null, code, locale);
        }

        public StringWithPlaceholders replace(String param, String value) {
            string = string.replace("${" + param + "}", value != null ? value : "N/A");
            return this;
        }
    }

}
