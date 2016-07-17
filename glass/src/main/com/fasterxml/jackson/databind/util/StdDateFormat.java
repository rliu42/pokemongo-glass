package com.fasterxml.jackson.databind.util;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class StdDateFormat extends DateFormat {
    protected static final String[] ALL_FORMATS;
    protected static final DateFormat DATE_FORMAT_ISO8601;
    protected static final DateFormat DATE_FORMAT_ISO8601_Z;
    protected static final DateFormat DATE_FORMAT_PLAIN;
    protected static final DateFormat DATE_FORMAT_RFC1123;
    protected static final String DATE_FORMAT_STR_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    protected static final String DATE_FORMAT_STR_ISO8601_Z = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    protected static final String DATE_FORMAT_STR_PLAIN = "yyyy-MM-dd";
    protected static final String DATE_FORMAT_STR_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";
    private static final Locale DEFAULT_LOCALE;
    private static final TimeZone DEFAULT_TIMEZONE;
    public static final StdDateFormat instance;
    protected transient DateFormat _formatISO8601;
    protected transient DateFormat _formatISO8601_z;
    protected transient DateFormat _formatPlain;
    protected transient DateFormat _formatRFC1123;
    protected final Locale _locale;
    protected transient TimeZone _timezone;

    static {
        ALL_FORMATS = new String[]{DATE_FORMAT_STR_ISO8601, DATE_FORMAT_STR_ISO8601_Z, DATE_FORMAT_STR_RFC1123, DATE_FORMAT_STR_PLAIN};
        DEFAULT_TIMEZONE = TimeZone.getTimeZone("GMT");
        DEFAULT_LOCALE = Locale.US;
        DATE_FORMAT_RFC1123 = new SimpleDateFormat(DATE_FORMAT_STR_RFC1123, DEFAULT_LOCALE);
        DATE_FORMAT_RFC1123.setTimeZone(DEFAULT_TIMEZONE);
        DATE_FORMAT_ISO8601 = new SimpleDateFormat(DATE_FORMAT_STR_ISO8601, DEFAULT_LOCALE);
        DATE_FORMAT_ISO8601.setTimeZone(DEFAULT_TIMEZONE);
        DATE_FORMAT_ISO8601_Z = new SimpleDateFormat(DATE_FORMAT_STR_ISO8601_Z, DEFAULT_LOCALE);
        DATE_FORMAT_ISO8601_Z.setTimeZone(DEFAULT_TIMEZONE);
        DATE_FORMAT_PLAIN = new SimpleDateFormat(DATE_FORMAT_STR_PLAIN, DEFAULT_LOCALE);
        DATE_FORMAT_PLAIN.setTimeZone(DEFAULT_TIMEZONE);
        instance = new StdDateFormat();
    }

    public StdDateFormat() {
        this._locale = DEFAULT_LOCALE;
    }

    @Deprecated
    public StdDateFormat(TimeZone tz) {
        this(tz, DEFAULT_LOCALE);
    }

    public StdDateFormat(TimeZone tz, Locale loc) {
        this._timezone = tz;
        this._locale = loc;
    }

    public static TimeZone getDefaultTimeZone() {
        return DEFAULT_TIMEZONE;
    }

    public StdDateFormat withTimeZone(TimeZone tz) {
        if (tz == null) {
            tz = DEFAULT_TIMEZONE;
        }
        return (tz == this._timezone || tz.equals(this._timezone)) ? this : new StdDateFormat(tz, this._locale);
    }

    public StdDateFormat withLocale(Locale loc) {
        return loc.equals(this._locale) ? this : new StdDateFormat(this._timezone, loc);
    }

    public StdDateFormat clone() {
        return new StdDateFormat(this._timezone, this._locale);
    }

    @Deprecated
    public static DateFormat getBlueprintISO8601Format() {
        return DATE_FORMAT_ISO8601;
    }

    @Deprecated
    public static DateFormat getISO8601Format(TimeZone tz) {
        return getISO8601Format(tz, DEFAULT_LOCALE);
    }

    public static DateFormat getISO8601Format(TimeZone tz, Locale loc) {
        return _cloneFormat(DATE_FORMAT_ISO8601, DATE_FORMAT_STR_ISO8601, tz, loc);
    }

    @Deprecated
    public static DateFormat getBlueprintRFC1123Format() {
        return DATE_FORMAT_RFC1123;
    }

    public static DateFormat getRFC1123Format(TimeZone tz, Locale loc) {
        return _cloneFormat(DATE_FORMAT_RFC1123, DATE_FORMAT_STR_RFC1123, tz, loc);
    }

    @Deprecated
    public static DateFormat getRFC1123Format(TimeZone tz) {
        return getRFC1123Format(tz, DEFAULT_LOCALE);
    }

    public TimeZone getTimeZone() {
        return this._timezone;
    }

    public void setTimeZone(TimeZone tz) {
        if (!tz.equals(this._timezone)) {
            this._formatRFC1123 = null;
            this._formatISO8601 = null;
            this._formatISO8601_z = null;
            this._formatPlain = null;
            this._timezone = tz;
        }
    }

    public Date parse(String dateStr) throws ParseException {
        dateStr = dateStr.trim();
        ParsePosition pos = new ParsePosition(0);
        Date result = parse(dateStr, pos);
        if (result != null) {
            return result;
        }
        StringBuilder sb = new StringBuilder();
        for (String f : ALL_FORMATS) {
            if (sb.length() > 0) {
                sb.append("\", \"");
            } else {
                sb.append('\"');
            }
            sb.append(f);
        }
        sb.append('\"');
        throw new ParseException(String.format("Can not parse date \"%s\": not compatible with any of standard forms (%s)", new Object[]{dateStr, sb.toString()}), pos.getErrorIndex());
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.Date parse(java.lang.String r7, java.text.ParsePosition r8) {
        /*
        r6 = this;
        r4 = 45;
        r3 = 0;
        r2 = r6.looksLikeISO8601(r7);
        if (r2 == 0) goto L_0x000e;
    L_0x0009:
        r2 = r6.parseAsISO8601(r7, r8);
    L_0x000d:
        return r2;
    L_0x000e:
        r1 = r7.length();
    L_0x0012:
        r1 = r1 + -1;
        if (r1 < 0) goto L_0x0026;
    L_0x0016:
        r0 = r7.charAt(r1);
        r2 = 48;
        if (r0 < r2) goto L_0x0022;
    L_0x001e:
        r2 = 57;
        if (r0 <= r2) goto L_0x0012;
    L_0x0022:
        if (r1 > 0) goto L_0x0026;
    L_0x0024:
        if (r0 == r4) goto L_0x0012;
    L_0x0026:
        if (r1 >= 0) goto L_0x003e;
    L_0x0028:
        r2 = r7.charAt(r3);
        if (r2 == r4) goto L_0x0034;
    L_0x002e:
        r2 = com.fasterxml.jackson.core.io.NumberInput.inLongRange(r7, r3);
        if (r2 == 0) goto L_0x003e;
    L_0x0034:
        r2 = new java.util.Date;
        r4 = java.lang.Long.parseLong(r7);
        r2.<init>(r4);
        goto L_0x000d;
    L_0x003e:
        r2 = r6.parseAsRFC1123(r7, r8);
        goto L_0x000d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fasterxml.jackson.databind.util.StdDateFormat.parse(java.lang.String, java.text.ParsePosition):java.util.Date");
    }

    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        if (this._formatISO8601 == null) {
            this._formatISO8601 = _cloneFormat(DATE_FORMAT_ISO8601, DATE_FORMAT_STR_ISO8601, this._timezone, this._locale);
        }
        return this._formatISO8601.format(date, toAppendTo, fieldPosition);
    }

    public String toString() {
        String str = "DateFormat " + getClass().getName();
        TimeZone tz = this._timezone;
        if (tz != null) {
            str = str + " (timezone: " + tz + ")";
        }
        return str + "(locale: " + this._locale + ")";
    }

    protected boolean looksLikeISO8601(String dateStr) {
        if (dateStr.length() >= 5 && Character.isDigit(dateStr.charAt(0)) && Character.isDigit(dateStr.charAt(3)) && dateStr.charAt(4) == '-') {
            return true;
        }
        return false;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected java.util.Date parseAsISO8601(java.lang.String r13, java.text.ParsePosition r14) {
        /*
        r12 = this;
        r7 = 90;
        r11 = 84;
        r10 = 58;
        r9 = 12;
        r8 = 48;
        r2 = r13.length();
        r6 = r2 + -1;
        r0 = r13.charAt(r6);
        r6 = 10;
        if (r2 > r6) goto L_0x0035;
    L_0x0018:
        r6 = java.lang.Character.isDigit(r0);
        if (r6 == 0) goto L_0x0035;
    L_0x001e:
        r1 = r12._formatPlain;
        if (r1 != 0) goto L_0x0030;
    L_0x0022:
        r6 = DATE_FORMAT_PLAIN;
        r7 = "yyyy-MM-dd";
        r8 = r12._timezone;
        r9 = r12._locale;
        r1 = _cloneFormat(r6, r7, r8, r9);
        r12._formatPlain = r1;
    L_0x0030:
        r6 = r1.parse(r13, r14);
        return r6;
    L_0x0035:
        if (r0 != r7) goto L_0x0062;
    L_0x0037:
        r1 = r12._formatISO8601_z;
        if (r1 != 0) goto L_0x0049;
    L_0x003b:
        r6 = DATE_FORMAT_ISO8601_Z;
        r7 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        r8 = r12._timezone;
        r9 = r12._locale;
        r1 = _cloneFormat(r6, r7, r8, r9);
        r12._formatISO8601_z = r1;
    L_0x0049:
        r6 = r2 + -4;
        r6 = r13.charAt(r6);
        if (r6 != r10) goto L_0x0030;
    L_0x0051:
        r4 = new java.lang.StringBuilder;
        r4.<init>(r13);
        r6 = r2 + -1;
        r7 = ".000";
        r4.insert(r6, r7);
        r13 = r4.toString();
        goto L_0x0030;
    L_0x0062:
        r6 = hasTimeZone(r13);
        if (r6 == 0) goto L_0x00ee;
    L_0x0068:
        r6 = r2 + -3;
        r0 = r13.charAt(r6);
        if (r0 != r10) goto L_0x00b1;
    L_0x0070:
        r4 = new java.lang.StringBuilder;
        r4.<init>(r13);
        r6 = r2 + -3;
        r7 = r2 + -2;
        r4.delete(r6, r7);
        r13 = r4.toString();
    L_0x0080:
        r2 = r13.length();
        r6 = r13.lastIndexOf(r11);
        r6 = r2 - r6;
        r5 = r6 + -6;
        if (r5 >= r9) goto L_0x009c;
    L_0x008e:
        r3 = r2 + -5;
        r4 = new java.lang.StringBuilder;
        r4.<init>(r13);
        switch(r5) {
            case 5: goto L_0x00e8;
            case 6: goto L_0x00e3;
            case 7: goto L_0x0098;
            case 8: goto L_0x00dd;
            case 9: goto L_0x00d7;
            case 10: goto L_0x00d1;
            case 11: goto L_0x00cd;
            default: goto L_0x0098;
        };
    L_0x0098:
        r13 = r4.toString();
    L_0x009c:
        r1 = r12._formatISO8601;
        r6 = r12._formatISO8601;
        if (r6 != 0) goto L_0x0030;
    L_0x00a2:
        r6 = DATE_FORMAT_ISO8601;
        r7 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
        r8 = r12._timezone;
        r9 = r12._locale;
        r1 = _cloneFormat(r6, r7, r8, r9);
        r12._formatISO8601 = r1;
        goto L_0x0030;
    L_0x00b1:
        r6 = 43;
        if (r0 == r6) goto L_0x00b9;
    L_0x00b5:
        r6 = 45;
        if (r0 != r6) goto L_0x0080;
    L_0x00b9:
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r6 = r6.append(r13);
        r7 = "00";
        r6 = r6.append(r7);
        r13 = r6.toString();
        goto L_0x0080;
    L_0x00cd:
        r4.insert(r3, r8);
        goto L_0x0098;
    L_0x00d1:
        r6 = "00";
        r4.insert(r3, r6);
        goto L_0x0098;
    L_0x00d7:
        r6 = "000";
        r4.insert(r3, r6);
        goto L_0x0098;
    L_0x00dd:
        r6 = ".000";
        r4.insert(r3, r6);
        goto L_0x0098;
    L_0x00e3:
        r6 = "00.000";
        r4.insert(r3, r6);
    L_0x00e8:
        r6 = ":00.000";
        r4.insert(r3, r6);
        goto L_0x0098;
    L_0x00ee:
        r4 = new java.lang.StringBuilder;
        r4.<init>(r13);
        r6 = r13.lastIndexOf(r11);
        r6 = r2 - r6;
        r5 = r6 + -1;
        if (r5 >= r9) goto L_0x0105;
    L_0x00fd:
        switch(r5) {
            case 9: goto L_0x0126;
            case 10: goto L_0x0123;
            case 11: goto L_0x0120;
            default: goto L_0x0100;
        };
    L_0x0100:
        r6 = ".000";
        r4.append(r6);
    L_0x0105:
        r4.append(r7);
        r13 = r4.toString();
        r1 = r12._formatISO8601_z;
        if (r1 != 0) goto L_0x0030;
    L_0x0110:
        r6 = DATE_FORMAT_ISO8601_Z;
        r7 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        r8 = r12._timezone;
        r9 = r12._locale;
        r1 = _cloneFormat(r6, r7, r8, r9);
        r12._formatISO8601_z = r1;
        goto L_0x0030;
    L_0x0120:
        r4.append(r8);
    L_0x0123:
        r4.append(r8);
    L_0x0126:
        r4.append(r8);
        goto L_0x0105;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fasterxml.jackson.databind.util.StdDateFormat.parseAsISO8601(java.lang.String, java.text.ParsePosition):java.util.Date");
    }

    protected Date parseAsRFC1123(String dateStr, ParsePosition pos) {
        if (this._formatRFC1123 == null) {
            this._formatRFC1123 = _cloneFormat(DATE_FORMAT_RFC1123, DATE_FORMAT_STR_RFC1123, this._timezone, this._locale);
        }
        return this._formatRFC1123.parse(dateStr, pos);
    }

    private static final boolean hasTimeZone(String str) {
        int len = str.length();
        if (len >= 6) {
            char c = str.charAt(len - 6);
            if (c == '+' || c == '-') {
                return true;
            }
            c = str.charAt(len - 5);
            if (c == '+' || c == '-') {
                return true;
            }
            c = str.charAt(len - 3);
            if (c == '+' || c == '-') {
                return true;
            }
        }
        return false;
    }

    private static final DateFormat _cloneFormat(DateFormat df, String format, TimeZone tz, Locale loc) {
        if (loc.equals(DEFAULT_LOCALE)) {
            df = (DateFormat) df.clone();
            if (tz != null) {
                df.setTimeZone(tz);
            }
        } else {
            df = new SimpleDateFormat(format, loc);
            if (tz == null) {
                tz = DEFAULT_TIMEZONE;
            }
            df.setTimeZone(tz);
        }
        return df;
    }
}
