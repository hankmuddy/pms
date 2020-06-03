package com.idgindigo.pms.web.controller.extranet;

import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.domain.extranet.BaseGroupRoomUse;
import com.idgindigo.pms.domain.extranet.CustomerGroup;
import com.idgindigo.pms.domain.pms.Bill;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.repository.extranet.CustomerGroupRepository;
import com.idgindigo.pms.repository.pms.BillRepository;
import com.idgindigo.pms.repository.pms.RoomUseRepository;
import com.idgindigo.pms.restutils.exception.CustomerGroupException;
import com.idgindigo.pms.restutils.exception.RestFriendlyException;
import com.idgindigo.pms.restutils.view.ResponseView;
import com.idgindigo.pms.service.approve.ApproveService;
import com.idgindigo.pms.service.approve.CustomerGroupApproveService;
import com.idgindigo.pms.service.filtering.CustomerGroupFilteringService;
import com.idgindigo.pms.service.filtering.FilteringService;
import com.idgindigo.pms.service.pms.CustomerGroupService;
import com.idgindigo.pms.web.controller.ApprovableController;
import com.idgindigo.pms.web.controller.DiscountDto;
import com.idgindigo.pms.web.controller.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 11/19/13 3:11 PM
 */
@Controller
@RequestMapping(CustomerGroupController.URL)
public class CustomerGroupController extends ApprovableController<CustomerGroup> {
    public static final String URL = WebConfiguration.REST_URL_PREFIX + CustomerGroup.GROUP;
    public static final List<RoomUse.Status> CLOSABLE_STATUSES = Arrays.asList(BaseGroupRoomUse.Status.OUTGO, BaseGroupRoomUse.Status.NOT_ARRIVED, BaseGroupRoomUse.Status.REFUSE);
    @Inject
    private CustomerGroupRepository repository;
    @Inject
    private RoomUseRepository roomUseRepository;
    @Inject
    private BillRepository billRepository;
    @Inject
    private CustomerGroupApproveService approveService;
    @Inject
    private CustomerGroupFilteringService filteringService;
    @Inject
    private CustomerGroupService customerGroupService;

    @Override
    public ResponseEntity<CustomerGroup> create(@RequestBody CustomerGroup entity) {
        if (entity.getCompany() == null && entity.getCustomer() == null) {
            throw new RestFriendlyException(RestFriendlyException.MAY_NOT_BE_NULL, "customer");
        }
        return super.create(entity);
    }

    @RequestMapping("{id}/bills")
    @ResponseView(Bill.BillView.class)
    public ResponseEntity<List<Bill>> getBills(@PathVariable("id") Long id) {
        return new ResponseEntity<>(billRepository.findByRoomUseCustomerGroupId(id));
    }

    @RequestMapping(value = "{id}/discount", method = RequestMethod.PUT)
    @Transactional
    @ResponseBody
    public void setDiscount(@RequestBody @Valid DiscountDto discount, @PathVariable("id") Long id) {
        CustomerGroup group = repository.findOne(id);
        if (group == null) {
            throw new EntityNotFoundException();
        }
        int newDiscount = discount.getDiscount();
        customerGroupService.setDiscount(group, newDiscount, discount.getBankDetails());
    }

    @RequestMapping(value = "{id}/closed", method = RequestMethod.PUT)
    @ResponseBody
    public void close(@PathVariable("id") Long id) {
        CustomerGroup group = repository.findOne(id);
        List<RoomUse> groupRoomUses = roomUseRepository.findByCustomerGroupId(id);
        for (RoomUse roomUse : groupRoomUses) {
            if (!CLOSABLE_STATUSES.contains(roomUse.getStatus())) {
                throw new CustomerGroupException(CustomerGroupException.CLOSE_ROOM_USE_NOT_CHECKED_OUT, roomUse.getId().toString());
            }
        }
        if (!repository.isFullyPaid(group)) {
            throw new CustomerGroupException(CustomerGroupException.CLOSE_ONLY_FOR_FULLY_PAID, "closed");
        }
        repository.close(id);
    }

    @Override
    protected FilteringService<CustomerGroup> getFilteringService() {
        return filteringService;
    }

    @Override
    public ApproveService<CustomerGroup> getApproveService() {
        return approveService;
    }

}
