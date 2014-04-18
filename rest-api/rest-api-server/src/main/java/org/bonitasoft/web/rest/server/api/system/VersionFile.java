package org.bonitasoft.web.rest.server.api.system;

import java.io.InputStream;

/**
 * @author Vincent Elcrin
 */
public class VersionFile {

    public InputStream getStream() {
        return BonitaVersion.class.getClassLoader().getResourceAsStream("VERSION");
    }
}
