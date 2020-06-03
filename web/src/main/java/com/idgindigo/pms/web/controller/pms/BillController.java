package com.idgindigo.pms.web.controller.pms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.domain.extranet.BaseGroupRoomUse;
import com.idgindigo.pms.domain.pms.BankDetails;
import com.idgindigo.pms.domain.pms.Bill;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.dto.BillTotalDto;
import com.idgindigo.pms.repository.extranet.roomuse.BaseGroupRoomUseRepository;
import com.idgindigo.pms.repository.pms.BillRepository;
import com.idgindigo.pms.repository.pms.RoomUseRepository;
import com.idgindigo.pms.restutils.exception.BillException;
import com.idgindigo.pms.restutils.view.ResponseView;
import com.idgindigo.pms.service.approve.ApproveService;
import com.idgindigo.pms.service.approve.BillApproveService;
import com.idgindigo.pms.service.filtering.BillFilteringService;
import com.idgindigo.pms.service.filtering.FilteringService;
import com.idgindigo.pms.service.pms.BillService;
import com.idgindigo.pms.web.controller.ApprovableController;
import com.idgindigo.pms.web.controller.ResponseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;

import static com.idgindigo.pms.domain.pms.Bill.BillView;

/**
 * @author valentyn_vakatsiienko
 * @since 11/22/13 11:40 AM
 */
@Controller
@RequestMapping(BillController.URL)
public class BillController extends ApprovableController<Bill> {
    public static final String URL = WebConfiguration.REST_URL_PREFIX + Bill.BILL;

    @Inject
    private BillRepository repository;
    @Inject
    private BillFilteringService filteringService;
    @Inject
    private BillApproveService approveService;
    @Inject
    private RoomUseRepository roomUseRepository;
    @Inject
    private BaseGroupRoomUseRepository baseGroupRoomUseRepository;
    @Inject
    private BillService billService;

    @Override
    @ResponseView(BillView.class)
    public ResponseEntity<Bill> getById(@PathVariable("id") Long id) {
        return super.getById(id);
    }

//    @Override
//    public ResponseEntity<Bill> update(@RequestBody Bill entity, @PathVariable("id") Long id) {
//        validateUpdate(entity, repository.findOne(id));
//        return super.update(entity, id);
//    }

    @RequestMapping(value = ID + "/forCustomer", method = RequestMethod.PUT)
    @ResponseBody
    public void setForCustomer(@PathVariable("id") long id, @RequestBody ForCustomerDto dto) {
        Bill bill = repository.findOne(id);
        if (!isUpdateRequired(dto, bill)) {
            return;
        }
        validateSetForCustomer(bill, dto);
        boolean forCustomer = dto.isForCustomer();
        RoomUse roomUse = dto.getRoomUse();
        billService.setForCustomer(bill, roomUse, forCustomer, dto.getDetails());
    }


    private boolean isUpdateRequired(ForCustomerDto dto, Bill bill) {
        return bill.isForCustomer() != dto.isForCustomer();
    }

    private void validateSetForCustomer(Bill o, ForCustomerDto dto) {
        //Validate suggested roomUse belongs to customer group and vice versa
        if (!dto.isForCustomer()) {
            if (!roomUseRepository.findOne(dto.getRoomUse().getId()).getCustomerGroup().equals(o.getGroup())) {
                throw new BillException(BillException.FOR_CUSTOMER_INVALID_DATA, "roomUse");
            }
        }
        //Validate forCustomer is subject to change
        if (o.isForCustomer() && !dto.isForCustomer()) {
            if (baseGroupRoomUseRepository.getStatus(dto.getRoomUse().getId()) == BaseGroupRoomUse.Status.OUTGO) {
                throw new BillException(BillException.ROOM_USE_CHECKED_OUT, "roomUse");
            }
        }
    }

    @RequestMapping(ID + "/fullyPaid")
    @ResponseBody
    public ResponseEntity<Boolean> isFullyPaid(@PathVariable("id") Long id) {
        return new ResponseEntity<>(repository.isFullyPaid(id));
    }

    @RequestMapping(value = ID + "/discount", method = RequestMethod.PUT)
    @ResponseBody
    public void setDiscount(@PathVariable("id") Long id, @RequestBody DiscountDto dto) {
        billService.setDiscount(repository.findOne(id), dto.getValue(), dto.getBankDetails());
    }


    @RequestMapping(value = "total", params = {"startDate", "endDate"})
    @ResponseBody
    public ResponseEntity<BillTotalDto> getTotal(@RequestParam("startDate") LocalDateTime startDate, @RequestParam("endDate") LocalDateTime endDate) {
        return new ResponseEntity<>(((BillRepository) getRepository()).getTotal(startDate, endDate));
    }

    @Override
    public ApproveService<Bill> getApproveService() {
        return approveService;
    }

    @Override
    protected FilteringService<Bill> getFilteringService() {
        return filteringService;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ForCustomerDto {
        private RoomUse roomUse;
        private BankDetails details;

        public ForCustomerDto(RoomUse roomUse) {
            this(roomUse, null);
        }

        @JsonIgnore
        public boolean isForCustomer() {
            return roomUse == null;
        }
    }

    @Getter
    @Setter
    public static class DiscountDto {
        private long value;
        private BankDetails bankDetails;
    }
}
