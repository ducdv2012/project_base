package com.project.base.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Utils {
    public static String convertDateToString(Date date, String pattern) {
        if (date == null)
            return null;
        return new SimpleDateFormat(pattern).format(date);
    }

    public static LocalDateTime convertStringToDate(String dateStr, String pattern) {
        if (dateStr == null)
            return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(dateStr, formatter);
    }
}
