package com.idgindigo.pms.web.controller.extranet;

import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.domain.extranet.person.Adult;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.extranet.person.AdultRepository;
import com.idgindigo.pms.service.filtering.AdultFilteringService;
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
 * @since 11/19/13 3:13 PM
 */
@Controller
@RequestMapping(AdultController.URL)
public class AdultController extends BaseCrudController<Adult> {
    public static final String URL = WebConfiguration.REST_URL_PREFIX + Adult.ADULT;
    @Inject
    private AdultRepository repository;
    @Inject
    private AdultFilteringService filteringService;

    @RequestMapping(value = "{id}/discount", method = RequestMethod.PUT)
    @Transactional
    @ResponseBody
    public void setDiscount(@RequestBody @Valid DiscountDto discount, @PathVariable("id") Long id) {
        repository.setDiscount(id, discount.getDiscount());
    }

    @Override
    protected FilteringService<Adult> getFilteringService() {
        return filteringService;
    }

    @Override
    public BaseRepository<Adult> getRepository() {
        return repository;
    }
}