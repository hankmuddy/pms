package com.idgindigo.pms.price;

import com.idgindigo.pms.domain.pms.SimpleService;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

/**
 * @author valentyn_vakatsiienko
 * @since 12/4/13 6:31 PM
 */
@Component
public class SimpleServicePriceResolver implements PriceResolver<SimpleService> {
    @Override
    public Long getPrice(SimpleService service, LocalDate date) {
        return service.getPrice();
    }
}
