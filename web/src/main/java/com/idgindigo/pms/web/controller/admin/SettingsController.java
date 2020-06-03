package com.idgindigo.pms.web.controller.admin;

import com.idgindigo.pms.channel.wubook.WubookAccount;
import com.idgindigo.pms.channel.wubook.WubookImpl;
import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.domain.Identifiable;
import com.idgindigo.pms.domain.extranet.plan.BasePlan;
import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeValue;
import com.idgindigo.pms.logins.domain.Hotel;
import com.idgindigo.pms.logins.domain.HotelFacility;
import com.idgindigo.pms.logins.domain.HotelFacilityDto;
import com.idgindigo.pms.logins.domain.HotelInfo;
import com.idgindigo.pms.logins.domain.HotelToFacility;
import com.idgindigo.pms.logins.repository.HotelFacilityRepository;
import com.idgindigo.pms.logins.repository.HotelInfoRepository;
import com.idgindigo.pms.logins.repository.HotelRepository;
import com.idgindigo.pms.logins.repository.HotelToFacilityRepository;
import com.idgindigo.pms.repository.extranet.RoomTypeRepository;
import com.idgindigo.pms.repository.extranet.rate.RoomTypeValueRepository;
import com.idgindigo.pms.restutils.exception.RestFriendlyException;
import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.service.channels.ChannelService;
import com.idgindigo.pms.service.pms.QuotaService;
import com.idgindigo.pms.web.controller.ResponseEntity;
import com.idgindigo.pms.web.websocket.WebSocketEndpoint;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.idgindigo.pms.logins.domain.Hotel.WubookImportStatus;
import static com.idgindigo.pms.logins.domain.Hotel.WubookImportStatus.DATA_IMPORTED;
import static com.idgindigo.pms.logins.domain.Hotel.WubookImportStatus.INIT;
import static com.idgindigo.pms.logins.domain.Hotel.WubookImportStatus.RESERVATIONS_IMPORTED;
import static com.idgindigo.pms.logins.domain.HotelInfo.HotelInfoDto;


/**
 * @author valentyn_vakatsiienko
 * @since 1/31/14 10:41 AM
 */
@Controller
@RequestMapping(SettingsController.URL)
public class SettingsController {

    public static final String URL = WebConfiguration.REST_URL_PREFIX + "settings";
    private static final Logger logger = LoggerFactory.getLogger(SettingsController.class);
    @Inject
    private HotelInfoRepository repository;
    @Inject
    private HotelRepository hotelRepository;
    @Inject
    private RoomTypeRepository roomTypeRepository;
    @Inject
    private RoomTypeValueRepository roomTypeValueRepository;
    @Inject
    private QuotaService quotaService;
    @Inject
    private HotelFacilityRepository hfRepository;
    @Inject
    private HotelToFacilityRepository htfRepository;
    @Inject
    private ChannelService channelService;

    @RequestMapping
    @ResponseBody
    public ResponseEntity<HotelInfoDto> getSettings() {
        return new ResponseEntity<>(getCurrentSettings());
    }

    @RequestMapping("allFacilities")
    @ResponseBody
    public ResponseEntity<List<HotelFacility>> allFacilities() {
        return new ResponseEntity<>(hfRepository.findAll());
    }

    @RequestMapping("hotelFacilities")
    @ResponseBody
    public ResponseEntity<List<HotelFacilityDto>> hotelFacilities() {
        return new ResponseEntity<>(htfRepository.findDtosByHotel(SecurityUtils.getHotel().getId()));
    }

    @RequestMapping(value = "saveChannelsAccount", method = RequestMethod.PUT)
    @Transactional
    @ResponseBody
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<WubookAccount> saveChannelsAccount(@RequestBody WubookAccount wubookAccount, @RequestParam Long id) {
        Hotel hotel = hotelRepository.findOne(id);
        if (hotel == null) {
            throw new EntityNotFoundException();
        }
        if (SecurityUtils.isWubookConfigured(hotel)) {
            throw new RestFriendlyException(RestFriendlyException.WUBOOK_ALREADY_CONFIGURED);
        }
        setWubookAccount(wubookAccount, hotel);
        hotelRepository.save(hotel);
        setWubookAccount(wubookAccount, SecurityUtils.getHotel());
        return new ResponseEntity<>(wubookAccount);
    }

    private static void setWubookAccount(WubookAccount wubookAccount, Hotel hotel) {
        hotel.setWuName(wubookAccount.getUsername());
        hotel.setWuPass(wubookAccount.getPassword());
        hotel.setLcode(wubookAccount.getLcode());
    }

    @RequestMapping(method = RequestMethod.PUT)
    @Transactional
    @ResponseBody
    public ResponseEntity<HotelInfoDto> saveSettings(@RequestBody HotelInfo settings) {
        HotelInfo current = repository.findOne(SecurityUtils.getHotel().getInfo().getId());
        settings.setId(current.getId());
        if (StringUtils.isBlank(settings.getLogo())) settings.setLogo(current.getLogo());//UI does not send logo if not changed
        if (StringUtils.isBlank(settings.getMainPhoto())) settings.setMainPhoto(current.getMainPhoto());//UI does not send mainPhoto if not changed
        HotelInfo saved = repository.save(settings);
        SecurityUtils.getHotel().setInfo(saved);
        return new ResponseEntity<>(getCurrentSettings());
    }

    @RequestMapping(value = "logo", method = RequestMethod.DELETE)
    @Transactional
    @ResponseBody
    public void deleteLogo() {
        //TODO delete from file system
        HotelInfo current = repository.findOne(SecurityUtils.getHotel().getInfo().getId());
        current.setLogo(null);
        repository.save(current);
        SecurityUtils.getHotel().setInfo(current);
    }

    @RequestMapping(value = "paymentInfo", method = RequestMethod.PUT)
    @Transactional
    @ResponseBody
    public void savePaymentInfo(@RequestBody ValueDto dto) {
        HotelInfo current = repository.findOne(SecurityUtils.getHotel().getInfo().getId());
        current.setPaymentInfo(dto.value);
        repository.setPaymentInfo(dto.value, current.getId());
        SecurityUtils.getHotel().setInfo(current);
    }

    @RequestMapping(value = "importantInfo", method = RequestMethod.PUT)
    @Transactional
    @ResponseBody
    public void saveImportantInfo(@RequestBody ValueDto dto) {
        HotelInfo current = repository.findOne(SecurityUtils.getHotel().getInfo().getId());
        current.setImportantInfo(dto.value);
        repository.setImportantInfo(dto.value, current.getId());
        SecurityUtils.getHotel().setInfo(current);
    }

    @RequestMapping(value = "mainPhoto", method = RequestMethod.PUT)
    @Transactional
    @ResponseBody
    public void saveMainPhoto(@RequestBody ValueDto dto) {
        HotelInfo current = repository.findOne(SecurityUtils.getHotel().getInfo().getId());
        current.setMainPhoto(dto.value);
        repository.setMainPhoto(dto.value, current.getId());
        SecurityUtils.getHotel().setInfo(current);
    }

    @RequestMapping(method = RequestMethod.GET, value = "synchronizeQuota")
    @ResponseBody
    public ResponseEntity<?> synchronizeQuota(@RequestParam("dfrom") String dfrom, @RequestParam("dto") String dto) {
        if (WubookImpl.ENABLED && SecurityUtils.isWubookConfigured() && SecurityUtils.getHotel().getImportStatus() == RESERVATIONS_IMPORTED) {
            List<RoomType> roomTypes = roomTypeRepository.findAll();
            for (RoomType roomType : roomTypes) {
                LocalDate start = LocalDate.parse(dfrom, ChannelService.BOOKING_FORMATTER);
                LocalDate end = LocalDate.parse(dto, ChannelService.BOOKING_FORMATTER);
                List<RoomTypeValue> values = roomTypeValueRepository.findByRoomTypeAndDateBetween(roomType, start, end);
                List<RoomTypeValue> blanks = quotaService.fillBlanks(roomType, values, start, end);
                blanks.addAll(values);
                Collections.sort(blanks, RoomTypeValue.DATE_COMPARATOR);
                channelService.updateRoomValues(blanks, start, SecurityUtils.getWubookAccount());
            }
        } else {
            throw new AccessDeniedException("Not allowed");
        }
        return new ResponseEntity<>();
    }

    @RequestMapping(method = RequestMethod.POST, value = "exportToWubook")
    @Transactional
    @ResponseBody
    public ResponseEntity<?> exportToWubook() {
        Hotel hotel = SecurityUtils.getHotel();
        if (WubookImpl.ENABLED && SecurityUtils.isWubookConfigured() && SecurityUtils.getHotel().getImportStatus() == INIT) {
            logger.info("Exporting data from hotel: {}", hotel);
            channelService.attachPushUrlToLcode(SecurityUtils.getWubookAccount(hotel));
            channelService.exportToWubook(hotel);
            setWubookSynchStatus(hotel, RESERVATIONS_IMPORTED);
            logger.info("Successfully exported data from hotel: {}", hotel);
        } else {
            throw new AccessDeniedException("Not allowed");
        }
        return new ResponseEntity<>();
    }

    @RequestMapping(method = RequestMethod.POST, value = "importFromWubook")
    @Transactional
    @ResponseBody
    public ResponseEntity<?> importFromWubook() {
        Hotel hotel = SecurityUtils.getHotel();
        if (WubookImpl.ENABLED && SecurityUtils.isWubookConfigured() && SecurityUtils.getHotel().getImportStatus() == INIT) {
            logger.info("Importing data from hotel: {}", hotel);
            logger.info("Importing roomTypes...");
            Collection<RoomType> roomTypes = channelService.fetchRooms(SecurityUtils.getWubookAccount());
            logger.info("Imported roomTypes successfully: {}", roomTypes);
            logger.info("Importing plans, seasons and prices...");
            Collection<BasePlan> plans = channelService.fetchPlans(SecurityUtils.getWubookAccount());
            logger.info("Imported plans, seasons and prices successfully: {}", plans);
            logger.info("Successfully imported data from hotel: {}", hotel);
            turnWubookSynchOff(hotel);
        } else {
            throw new AccessDeniedException("Not allowed");
        }
        return new ResponseEntity<>();
    }

    public void turnWubookSynchOff(Identifiable hotel) {
        logger.info("Setting WuBook synch status for hotel: {} ...", hotel);
        Hotel current = setWubookSynchStatus(hotel, DATA_IMPORTED);
        logger.info("Set WuBook synch status for hotel: {}", current);
    }

    public Hotel setWubookSynchStatus(Identifiable hotel, WubookImportStatus status) {
        Hotel current = hotelRepository.findOne(hotel.getId());
        current.setImportStatus(status);
        hotelRepository.save(current);
        SecurityUtils.getUserDetails().getAuthentication().setHotel(current);
        return current;
    }

    @RequestMapping(value = "bind", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @Transactional
    public ResponseEntity<?> create(@RequestBody Iterable<HotelFacilityDto> facilities) {
        Hotel hotel = SecurityUtils.getHotel();
        Long hotelId = hotel.getId();
        List<HotelFacility> existing = htfRepository.findFacilitiesByHotel(hotelId);
        Collection<HotelToFacility> added = new ArrayList<>();
        for (HotelFacilityDto hfDto : facilities) {
            if (existing.contains(hfDto.getFacility())) {
                existing.remove(hfDto.getFacility());
            } else {
                added.add(new HotelToFacility(hotel, hfDto.getFacility(), hfDto.getChargeFree()));
            }
        }
        htfRepository.save(added);
        if (!existing.isEmpty()) {
            htfRepository.removeSelected(hotelId, existing);
        }
        return new ResponseEntity<>();
    }

    @RequestMapping(value = "broadcast", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> broadcast(@RequestParam("text") String text) {
        WebSocketEndpoint.broadcast(text);
        return new ResponseEntity<>();
    }


    private HotelInfoDto getCurrentSettings() {
        if (SecurityUtils.getHotel() == null) {
            logger.warn("{}:{}:Not a tenant user!", SecurityUtils.getCurrentTenantId(), SecurityUtils.getUsername());
            return null;
        }
        return new HotelInfoDto(
                WubookImpl.ENABLED && SecurityUtils.isWubookConfigured(),
                repository.findOne(SecurityUtils.getHotel().getInfo().getId()));
    }

    @Getter
    @Setter
    public static class ValueDto {
        @NotNull
        private String value;
    }
}
