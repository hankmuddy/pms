package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.domain.pms.SimpleService;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.extranet.service.FixedServiceRepository;
import com.idgindigo.pms.repository.extranet.service.ServiceRepository;
import com.idgindigo.pms.repository.extranet.service.SimpleServiceRepository;
import com.idgindigo.pms.restutils.exception.RestFriendlyException;
import com.idgindigo.pms.web.controller.BaseCrudController;
import com.idgindigo.pms.web.controller.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 11/20/13 2:40 PM
 */
@Component
@RequestMapping(SimpleServiceController.URL)
public class SimpleServiceController extends BaseCrudController<SimpleService> {
    public static final String URL = WebConfiguration.REST_URL_PREFIX + SimpleService.SIMPLE_SERVICE;

    @Inject
    private SimpleServiceRepository repository;
    @Inject
    private FixedServiceRepository fixedServiceRepository;
    @Inject
    private ServiceRepository serviceRepository;

    @Override
    @Transactional
    public ResponseEntity<SimpleService> create(@RequestBody SimpleService entity) {
        if (fixedServiceRepository.existsByTitle(entity.getTitle())) {
            throw new RestFriendlyException(RestFriendlyException.DUPLICATE_ENTRY, "title");
        }
        return super.create(entity);
    }

    @Override
    @Transactional
    public ResponseEntity<SimpleService> update(@RequestBody SimpleService entity, @PathVariable("id") Long id) {
        SimpleService old = repository.findOne(id);
        if (old != null && !old.getTitle().equals(entity.getTitle()) && fixedServiceRepository.existsByTitle(entity.getTitle())) {
            throw new RestFriendlyException(RestFriendlyException.DUPLICATE_ENTRY, "title");
        }
        return super.update(entity, id);
    }

    @Override
    @Transactional
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        serviceRepository.deprecate(id);
        return new ResponseEntity<>();
    }

    @Override
    public BaseRepository<SimpleService> getRepository() {
        return repository;
    }

}