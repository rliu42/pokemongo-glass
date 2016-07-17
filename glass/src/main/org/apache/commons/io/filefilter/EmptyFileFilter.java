package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;

public class EmptyFileFilter extends AbstractFileFilter implements Serializable {
    public static final IOFileFilter EMPTY;
    public static final IOFileFilter NOT_EMPTY;

    static {
        EMPTY = new EmptyFileFilter();
        NOT_EMPTY = new NotFileFilter(EMPTY);
    }

    protected EmptyFileFilter() {
    }

    public boolean accept(File file) {
        boolean z = true;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null || files.length == 0) {
                return true;
            }
            return false;
        }
        if (file.length() != 0) {
            z = false;
        }
        return z;
    }
}
