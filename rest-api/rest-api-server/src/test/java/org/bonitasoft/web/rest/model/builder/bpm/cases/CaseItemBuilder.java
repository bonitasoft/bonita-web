/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * 
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
package org.bonitasoft.web.rest.model.builder.bpm.cases;

import org.bonitasoft.web.rest.model.bpm.cases.CaseItem;

public class CaseItemBuilder {

    private long processId;

    private String jsonVariables;

    private long userId;

    public static CaseItemBuilder aCaseItem() {
        return new CaseItemBuilder();
    }

    public CaseItemBuilder withProcessId(final long processId) {
        this.processId = processId;
        return this;
    }

    public CaseItemBuilder withSubstituteUserId(final long userId) {
        this.userId = userId;
        return this;
    }

    public CaseItemBuilder withVariables(final String jsonVariables) {
        this.jsonVariables = jsonVariables;
        return this;
    }

    public CaseItem build() {
        final CaseItem item = new CaseItem();
        setAttributeIfNotNull(item, CaseItem.ATTRIBUTE_PROCESS_ID, processId);
        setAttributeIfNotNull(item, CaseItem.ATTRIBUTE_STARTED_BY_SUBSTITUTE_USER_ID, userId);
        setAttributeIfNotNull(item, CaseItem.ATTRIBUTE_VARIABLES, jsonVariables);
        return item;
    }

    private void setAttributeIfNotNull(final CaseItem caseItem, final String attributeName, final Object attributeValue) {
        if (attributeValue != null) {
            caseItem.setAttribute(attributeName, attributeValue);
        }
    }
}
