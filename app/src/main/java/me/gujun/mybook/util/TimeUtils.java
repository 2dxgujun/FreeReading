package me.gujun.mybook.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utilities for dealing with dates and times.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-3-23 15:42:41
 */
public class TimeUtils {
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("MM-dd HH:mm");


    public static String getCurrentTimeFormated() {
        return FORMATTER.format(new Date(System.currentTimeMillis()));
    }
}