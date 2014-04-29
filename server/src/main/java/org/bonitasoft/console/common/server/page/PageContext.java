/*******************************************************************************
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.console.common.server.page;

import java.util.Locale;

import org.bonitasoft.engine.session.APISession;

/**
 * This class provide access to the data relative to the context in which the custom page is displayed
 * 
 * @author Anthony Birembaut
 * 
 */
public class PageContext {

    protected APISession apiSession;

    protected Locale locale;

    protected String profileID;

    protected PageContext(final APISession apiSession, final Locale locale, final String profileID) {
        super();
        this.apiSession = apiSession;
        this.locale = locale;
        this.profileID = profileID;
    }

    /**
     * @return the engine {@link APISession}
     */
    public APISession getApiSession() {
        return apiSession;
    }

    /**
     * @return the user locale
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * @return the ID of the profile in which the page is currently displayed
     */
    public String getProfileID() {
        return profileID;
    }

}
