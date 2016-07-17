package spacemadness.com.lunarconsole.utils;

import org.apache.commons.io.IOUtils;
import spacemadness.com.lunarconsole.debug.Log;

public class StackTrace {
    public static final String MARKER_ASSETS = "/Assets/";
    public static final String MARKER_AT = " (at ";

    public static String optimize(String stackTrace) {
        if (stackTrace != null) {
            try {
                if (stackTrace.length() > 0) {
                    String[] lines = stackTrace.split(IOUtils.LINE_SEPARATOR_UNIX);
                    Object[] newLines = new String[lines.length];
                    for (int i = 0; i < lines.length; i++) {
                        newLines[i] = optimizeLine(lines[i]);
                    }
                    stackTrace = StringUtils.Join(newLines, IOUtils.LINE_SEPARATOR_UNIX);
                }
            } catch (Throwable e) {
                Log.m862e(e, "Error while optimizing stacktrace: %s", stackTrace);
            }
        }
        return stackTrace;
    }

    private static String optimizeLine(String line) {
        int start = line.indexOf(MARKER_AT);
        if (start == -1) {
            return line;
        }
        int end = line.lastIndexOf(MARKER_ASSETS);
        return end != -1 ? line.substring(0, MARKER_AT.length() + start) + line.substring(end + 1) : line;
    }
}
