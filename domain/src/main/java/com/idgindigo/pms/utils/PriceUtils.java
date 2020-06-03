package com.idgindigo.pms.utils;

/**
 * @author valentyn_vakatsiienko
 * @since 1/11/14 11:19 AM
 */
public class PriceUtils {
    public static long getDiscountValue(long value, int discount) {
        return (long) Math.floor(value * discount / 100.0);
    }
}
