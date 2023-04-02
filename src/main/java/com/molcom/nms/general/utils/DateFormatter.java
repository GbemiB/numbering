package com.molcom.nms.general.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateFormatter {


    public static void main(String[] args) {

        String date = "2023-02-05 19:23:07.855";
        String result = formatDateFromIso8601(date) + "T00:00:00.000Z";
        System.out.println(result);
    }

    public static String finalFormattedDate(String value) {
        String variable = formatDateFromIso8601(value);
        String result = variable + "T00:00:00.000Z";
        return result;
    }


    public static String formatDateFromIso8601(String date) {
        String dt = date.replace("Z", "");
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatter4 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        if (checkDateFormat(dt, isoDateLengthCheck(dt) ? formatter : formatter4)) {
            System.out.println("Formatted date");
            return LocalDateTime.parse(dt, isoDateLengthCheck(dt) ? formatter : formatter4).format(formatter);
        }
        System.out.println("Formatted date " + date);
        return date;
    }

    private static boolean checkDateFormat(String date, DateTimeFormatter formatter) {
        try {
            LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    public static boolean isoDateLengthCheck(String date) {
        String[] dateSplits = date.split("-");
        String dt = dateSplits[1];
        return dt.length() == 3;
    }
}
