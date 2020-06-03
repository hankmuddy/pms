package com.idgindigo.pms.price;

import com.idgindigo.pms.domain.extranet.service.Living;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

import static org.joda.time.DateTimeConstants.FRIDAY;
import static org.joda.time.DateTimeConstants.MONDAY;
import static org.joda.time.DateTimeConstants.SATURDAY;
import static org.joda.time.DateTimeConstants.SUNDAY;
import static org.joda.time.DateTimeConstants.THURSDAY;
import static org.joda.time.DateTimeConstants.TUESDAY;
import static org.joda.time.DateTimeConstants.WEDNESDAY;

/**
 * @author valentyn_vakatsiienko
 * @since 12/4/13 5:50 PM
 */
@Component
public class LivingPriceResolver implements PriceResolver<Living> {

    @Override
    public Long getPrice(Living living, LocalDate date) {
        return getPriceForDayOfWeek(living, date.getDayOfWeek());
    }

    private static Long getPriceForDayOfWeek(@NotNull Living living, int dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY:
                return living.getMon();
            case TUESDAY:
                return living.getTue();
            case WEDNESDAY:
                return living.getWed();
            case THURSDAY:
                return living.getThu();
            case FRIDAY:
                return living.getFri();
            case SATURDAY:
                return living.getSat();
            case SUNDAY:
                return living.getSun();
            default:
                throw new IllegalArgumentException("Not recognized day of the week: " + dayOfWeek);
        }
    }
}
