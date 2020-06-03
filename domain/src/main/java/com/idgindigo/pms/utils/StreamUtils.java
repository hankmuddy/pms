package com.idgindigo.pms.utils;

import java.io.InputStream;
import java.util.Scanner;

/**
 * @author vomel
 * @since 19.12.13 17:09
 */
public class StreamUtils {

    /**
     * I learned this trick from "Stupid Scanner tricks" article.
     * The reason it works is because Scanner iterates over tokens in the stream,
     * and in this case we separate tokens using "beginning of the input boundary" (\A)
     * thus giving us only one token for the entire contents of the stream.
     *
     * @param is InputStream
     * @return String value
     */
    public static String convertStreamToString(InputStream is) {
        if (is == null) return null;
        Scanner s = new Scanner(is, "UTF-8").useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}
