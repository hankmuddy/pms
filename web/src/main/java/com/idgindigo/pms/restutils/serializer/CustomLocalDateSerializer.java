package com.idgindigo.pms.restutils.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

import java.io.IOException;

/**
 * @author valentyn_vakatsiienko
 * @since 12/3/13 11:36 AM
 */
public class CustomLocalDateSerializer extends StdSerializer<LocalDate> {
    public CustomLocalDateSerializer() {
        super(LocalDate.class);
    }

    @Override
    public void serialize(LocalDate value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
        jgen.writeNumber(value.toDateTimeAtStartOfDay(DateTimeZone.UTC).toInstant().getMillis() / 1000);
    }

    @Override
    public Class<LocalDate> handledType() {
        return LocalDate.class;
    }
}
