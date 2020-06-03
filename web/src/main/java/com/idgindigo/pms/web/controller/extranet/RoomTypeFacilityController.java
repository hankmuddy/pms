package com.idgindigo.pms.web.controller.extranet;

import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.domain.BaseEntity;
import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeFacility;
import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeToFacility;
import com.idgindigo.pms.repository.extranet.RoomTypeFacilityRepository;
import com.idgindigo.pms.repository.extranet.RoomTypeRepository;
import com.idgindigo.pms.repository.extranet.RoomTypeToFacilityRepository;
import com.idgindigo.pms.restutils.PageWithTotalCount;
import com.idgindigo.pms.restutils.view.ResponseView;
import com.idgindigo.pms.web.controller.ResponseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.idgindigo.pms.web.controller.BaseCrudController.DEFAULT_RECORDS_ON_PAGE;
import static com.idgindigo.pms.web.controller.BaseCrudController.LIMIT;
import static com.idgindigo.pms.web.controller.BaseCrudController.PAGE;
import static com.idgindigo.pms.web.controller.BaseCrudController.SORT;
import static com.idgindigo.pms.web.controller.BaseCrudController.START;
import static com.idgindigo.pms.web.controller.BaseCrudController.getListResponseEntity;
import static com.idgindigo.pms.web.controller.BaseCrudController.getPageable;

/**
 * @author vomel
 * @since 19.02.14 14:39
 */
@Controller
@RequestMapping(RoomTypeFacilityController.URL)
public class RoomTypeFacilityController {
    public static final String URL = WebConfiguration.REST_URL_PREFIX + "roomTypeFacility";
    @Inject
    private RoomTypeFacilityRepository repository;
    @Inject
    private RoomTypeRepository roomTypeRepository;
    @Inject
    private RoomTypeToFacilityRepository rttfRepository;

    @RequestMapping(method = RequestMethod.GET, params = "!filter[0][field]")
    @ResponseBody
    @ResponseView(BaseEntity.ListView.class)
    public ResponseEntity<List<RoomTypeFacility>> list(@RequestParam(value = PAGE, defaultValue = "1") Integer page,
                                                       @RequestParam(value = START, defaultValue = "0") Integer start,
                                                       @RequestParam(value = LIMIT, defaultValue = DEFAULT_RECORDS_ON_PAGE) Integer limit,
                                                       @RequestParam(value = SORT, required = false) String sortString) throws IOException {
        Pageable pageable = getPageable(page, start, limit, sortString);
        Page<RoomTypeFacility> result = repository.findAll(pageable);
        return getListResponseEntity(result);
    }

    @RequestMapping("byRoomType/{id}")
    @ResponseBody
    public ResponseEntity<List<RoomTypeFacility>> getByRoomType(@PathVariable("id") Long id) {
        List<RoomTypeFacility> list = rttfRepository.findByRoomType(id);
        return new ResponseEntity<>(list, new PageWithTotalCount(0, list.isEmpty() ? 1 : list.size(), 0));
    }

    @RequestMapping(value = "bind", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @Transactional
    public ResponseEntity<RoomTypeFacilityDto> create(@RequestBody RoomTypeFacilityDto entity) {
        if (entity.getRoomType().getId() == null || !roomTypeRepository.exists(entity.getRoomType().getId())) throw new EntityNotFoundException();
        List<RoomTypeFacility> existing = rttfRepository.findByRoomType(entity.getRoomType().getId());
        Collection<RoomTypeToFacility> added = new ArrayList<>();
        List<RoomTypeFacility> facilitiesFromUi = entity.getFacilities();
        for (RoomTypeFacility facility : facilitiesFromUi) {
            if (existing.contains(facility)) {
                existing.remove(facility);
            } else {
                added.add(new RoomTypeToFacility(entity.getRoomType(), facility));
            }
        }
        rttfRepository.save(added);
        if (!existing.isEmpty()) {
            rttfRepository.removeSelected(entity.getRoomType(), existing);
        }
        return new ResponseEntity<>();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoomTypeFacilityDto {
        private RoomType roomType;
        private List<RoomTypeFacility> facilities;
    }

}
