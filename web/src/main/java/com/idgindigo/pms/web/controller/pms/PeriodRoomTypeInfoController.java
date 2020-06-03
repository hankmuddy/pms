package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.domain.pms.PeriodRoomTypeInfo;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.pms.PeriodRoomTypeInfoRepository;
import com.idgindigo.pms.service.filtering.FilteringService;
import com.idgindigo.pms.service.filtering.PeriodRoomTypeInfoFilteringService;
import com.idgindigo.pms.web.controller.BaseCrudController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 2/25/14 10:34 AM
 */
@Controller
@RequestMapping(PeriodRoomTypeInfoController.URL)
public class PeriodRoomTypeInfoController extends BaseCrudController<PeriodRoomTypeInfo> {
    public static final String URL = WebConfiguration.REST_URL_PREFIX + PeriodRoomTypeInfo.PERIOD_ROOM_TYPE_INFO;
    @Inject
    private PeriodRoomTypeInfoRepository repository;
    @Inject
    private PeriodRoomTypeInfoFilteringService filteringService;

    @Override
    protected FilteringService<PeriodRoomTypeInfo> getFilteringService() {
        return filteringService;
    }

    @Override
    public BaseRepository<PeriodRoomTypeInfo> getRepository() {
        return repository;
    }
}
