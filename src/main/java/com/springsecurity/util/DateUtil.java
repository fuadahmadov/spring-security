package com.springsecurity.util;

import java.util.Date;

public class DateUtil {
    public static Date epochMillisToDate(long millis) {
        return new Date(new Date().getTime() + millis);
    }
}
