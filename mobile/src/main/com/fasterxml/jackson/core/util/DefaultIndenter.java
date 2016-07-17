package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter.NopIndenter;
import java.io.IOException;
import org.apache.commons.io.IOUtils;

public class DefaultIndenter extends NopIndenter {
    private static final int INDENT_LEVELS = 16;
    public static final DefaultIndenter SYSTEM_LINEFEED_INSTANCE;
    public static final String SYS_LF;
    private static final long serialVersionUID = 1;
    private final int charsPerLevel;
    private final String eol;
    private final char[] indents;

    static {
        String lf;
        try {
            lf = System.getProperty("line.separator");
        } catch (Throwable th) {
            lf = IOUtils.LINE_SEPARATOR_UNIX;
        }
        SYS_LF = lf;
        SYSTEM_LINEFEED_INSTANCE = new DefaultIndenter("  ", SYS_LF);
    }

    public DefaultIndenter() {
        this("  ", SYS_LF);
    }

    public DefaultIndenter(String indent, String eol) {
        this.charsPerLevel = indent.length();
        this.indents = new char[(indent.length() * INDENT_LEVELS)];
        int offset = 0;
        for (int i = 0; i < INDENT_LEVELS; i++) {
            indent.getChars(0, indent.length(), this.indents, offset);
            offset += indent.length();
        }
        this.eol = eol;
    }

    public DefaultIndenter withLinefeed(String lf) {
        return lf.equals(this.eol) ? this : new DefaultIndenter(getIndent(), lf);
    }

    public DefaultIndenter withIndent(String indent) {
        return indent.equals(getIndent()) ? this : new DefaultIndenter(indent, this.eol);
    }

    public boolean isInline() {
        return false;
    }

    public void writeIndentation(JsonGenerator jg, int level) throws IOException {
        jg.writeRaw(this.eol);
        if (level > 0) {
            level *= this.charsPerLevel;
            while (level > this.indents.length) {
                jg.writeRaw(this.indents, 0, this.indents.length);
                level -= this.indents.length;
            }
            jg.writeRaw(this.indents, 0, level);
        }
    }

    public String getEol() {
        return this.eol;
    }

    public String getIndent() {
        return new String(this.indents, 0, this.charsPerLevel);
    }
}
