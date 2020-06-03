package com.idgindigo.pms.service.pms;

import com.idgindigo.pms.domain.pms.Bill;
import com.idgindigo.pms.domain.pms.LivingUse;
import com.idgindigo.pms.repository.pms.LivingUseRepository;
import com.idgindigo.pms.utils.PriceUtils;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 3/27/14 11:21 AM
 */
@Service
public class LivingUseService {
    @Inject
    private TouristTaxService touristTaxService;
    @Inject
    private LivingUseRepository repository;

    /**
     * @param livingUse
     * @param discount
     * @return difference between old total and new
     */
    public long setDiscount(LivingUse livingUse, int discount) {
        long rawTotal = livingUse.getRawTotal();
        long oldTotal = livingUse.getTotal();
        long taxAmount = touristTaxService.getTaxAmount(livingUse);
        long newTotal = rawTotal - PriceUtils.getDiscountValue(rawTotal, discount) + taxAmount;
        livingUse.setTotal(newTotal);
        repository.save(livingUse);
        return oldTotal - newTotal;
    }

    public void setDiscount(Bill bill, int discount) {
        List<LivingUse> livingUses = repository.findByBill(bill);
        long delta = 0;
        for (LivingUse livingUse : livingUses) {
            if (livingUse.isRefunded()) {
                continue; //Just in case
            }
            delta += setDiscount(livingUse, discount);
        }
        bill.setTotal(bill.getTotal() - delta);
    }
}
