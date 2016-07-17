package spacemadness.com.lunarconsole.debug;

import spacemadness.com.lunarconsole.utils.StringUtils;

public class Log {
    private static final String TAG = "LunarConsole";
    private static final LogLevel logLevel;

    public enum LogLevel {
        Crit(6),
        Error(6),
        Warn(5),
        Info(4),
        Debug(3),
        None(-1);
        
        private int androidLogPriority;

        private LogLevel(int priority) {
            this.androidLogPriority = priority;
        }

        public int getAndroidLogPriority() {
            return this.androidLogPriority;
        }
    }

    static {
        logLevel = LogLevel.Info;
    }

    public static void m864i(String format, Object... args) {
        m865i(null, format, args);
    }

    public static void m865i(Tag tag, String format, Object... args) {
        log(LogLevel.Info, tag, format, args);
    }

    public static void m866w(String format, Object... args) {
        m867w(null, format, args);
    }

    public static void m867w(Tag tag, String format, Object... args) {
        log(LogLevel.Warn, tag, format, args);
    }

    public static void m859d(String format, Object... args) {
        m860d(null, format, args);
    }

    public static void m860d(Tag tag, String format, Object... args) {
        log(LogLevel.Debug, tag, format, args);
    }

    public static void m863e(Tag tag, String format, Object... args) {
        log(LogLevel.Error, tag, format, args);
    }

    public static void m861e(String format, Object... args) {
        m863e((Tag) null, format, args);
    }

    public static void m862e(Throwable t, String format, Object... args) {
        m861e(format, args);
        if (t != null) {
            t.printStackTrace();
        }
    }

    private static void log(LogLevel level, Tag tag, String format, Object... args) {
        if (!shouldLogLevel(level) || !shouldLogTag(tag)) {
            return;
        }
        if (format != null) {
            logHelper(level, format, args);
        } else {
            logHelper(level, "null", new Object[0]);
        }
    }

    private static void logHelper(LogLevel level, String format, Object... args) {
        android.util.Log.println(level.getAndroidLogPriority(), "LunarConsole/" + Thread.currentThread().getName(), StringUtils.TryFormat(format, args));
    }

    private static boolean shouldLogLevel(LogLevel level) {
        return level.ordinal() <= logLevel.ordinal();
    }

    private static boolean shouldLogTag(Tag tag) {
        return tag == null || tag.enabled;
    }
}
