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
package org.bonitasoft.web.rest.server.datastore.bpm.cases;

import java.io.Serializable;

import org.bonitasoft.web.rest.server.datastore.filter.Field;
import org.bonitasoft.web.rest.server.datastore.filter.Filter;
import org.bonitasoft.web.rest.server.datastore.filter.FilterCreator;
import org.bonitasoft.web.rest.server.datastore.filter.StrValue;

/**
 * @author Fabio Lombardi
 *
 */

public class ArchivedCaseDocumentFilterCreator implements FilterCreator {

    private final ArchivedCaseDocumentSearchAttributeConverter converter;

    public ArchivedCaseDocumentFilterCreator(final ArchivedCaseDocumentSearchAttributeConverter converter) {
        this.converter = converter;
    }

    @Override
    public Filter<? extends Serializable> create(final String attribute, final String value) {
        return new Filter<String>(new Field(attribute, converter), new StrValue(value));
    }
}