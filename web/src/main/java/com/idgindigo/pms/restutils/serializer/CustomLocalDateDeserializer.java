package com.idgindigo.pms.restutils.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

import java.io.IOException;

/**
 * @author valentyn_vakatsiienko
 * @since 12/9/13 11:17 AM
 */
public class CustomLocalDateDeserializer extends StdDeserializer<LocalDate> {

    public CustomLocalDateDeserializer() {
        super(LocalDate.class);
    }

    @Override
    public LocalDate deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        return new LocalDate(jp.getLongValue() * 1000, DateTimeZone.UTC);
    }
}
