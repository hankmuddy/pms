package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.domain.pms.CompanyContact;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.pms.CompanyContactRepository;
import com.idgindigo.pms.service.filtering.CompanyContactFilteringService;
import com.idgindigo.pms.service.filtering.FilteringService;
import com.idgindigo.pms.web.controller.BaseCrudController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 1/10/14 11:22 AM
 */
@Controller
@RequestMapping(CompanyContactController.URL)
public class CompanyContactController extends BaseCrudController<CompanyContact> {
    public static final String URL = WebConfiguration.REST_URL_PREFIX + CompanyContact.COMPANY_CONTACT;
    @Inject
    private CompanyContactRepository repository;
    @Inject
    private CompanyContactFilteringService filteringService;

    @Override
    protected FilteringService<CompanyContact> getFilteringService() {
        return filteringService;
    }

    @Override
    public BaseRepository<CompanyContact> getRepository() {
        return repository;
    }
}
