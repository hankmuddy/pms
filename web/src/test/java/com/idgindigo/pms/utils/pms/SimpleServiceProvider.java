package com.idgindigo.pms.utils.pms;

import com.idgindigo.pms.domain.pms.SimpleService;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.extranet.service.FixedServiceRepository;
import com.idgindigo.pms.repository.extranet.service.SimpleServiceRepository;
import com.idgindigo.pms.utils.EntityProvider;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 11/20/13 2:42 PM
 */
@Component
public class SimpleServiceProvider extends EntityProvider<SimpleService> {
    @Inject
    private SimpleServiceRepository repository;
    @Inject
    private FixedServiceRepository fixedServiceRepository;

    @Override
    public SimpleService createAndFill() {
        SimpleService service = new SimpleService();
        do {
            service.setTitle(randomString());
        } while (fixedServiceRepository.existsByTitle(service.getTitle()));
        service.setMeasure(randomString());
        service.setPrice(randomPositiveLong());
        return service;
    }

    @Override
    public BaseRepository<SimpleService> getRepository() {
        return repository;
    }
}