/**
 * Copyright (C) 2010 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.forms.server.accessor.impl.util;

import java.io.IOException;
import java.util.Date;

import org.bonitasoft.console.common.server.utils.BPMEngineException;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.engine.exception.RetrieveException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.InvalidSessionException;
import org.bonitasoft.forms.server.exception.InvalidFormDefinitionException;

/**
 * @author Anthony Birembaut
 * 
 */
public class FormDocumentBuilderFactory {

    /**
     * @param session
     * @param locale
     * @param processDeployementDate
     * @return
     * @throws IOException
     * @throws InvalidFormDefinitionException
     * @throws BPMEngineException
     * @throws InvalidSessionException
     */
    public static FormDocumentBuilder getFormDocumentBuilder(final APISession session, final long processDefinitionID, final String locale,
            final Date processDeployementDate) throws ProcessDefinitionNotFoundException, IOException, InvalidFormDefinitionException, BPMEngineException,
            InvalidSessionException, RetrieveException {
        return FormDocumentBuilder.getInstance(session, processDefinitionID, locale, processDeployementDate);
    }
}
