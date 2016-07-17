package com.fasterxml.jackson.databind.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import org.apache.commons.io.FilenameUtils;
import spacemadness.com.lunarconsole.BuildConfig;

public class ISO8601Utils {
    private static final String GMT_ID = "GMT";
    private static final TimeZone TIMEZONE_GMT;
    private static final TimeZone TIMEZONE_Z;

    static {
        TIMEZONE_GMT = TimeZone.getTimeZone(GMT_ID);
        TIMEZONE_Z = TIMEZONE_GMT;
    }

    @Deprecated
    public static TimeZone timeZoneGMT() {
        return TIMEZONE_GMT;
    }

    public static String format(Date date) {
        return format(date, false, TIMEZONE_GMT);
    }

    public static String format(Date date, boolean millis) {
        return format(date, millis, TIMEZONE_GMT);
    }

    public static String format(Date date, boolean millis, TimeZone tz) {
        int length;
        Calendar calendar = new GregorianCalendar(tz, Locale.US);
        calendar.setTime(date);
        int capacity = "yyyy-MM-ddThh:mm:ss".length() + (millis ? ".sss".length() : 0);
        if (tz.getRawOffset() == 0) {
            length = "Z".length();
        } else {
            length = "+hh:mm".length();
        }
        StringBuilder formatted = new StringBuilder(capacity + length);
        padInt(formatted, calendar.get(1), "yyyy".length());
        formatted.append('-');
        padInt(formatted, calendar.get(2) + 1, "MM".length());
        formatted.append('-');
        padInt(formatted, calendar.get(5), "dd".length());
        formatted.append('T');
        padInt(formatted, calendar.get(11), "hh".length());
        formatted.append(':');
        padInt(formatted, calendar.get(12), "mm".length());
        formatted.append(':');
        padInt(formatted, calendar.get(13), "ss".length());
        if (millis) {
            formatted.append(FilenameUtils.EXTENSION_SEPARATOR);
            padInt(formatted, calendar.get(14), "sss".length());
        }
        int offset = tz.getOffset(calendar.getTimeInMillis());
        if (offset != 0) {
            int hours = Math.abs((offset / 60000) / 60);
            int minutes = Math.abs((offset / 60000) % 60);
            formatted.append(offset < 0 ? '-' : '+');
            padInt(formatted, hours, "hh".length());
            formatted.append(':');
            padInt(formatted, minutes, "mm".length());
        } else {
            formatted.append('Z');
        }
        return formatted.toString();
    }

    public static Date parse(String date, ParsePosition pos) throws ParseException {
        Exception fail;
        String input;
        String msg;
        ParseException ex;
        try {
            int index = pos.getIndex();
            int i = index + 4;
            int year = parseInt(date, index, i);
            if (checkOffset(date, i, '-')) {
                i++;
            }
            index = i + 2;
            int month = parseInt(date, i, index);
            if (checkOffset(date, index, '-')) {
                i = index + 1;
            } else {
                i = index;
            }
            index = i + 2;
            int day = parseInt(date, i, index);
            int hour = 0;
            int minutes = 0;
            int seconds = 0;
            int milliseconds = 0;
            boolean hasT = checkOffset(date, index, 'T');
            Calendar calendar;
            if (hasT || date.length() > index) {
                if (hasT) {
                    index++;
                    i = index + 2;
                    hour = parseInt(date, index, i);
                    if (checkOffset(date, i, ':')) {
                        i++;
                    }
                    index = i + 2;
                    minutes = parseInt(date, i, index);
                    if (checkOffset(date, index, ':')) {
                        i = index + 1;
                    } else {
                        i = index;
                    }
                    if (date.length() > i) {
                        char c = date.charAt(i);
                        if (!(c == 'Z' || c == '+' || c == '-')) {
                            index = i + 2;
                            seconds = parseInt(date, i, index);
                            if (checkOffset(date, index, FilenameUtils.EXTENSION_SEPARATOR)) {
                                index++;
                                i = index + 3;
                                milliseconds = parseInt(date, index, i);
                                index = i;
                            }
                        }
                    }
                    index = i;
                }
                if (date.length() <= index) {
                    throw new IllegalArgumentException("No time zone indicator");
                }
                TimeZone timezone;
                char timezoneIndicator = date.charAt(index);
                if (timezoneIndicator == 'Z') {
                    timezone = TIMEZONE_Z;
                    index++;
                } else if (timezoneIndicator == '+' || timezoneIndicator == '-') {
                    String timezoneOffset = date.substring(index);
                    index += timezoneOffset.length();
                    if (!"+0000".equals(timezoneOffset)) {
                        if (!"+00:00".equals(timezoneOffset)) {
                            String timezoneId = GMT_ID + timezoneOffset;
                            timezone = TimeZone.getTimeZone(timezoneId);
                            String act = timezone.getID();
                            if (!act.equals(timezoneId)) {
                                if (!act.replace(UpsightEndpoint.SIGNED_MESSAGE_SEPARATOR, BuildConfig.FLAVOR).equals(timezoneId)) {
                                    throw new IndexOutOfBoundsException("Mismatching time zone indicator: " + timezoneId + " given, resolves to " + timezone.getID());
                                }
                            }
                        }
                    }
                    timezone = TIMEZONE_Z;
                } else {
                    throw new IndexOutOfBoundsException("Invalid time zone indicator '" + timezoneIndicator + "'");
                }
                calendar = new GregorianCalendar(timezone);
                calendar.setLenient(false);
                calendar.set(1, year);
                calendar.set(2, month - 1);
                calendar.set(5, day);
                calendar.set(11, hour);
                calendar.set(12, minutes);
                calendar.set(13, seconds);
                calendar.set(14, milliseconds);
                pos.setIndex(index);
                return calendar.getTime();
            }
            calendar = new GregorianCalendar(year, month - 1, day);
            pos.setIndex(index);
            return calendar.getTime();
        } catch (Exception e) {
            fail = e;
            if (date == null) {
                input = null;
            } else {
                input = '\"' + date + "'";
            }
            msg = fail.getMessage();
            if (msg == null || msg.isEmpty()) {
                msg = "(" + fail.getClass().getName() + ")";
            }
            ex = new ParseException("Failed to parse date [" + input + "]: " + msg, pos.getIndex());
            ex.initCause(fail);
            throw ex;
        } catch (Exception e2) {
            fail = e2;
            if (date == null) {
                input = '\"' + date + "'";
            } else {
                input = null;
            }
            msg = fail.getMessage();
            msg = "(" + fail.getClass().getName() + ")";
            ex = new ParseException("Failed to parse date [" + input + "]: " + msg, pos.getIndex());
            ex.initCause(fail);
            throw ex;
        } catch (Exception e22) {
            fail = e22;
            if (date == null) {
                input = null;
            } else {
                input = '\"' + date + "'";
            }
            msg = fail.getMessage();
            msg = "(" + fail.getClass().getName() + ")";
            ex = new ParseException("Failed to parse date [" + input + "]: " + msg, pos.getIndex());
            ex.initCause(fail);
            throw ex;
        }
    }

    private static boolean checkOffset(String value, int offset, char expected) {
        return offset < value.length() && value.charAt(offset) == expected;
    }

    private static int parseInt(String value, int beginIndex, int endIndex) throws NumberFormatException {
        if (beginIndex < 0 || endIndex > value.length() || beginIndex > endIndex) {
            throw new NumberFormatException(value);
        }
        int i;
        int digit;
        int i2 = beginIndex;
        int result = 0;
        if (i2 < endIndex) {
            i = i2 + 1;
            digit = Character.digit(value.charAt(i2), 10);
            if (digit < 0) {
                throw new NumberFormatException("Invalid number: " + value.substring(beginIndex, endIndex));
            }
            result = -digit;
        } else {
            i = i2;
        }
        while (i < endIndex) {
            i2 = i + 1;
            digit = Character.digit(value.charAt(i), 10);
            if (digit < 0) {
                throw new NumberFormatException("Invalid number: " + value.substring(beginIndex, endIndex));
            }
            result = (result * 10) - digit;
            i = i2;
        }
        return -result;
    }

    private static void padInt(StringBuilder buffer, int value, int length) {
        String strValue = Integer.toString(value);
        for (int i = length - strValue.length(); i > 0; i--) {
            buffer.append('0');
        }
        buffer.append(strValue);
    }
}
