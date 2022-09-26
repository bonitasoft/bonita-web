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

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Baptiste Mesta
 */
public class MultipleBusinessDataReferenceClient extends BusinessDataReferenceClient {

    private final List<Long> storageIds;

    @JsonProperty("storageIds_string")
    private final List<String> storageIdsAsString;

    public MultipleBusinessDataReferenceClient(String name, String type, String link, List<Long> storageIds) {
        super(name, type, link);
        this.storageIds = storageIds;
        storageIdsAsString=new ArrayList<>();
        for (Long storageId : storageIds) {
            storageIdsAsString.add(storageId.toString());
        }
    }

    public List<Long> getStorageIds() {
        return storageIds;
    }

    public List<String> getStorageIdsAsString() {
        return storageIdsAsString;
    }

}
