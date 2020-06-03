package com.idgindigo.pms.web.controller.admin;

import com.idgindigo.pms.configuration.LoginsJpaConfig;
import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.logins.domain.Authentication;
import com.idgindigo.pms.logins.domain.Hotel;
import com.idgindigo.pms.logins.domain.HotelUser;
import com.idgindigo.pms.logins.domain.Manager;
import com.idgindigo.pms.logins.domain.ManagerSupervisor;
import com.idgindigo.pms.logins.repository.AuthenticationRepository;
import com.idgindigo.pms.logins.repository.HotelRepository;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.restutils.exception.AuthenticationException;
import com.idgindigo.pms.restutils.exception.RestFriendlyException;
import com.idgindigo.pms.restutils.view.ResponseView;
import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.service.admin.AuthenticationService;
import com.idgindigo.pms.service.filtering.AuthenticationFilteringService;
import com.idgindigo.pms.service.filtering.FilteringService;
import com.idgindigo.pms.web.controller.BaseCrudController;
import com.idgindigo.pms.web.controller.ResponseEntity;
import com.idgindigo.pms.web.websocket.WebSocketEndpoint;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 4/14/14 1:17 PM
 */
@Controller
@RequestMapping(AuthenticationController.URL)
public class AuthenticationController extends BaseCrudController<Authentication> {
    public static final String URL = WebConfiguration.ADMIN_URL_PREFIX + Authentication.AUTHENTICATION;

    @Inject
    private AuthenticationRepository authenticationRepository;
    @Inject
    private HotelRepository hotelRepository;
    @Inject
    private PasswordEncoder passwordEncoder;
    @Inject
    private AuthenticationService authenticationService;
    @Inject
    private AuthenticationFilteringService filteringService;

    @Override
    @PreAuthorize("hasPermission(#authentication, 'AUTHENTICATION_CREATE')")
    public ResponseEntity<Authentication> create(@RequestBody Authentication authentication) {
        if (authentication.getUserType().equals(HotelUser.USER)) {
            throw new AuthenticationException(AuthenticationException.HOTEL_USER_CREATION_FORBIDDEN, "userType");
        }
        if (authenticationRepository.findAdministrativeByUsername(authentication.getUsername()) != null) {
            throw new RestFriendlyException(RestFriendlyException.DUPLICATE_ENTRY, "username");
        }
        if (authentication.getUserType().equals(Manager.MANAGER)) {
            injectSupervisor((Manager) authentication);
        }
        authentication.setPassword(passwordEncoder.encode(authentication.getPassword()));
        return new ResponseEntity<>(authenticationRepository.save(authentication));
    }

    private void injectSupervisor(Manager manager) {
        Authentication currentUser = SecurityUtils.getUserDetails().getAuthentication();
        if (currentUser.getUserType().equals(ManagerSupervisor.MANAGER_SUPERVISOR)) {
            manager.setSupervisor((ManagerSupervisor) currentUser);
        }
    }

    @Override
    public ResponseEntity<Authentication> update(@RequestBody Authentication entity, @PathVariable("id") Long id) {
        throw new EntityNotFoundException();//TODO Check if this has to be implemented
    }

    @RequestMapping(value = "hotel/{hotelId}", method = RequestMethod.PUT)
    @ResponseBody
    @PreAuthorize("hasPermission(#hotelId, 'AUTHENTICATION_CHANGE_HOTEL')")
    public void setHotel(@PathVariable("hotelId") String hotelId) {
        Authentication auth = authenticationRepository.findOne(SecurityUtils.getUserDetails().getAuthentication().getId());
        Hotel hotel = hotelRepository.findByTenantId(hotelId);
        if (hotel == null) {
            throw new EntityNotFoundException();
        }
        auth.setHotel(hotel);
        authenticationRepository.save(auth);
        SecurityUtils.getUserDetails().setAuthentication(auth);
    }

    @Override
    @PreAuthorize("hasPermission(#id, 'AUTHENTICATION_VIEW')")
    @ResponseView(Authentication.AuthView.class)
    public ResponseEntity<Authentication> getById(@PathVariable("id") Long id) {
        return super.getById(id);
    }

    @Override
    @Transactional(LoginsJpaConfig.TRANSACTION_MANAGER)
    @PreAuthorize("hasPermission('null', 'AUTHENTICATION_LIST')")
    @ResponseView(Authentication.AuthView.class)
    public ResponseEntity<List<Authentication>> list(
            @RequestParam(value = PAGE, defaultValue = "1") Integer page,
            @RequestParam(value = START, defaultValue = "0") Integer start,
            @RequestParam(value = LIMIT, defaultValue = DEFAULT_RECORDS_ON_PAGE) Integer limit,
            @RequestParam(value = SORT, required = false) String sortString,
            @RequestParam(value = SHOW_DELETED, defaultValue = "false") boolean showDeleted) throws IOException {
        ResponseEntity<List<Authentication>> list = super.list(page, start, limit, sortString, showDeleted);
        for (Authentication authentication : list.getContent()) {
            authentication.setOnline(isOnline(authentication));
        }
        return list;
    }

    public boolean isOnline(Authentication authentication) {
        return WebSocketEndpoint.isOnline(authentication.getHotel() != null ? authentication.getHotel().getTenantId() : null, authentication.getUsername());
    }

    @Override
    @Transactional(LoginsJpaConfig.TRANSACTION_MANAGER)
    @PreAuthorize("hasPermission('null', 'AUTHENTICATION_LIST')")
    @ResponseView(Authentication.AuthView.class)
    public ResponseEntity<List<Authentication>> listFiltered(
            @RequestParam(value = PAGE, defaultValue = "1") Integer page,
            @RequestParam(value = START, defaultValue = "0") Integer start,
            @RequestParam(value = LIMIT, defaultValue = DEFAULT_RECORDS_ON_PAGE) Integer limit,
            @RequestParam(value = SORT, required = false) String sortString,
            @RequestParam(value = CONNECTIVE, defaultValue = "and") String connective, HttpServletRequest request) throws Exception {
        ResponseEntity<List<Authentication>> list = super.listFiltered(page, start, limit, sortString, connective, request);
        for (Authentication authentication : list.getContent()) {
            authentication.setOnline(isOnline(authentication));
        }
        return list;
    }

    @RequestMapping(value = ID + "/password", method = RequestMethod.PUT)
    @ResponseBody
    @PreAuthorize("hasPermission(#id, 'AUTHENTICATION_CHANGE_PASSWORD')")
    public void setPassword(@PathVariable("id") Long id, @RequestBody @Valid AuthenticationService.PasswordDto dto) {
        authenticationService.setPassword(SecurityUtils.getUserDetails().getAuthentication(), dto);
    }

    @RequestMapping(value = ID + "/language", method = RequestMethod.PUT)
    @ResponseBody
//    @PreAuthorize("hasPermission(#id, 'AUTHENTICATION_CHANGE_LANGUAGE')")
    public void setLang(@PathVariable("id") Long id, @RequestBody @Valid ValueDto dto) {
        authenticationRepository.setLang(dto.getValue(), id);
    }

    @Override
    public BaseRepository<Authentication> getRepository() {
        return authenticationRepository;
    }

    @Override
    protected FilteringService<Authentication> getFilteringService() {
        return filteringService;
    }

    @Getter
    @Setter
    public static class ValueDto {
        @NotNull
        private String value;
    }
}
