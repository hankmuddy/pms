package com.idgindigo.pms.web.utils.converter;

import org.joda.time.LocalDateTime;
import org.springframework.core.convert.converter.Converter;

/**
 * @author valentyn_vakatsiienko
 * @since 7/10/14 11:05 AM
 */
public class InstantToLocalDateTimeConverter implements Converter<String, LocalDateTime> {
    @Override
    public LocalDateTime convert(String source) {
        return new LocalDateTime(Long.valueOf(source) * 1000);
    }
}
