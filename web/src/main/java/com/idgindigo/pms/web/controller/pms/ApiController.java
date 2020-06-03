package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.channel.wubook.WubookImpl;
import com.idgindigo.pms.domain.BaseEntity;
import com.idgindigo.pms.domain.extranet.BaseGroupRoomUse;
import com.idgindigo.pms.domain.extranet.CodeDto;
import com.idgindigo.pms.domain.extranet.CustomerGroup;
import com.idgindigo.pms.domain.extranet.Document;
import com.idgindigo.pms.domain.extranet.plan.Plan;
import com.idgindigo.pms.domain.extranet.roomtype.BaseRoom;
import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeFacility;
import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeQuota;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.logins.domain.BookingButtonSettings;
import com.idgindigo.pms.logins.domain.HotelFacilityDto;
import com.idgindigo.pms.logins.domain.HotelInfo;
import com.idgindigo.pms.logins.repository.BookingButtonSettingsRepository;
import com.idgindigo.pms.logins.repository.HotelInfoRepository;
import com.idgindigo.pms.logins.repository.HotelToFacilityRepository;
import com.idgindigo.pms.price.LivingPriceResolver;
import com.idgindigo.pms.repository.extranet.BaseRoomRepository;
import com.idgindigo.pms.repository.extranet.DocumentRepository;
import com.idgindigo.pms.repository.extranet.RoomTypeRepository;
import com.idgindigo.pms.repository.extranet.RoomTypeToFacilityRepository;
import com.idgindigo.pms.repository.extranet.RoomTypeToPhotoRepository;
import com.idgindigo.pms.repository.extranet.VirtualRoomRepository;
import com.idgindigo.pms.repository.extranet.plan.PlanRepository;
import com.idgindigo.pms.repository.extranet.roomuse.BaseGroupRoomUseRepository;
import com.idgindigo.pms.repository.pms.RoomUseRepository;
import com.idgindigo.pms.restutils.PageWithTotalCount;
import com.idgindigo.pms.restutils.exception.ApiException;
import com.idgindigo.pms.restutils.exception.RestFriendlyException;
import com.idgindigo.pms.restutils.exception.RoomUseException;
import com.idgindigo.pms.restutils.view.ResponseView;
import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.service.admin.SettingsService;
import com.idgindigo.pms.service.broadcast.BroadcastService;
import com.idgindigo.pms.service.channels.ChannelService;
import com.idgindigo.pms.service.extranet.LivingService;
import com.idgindigo.pms.service.extranet.MailService;
import com.idgindigo.pms.service.pms.QuotaService;
import com.idgindigo.pms.web.controller.ResponseEntity;
import com.idgindigo.pms.web.utils.BookingHandlerResolver;
import com.idgindigo.pms.web.utils.LabelsResolver;
import com.idgindigo.pms.web.utils.MailHelper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * @author etipalchuk
 * @since 17.03.14 16:12
 */
@Controller
@RequestMapping(ApiController.URL)
public class ApiController {
    public static final String URL = "/api";
    public static final String FIND_AVAILABLE = "findAvailable";
    public static final int A_CODE_LENGTH = 5;
    public static final int START_END_SPAN = 30;

    @Inject
    private PlanRepository planRepository;
    @Inject
    private RoomTypeToPhotoRepository rttpRepository;
    @Inject
    private RoomTypeToFacilityRepository rttfRepository;
    @Inject
    private LivingService livingService;
    @Inject
    private LivingPriceResolver priceResolver;
    @Inject
    private BaseRoomRepository baseRoomRepository;
    @Inject
    private HotelToFacilityRepository htfRepository;
    @Inject
    private HotelInfoRepository hiRepository;
    @Inject
    private DocumentRepository documentRepository;
    @Inject
    private RoomUseRepository roomUseRepository;
    @Inject
    private BookingButtonSettingsRepository bbsRepository;
    @Inject
    private SettingsService settingsService;
    @Inject
    private BroadcastService broadcastService;
    @Inject
    private MailService mailService;
    @Inject
    private MailHelper mailHelper;
    @Inject
    private LocaleResolver localeResolver;
    @Inject
    private BookingHandlerResolver handlerResolver;
    @Inject
    private QuotaService quotaService;
    @Inject
    private BaseGroupRoomUseRepository baseGroupRoomUseRepository;
    @Inject
    private RoomTypeRepository roomTypeRepository;
    @Inject
    private VirtualRoomRepository virtualRoomRepository;

    @RequestMapping("bbs/{id}")
    @ResponseBody
    @ResponseView(BaseEntity.ListView.class)
    public ResponseEntity<BookingButtonSettings> getSettings(@PathVariable("id") Long id) {
        return new ResponseEntity<>(bbsRepository.findOne(id));
    }

    @RequestMapping("imagesByRoomType/{id}")
    @ResponseBody
    public ResponseEntity<List<CodeDto>> getPhotoCodesByRoomType(@PathVariable("id") Long id) {
        List<CodeDto> list = rttpRepository.findDtosByRoomType(id);
        return new ResponseEntity<>(list, new PageWithTotalCount(0, list.isEmpty() ? 1 : list.size(), 0));
    }

    @RequestMapping("hotelPhotos")
    @ResponseBody
    public ResponseEntity<List<CodeDto>> getAllPhotoCodes() {
        List<CodeDto> list = documentRepository.findAllByType(Document.DocType.PHOTO);
        return new ResponseEntity<>(list, new PageWithTotalCount(0, list.isEmpty() ? 1 : list.size(), 0));
    }

    @RequestMapping("facilityByRoomType/{id}")
    @ResponseBody
    public ResponseEntity<List<RoomTypeFacility>> getByRoomType(@PathVariable("id") Long id) {
        List<RoomTypeFacility> list = rttfRepository.findByRoomType(id);
        return new ResponseEntity<>(list, new PageWithTotalCount(0, list.isEmpty() ? 1 : list.size(), 0));
    }

    @RequestMapping(value = FIND_AVAILABLE, params = "hotelId", method = RequestMethod.POST)
    @ResponseBody
    @ResponseView(BaseEntity.ListView.class)
    public ResponseEntity<List<RoomTypeQuota>> findAvailable(@RequestParam("dfrom") String dfrom, @RequestParam("dto") String dto) {
        LocalDate startDate = getLocalDate(dfrom);
        LocalDate endDate = getLocalDate(dto);
        checkDates(startDate, endDate);
        return new ResponseEntity<>(fillBlanks(quotaService.getRoomTypeQuota(startDate, endDate)));
    }

    private List<RoomTypeQuota> fillBlanks(List<RoomTypeQuota> roomTypeQuota) {
        roomTypeQuota = new ArrayList<>(roomTypeQuota);
        Map<RoomType, RoomTypeQuota> byRoomType = new HashMap<>();
        for (RoomTypeQuota typeQuota : roomTypeQuota) {
            byRoomType.put(typeQuota.getRoomType(), typeQuota);
        }
        List<RoomType> all = roomTypeRepository.findByApprovedTrue();
        for (RoomType roomType : all) {
            if (!byRoomType.containsKey(roomType)) {
                roomTypeQuota.add(new RoomTypeQuota(roomType, roomType.getOtaRooms()));
            }
        }
        for (Iterator<RoomTypeQuota> iterator = roomTypeQuota.iterator(); iterator.hasNext(); ) {
            RoomTypeQuota rtq = iterator.next();
            if (rtq.getQuota() == 0) {
                iterator.remove();
            }
        }
        return roomTypeQuota;
    }

    @Getter
    @Setter
    @ToString
    private static class ResponseDto {
        private String hotelId;
        private HotelInfo info;
        private List<String> hotelPhotos;
        private List<RoomTypeQuotaDto> roomPrices;
        private List<PlanDto> plans;
    }

    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    private static class PlanDto {
        private long id;
        private Plan.Board board;
        private String name;

        private PlanDto(Plan plan) {
            id = plan.getId();
            board = plan.getBoard();
            name = plan.getName();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    private static class PlanIdsToPrices {
        private long planId;
        private List<Long> prices;
    }

    private static LocalDate getLocalDate(String dateString) {
        LocalDate date;
        try {
            date = LocalDate.parse(dateString, ChannelService.FORMATTER);
        } catch (IllegalArgumentException e) {
            throw new ApiException(ApiException.AVAILABILITY_INVALID_DATE_FORMAT);
        }
        return date;
    }

    @NoArgsConstructor
    @Getter
    @Setter
    @ToString
    public static class RoomTypeQuotaDto {
        private long id;
        private String name;
        private String description;
        private int quota;
        private int adults;
        private int children;
        private List<String> photos;
        private List<RoomTypeQuotaDto> virtualRooms;
        private List<PlanIdsToPrices> prices;

        public RoomTypeQuotaDto(RoomTypeQuota roomTypeQuota, List<String> photos, List<PlanIdsToPrices> prices) {
            id = roomTypeQuota.getRoomType().getId();
            name = roomTypeQuota.getRoomType().getName();
            description = roomTypeQuota.getRoomType().getDescription();
            adults = roomTypeQuota.getRoomType().getAdults();
            children = roomTypeQuota.getRoomType().getChildren();
            quota = roomTypeQuota.getQuota();
            this.photos = photos;
            this.prices = prices;
        }

        public RoomTypeQuotaDto(BaseRoom virtualRoom, List<PlanIdsToPrices> prices) {
            id = virtualRoom.getId();
            name = virtualRoom.getName();
            adults = virtualRoom.getAdults();
            children = virtualRoom.getChildren();
            this.prices = prices;
        }
    }

    @RequestMapping(value = "findAvailableRooms", params = "hotelId", method = RequestMethod.POST)
    @ResponseBody
    @ResponseView(BaseEntity.ListView.class)
    //TODO refactor and remove n qubed queries to DB!!!
    public ResponseEntity<ResponseDto> findAvailableRooms(@RequestParam("dfrom") String dfrom, @RequestParam("dto") String dto) {
        LocalDate startDate = getLocalDate(dfrom);
        LocalDate endDate = getLocalDate(dto);
        checkDates(startDate, endDate);
        List<RoomTypeQuota> freeRoomsQuota = fillBlanks(quotaService.getRoomTypeQuota(startDate, endDate));
        List<BaseRoom> rooms = new ArrayList<>(freeRoomsQuota.size());
        for (RoomTypeQuota roomTypeQuota : freeRoomsQuota) {
            rooms.add(roomTypeQuota.getRoomType());
        }

        List<RoomTypeQuotaDto> roomTypePhotoToPrices = new ArrayList<>();

        List<Plan> plans = planRepository.findByApprovedTrue();
        for (RoomTypeQuota roomTypeQuota : freeRoomsQuota) {
            BaseRoom room = roomTypeQuota.getRoomType();
            RoomTypeQuotaDto rtqd = new RoomTypeQuotaDto(roomTypeQuota, rttpRepository.findByRoomType(room.getId()), getPlanIdsToPrices(startDate, endDate, plans, room));
            rtqd.setVirtualRooms(getVirtualRooms(startDate, endDate, plans, roomTypeQuota.getRoomType()));
            roomTypePhotoToPrices.add(rtqd);
        }
        ResponseDto response = new ResponseDto();
        response.setHotelId(SecurityUtils.getCurrentTenantId());
        response.setInfo(hiRepository.findOne(SecurityUtils.getHotel().getInfo().getId()));
        response.setHotelPhotos(documentRepository.findAccessKeyByType(Document.DocType.PHOTO));
        response.setRoomPrices(roomTypePhotoToPrices);
        response.setPlans(getPlans(plans));
        return new ResponseEntity<>(response);
    }

    private static void checkDates(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new ApiException(RestFriendlyException.INCORRECT_VALUE, "dto");
        }
        if (Days.daysBetween(startDate, endDate).getDays() > START_END_SPAN) {
            throw new ApiException(ApiException.DATE_SPAN_EXCEEDED);
        }
    }

    private static List<PlanDto> getPlans(Collection<Plan> plans) {
        List<PlanDto> result = new ArrayList<>(plans.size());
        for (Plan plan : plans) {
            result.add(new PlanDto(plan));
        }
        return result;
    }

    private List<RoomTypeQuotaDto> getVirtualRooms(LocalDate startDate, LocalDate endDate, Iterable<Plan> plans, RoomType roomType) {
        List<RoomTypeQuotaDto> virtualRooms = new ArrayList<>();
        for (BaseRoom virtualRoom : virtualRoomRepository.findByRoomType(roomType)) {
            virtualRooms.add(new RoomTypeQuotaDto(virtualRoom, getPlanIdsToPrices(startDate, endDate, plans, virtualRoom)));
        }
        return virtualRooms;
    }

    private List<PlanIdsToPrices> getPlanIdsToPrices(LocalDate startDate, LocalDate endDate, Iterable<Plan> plans, BaseRoom room) {
        List<PlanIdsToPrices> res = new ArrayList<>();
        for (Plan plan : plans) {
            res.add(new PlanIdsToPrices(plan.getId(), getPrices(startDate, endDate, plan, room)));
        }
        return res;
    }

    private List<Long> getPrices(LocalDate start, LocalDate end, Plan plan, BaseRoom room) {
        List<Long> prices = new ArrayList<>();
        for (LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {
            prices.add(priceResolver.getPrice(livingService.get(room, plan, date), date));
        }
        return prices;
    }

    /*
Obj
 hotelitem
     datestart
  dateend
  info
  roomtypes
   type1
    info
    prices
   type2
             info
    prices
   ...
   type n
  images
   image 1
   image 2
   ...
  ...

Идея в том, что передавая айди, наду начала и дату конца
получать в ответе объект или массив, который будет содержать (основное)
1. типы доступных номеров
2. их картинки
3. цена за 1 сутки
4. другая инфа, которая есть по отелю
*/

    @RequestMapping(value = "baseRooms", method = RequestMethod.POST)
    @ResponseBody
    @ResponseView(RoomType.DetailedView.class)
    public ResponseEntity<List<BaseRoom>> getBaseRooms() {
        return new ResponseEntity<>(baseRoomRepository.findAll());
    }

    @RequestMapping(value = "plans", method = RequestMethod.POST)
    @ResponseBody
    @ResponseView(BaseEntity.ListView.class)
    public ResponseEntity<List<Plan>> getPlans() {
        return new ResponseEntity<>(planRepository.findByApprovedTrue());
    }

    @RequestMapping(value = "prices", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Map<Long, List<Long>>> getPrices(@RequestBody @Valid PriceRequestDto dto) {
        Map<Long, List<Long>> res = new HashMap<>();
        List<BaseRoom> rooms = baseRoomRepository.findByApprovedTrue();
        Long id = dto.plan.getId();
        if (id == null) {
            throw new ApiException(ApiException.PRICES_INVALID_PLAN, "plan.id");
        }
        Plan plan = planRepository.findOne(id);
        if (plan == null || !plan.getApproved()) {
            throw new ApiException(ApiException.PRICES_INVALID_PLAN, "plan");
        }
        for (BaseRoom room : rooms) {
            res.put(room.getId(), getPrices(dto.start, dto.end, dto.plan, room));
        }
        return new ResponseEntity<>(res);
    }

    @RequestMapping(value = "book", method = RequestMethod.POST)
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @ResponseBody
    public ResponseEntity<String> book(@RequestBody BookDto dto, HttpServletRequest request) {
        validate(dto);
        List<BaseGroupRoomUse> created = new ArrayList<>(dto.getRoomUses().size());
        CustomerGroup group = dto.getGroup();
        String acode;
        do {
            acode = RandomStringUtils.randomAlphanumeric(A_CODE_LENGTH).toUpperCase();
        } while (baseGroupRoomUseRepository.existsByAcode(acode));

        setData(dto.getRoomUses());
        for (BaseGroupRoomUse roomUse : dto.getRoomUses()) {
            roomUse.setStartDate(dto.getStartDate());
            roomUse.setEndDate(dto.getEndDate());
            roomUse.setCustomerGroup(group);
            roomUse.setStatus(BaseGroupRoomUse.Status.BOOKING_FREE);
            roomUse.setSource(RoomUse.Source.BOOKING_BUTTON);
            roomUse.setAcode(acode);
            List<? extends BaseGroupRoomUse> handled;
            try {
                handled = handlerResolver.resolve().handleCreate(roomUse);
            } catch (Exception e) {
                broadcastService.broadcastBookingFailed(dto.getRoomUses());
                throw e;
            }
            created.addAll(handled);
            if (handled.isEmpty()) {
                broadcastService.broadcastBookingFailed(dto.getRoomUses());
                throw new ApiException(ApiException.BOOKING_FAILURE);
            } else {
                group = created.get(0).getCustomerGroup();
            }
        }
        mailService.sendMail(group.getCustomer().getEmail(), "Booking confirmation", mailHelper.getConfirmationMailBody(dto.getStartDate(), dto.getEndDate(), created, localeResolver.resolveLocale(request)), true);
        broadcastService.broadcastBooking(created);
        return new ResponseEntity<>(acode);
    }

    private void validate(BookDto dto) {
        if (!dto.getStartDate().isBefore(dto.getEndDate())) {
            throw new ApiException(ApiException.BOOKING_INVALID_DATES, "startDate, endDate");
        }
        if (!dto.getStartDate().isAfter(LocalDate.now(SecurityUtils.getTimeZone()))) {
            throw new ApiException(ApiException.BOOKING_IN_PAST, "startDate");
        }
    }

    private void setData(Iterable<BaseGroupRoomUse> uses) {
        for (BaseGroupRoomUse use : uses) {
            use.setBaseRoom(baseRoomRepository.findOne(use.getBaseRoom().getId()));
            use.setPlan(planRepository.findOne(use.getPlan().getId()));
        }
    }

    @RequestMapping(value = "bookings/{acode}/refused", method = RequestMethod.POST, params = "email")
    @ResponseBody
    public void refuse(@PathVariable("acode") String acode, @RequestParam("email") String email) {
        acode = acode.toUpperCase();
        List<RoomUse> roomUses = roomUseRepository.findByAcode(acode);
        if (roomUses.isEmpty()) {
            throw new AccessDeniedException("forbidden");
        }
        CustomerGroup group = roomUses.get(0).getCustomerGroup();
        if (!email.equals(group.getCustomer().getEmail())) {
            throw new AccessDeniedException("forbidden");
        }
        if (!canPerformRefuse(roomUses)) {
            throw new RoomUseException(RoomUseException.REFUSE_INVALID_DATE, "startDate");
        }

        List<? extends BaseGroupRoomUse> refused = handlerResolver.resolve().handleRefuse(roomUses);
        broadcastService.broadcastRefuse(refused);
    }

    private boolean canPerformRefuse(Iterable<RoomUse> roomUses) {
        LocalDate now = LocalDate.now(settingsService.getTimeZone());
        for (RoomUse roomUse : roomUses) {
            if (now.isBefore(roomUse.getStartDate())) {
                return false;
            }
        }
        return true;
    }

    @RequestMapping(value = "hotelFacilities", params = "hotelId", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<List<HotelFacilityDto>> hotelFacilities() {
        return new ResponseEntity<>(htfRepository.findDtosByHotel(SecurityUtils.getHotel().getId()));
    }

    @RequestMapping(value = "info", params = "hotelId", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<HotelInfo.HotelInfoDto> getSettings() {
        return new ResponseEntity<>(getCurrentSettings());
    }

    private HotelInfo.HotelInfoDto getCurrentSettings() {
        return new HotelInfo.HotelInfoDto(
                WubookImpl.ENABLED && SecurityUtils.isWubookConfigured(),
                hiRepository.findOne(SecurityUtils.getHotel().getInfo().getId()));
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class BookDto {
        @NotNull
        private LocalDate startDate;
        @NotNull
        private LocalDate endDate;
        @NotEmpty
        private List<BaseGroupRoomUse> roomUses;
        @NotNull
        private CustomerGroup group;
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class PriceRequestDto {
        @NotNull
        private Plan plan;
        @NotNull
        private LocalDate start;
        @NotNull
        private LocalDate end;
    }

    @Inject
    private LabelsResolver labelsResolver;

    @RequestMapping("labels")
    @ResponseBody
    public Map<String, String> getLabels(HttpServletRequest request) {
        return labelsResolver.getLabels(request);
    }
}