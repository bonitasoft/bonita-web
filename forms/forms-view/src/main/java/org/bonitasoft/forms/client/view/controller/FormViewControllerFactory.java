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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.forms.client.view.controller;

import java.util.Map;

import org.bonitasoft.forms.client.model.ReducedFormPage;
import org.bonitasoft.web.rest.api.model.user.User;

import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * @author Anthony Birembaut
 * 
 */
public class FormViewControllerFactory {

    public static FormPagesViewController getFormPagesViewController(final String formID, Map<String, Object> contextMap, final ReducedFormPage firstPage, final HTMLPanel applicationHTMLPanel, final User user, String elementId) {
        return new FormPagesViewController(formID, contextMap, firstPage, applicationHTMLPanel, user, elementId);
    }

    public static FormApplicationViewController getFormApplicationViewController(final String formID, final Map<String, Object> urlContext, final User user) {
        return new FormApplicationViewController(formID, urlContext, user);
    }

    public static PageflowViewController getPageflowViewController(String formID, Map<String, Object> contextMap, User user, String elementId, HTMLPanel applicationHTMLPanel) {
        return new PageflowViewController(formID, contextMap, user, elementId, applicationHTMLPanel);
    }
}
