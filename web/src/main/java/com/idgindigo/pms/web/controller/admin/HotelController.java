package com.idgindigo.pms.web.controller.admin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.idgindigo.pms.channel.wubook.WubookImpl;
import com.idgindigo.pms.configuration.LoginsJpaConfig;
import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.domain.Role;
import com.idgindigo.pms.logins.domain.Authentication;
import com.idgindigo.pms.logins.domain.Hotel;
import com.idgindigo.pms.logins.domain.Manager;
import com.idgindigo.pms.logins.domain.ManagerSupervisor;
import com.idgindigo.pms.logins.repository.*;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.restutils.view.ResponseView;
import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.service.admin.AuthenticationService;
import com.idgindigo.pms.service.admin.HotelService;
import com.idgindigo.pms.service.admin.SettingsService;
import com.idgindigo.pms.service.channels.ChannelService;
import com.idgindigo.pms.service.multitenancy.TenantService;
import com.idgindigo.pms.web.controller.BaseCrudController;
import com.idgindigo.pms.web.controller.ResponseEntity;
import com.idgindigo.pms.web.controller.extranet.DocumentController;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 12/19/13 5:04 PM
 */
@Controller
@RequestMapping(HotelController.URL)
public class HotelController extends BaseCrudController<Hotel> {
    public static final String URL = WebConfiguration.ADMIN_URL_PREFIX + Hotel.HOTEL;
    private static final Logger logger = LoggerFactory.getLogger(HotelController.class);

    @Inject
    private HotelRepository hotelRepository;
    @Inject
    private HotelToFacilityRepository htfRepository;
    @Inject
    private TenantService tenantService;
    @Inject
    private HotelService hotelService;
    @Inject
    private AuthenticationService authenticationService;
    @Inject
    private SettingsService settingsService;
    @Inject
    private ChannelService channelService;
    @Inject
    private HotelInfoRepository hotelInfoRepository;
    @Inject
    private AuthenticationRepository authenticationRepository;
    @Inject
    private BookingButtonSettingsRepository bbsRepository;
    @Inject
    private BookingButtonSettingsValuesRepository bbsvRepository;
    @Inject
    private ActivationRepository activationRepository;
    @Inject
    private ManagerSupervisorRepository supervisorRepository;
    @Inject
    private ManagerRepository managerRepository;

    @RequestMapping(method = RequestMethod.POST)
    @Transactional(LoginsJpaConfig.TRANSACTION_MANAGER)
    @ResponseBody
    @PreAuthorize("hasPermission(#dto, 'HOTEL_CREATE')")
    public void create(@RequestBody @Valid UserCreateDto dto) {
        HotelUserDto userData = dto.getUser();
        Hotel hotel = hotelService.save(dto.getHotel());
        if (WubookImpl.ENABLED && SecurityUtils.isWubookConfigured(hotel)) {
            String result = channelService.attachPushUrlToLcode(SecurityUtils.getWubookAccount(hotel));
            if (!result.equalsIgnoreCase("Ok")) throw new IllegalArgumentException("Wrong Wubook data");
        }
        logger.info("Creating hotel: {} ", hotel);
        authenticationService.create(userData.getAuthentication(), hotel);
        tenantService.initTenant(hotel.getTenantId());
        List<Role> roles = dto.getRoles();
        hotelService.initHotel(userData, roles);
        logger.info("Successfully created hotel: {} ", hotel);

    }

    @Override
    @PreAuthorize("hasPermission(#id, 'HOTEL_VIEW')")
    @ResponseView(Hotel.HotelView.class)
    public ResponseEntity<Hotel> getById(@PathVariable("id") Long id) {
        return super.getById(id);
    }

    @Override
    @Transactional(LoginsJpaConfig.TRANSACTION_MANAGER)
    @PreAuthorize("hasPermission('null', 'HOTEL_LIST')")
    @ResponseView(Hotel.HotelView.class)
    public ResponseEntity<List<Hotel>> list(@RequestParam(value = PAGE, defaultValue = "1") Integer page, @RequestParam(value = START, defaultValue = "0") Integer start, @RequestParam(value = LIMIT, defaultValue = DEFAULT_RECORDS_ON_PAGE) Integer limit, @RequestParam(value = SORT, required = false) String sortString, @RequestParam(value = SHOW_DELETED, defaultValue = "false") boolean showDeleted) throws IOException {
        return super.list(page, start, limit, sortString, showDeleted);
    }

    @Override
    @Transactional(LoginsJpaConfig.TRANSACTION_MANAGER)
    @PreAuthorize("hasPermission('null', 'HOTEL_LIST')")
    @ResponseView(Hotel.HotelView.class)
    public ResponseEntity<List<Hotel>> listFiltered(@RequestParam(value = PAGE, defaultValue = "1") Integer page, @RequestParam(value = START, defaultValue = "0") Integer start, @RequestParam(value = LIMIT, defaultValue = DEFAULT_RECORDS_ON_PAGE) Integer limit, @RequestParam(value = SORT, required = false) String sortString, @RequestParam(value = CONNECTIVE, defaultValue = "and") String connective, HttpServletRequest request) throws Exception {
        return super.listFiltered(page, start, limit, sortString, connective, request);
    }

    @ResponseBody
    @RequestMapping(value = ID + "/blocked/{blocked}", method = RequestMethod.PUT)
    @PreAuthorize("hasPermission(#id, 'HOTEL_VIEW')")
    public void block(@PathVariable("id") Long id, @PathVariable("blocked") boolean blocked) {
        hotelRepository.setBlocked(id, blocked);
    }

    @ResponseBody
    @RequestMapping(value = ID + "/maxRooms/{maxRooms}", method = RequestMethod.PUT)
    @PreAuthorize("hasPermission(#id, 'HOTEL_VIEW')")
    public void setMaxRooms(@PathVariable("id") Long id, @PathVariable("maxRooms") int maxRooms) {
        hotelRepository.setMaxRooms(id, maxRooms);
    }

    @ResponseBody
    @RequestMapping(value = ID + "/paidUntil/{paidUntil}", method = RequestMethod.PUT)
    @PreAuthorize("hasPermission(#id, 'HOTEL_VIEW')")
    public void setPaidUntil(@PathVariable("id") Long id, @PathVariable("paidUntil") String paidUntil) {
        hotelRepository.setPaidUntil(id, LocalDate.parse(paidUntil, ChannelService.BOOKING_FORMATTER));
    }

    @ResponseBody
    @RequestMapping(value = ID + "/supervisor/{supervisorId}", method = RequestMethod.PUT)
    public void setSupervisor(@PathVariable("id") Long id, @PathVariable("supervisorId") Long supervisorId) {
        Hotel hotel = hotelRepository.findOne(id);
        ManagerSupervisor supervisor = supervisorRepository.findOne(supervisorId);
        if (hotel == null || supervisor == null) {
            throw new EntityNotFoundException();
        }
        hotel.setManager(null);
        hotel.setSupervisor(supervisor);
        hotelRepository.save(hotel);
    }

    @ResponseBody
    @RequestMapping(value = ID + "/manager/{managerId}", method = RequestMethod.PUT)
    @PreAuthorize("hasPermission(#id, 'HOTEL_VIEW') and hasAnyRole('admin','managerSupervisor')")
    public void setManager(@PathVariable("id") Long id, @PathVariable("managerId") Long managerId) {
        Hotel hotel = hotelRepository.findOne(id);
        Manager manager = managerRepository.findOne(managerId);
        if (hotel == null || manager == null) {
            throw new EntityNotFoundException();
        }
        if (hotel.getSupervisor() == null) {
            hotel.setSupervisor(manager.getSupervisor());
        }
        if (!hotel.getSupervisor().equals(manager.getSupervisor())) {
            throw new AccessDeniedException("forbidden");
        }
        hotel.setManager(manager);
        hotelRepository.save(hotel);
    }

    @Override
    @RequestMapping("null1")
    public ResponseEntity<Hotel> update(@RequestBody Hotel entity, @PathVariable("id") Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    @RequestMapping("null")
    public ResponseEntity<Hotel> create(@RequestBody Hotel entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        throw new UnsupportedOperationException();
    }

    @RequestMapping("timeZones")
    @ResponseBody
    public ResponseEntity<List<TimeZoneDto>> getTimeZones() {
        long now = settingsService.getHotelDate().toDate().getTime();
        List<TimeZoneDto> res = new LinkedList<>();
        for (String s : DateTimeZone.getAvailableIDs()) {
            DateTimeZone zone = DateTimeZone.forID(s);
            res.add(new TimeZoneDto(s, zone.getStandardOffset(now), zone.getName(now)));
        }
        Collections.sort(res);
        return new ResponseEntity<>(res);
    }

    @RequestMapping(value = "dropTenant", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> dropTenant(@RequestParam("hotelId") String hotelId) {
        Hotel hotel = hotelRepository.findByTenantId(hotelId);
        if (hotel == null) {
            logger.warn("Cannot drop hotel: not found by tenantId='{}'", hotelId);
            return new ResponseEntity<>();
        }
        if (!hotel.isBlocked()) {
            logger.warn("Cannot drop active hotel by tenantId='{}'", hotelId);
            return new ResponseEntity<>();
        }
        //1 drop photos
        logger.warn("Dropping photos for tenantId='{}'", hotelId);
        doDeleteTenantDirectory(hotelId);
        logger.warn("Successfully dropped photos for tenantId='{}'", hotelId);

        //2 drop 'logins' records
        logger.warn("Dropping records from 'logins' schema for tenantId='{}'", hotelId);
        activationRepository.deleteByHotelId(hotelId);
        authenticationRepository.deleteUsersByHotelId(hotel.getId());
        htfRepository.deleteByHotelId(hotel.getId());

        //reset all connected users
        List<Authentication> toReset = authenticationRepository.findByHotelId(hotel.getId());
        for (Authentication authentication : toReset) {
            authentication.setHotel(null);
        }
        authenticationRepository.save(toReset);

        bbsvRepository.deleteByHotelId(hotel.getId());
        bbsRepository.deleteByHotelId(hotel.getId());
        hotelRepository.delete(hotel);
        hotelInfoRepository.deleteForHotelId(hotel.getId());
        logger.warn("Successfully dropped records from 'logins' schema for tenantId='{}'", hotelId);

        //3 drop schema
        tenantService.dropTenant(hotelId);

        return new ResponseEntity<>();
    }

    private static void doDeleteTenantDirectory(String tenantId) {
        File dest = DocumentController.getHotelImagesDirectory(tenantId);
        File[] files = dest.listFiles();
        for (File file : files) {
            file.delete();
        }
        dest.delete();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class TimeZoneDto implements Comparable<TimeZoneDto> {
        private String id;
        private int offset;
        private String name;

        @Override
        public int compareTo(TimeZoneDto o) {
            int res = offset - o.offset;
            if (res != 0) {
                return res;
            } else {
                return id.compareTo(o.id);
            }
        }
    }

    @Override
    public BaseRepository<Hotel> getRepository() {
        return hotelRepository;
    }


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)//TODO Remove this annotation
    public static class UserCreateDto extends SignupController.UserSignupDto {
        private List<Role> roles;
    }
}
