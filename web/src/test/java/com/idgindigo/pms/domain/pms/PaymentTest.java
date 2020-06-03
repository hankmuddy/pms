package com.idgindigo.pms.domain.pms;

import com.idgindigo.pms.domain.ApprovableEntityTest;
import com.idgindigo.pms.repository.pms.PaymentRepository;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.utils.pms.PaymentProvider;
import org.joda.time.LocalDateTime;
import org.springframework.data.jpa.domain.Specification;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @author valentyn_vakatsiienko
 * @since 11/22/13 11:58 AM
 */
public class PaymentTest extends ApprovableEntityTest<Payment> {
    @Inject
    private PaymentProvider provider;

    @Test
    public void testGetTotal() {
        PaymentRepository repository = (PaymentRepository) provider.getRepository();
        final LocalDateTime start = LocalDateTime.now();
        final LocalDateTime end = start.plusDays(5);
        for (LocalDateTime date = start; !date.isAfter(end); date = date.plusDays(1)) {
            final LocalDateTime paymentDate = date;
            provider.getPersistentEntity(new Visitor<Payment>() {
                @Override
                public void visit(Payment entity) {
                    entity.setDate(paymentDate);
                }
            });
        }

        Long expected = 0L;
        for (Payment payment : repository.findAll(new Specification<Payment>() {
            @Override
            public Predicate toPredicate(Root<Payment> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Path<LocalDateTime> date = root.get("date");
                Predicate startPred = cb.greaterThanOrEqualTo(date, start);
                Predicate endPred = cb.lessThanOrEqualTo(date, end);
                return cb.and(startPred, endPred);
            }
        })) {
            expected += payment.getTotal();
        }
        Long actual = repository.getTotal(start, end);
        Assert.assertEquals(actual, expected);
    }

    @Override
    protected EntityProvider<Payment> getProvider() {
        return provider;
    }
}
