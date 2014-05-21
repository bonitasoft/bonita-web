/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.console.client.common.system.view;

import org.bonitasoft.web.toolkit.client.Session;
import org.bonitasoft.web.toolkit.client.common.session.SessionItem;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.component.Definition;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

/**
 * @author Julien Mege
 * 
 */
public class PopupAboutPage extends Page {

    public static final String TOKEN = "popupaboutpage";

    @Override
    public void defineTitle() {
        this.setTitle(_("Bonita Portal"));
    }

    @Override
    public void buildView() {
        addBody(new Definition(_("Version") + " : ", getVersion()));
    }
    
    /**
     * get version from html host page in meta tag named version.
     */
    private String getVersion() {
        return Session.getParameter(SessionItem.ATTRIBUTE_VERSION);
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }
}
