/**
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.model.builder.bpm.cases;

import java.io.Serializable;

import org.bonitasoft.web.rest.model.bpm.cases.CaseVariableItem;


/**
 * @author Colin PUY
 *
 */
public class CaseVariableItemBuilder {

    private long caseId = 1L;
    private final String name = "aName";
    private final Serializable value = "aValue";
    private final String type = "aType";
    private final String description = "aDescription";

    public static CaseVariableItemBuilder aCaseVariableItem() {
        return new CaseVariableItemBuilder();
    }

    public CaseVariableItem build() {
        return new CaseVariableItem(caseId, name, value, type, description);
    }

    public CaseVariableItemBuilder withCaseId(final long caseId) {
        this.caseId = caseId;
        return this;
    }
}
