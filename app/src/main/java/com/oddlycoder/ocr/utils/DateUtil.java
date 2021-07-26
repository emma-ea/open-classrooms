package com.oddlycoder.ocr.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    public static final String DAY = "EEEE";
    public static final String TIME = "HH:mm";

    public static String getDayOrTime(String pattern) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.ENGLISH);
        return simpleDateFormat.format(calendar.getTime());
    }
}
