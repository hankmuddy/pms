package com.idgindigo.pms.web.controller.extranet;

import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.domain.BaseEntity;
import com.idgindigo.pms.domain.extranet.BaseGroupRoomUse;
import com.idgindigo.pms.domain.extranet.BaseRoomUse;
import com.idgindigo.pms.repository.extranet.roomuse.BaseRoomUseRepository;
import com.idgindigo.pms.restutils.view.ResponseView;
import com.idgindigo.pms.service.filtering.BaseRoomUseFilteringService;
import com.idgindigo.pms.web.controller.BaseCrudController;
import com.idgindigo.pms.web.controller.ResponseEntity;
import org.joda.time.LocalDate;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author valentyn_vakatsiienko
 * @since 12/23/13 6:13 PM
 */
@Controller
@RequestMapping(WebConfiguration.REST_URL_PREFIX + BaseRoomUse.BASE_ROOM_USE)
public class BaseRoomUseController /*implements FilteringController<BaseRoomUse>*/ {
    @Inject
    private BaseRoomUseRepository repository;
    @Inject
    private BaseRoomUseFilteringService filteringService;

    //    @Override
    @RequestMapping
    @ResponseBody
    @ResponseView(BaseEntity.ListView.class)
    public ResponseEntity<List<BaseRoomUse>> listFiltered(@RequestParam(value = BaseCrudController.PAGE, defaultValue = "1") Integer page, @RequestParam(value = BaseCrudController.START, defaultValue = "0") Integer start, @RequestParam(value = BaseCrudController.LIMIT, defaultValue = BaseCrudController.DEFAULT_RECORDS_ON_PAGE) Integer limit, @RequestParam(value = BaseCrudController.SORT, required = false) String sortString, @RequestParam(value = BaseCrudController.CONNECTIVE, defaultValue = "and") String connective, HttpServletRequest request) throws Exception {
        return filteringService.listFiltered(page, start, limit, sortString, connective, request);
    }

    @RequestMapping(params = {"startDate", "endDate"})
    @ResponseBody
    @ResponseView(BaseEntity.ListView.class)
    public ResponseEntity<List<BaseRoomUse>> filteredByDatedWithoutRefuses(@RequestParam(value = BaseCrudController.PAGE, defaultValue = "1") Integer page,
                                                                           @RequestParam(value = BaseCrudController.START, defaultValue = "0") Integer start,
                                                                           @RequestParam(value = BaseCrudController.LIMIT, defaultValue = BaseCrudController.DEFAULT_RECORDS_ON_PAGE) Integer limit,
                                                                           @RequestParam(value = BaseCrudController.SORT, required = false) String sortString,
                                                                           @RequestParam("startDate") Long startDateTimestamp,
                                                                           @RequestParam("endDate") Long endDateTimestamp,
                                                                           @RequestParam(value = "statuses", required = false) String... statusString) throws IOException {
        Pageable pageable = BaseCrudController.getPageable(page, start, limit, sortString);
        Iterable<BaseGroupRoomUse.Status> statuses;
        if (statusString != null && statusString.length > 0) {
            Set<BaseGroupRoomUse.Status> parsed = new HashSet<>();
            for (String status : statusString) {
                parsed.add(BaseGroupRoomUse.Status.valueOf(status));
            }
            statuses = parsed;
        } else {
            statuses = BaseGroupRoomUse.NOT_REFUSED_STATUSES;
        }
        return new ResponseEntity<>(repository.getForDatesAndStatuses(new LocalDate(startDateTimestamp), new LocalDate(endDateTimestamp), statuses, pageable));
    }

}
