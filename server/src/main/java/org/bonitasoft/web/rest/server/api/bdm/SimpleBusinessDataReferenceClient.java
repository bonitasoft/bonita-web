/** Copyright (C) 2015 Bonitasoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.rest.server.api.bdm;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Baptiste Mesta
 */
public class SimpleBusinessDataReferenceClient extends BusinessDataReferenceClient {

    /**
     * UID
     */
    private static final long serialVersionUID = 4377973199157064562L;

    private Long storageId;

    @JsonProperty("storageId_string")
    private String storageIdAsString;

    public SimpleBusinessDataReferenceClient(final String name, final String type, final String link, final Long storageId) {
        super(name, type, link);
        this.storageId = storageId;
        if (storageId != null) {
            storageIdAsString = storageId.toString();
        }
    }

    public Long getStorageId() {
        return storageId;
    }

    public void setStorageId(final Long storageId) {
        this.storageId = storageId;
    }

    public String getStorageIdAsString() {
        return storageIdAsString;
    }

    public void setStorageIdAsString(final String storageIdAsString) {
        this.storageIdAsString = storageIdAsString;
    }
}
