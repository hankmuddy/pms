package com.idgindigo.pms.web.controller.admin;

import com.idgindigo.pms.configuration.LoginsJpaConfig;
import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.domain.Permission;
import com.idgindigo.pms.logins.domain.BookingButtonSettings;
import com.idgindigo.pms.logins.domain.BookingButtonSettingsValues;
import com.idgindigo.pms.logins.repository.BookingButtonSettingsRepository;
import com.idgindigo.pms.logins.repository.BookingButtonSettingsValuesRepository;
import com.idgindigo.pms.logins.repository.HotelRepository;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.web.controller.BaseCrudController;
import com.idgindigo.pms.web.controller.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * @author vomel
 * @since 27.05.14 15:49
 */
@Controller
@RequestMapping(BookingButtonSettingsController.URL)
public class BookingButtonSettingsController extends BaseCrudController<BookingButtonSettings> {
    public static final String URL = WebConfiguration.ADMIN_URL_PREFIX + "bbs";
    @Inject
    private BookingButtonSettingsRepository repository;
    @Inject
    private BookingButtonSettingsValuesRepository bbsvRepository;
    @Inject
    private HotelRepository hotelRepository;

    @Override
    public BaseRepository<BookingButtonSettings> getRepository() {
        return repository;
    }

    @Transactional(LoginsJpaConfig.TRANSACTION_MANAGER)
    @Override
    public ResponseEntity<BookingButtonSettings> update(@RequestBody BookingButtonSettings entity, @PathVariable("id") Long id) {
        BookingButtonSettings existing = repository.findOne(id);
        checkPermission(entity);
        entity.setHotel(existing.getHotel());
        List<BookingButtonSettingsValues> keyValues = entity.getKeyValues();
        ResponseEntity<BookingButtonSettings> updated = super.update(entity, id);
        bbsvRepository.deleteByBbsId(id);
        for (BookingButtonSettingsValues keyValue : keyValues) {
            keyValue.setBbs(updated.getContent());
        }
        updated.getContent().setKeyValues(bbsvRepository.save(keyValues));
        return updated;
    }

    public static void checkPermission(BookingButtonSettings entity) {
        //TODO add 'id' based restriction!!!
        Collection<? extends GrantedAuthority> authorities = SecurityUtils.getAuthentication().getAuthorities();
        if (!authorities.contains(Permission.SETTINGS_SAVE)) {
            throw new AccessDeniedException("Sorry");
        }
    }

    @Transactional(LoginsJpaConfig.TRANSACTION_MANAGER)
    @Override
    public ResponseEntity<List<BookingButtonSettings>> list(@RequestParam(value = PAGE, defaultValue = "1") Integer page, @RequestParam(value = START, defaultValue = "0") Integer start, @RequestParam(value = LIMIT, defaultValue = DEFAULT_RECORDS_ON_PAGE) Integer limit, @RequestParam(value = SORT, required = false) String sortString, @RequestParam(value = SHOW_DELETED, defaultValue = "false") boolean showDeleted) throws IOException {
        return super.list(page, start, limit, sortString, showDeleted);
    }

    @Transactional(LoginsJpaConfig.TRANSACTION_MANAGER)
    @Override
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        bbsvRepository.deleteByBbsId(id);
        return super.delete(id);
    }

    @Transactional(LoginsJpaConfig.TRANSACTION_MANAGER)
    @Override
    public ResponseEntity<BookingButtonSettings> getById(@PathVariable("id") Long id) {
        return super.getById(id);
    }

    @Transactional(LoginsJpaConfig.TRANSACTION_MANAGER)
    @Override
    public ResponseEntity<BookingButtonSettings> create(@RequestBody BookingButtonSettings entity) {
        String tenantId = entity.getHotel().getTenantId();
        if (tenantId != null) {
            entity.setHotel(hotelRepository.findByTenantId(tenantId));
        }
        List<BookingButtonSettingsValues> keyValues = entity.getKeyValues();
        ResponseEntity<BookingButtonSettings> bbs = super.create(entity);
        for (BookingButtonSettingsValues keyValue : keyValues) {
            keyValue.setBbs(bbs.getContent());
        }
        bbsvRepository.save(keyValues);
        bbs.setContent(repository.findOne(bbs.getContent().getId()));
        return bbs;
    }

    public BookingButtonSettings getSettings() {
        List<BookingButtonSettings> all = repository.findAll();
        return all.isEmpty() ? null : all.get(0);
    }
}
