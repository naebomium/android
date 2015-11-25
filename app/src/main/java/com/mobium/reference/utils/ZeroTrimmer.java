package com.mobium.reference.utils;

/**
 *  on 06.03.14.
 * http://mobiumapps.com/
 */
public class ZeroTrimmer {
    public static String trimZero(double value, double step) {
        String res = String.valueOf(round((int) (value / step) * step, 5));
        if (res.contains(".")) {
            while (res.length() > 1 && res.contains(".") && (res.endsWith("0") || res.endsWith("."))) {
                res = res.substring(0, res.length() - 2);
            }
        }
        return res;
    }
    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

}
