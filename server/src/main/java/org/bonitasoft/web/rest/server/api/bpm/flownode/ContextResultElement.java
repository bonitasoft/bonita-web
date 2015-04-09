package org.bonitasoft.web.rest.server.api.bpm.flownode;

import java.io.Serializable;

/**
 * Created by fabiolombardi on 08/04/2015.
 */
public class ContextResultElement implements Serializable {
    private static final long serialVersionUID = -6913883854275484141L;

    private final String type;

    private final String value;

    private final String link;

    public ContextResultElement(String type, String value, String link) {
        super();
        this.type = type;
        this.value = value;
        this.link = link;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public String getLink() {
        return link;
    }
}
