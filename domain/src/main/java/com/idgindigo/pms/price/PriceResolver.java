package com.idgindigo.pms.price;

import com.idgindigo.pms.domain.extranet.service.Service;
import org.joda.time.LocalDate;

/**
 * @author valentyn_vakatsiienko
 * @since 12/4/13 5:48 PM
 */
public interface PriceResolver<T extends Service> {
    Long getPrice(T service, LocalDate date);
}
