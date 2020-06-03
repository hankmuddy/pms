package com.idgindigo.pms.web.controller.extranet;

import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.domain.extranet.ExtranetRoomUse;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.extranet.roomuse.ExtranetRoomUseRepository;
import com.idgindigo.pms.service.filtering.ExtranetRoomUseFilteringService;
import com.idgindigo.pms.service.filtering.FilteringService;
import com.idgindigo.pms.web.controller.BaseCrudController;
import com.idgindigo.pms.web.controller.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;

/**
 * @author valentyn_vakatsiienko
 * @since 6/10/14 2:57 PM
 */
@Controller
@RequestMapping(ExtranetRoomUseController.URL)
public class ExtranetRoomUseController extends BaseCrudController<ExtranetRoomUse> {
    public static final String URL = WebConfiguration.REST_URL_PREFIX + ExtranetRoomUse.EXTRANET_ROOM_USE;
    @Inject
    private ExtranetRoomUseFilteringService filteringService;

    @Override
    public ResponseEntity<ExtranetRoomUse> create(@RequestBody ExtranetRoomUse entity) {
        throw new EntityNotFoundException();
    }

    @Override
    public ResponseEntity<ExtranetRoomUse> update(@RequestBody ExtranetRoomUse entity, @PathVariable("id") Long id) {
        throw new EntityNotFoundException();
    }

    @Inject
    private ExtranetRoomUseRepository repository;

    @Override
    public BaseRepository<ExtranetRoomUse> getRepository() {
        return repository;
    }

    @Override
    protected FilteringService<ExtranetRoomUse> getFilteringService() {
        return filteringService;
    }
}
