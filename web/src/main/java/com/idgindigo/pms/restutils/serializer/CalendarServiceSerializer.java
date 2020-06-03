package com.idgindigo.pms.restutils.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.idgindigo.pms.domain.extranet.service.CalendarService;
import com.idgindigo.pms.restutils.exception.RestFriendlyException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author valentyn_vakatsiienko
 * @since 11/26/13 1:13 PM
 */
public class CalendarServiceSerializer extends StdDeserializer<CalendarService> {

    private Map<String, Class<? extends CalendarService>> registry =
            new HashMap<String, Class<? extends CalendarService>>();

    CalendarServiceSerializer() {
        super(CalendarService.class);
    }

    public void registerDocumentType(String uniqueAttribute, Class<? extends CalendarService> documentClass) {
        registry.put(uniqueAttribute, documentClass);
    }

    @Override
    public CalendarService deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        ObjectNode root = mapper.readTree(jp);
        Class<? extends CalendarService> documentClass = null;
        Iterator<Map.Entry<String, JsonNode>> elementsIterator = root.fields();
        while (elementsIterator.hasNext()) {
            Map.Entry<String, JsonNode> element = elementsIterator.next();
            String name = element.getKey();
            if (registry.containsKey(name)) {
                documentClass = registry.get(name);
                break;
            }
        }
        if (documentClass == null) {
            throw new RestFriendlyException(RestFriendlyException.UNKNOWN_ERROR);
        }
        return mapper.readValue(jp, documentClass);
    }
}
