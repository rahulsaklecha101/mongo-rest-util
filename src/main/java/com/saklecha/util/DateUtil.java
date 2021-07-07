package com.saklecha.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class DateUtil
{
    private static final Logger LOGGER = LoggerFactory.getLogger( DateUtil.class.getCanonicalName());

    public static boolean isValidDateFormat(String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            value = StringUtils.trimAllWhitespace(value);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (java.text.ParseException ex) {
            LOGGER.error("CLAIMS DS API | Exception Occurred while parsing the date: ", ex);
        }
        return date != null;
    }

    public static String currentDate() {
        Date date = new Date();
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return dateFormat.format(date);
    }


    public static String currentDateMMddYYYY() {
        Date date = new Date();
        DateFormat dateFormat=new SimpleDateFormat("MM/dd/yyyy");
        return dateFormat.format(date);
    }

    public static String convertDateTimeToUIDateFormat(String value) {
        final String OLD_FORMAT = "yyyy-MM-dd HH:mm:ss";
        final String NEW_FORMAT = "MM/dd/yyyy";
        String newDateString = null;

        if (StringUtils.isEmpty(value))
            return newDateString;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
            Date d = sdf.parse(value.trim());
            sdf.applyPattern(NEW_FORMAT);
            newDateString = sdf.format(d);
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        return newDateString;
    }
    public static Date getCurrentDateTimeInUTC() {
        Instant instant = Instant.now();
        OffsetDateTime odt = instant.atOffset( ZoneOffset.UTC );
        long epochMilli = odt.toInstant().toEpochMilli();
        return new Date(epochMilli);

    }

}
