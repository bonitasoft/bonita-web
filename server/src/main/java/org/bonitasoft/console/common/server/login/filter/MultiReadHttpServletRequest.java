/**
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.console.common.server.login.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;

public class MultiReadHttpServletRequest extends HttpServletRequestWrapper {

    private ByteArrayOutputStream readBytes;

    public MultiReadHttpServletRequest(final HttpServletRequest request) {
        super(request);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (readBytes == null) {
            readInputStream();
        }
        return new CachedServletInputStream();
    }

    @Override
    public BufferedReader getReader() throws IOException {
        String enc = getCharacterEncoding();
        if (enc == null) {
            enc = "UTF-8";
        }
        return new BufferedReader(new InputStreamReader(getInputStream(), enc));
    }

    private void readInputStream() throws IOException {
        readBytes = new ByteArrayOutputStream();
        IOUtils.copy(super.getInputStream(), readBytes);
    }

    class CachedServletInputStream extends ServletInputStream {

        private final ByteArrayInputStream input;

        public CachedServletInputStream() {
            input = new ByteArrayInputStream(readBytes.toByteArray());
        }

        @Override
        public int read() throws IOException {
            return input.read();
        }

        @Override
        public int read(final byte[] b) throws IOException {
            return input.read(b);
        }

        @Override
        public int read(final byte[] b, final int off, final int len) throws IOException {
            return input.read(b, off, len);
        }

        @Override
        public void close() throws IOException {
            input.close();
            super.close();
        }
    }
}