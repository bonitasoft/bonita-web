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
package org.bonitasoft.test.toolkit.exception;

import java.text.MessageFormat;

import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance;

/**
 * @author Vincent Elcrin
 * 
 */
public class NextActivityIsNotAllowedStateException extends TestToolkitException {

    /**
     * 
     */
    private static final long serialVersionUID = 8542205212242675994L;

    /**
     * Default Constructor.
     */
    public NextActivityIsNotAllowedStateException(final HumanTaskInstance humanTask) {
        super(MessageFormat.format("Activity with id {0} is the state {1} which is not allowed.", humanTask.getId(), humanTask.getState()));
    }

}
