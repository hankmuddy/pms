package com.idgindigo.pms.restutils.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.LocalDate;

import java.io.IOException;

/**
 * @author valentyn_vakatsiienko
 * @since 2/4/14 7:24 PM
 */
public class LocalDateKeyDeserializer extends KeyDeserializer {
    @Override
    public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return new ObjectMapper().readValue(key, LocalDate.class);
    }
}
