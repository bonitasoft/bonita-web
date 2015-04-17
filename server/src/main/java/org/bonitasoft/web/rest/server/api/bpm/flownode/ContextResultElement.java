/**
 * Copyright (C) 2015 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 **/

package org.bonitasoft.web.rest.server.api.bpm.flownode;

import java.io.Serializable;

/**
 * @author Baptiste Mesta
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContextResultElement that = (ContextResultElement) o;

        if (link != null ? !link.equals(that.link) : that.link != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (link != null ? link.hashCode() : 0);
        return result;
    }
}
