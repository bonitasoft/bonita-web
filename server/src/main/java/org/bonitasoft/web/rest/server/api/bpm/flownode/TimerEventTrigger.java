/**
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation
 * version 2.1 of the License.
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth
 * Floor, Boston, MA 02110-1301, USA.
 **/
package org.bonitasoft.web.rest.server.api.bpm.flownode;

/**
 * Represent the instance of the {@link TimerEventTriggerDefinition} (only for the type {@link TimerType#DATE} and {@link TimerType#DURATION})
 * 
 * @author Celine Souchet
 */
public class TimerEventTrigger {

    private Long executionDate = null;

    /**
     * Default Constructor.
     */
    public TimerEventTrigger() {
    }

    /**
     * The Constructor with the new value for the date of the execution of the trigger.
     *
     * @param executionDate
     *        The new date of the execution of the trigger
     */
    public TimerEventTrigger(final long executionDate) {
        this.executionDate = executionDate;
    }

    /**
     * Set the date of the execution of the trigger in milliseconds since January 1, 1970, 00:00:00 GMT.
     *
     * @param executionDate
     *        The new date of the execution of the trigger
     */
    public void setExecutionDate(final Long executionDate) {
        this.executionDate = executionDate;
    }

    /**
     * Return the date of the execution of the trigger.
     *
     * @return Returns the number of milliseconds since January 1, 1970, 00:00:00 GMT for the date of the execution of the trigger.
     * @since 6.4.0
     * @see java.util.Date#getTime()
     */
    public Long getExecutionDate() {
        return executionDate;
    }

}
