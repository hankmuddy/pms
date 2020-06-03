package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.configuration.WebConfiguration;
import com.idgindigo.pms.domain.pms.Bill;
import com.idgindigo.pms.domain.pms.Payment;
import com.idgindigo.pms.repository.pms.BankDetailsRepository;
import com.idgindigo.pms.repository.pms.BillRepository;
import com.idgindigo.pms.repository.pms.PaymentRepository;
import com.idgindigo.pms.restutils.exception.PaymentException;
import com.idgindigo.pms.service.approve.ApproveService;
import com.idgindigo.pms.service.approve.PaymentApproveService;
import com.idgindigo.pms.service.filtering.FilteringService;
import com.idgindigo.pms.service.filtering.PaymentFilteringService;
import com.idgindigo.pms.web.controller.ApprovableController;
import com.idgindigo.pms.web.controller.ResponseEntity;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.validation.Valid;

/**
 * @author valentyn_vakatsiienko
 * @since 11/22/13 11:52 AM
 */
@Controller
@RequestMapping(PaymentController.URL)
public class PaymentController extends ApprovableController<Payment> {
    public static final String URL = WebConfiguration.REST_URL_PREFIX + Payment.PAYMENT;
    @Inject
    private BillRepository billRepository;
    @Inject
    private PaymentFilteringService filteringService;
    @Inject
    private PaymentApproveService approveService;
    @Inject
    private BankDetailsRepository bankDetailsRepository;

    @Override
    @Transactional
    public ResponseEntity<Payment> create(@RequestBody @Valid Payment entity) {
        validate(entity);
        if (entity.getBankDetails() == null || bankDetailsRepository.isBlocked(entity.getBankDetails().getId())) {
            throw new PaymentException(PaymentException.BANK_DETAILS_BLOCKED, "bankDetails");
        }
        ResponseEntity<Payment> paymentResponseEntity = super.create(entity);
        approve(paymentResponseEntity.getContent().getId());
        return paymentResponseEntity;
    }

    @Override
    @Transactional
    public ResponseEntity<Payment> update(@RequestBody Payment entity, @PathVariable("id") Long id) {
        validate(entity);
        return super.update(entity, id);
    }

    @RequestMapping(value = "total", params = {"startDate", "endDate"})
    @ResponseBody
    public ResponseEntity<Long> getTotal(@RequestParam("startDate") LocalDateTime startDate, @RequestParam("endDate") LocalDateTime endDate) {
        return new ResponseEntity<>(((PaymentRepository) getRepository()).getTotal(startDate, endDate));
    }

    private void validate(Payment entity) {
        Bill bill = billRepository.findOne(entity.getBill().getId());
        if (bill.getTotal() < entity.getTotal()) {
            throw new PaymentException(PaymentException.PAYMENT_TOTAL_EXCEEDS_BILL, "total");
        }
        if (substractSeconds(bill.getCreatedDate()).isAfter(substractSeconds(entity.getDate()))) {
            throw new PaymentException(PaymentException.PAYMENT_DATE_BEFORE_BILL_DATE, "date");
        }
    }

    private LocalDateTime substractSeconds(LocalDateTime time) {
        return time.withSecondOfMinute(0).withMillisOfSecond(0);
    }

    @Override
    public ApproveService<Payment> getApproveService() {
        return approveService;
    }

    @Override
    protected FilteringService<Payment> getFilteringService() {
        return filteringService;
    }
}