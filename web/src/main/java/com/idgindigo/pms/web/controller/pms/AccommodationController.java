package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.domain.pms.Accommodation;
import com.idgindigo.pms.service.approve.AccommodationApproveService;
import com.idgindigo.pms.service.approve.ApproveService;
import com.idgindigo.pms.service.filtering.AccommodationFilteringService;
import com.idgindigo.pms.service.filtering.FilteringService;
import com.idgindigo.pms.web.controller.ApprovableController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;

import static com.idgindigo.pms.domain.pms.Accommodation.ACCOMMODATION;

/**
 * @author vomel
 * @since 31.10.13 21:13
 */
@Controller
@RequestMapping(AccommodationController.URL)
public class AccommodationController extends ApprovableController<Accommodation> {
    public static final String URL = WebConfiguration.REST_URL_PREFIX + ACCOMMODATION;

    @Inject
    private AccommodationApproveService approveService;
    @Inject
    private AccommodationFilteringService filteringService;

    @Override
    protected FilteringService<Accommodation> getFilteringService() {
        return filteringService;
    }

    @Override
    public ApproveService<Accommodation> getApproveService() {
        return approveService;
    }
}
