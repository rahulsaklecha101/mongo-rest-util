package com.saklecha.util;

import com.saklecha.response.BaseResponse;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ErrorUtil {

    //Common for all functions.
    public static final String ERROR_TIME_FORMAT = "yyyy.MM.dd@hh:mm:ss.SSS";
    private static SimpleDateFormat errorDateFormat = new SimpleDateFormat ( ERROR_TIME_FORMAT );


    //Errors for Generic BO calls
    public static final String VALIDATION_GENERIC_BO_NO_CONTENT = "DS:V:GBO:NIL";
    public static final String VALIDATION_GENERIC_BO_NO_INSERT = "DS:V:GBO:ADD";
    public static final String VALIDATION_GENERIC_BO_NO_UPDATE = "DS:V:GBO:UPD";
    public static final String VALIDATION_GENERIC_BO_INVALID_PARAM = "DS:V:GBO:PRAM";
    public static final String VALIDATION_GENERIC_BO_INVALID_COLLECTION = "DS:V:GBO:INVC";
    public static final String ERROR_CODE_GENERIC_BO_ERROR_KEY = "DS:E:GBO:TER";

    public static boolean isErrorResponse ( BaseResponse response ) {
        return response !=null && response.getError () !=null;
    }

    public static String errorTimeStamp () {
        Date dNow = new Date();
        return errorDateFormat.format ( dNow );
    }
}
