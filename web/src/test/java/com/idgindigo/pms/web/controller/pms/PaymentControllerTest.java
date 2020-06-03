package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.domain.pms.BankDetails;
import com.idgindigo.pms.domain.pms.Bill;
import com.idgindigo.pms.domain.pms.Payment;
import com.idgindigo.pms.restutils.exception.PaymentException;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.pms.BankDetailsProvider;
import com.idgindigo.pms.utils.pms.PaymentProvider;
import com.idgindigo.pms.web.controller.ApprovableControllerTest;
import junit.framework.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertEquals;

/**
 * @author valentyn_vakatsiienko
 * @since 11/22/13 11:59 AM
 */
public class PaymentControllerTest extends ApprovableControllerTest<Payment> {
    @Inject
    private PaymentProvider provider;
    @Inject
    private BankDetailsProvider bankDetailsProvider;
    @Inject
    private PaymentController controller;

    @Override
    protected boolean doRunUpdateStep() {
        return false;
    }

    @Override
    protected boolean doRunDeleteStep() {
        return false;
    }

    @Test
    public void testInvalidTotal() throws Exception {
        Payment payment = provider.getTransientEntity(new Visitor<Payment>() {
            @Override
            public void visit(Payment entity) {
                entity.setTotal(entity.getBill().getTotal() + 1);
            }
        });
        testBadRequest(preparePost(payment), PaymentException.PAYMENT_TOTAL_EXCEEDS_BILL);

        payment = provider.getPersistentEntity(new Visitor<Payment>() {
            @Override
            public void visit(Payment entity) {
                entity.setApproved(false);
                entity.setTotal(entity.getBill().getTotal() + 1);
            }
        });
        testBadRequest(preparePut(payment), PaymentException.PAYMENT_TOTAL_EXCEEDS_BILL);
    }

    @Test
    public void testApproveWithExceedingTotal() throws Exception {
        //Test with single bill
        Payment payment = provider.getPersistentEntity(new Visitor<Payment>() {
            @Override
            public void visit(Payment entity) {
                entity.setApproved(false);
                entity.setTotal(entity.getBill().getTotal() + 1);
            }
        });
        testBadRequest(preparePut(payment.getId() + "/approved"), PaymentException.PAYMENT_TOTAL_EXCEEDS_NON_PAID_PART);

        //Multiple bills
        payment = provider.getPersistentEntity(new Visitor<Payment>() {
            @Override
            public void visit(Payment entity) {
                entity.setTotal(entity.getBill().getTotal() - 1);
                entity.setApproved(false);
            }
        });
        mvc.perform(preparePut(payment.getId() + "/approved")).andExpect(status().isOk());
        final Bill bill = payment.getBill();

        Payment paymentToTest = provider.getPersistentEntity(new Visitor<Payment>() {
            @Override
            public void visit(Payment entity) {
                entity.setTotal(2L);
                entity.setApproved(false);
                entity.setBill(bill);
            }
        });
        testBadRequest(preparePut(paymentToTest.getId() + "/approved"), PaymentException.PAYMENT_TOTAL_EXCEEDS_NON_PAID_PART);
    }

    @Test
    public void testBlockedDetailsForbidden() throws Exception {
        Payment payment = provider.getTransientEntity(new Visitor<Payment>() {
            @Override
            public void visit(Payment entity) {
                entity.setBankDetails(bankDetailsProvider.getPersistentEntity(new Visitor<BankDetails>() {
                    @Override
                    public void visit(BankDetails entity) {
                        entity.setBlocked(true);
                    }
                }));
            }
        });

        testBadRequest(preparePost(payment), PaymentException.BANK_DETAILS_BLOCKED);
    }

    @Test
    public void testPaymentDateBeforeBillDate() {
        Payment payment = provider.getTransientEntity(new Visitor<Payment>() {
            @Override
            public void visit(Payment entity) {
                entity.setDate(entity.getBill().getCreatedDate().toLocalDate().toDateTimeAtStartOfDay().toLocalDateTime().minusSeconds(1));
            }
        });
        try {
            controller.create(payment);
            Assert.fail();
        } catch (PaymentException e) {
            assertEquals(e.getMessage(), PaymentException.PAYMENT_DATE_BEFORE_BILL_DATE);
            assertEquals(e.getSource(), "date");
        }
    }

    @Override
    protected EntityProvider<Payment> getProvider() {
        return provider;
    }

    @Override
    protected String getUrl() {
        return PaymentController.URL + "/";
    }
}
