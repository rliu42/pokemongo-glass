package com.fasterxml.jackson.core.io;

import java.math.BigDecimal;

public final class NumberInput {
    static final long L_BILLION = 1000000000;
    static final String MAX_LONG_STR;
    static final String MIN_LONG_STR_NO_SIGN;
    public static final String NASTY_SMALL_DOUBLE = "2.2250738585072012e-308";

    static {
        MIN_LONG_STR_NO_SIGN = String.valueOf(Long.MIN_VALUE).substring(1);
        MAX_LONG_STR = String.valueOf(Long.MAX_VALUE);
    }

    public static int parseInt(char[] ch, int off, int len) {
        int num = ch[off] - 48;
        if (len > 4) {
            off++;
            off++;
            off++;
            off++;
            num = (((((((num * 10) + (ch[off] - 48)) * 10) + (ch[off] - 48)) * 10) + (ch[off] - 48)) * 10) + (ch[off] - 48);
            len -= 4;
            if (len > 4) {
                off++;
                off++;
                off++;
                return (((((((num * 10) + (ch[off] - 48)) * 10) + (ch[off] - 48)) * 10) + (ch[off] - 48)) * 10) + (ch[off + 1] - 48);
            }
        }
        if (len > 1) {
            off++;
            num = (num * 10) + (ch[off] - 48);
            if (len > 2) {
                off++;
                num = (num * 10) + (ch[off] - 48);
                if (len > 3) {
                    num = (num * 10) + (ch[off + 1] - 48);
                }
            }
        }
        return num;
    }

    public static int parseInt(String s) {
        int offset;
        boolean neg = false;
        char c = s.charAt(0);
        int len = s.length();
        if (c == '-') {
            neg = true;
        }
        if (neg) {
            if (len == 1 || len > 10) {
                return Integer.parseInt(s);
            }
            offset = 1 + 1;
            c = s.charAt(1);
        } else if (len > 9) {
            return Integer.parseInt(s);
        } else {
            offset = 1;
        }
        if (c > '9' || c < '0') {
            int i = offset;
            return Integer.parseInt(s);
        }
        int num = c - 48;
        if (offset < len) {
            i = offset + 1;
            c = s.charAt(offset);
            if (c > '9' || c < '0') {
                return Integer.parseInt(s);
            }
            num = (num * 10) + (c - 48);
            if (i < len) {
                offset = i + 1;
                c = s.charAt(i);
                if (c > '9' || c < '0') {
                    i = offset;
                    return Integer.parseInt(s);
                }
                num = (num * 10) + (c - 48);
                if (offset < len) {
                    do {
                        i = offset;
                        offset = i + 1;
                        c = s.charAt(i);
                        if (c > '9' || c < '0') {
                            i = offset;
                            return Integer.parseInt(s);
                        }
                        num = (num * 10) + (c - 48);
                    } while (offset < len);
                }
            }
            return neg ? -num : num;
        }
        i = offset;
        if (neg) {
        }
    }

    public static long parseLong(char[] ch, int off, int len) {
        int len1 = len - 9;
        return ((long) parseInt(ch, off + len1, 9)) + (((long) parseInt(ch, off, len1)) * L_BILLION);
    }

    public static long parseLong(String s) {
        if (s.length() <= 9) {
            return (long) parseInt(s);
        }
        return Long.parseLong(s);
    }

    public static boolean inLongRange(char[] ch, int off, int len, boolean negative) {
        String cmpStr = negative ? MIN_LONG_STR_NO_SIGN : MAX_LONG_STR;
        int cmpLen = cmpStr.length();
        if (len < cmpLen) {
            return true;
        }
        if (len > cmpLen) {
            return false;
        }
        int i = 0;
        while (i < cmpLen) {
            int diff = ch[off + i] - cmpStr.charAt(i);
            if (diff == 0) {
                i++;
            } else if (diff >= 0) {
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    public static boolean inLongRange(String s, boolean negative) {
        String cmp = negative ? MIN_LONG_STR_NO_SIGN : MAX_LONG_STR;
        int cmpLen = cmp.length();
        int alen = s.length();
        if (alen < cmpLen) {
            return true;
        }
        if (alen > cmpLen) {
            return false;
        }
        int i = 0;
        while (i < cmpLen) {
            int diff = s.charAt(i) - cmp.charAt(i);
            if (diff == 0) {
                i++;
            } else if (diff >= 0) {
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    public static int parseAsInt(String s, int def) {
        if (s == null) {
            return def;
        }
        s = s.trim();
        int len = s.length();
        if (len == 0) {
            return def;
        }
        char c;
        int i = 0;
        if (0 < len) {
            c = s.charAt(0);
            if (c == '+') {
                s = s.substring(1);
                len = s.length();
            } else if (c == '-') {
                i = 0 + 1;
            }
        }
        while (i < len) {
            c = s.charAt(i);
            if (c > '9' || c < '0') {
                try {
                    return (int) parseDouble(s);
                } catch (NumberFormatException e) {
                    return def;
                }
            }
            i++;
        }
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e2) {
            return def;
        }
    }

    public static long parseAsLong(String s, long def) {
        if (s == null) {
            return def;
        }
        s = s.trim();
        int len = s.length();
        if (len == 0) {
            return def;
        }
        char c;
        int i = 0;
        if (0 < len) {
            c = s.charAt(0);
            if (c == '+') {
                s = s.substring(1);
                len = s.length();
            } else if (c == '-') {
                i = 0 + 1;
            }
        }
        while (i < len) {
            c = s.charAt(i);
            if (c > '9' || c < '0') {
                try {
                    return (long) parseDouble(s);
                } catch (NumberFormatException e) {
                    return def;
                }
            }
            i++;
        }
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e2) {
            return def;
        }
    }

    public static double parseAsDouble(String s, double def) {
        if (s != null) {
            s = s.trim();
            if (s.length() != 0) {
                try {
                    def = parseDouble(s);
                } catch (NumberFormatException e) {
                }
            }
        }
        return def;
    }

    public static double parseDouble(String s) throws NumberFormatException {
        if (NASTY_SMALL_DOUBLE.equals(s)) {
            return Double.MIN_VALUE;
        }
        return Double.parseDouble(s);
    }

    public static BigDecimal parseBigDecimal(String s) throws NumberFormatException {
        try {
            return new BigDecimal(s);
        } catch (NumberFormatException e) {
            throw _badBD(s);
        }
    }

    public static BigDecimal parseBigDecimal(char[] b) throws NumberFormatException {
        return parseBigDecimal(b, 0, b.length);
    }

    public static BigDecimal parseBigDecimal(char[] b, int off, int len) throws NumberFormatException {
        try {
            return new BigDecimal(b, off, len);
        } catch (NumberFormatException e) {
            throw _badBD(new String(b, off, len));
        }
    }

    private static NumberFormatException _badBD(String s) {
        return new NumberFormatException("Value \"" + s + "\" can not be represented as BigDecimal");
    }
}
