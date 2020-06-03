package com.idgindigo.pms.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author valentyn_vakatsiienko
 * @since 1/10/14 6:45 PM
 */
public class SerializerUtils {

    public static String serializeWithTypeInfo(Object value, ObjectMapper objectMapper) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper().setSerializerFactory(objectMapper.getSerializerFactory());
        return mapper.writeValueAsString(value);
    }
}
