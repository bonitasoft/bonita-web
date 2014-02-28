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
package org.bonitasoft.forms.server.accessor.api;

import org.bonitasoft.console.common.server.utils.BPMEngineException;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.session.APISession;

/**
 * @author Colin PUY
 * 
 */
public class EngineClientFactory {

    public ExpressionEvaluatorEngineClient createExpressionEvaluatorEngineClient(APISession session)
            throws BPMEngineException {
        return new ExpressionEvaluatorEngineClient(getProcessAPI(session));
    }

    public ProcessInstanceAccessorEngineClient createProcessInstanceEngineClient(APISession session)
            throws BPMEngineException {
        return new ProcessInstanceAccessorEngineClient(getProcessAPI(session));
    }

    public ProcessAPI getProcessAPI(APISession session) throws BPMEngineException {
        try {
            return TenantAPIAccessor.getProcessAPI(session);
        } catch (Exception e) {
            throw new BPMEngineException("Unable to get engine process API", e);
        }
    }
}
