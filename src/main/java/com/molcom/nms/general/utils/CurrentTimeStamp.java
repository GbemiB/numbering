package com.molcom.nms.general.utils;

import java.util.Calendar;

public class CurrentTimeStamp {

    public static java.sql.Timestamp getCurrentTimeStamp() {
        java.util.Date today = new java.util.Date();
        return new java.sql.Timestamp(today.getTime());
    }


    public static java.sql.Timestamp getNextYearTimeStamp(java.sql.Timestamp date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date); // w ww.  j ava  2  s  .co m
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return new java.sql.Timestamp(cal.getTime().getTime());
    }

}
