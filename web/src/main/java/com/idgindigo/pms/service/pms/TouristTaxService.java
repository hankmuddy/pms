package com.idgindigo.pms.service.pms;

import com.idgindigo.pms.domain.extranet.CustomerGroup;
import com.idgindigo.pms.domain.pms.Bill;
import com.idgindigo.pms.domain.pms.LivingUse;
import com.idgindigo.pms.service.admin.SettingsService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import static com.idgindigo.pms.domain.extranet.CustomerGroup.PurposeOfVisit.BUSINESS;

/**
 * @author valentyn_vakatsiienko
 * @since 12/27/13 5:35 PM
 */
@Service
public class TouristTaxService {
    @Inject
    private SettingsService settingsService;

    public long getTaxAmount(LivingUse livingUse) {
        CustomerGroup group;
        Bill bill = livingUse.getBill();
        if (bill.isForCustomer()) {
            group = bill.getGroup();
        } else {
            group = bill.getRoomUse().getCustomerGroup();
        }
        if (group.getPov() == BUSINESS) {
            return 0;
        }
        return getTaxAmount(getTargetAmount(livingUse), group.getDiscount(), livingUse.getTourismTax());
    }

    private long getTargetAmount(LivingUse livingUse) {
        return settingsService.isTourismTaxFromFullPrice() ? livingUse.getRawTotal() : livingUse.getLivingAmount();
    }

    public long getTaxAmount(long livingAmount, int discount, float tourismTax) {
        if (tourismTax <= 0) {
            return 0;
        }
        double result = livingAmount * tourismTax * ((100 - discount) / 10000.0);
        return (long) Math.ceil(result);
    }
}
