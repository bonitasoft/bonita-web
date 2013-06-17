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

import org.bonitasoft.engine.bpm.connector.ConnectorState;
import org.bonitasoft.web.rest.api.model.bpm.connector.ConnectorInstanceItem;
import org.bonitasoft.web.toolkit.server.api.EnumConverter;

/**
 * Convenient object to convert ATTRIBUTE_VALUE from {@link ConnectorInstanceItem} into {@link ConnectorState}
 * 
 * @author Vincent Elcrin
 * 
 */
public class ConnectorInstanceStateConverter implements EnumConverter<ConnectorState> {

    public final ConnectorState convert(final String attributeStateValue) {
        if (ConnectorInstanceItem.VALUE_STATE_DONE.equals(attributeStateValue)) {
            return ConnectorState.DONE;
        } else if (ConnectorInstanceItem.VALUE_STATE_FAILED.equals(attributeStateValue)) {
            return ConnectorState.FAILED;
        } else if (ConnectorInstanceItem.VALUE_STATE_SKIPPED.equals(attributeStateValue)) {
            return ConnectorState.SKIPPED;
        } else if (ConnectorInstanceItem.VALUE_STATE_TO_BE_EXECUTED.equals(attributeStateValue)) {
            return ConnectorState.TO_BE_EXECUTED;
        } else if (ConnectorInstanceItem.VALUE_STATE_TO_RE_EXECUTE.equals(attributeStateValue)) {
            return ConnectorState.TO_RE_EXECUTE;
        } else {
            throw new RuntimeException("Can't convert following state into engine state <" + attributeStateValue + ">");
        }
    }

    @Override
    public String convert(ConnectorState enumValue) {
        switch (enumValue) {
            case DONE:
                return ConnectorInstanceItem.VALUE_STATE_DONE;
            case FAILED:
                return ConnectorInstanceItem.VALUE_STATE_FAILED;
            case SKIPPED:
                return ConnectorInstanceItem.VALUE_STATE_SKIPPED;
            case TO_BE_EXECUTED:
                return ConnectorInstanceItem.VALUE_STATE_TO_BE_EXECUTED;
            case TO_RE_EXECUTE:
                return ConnectorInstanceItem.VALUE_STATE_TO_RE_EXECUTE;
            default:
                throw new RuntimeException("Can't convert <" + enumValue + ">. Flow node type not supported.");
        }

    }
}
