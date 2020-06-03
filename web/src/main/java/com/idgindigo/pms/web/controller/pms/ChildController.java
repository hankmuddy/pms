package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.domain.pms.Child;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.pms.ChildRepository;
import com.idgindigo.pms.service.filtering.ChildFilteringService;
import com.idgindigo.pms.service.filtering.FilteringService;
import com.idgindigo.pms.web.controller.BaseCrudController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 12/27/13 11:57 AM
 */
@Controller
@RequestMapping(ChildController.URL)
public class ChildController extends BaseCrudController<Child> {
    public static final String URL = WebConfiguration.REST_URL_PREFIX + Child.CHILD;
    @Inject
    private ChildFilteringService filteringService;
    @Inject
    private ChildRepository repository;

    @Override
    public BaseRepository<Child> getRepository() {
        return repository;
    }

    @Override
    protected FilteringService<Child> getFilteringService() {
        return filteringService;
    }
}
