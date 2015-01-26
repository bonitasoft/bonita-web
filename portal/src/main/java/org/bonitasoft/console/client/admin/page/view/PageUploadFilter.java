/*******************************************************************************
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.console.client.admin.page.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.web.toolkit.client.ui.component.form.entry.UploadFilter;

/**
 * @author Anthony Birembaut
 * 
 */
public class PageUploadFilter extends UploadFilter {

    /**
     * Default Constructor.
     * 
     * @param title
     * @param extensions
     */
    public PageUploadFilter() {
        super(_("Zip Archive"), "zip");
    }
}
