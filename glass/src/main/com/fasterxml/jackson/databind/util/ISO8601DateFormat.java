package com.fasterxml.jackson.databind.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ISO8601DateFormat extends DateFormat {
    private static Calendar CALENDAR = null;
    private static NumberFormat NUMBER_FORMAT = null;
    private static final long serialVersionUID = 1;

    static {
        CALENDAR = new GregorianCalendar();
        NUMBER_FORMAT = new DecimalFormat();
    }

    public ISO8601DateFormat() {
        this.numberFormat = NUMBER_FORMAT;
        this.calendar = CALENDAR;
    }

    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        toAppendTo.append(ISO8601Utils.format(date));
        return toAppendTo;
    }

    public Date parse(String source, ParsePosition pos) {
        try {
            return ISO8601Utils.parse(source, pos);
        } catch (ParseException e) {
            return null;
        }
    }

    public Date parse(String source) throws ParseException {
        return ISO8601Utils.parse(source, new ParsePosition(0));
    }

    public Object clone() {
        return this;
    }

    public String toString() {
        return getClass().getName();
    }
}
