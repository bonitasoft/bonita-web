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
package org.bonitasoft.forms.server.accessor.api.utils;

import org.bonitasoft.engine.bpm.process.ArchivedProcessInstance;
import org.bonitasoft.engine.bpm.process.ArchivedProcessInstanceNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessInstance;
import org.bonitasoft.engine.bpm.process.ProcessInstanceNotFoundException;
import org.bonitasoft.forms.server.accessor.api.ProcessInstanceAccessorEngineClient;
import org.bonitasoft.forms.server.exception.BPMEngineException;

/**
 * @author Vincent Elcrin
 * 
 */
public class ProcessInstanceAccessor {

    private final ProcessInstanceAccessorEngineClient processInstanceAccessor;

    private long id;

    private boolean archived;

    public ProcessInstanceAccessor(ProcessInstanceAccessorEngineClient processInstanceAccessor, long id) throws BPMEngineException {
        this.processInstanceAccessor = processInstanceAccessor;
        fecthConfiguration(id);
    }

    private void fecthConfiguration(long id) throws BPMEngineException {
        try {
            this.id = getProcessInstance(id).getId();
            this.archived = false;
        } catch (ProcessInstanceNotFoundException e) {
            this.id = getArchivedProcessInstance(id).getSourceObjectId();
            this.archived = true;
        }
    }

    public long getId() {
        return id;
    }

    public boolean isArchived() {
        return archived;
    }

    private ProcessInstance getProcessInstance(long id) throws BPMEngineException, ProcessInstanceNotFoundException {
        return processInstanceAccessor.getProcessInstance(id);
    }

    private ArchivedProcessInstance getArchivedProcessInstance(long id) throws BPMEngineException {
        try {
            return processInstanceAccessor.getArchivedProcessInstance(id);
        } catch (ArchivedProcessInstanceNotFoundException e) {
            throw new BPMEngineException(e);
        }
    }
}
