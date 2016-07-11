/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.web.rest.server.api.system;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

/**
 * @author Vincent Elcrin
 */
public class BonitaVersion {

    private static Logger LOGGER = Logger.getLogger(BonitaVersion.class.getName());

    private List<String> metadata;

    private final VersionFile file;

    public BonitaVersion(final VersionFile file) {
        this.file = file;
    }

    private List<String> read(final InputStream stream) {
        if (stream != null) {
            try {
                return IOUtils.readLines(stream, "UTF-8");
            } catch (final Exception e) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, "Unable to read the file VERSION", e);
                }
                return Collections.emptyList();
            } finally {
                try {
                    stream.close();
                } catch (final IOException e) {
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(Level.WARNING, "Unable to close the input stream for file VERSION", e);
                    }
                }
            }
        } else {
            return Collections.emptyList();
        }
    }

    public String getVersion() {
        if (metadata == null) {
            metadata = read(file.getStream());
        }
        if (metadata.size() > 0) {
            return metadata.get(0).trim();
        } else {
            return "";
        }
    }

    public String getCopyright() {
        if (metadata == null) {
            metadata = read(file.getStream());
        }
        if (metadata.size() > 1) {
            return metadata.get(1).trim();
        } else {
            return "";
        }
    }
}
