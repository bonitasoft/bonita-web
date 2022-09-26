/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 */
package org.bonitasoft.console.common.server.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Yongtao Guo
 * 
 */
public class DocumentUtil {

    public static byte[] getArrayByte(final InputStream input, final int estimatedSize) throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream(estimatedSize);
        try (output) {
            final byte[] buf = new byte[8192];
            int len;

            while ((len = input.read(buf)) >= 0) {
                output.write(buf, 0, len);
            }
        }
        return output.toByteArray();
    }

    public static byte[] getArrayByteFromFile(final File f) throws IOException {
        final long length = f.length();
        if (length > Integer.MAX_VALUE) { // more than 2 GB
            throw new IOException("File too big");
        }

        try (FileInputStream input = new FileInputStream(f)) {
            return getArrayByte(input, (int) length);
        }
    }

}
