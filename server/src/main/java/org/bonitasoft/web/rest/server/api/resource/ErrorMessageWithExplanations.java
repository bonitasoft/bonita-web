/*******************************************************************************
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.server.api.resource;

import java.util.List;

/**
 * Representation for error entity
 *
 * @author Colin Puy
 */
public class ErrorMessageWithExplanations extends ErrorMessage {

    private List<String> explanations;

    // DO NOT PUT stacktrace, this is not coherent with old API toolkit but as a client of REST API, I do not need stacktrace.

    public ErrorMessageWithExplanations() {
        // empty constructor for json serialization
    }

    public ErrorMessageWithExplanations(final Throwable t) {
        super(t);
    }

    public List<String> getExplanations() {
        return explanations;
    }

    public void setExplanations(final List<String> explanations) {
        this.explanations = explanations;
    }
}
