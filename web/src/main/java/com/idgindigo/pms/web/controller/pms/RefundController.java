package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.domain.BaseEntity;
import com.idgindigo.pms.domain.pms.BaseServiceUse;
import com.idgindigo.pms.domain.pms.Refund;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.pms.BankDetailsRepository;
import com.idgindigo.pms.repository.pms.BaseServiceUseRepository;
import com.idgindigo.pms.repository.pms.RefundRepository;
import com.idgindigo.pms.restutils.exception.RefundException;
import com.idgindigo.pms.restutils.view.ResponseView;
import com.idgindigo.pms.service.filtering.FilteringService;
import com.idgindigo.pms.service.filtering.RefundFilteringService;
import com.idgindigo.pms.service.pms.RefundService;
import com.idgindigo.pms.web.controller.BaseCrudController;
import com.idgindigo.pms.web.controller.ResponseEntity;
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
import java.util.ArrayList;
import java.util.List;


/**
 * @author valentyn_vakatsiienko
 * @since 1/10/14 11:13 AM
 */
@Controller
@RequestMapping(RefundController.URL)
public class RefundController extends BaseCrudController<Refund> {
    public static final String URL = WebConfiguration.REST_URL_PREFIX + Refund.REFUND;
    @Inject
    private RefundRepository repository;
    @Inject
    private RefundService service;
    @Inject
    private RefundFilteringService filteringService;
    @Inject
    private BaseServiceUseRepository baseServiceUseRepository;
    @Inject
    private BankDetailsRepository bankDetailsRepository;

    @Override
    @RequestMapping(value = "null", method = RequestMethod.POST)
    @Transactional
    public ResponseEntity<Refund> create(@RequestBody Refund entity) {
        throw new EntityNotFoundException();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseView(BaseEntity.SoloView.class)
    @ResponseBody
    @Transactional
    public ResponseEntity<List<Refund>> refund(@RequestBody Refund entity) {
        validateCreate(entity);
        entity.setServiceUses(initialize(entity.getServiceUses()));
        return new ResponseEntity<>(service.refund(entity));
    }

    private List<BaseServiceUse> initialize(List<BaseServiceUse> serviceUses) {
        List<Long> ids = new ArrayList<>(serviceUses.size());
        for (BaseServiceUse serviceUse : serviceUses) {
            ids.add(serviceUse.getId());
        }
        serviceUses = baseServiceUseRepository.findByIdIn(ids);
        return serviceUses;
    }

    @Override
    public ResponseEntity<Refund> update(@RequestBody Refund entity, @PathVariable("id") Long id) {
        throw new EntityNotFoundException();
    }

    @Override
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        throw new EntityNotFoundException();
    }

    @RequestMapping(value = ID + "/bankDetails/{detailsId}", method = RequestMethod.PUT)
    @ResponseBody
    public void setDetails(@RequestParam("detailsId") long detailsId, @RequestParam("id") long id) {
        repository.setDetails(id, bankDetailsRepository.findOne(detailsId));
    }

    @Override
    protected FilteringService<Refund> getFilteringService() {
        return filteringService;
    }

    private void validateCreate(Refund refund) {
        if (refund.getServiceUses().isEmpty()) {
            throw new RefundException(RefundException.SERVICE_USES_EMPTY, "serviceUses");
        }
        for (BaseServiceUse serviceUse : refund.getServiceUses()) {
            if (refund.getRoomUse() != null && !baseServiceUseRepository.findOne(serviceUse.getId()).getRoomUse().equals(refund.getRoomUse())) {
                throw new RefundException(RefundException.INVALID_ROOM_USE, "roomUse");
            } else if (refund.getRoomUse() == null && !baseServiceUseRepository.findOne(serviceUse.getId()).getRoomUse().getCustomerGroup().equals(refund.getGroup())) {
                throw new RefundException(RefundException.INVALID_GROUP, "group");
            }
        }
    }

    @Override
    public BaseRepository<Refund> getRepository() {
        return repository;
    }
}
