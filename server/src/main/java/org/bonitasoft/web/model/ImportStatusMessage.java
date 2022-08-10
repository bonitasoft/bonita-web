/**
 * Copyright (C) 2013 BonitaSoft S.A.
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
package org.bonitasoft.web.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Fabio Lombardi
 *
 */
public class ImportStatusMessage implements Serializable {

    public static String SKIPPED = "SKIPPED";

    public static String IMPORTED = "IMPORTED";

    public static String ERRORS = "ERRORS";

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Map<String, List<String>> errors;

    private String name;

    private final String statusType;

    public ImportStatusMessage(final String name, final String statusType) {
        errors = new HashMap<String, List<String>>();
        this.name = name;
        this.statusType = statusType;
    }

    public void addError(final String elementType, final String errorMessage) {
        if (!errors.containsKey(elementType)) {
            errors.put(elementType, new ArrayList<String>());
        }
        errors.get(elementType).add(errorMessage);
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Map<String, List<String>> getErrors() {
        return errors;
    }

    public void setErrors(final Map<String, List<String>> errors) {
        this.errors = errors;
    }

    public String getName() {
        return name;
    }

    public String getStatusType() {
        return statusType;
    }
}
