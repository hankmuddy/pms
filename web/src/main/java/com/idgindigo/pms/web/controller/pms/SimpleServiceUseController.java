package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.domain.BaseEntity;
import com.idgindigo.pms.domain.pms.Bill;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.domain.pms.SimpleService;
import com.idgindigo.pms.domain.pms.SimpleServiceUse;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.repository.extranet.service.SimpleServiceRepository;
import com.idgindigo.pms.repository.pms.BaseServiceUseRepository;
import com.idgindigo.pms.repository.pms.BillRepository;
import com.idgindigo.pms.repository.pms.RoomUseRepository;
import com.idgindigo.pms.repository.pms.SimpleServiceUseRepository;
import com.idgindigo.pms.restutils.exception.ServiceUseException;
import com.idgindigo.pms.restutils.view.ResponseView;
import com.idgindigo.pms.service.misc.PriceResolverService;
import com.idgindigo.pms.service.pms.BillService;
import com.idgindigo.pms.web.controller.BaseCrudController;
import com.idgindigo.pms.web.controller.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 11/20/13 2:50 PM
 */
@Controller
@RequestMapping(SimpleServiceUseController.URL)
public class SimpleServiceUseController extends BaseCrudController<SimpleServiceUse> {
    public static final String URL = WebConfiguration.REST_URL_PREFIX + SimpleServiceUse.SIMPLE_SERVICE_USE;

    @Inject
    private SimpleServiceRepository serviceRepository;
    @Inject
    private BillService billService;
    @Inject
    private PriceResolverService priceResolverService;
    @Inject
    private BillRepository billRepository;
    @Inject
    private BaseServiceUseRepository baseServiceUseRepository;
    @Inject
    private SimpleServiceUseRepository repository;
    @Inject
    private RoomUseRepository roomUseRepository;


    @Override
    @Transactional
    @ResponseView(BaseEntity.SoloView.class)
    public ResponseEntity<SimpleServiceUse> create(@RequestBody SimpleServiceUse entity) {
        SimpleService service = serviceRepository.findOne(entity.getService().getId());
        RoomUse roomUse = roomUseRepository.findOne(entity.getRoomUse().getId());
        entity.setService(service);
        entity.setRoomUse(roomUse);
        validateServiceUse(entity);
        if (entity.getBill().getId() == null) {
            //Create bill
            Bill bill = billService.getBill(entity.getRoomUse(), entity.getBill().isForCustomer());
            entity.setBill(bill);
        } else {
            entity.setBill(billRepository.findOne(entity.getBill().getId()));
        }
        @SuppressWarnings("unchecked")
        Long price = priceResolverService.getPriceResolver(service).getPrice(service, entity.getDate());
        entity.setTotal(price * entity.getQuantity());
        entity.setRawTotal(entity.getTotal());
        ResponseEntity<SimpleServiceUse> serviceUseResponseEntity = super.create(entity);

        billService.updateTotal(entity.getBill(), null);
        return serviceUseResponseEntity;
    }

    private void validateServiceUse(SimpleServiceUse serviceUse) {
        Bill bill = serviceUse.getBill();
        if (bill == null) {
            throw new ServiceUseException(ServiceUseException.INVALID_BILL, "bill");
        }
    }

    @Override
    @Transactional
    @ResponseView(BaseEntity.SoloView.class)
    public ResponseEntity<SimpleServiceUse> update(@RequestBody SimpleServiceUse entity, @PathVariable("id") Long id) {
        ResponseEntity<SimpleServiceUse> res = super.update(entity, id);
        billService.updateTotal(baseServiceUseRepository.getBill(id), null);
        return res;
    }

    @Override
    @Transactional
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        Bill bill = baseServiceUseRepository.getBill(id);
        super.delete(id);
        billService.updateTotal(bill, null);
        return new ResponseEntity<>();
    }

    @Override
    public BaseRepository<SimpleServiceUse> getRepository() {
        return repository;
    }
}