/**
 * Copyright (C) 2015 Bonitasoft S.A.
 * Bonitasoft, 32 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.console.common.server.registration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SystemInfoSender {

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(SystemInfoSender.class.getName());

    protected final String serviceURL;

    public SystemInfoSender(final String serviceURL) {
        this.serviceURL = serviceURL;
    }

    public boolean call(final String data, final String email) {
        try {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Performing registration");
            }
            final URL url = new URL(serviceURL);
            final URLConnection conn = createConnection(url);
            conn.setDoOutput(true);
            final OutputStreamWriter wr = sendData(conn, email, data);
            final BufferedReader rd = checkResponse(conn);
            wr.close();
            rd.close();
        } catch (final Exception e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Could not perform registration", e);
            }
            return false;
        }
        return true;
    }

    protected OutputStreamWriter sendData(final URLConnection conn, final String email, final String data) throws IOException {
        final OutputStreamWriter wr = createOutputStreamWriter(conn);
        wr.write(email);
        wr.write(data);
        wr.flush();
        return wr;
    }

    protected BufferedReader checkResponse(final URLConnection conn) throws IOException {
        final BufferedReader rd = createBufferedReaderForResponse(conn);
        String line;
        while ((line = rd.readLine()) != null) {
            if ("1".equals(line)) {
                return rd;
            }
        }
        throw new RuntimeException("Error sending data");
    }

    protected URLConnection createConnection(final URL url) throws IOException {
        return url.openConnection();
    }

    protected OutputStreamWriter createOutputStreamWriter(final URLConnection conn) throws IOException {
        return new OutputStreamWriter(conn.getOutputStream());
    }

    protected BufferedReader createBufferedReaderForResponse(final URLConnection conn) throws IOException {
        return new BufferedReader(new InputStreamReader(conn.getInputStream()));
    }
}
