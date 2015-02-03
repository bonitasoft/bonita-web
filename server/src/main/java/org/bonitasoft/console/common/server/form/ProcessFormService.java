package org.bonitasoft.console.common.server.form;

import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.session.APISession;

public class ProcessFormService {

    /**
     * @param apiSession
     * @param processName
     * @param processVersion
     * @param taskName
     * @param isInstance indicate if the form is an instance form (to differenciate the process instantiation form from the process instance overview)
     * @return
     * @throws BonitaException
     */
    public String getForm(final APISession apiSession, final String processName, final String processVersion, final String taskName, final boolean isInstance)
            throws BonitaException {
        // TODO retrieve mapping from engine
        // Mock
        if ("request".equals(taskName)) {
            return "custompage_form";
        } else {
            return "/form.html";
        }
    }

}
