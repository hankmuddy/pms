package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.domain.pms.Company;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.pms.CompanyRepository;
import com.idgindigo.pms.service.filtering.CompanyFilteringService;
import com.idgindigo.pms.service.filtering.FilteringService;
import com.idgindigo.pms.web.controller.BaseCrudController;
import com.idgindigo.pms.web.controller.DiscountDto;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.validation.Valid;

/**
 * @author valentyn_vakatsiienko
 * @since 12/24/13 6:46 PM
 */
@Controller
@RequestMapping(CompanyController.URL)
public class CompanyController extends BaseCrudController<Company> {
    public static final String URL = WebConfiguration.REST_URL_PREFIX + Company.COMPANY;
    @Inject
    private CompanyRepository repository;
    @Inject
    private CompanyFilteringService filteringService;

    @RequestMapping(value = "{id}/discount", method = RequestMethod.PUT)
    @Transactional
    @ResponseBody
    public void setDiscount(@RequestBody @Valid DiscountDto discount, @PathVariable("id") Long id) {
        repository.setDiscount(id, discount.getDiscount());
    }

    @Override
    protected FilteringService<Company> getFilteringService() {
        return filteringService;
    }

    @Override
    public BaseRepository<Company> getRepository() {
        return repository;
    }
}
