package com.idgindigo.pms.channel.wubook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author vomel
 * @since 17.03.14 17:59
 */
public class Printer {
    private static final Logger logger = LoggerFactory.getLogger(Printer.class);
    private int offset;

    public void print(Object o) {
        StringBuilder sb = new StringBuilder();
        if (o instanceof Map) {
            print((Map) o, sb);
        } else if (o instanceof Object[]) {
            print((Object[]) o, sb);
        } else if (o instanceof List) {
            print(((List) o).toArray(), sb);
        } else if (o instanceof String) {
            sb.append('\n');
            sb.append(o);
        } else {
            throw new UnsupportedOperationException("Not supported Type: " + o.getClass());
        }
        logger.trace(sb.toString());
    }

    void print(Object[] array, StringBuilder sb) {
        sb.append('\n');
        for (Object o : array) {
            if (o instanceof Map) {
                offset++;
                print((Map<String, Object>) o, sb);
                continue;
            }
            if (o instanceof Object[]) {
                offset++;
                print((Object[]) o, sb);
                continue;
            }
            sb.append(repeat("  ", offset) + o);
        }
        sb.append('\n');
        offset--;
    }

    private static String repeat(String s, int offset) {
        StringBuilder sb = new StringBuilder(offset);
        for (int i = 0; i < offset; i++) {
            sb.append(s);
        }
        return sb.toString();
    }

    void print(Map<String, Object> map, StringBuilder sb) {
        sb.append('\n');
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof Object[]) {
                sb.append(repeat("  ", offset) + entry.getKey() + " => ");
                offset++;
                print((Object[]) entry.getValue(), sb);
                continue;
            }
            if (entry.getValue() instanceof Map) {
                sb.append(repeat("  ", offset) + entry.getKey() + " => ");
                offset++;
                print((Map<String, Object>) entry.getValue(), sb);
                continue;
            }
            sb.append(repeat("  ", offset) + entry.getKey() + " => " + entry.getValue());
            sb.append('\n');
        }
        offset--;
    }

}
