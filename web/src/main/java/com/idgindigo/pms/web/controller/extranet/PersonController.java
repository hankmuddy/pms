package com.idgindigo.pms.web.controller.extranet;

import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.domain.BaseEntity;
import com.idgindigo.pms.domain.extranet.person.Person;
import com.idgindigo.pms.restutils.view.ResponseView;
import com.idgindigo.pms.service.filtering.PersonFilteringService;
import com.idgindigo.pms.web.controller.BaseCrudController;
import com.idgindigo.pms.web.controller.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 1/9/14 2:03 PM
 */
@Controller
@RequestMapping(PersonController.URL)
public class PersonController {
    public static final String URL = WebConfiguration.REST_URL_PREFIX + Person.PERSON;
    @Inject
    private PersonFilteringService filteringService;

    @RequestMapping
    @ResponseBody
    @ResponseView(BaseEntity.ListView.class)
    public ResponseEntity<List<Person>> listFiltered(@RequestParam(value = BaseCrudController.PAGE, defaultValue = "1") Integer page, @RequestParam(value = BaseCrudController.START, defaultValue = "0") Integer start, @RequestParam(value = BaseCrudController.LIMIT, defaultValue = BaseCrudController.DEFAULT_RECORDS_ON_PAGE) Integer limit, @RequestParam(value = BaseCrudController.SORT, required = false) String sortString, @RequestParam(value = BaseCrudController.CONNECTIVE, defaultValue = "and") String connective, HttpServletRequest request) throws Exception {
        return filteringService.listFiltered(page, start, limit, sortString, connective, request);
    }
}
