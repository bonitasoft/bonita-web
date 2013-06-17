/**
 * Copyright (C) 2012 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.server.datastore.bpm.connector;

import org.bonitasoft.engine.bpm.connector.ConnectorStateReset;
import org.bonitasoft.web.rest.api.model.bpm.connector.ConnectorInstanceItem;
import org.bonitasoft.web.toolkit.server.api.EnumConverter;

/**
 * Convenient object to convert ATTRIBUTE_VALUE from {@link ConnectorInstanceItem} into {@link ConnectorStateReset}
 * 
 * @author Vincent Elcrin
 * 
 */
public class ConnectorInstanceResetStateConverter implements EnumConverter<ConnectorStateReset> {

    public final ConnectorStateReset convert(final String attributeStateValue) {
        if (ConnectorInstanceItem.VALUE_RESET_STATE_TO_RE_EXECUTE.equals(attributeStateValue)) {
            return ConnectorStateReset.TO_RE_EXECUTE;
        } else if (ConnectorInstanceItem.VALUE_RESET_STATE_SKIPPED.equals(attributeStateValue)) {
            return ConnectorStateReset.SKIPPED;
        } else {
            throw new RuntimeException("Can't convert following state into engine state <" + attributeStateValue + ">");
        }
    }

    @Override
    public String convert(ConnectorStateReset enumValue) {
        switch (enumValue) {
            case SKIPPED:
                return ConnectorInstanceItem.VALUE_RESET_STATE_SKIPPED;
            case TO_RE_EXECUTE:
                return ConnectorInstanceItem.VALUE_RESET_STATE_TO_RE_EXECUTE;
            default:
                throw new RuntimeException("Can't convert <" + enumValue + ">. Flow node type not supported.");
        }
    }
}
