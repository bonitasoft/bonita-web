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

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.web.toolkit.client.Session;
import org.bonitasoft.web.toolkit.client.common.session.SessionItem;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.component.Definition;
import org.bonitasoft.web.toolkit.client.ui.component.Paragraph;

/**
 * @author Julien Mege
 *
 */
public class PopupAboutPage extends Page {

    public static final String TOKEN = "popupaboutpage";

    @Override
    public void defineTitle() {
        this.setTitle("Bonita BPM Portal");
    }

    @Override
    public void buildView() {
        addBody(new Definition(_("Version") + " : ", getVersion()));
        addBody(new Paragraph(getCopyright()));
    }

    /**
     * get version from session.
     */
    protected String getVersion() {
        return Session.getParameter(SessionItem.ATTRIBUTE_VERSION);
    }

    /**
     * get copyright from session.
     */
    protected String getCopyright() {
        return Session.getParameter(SessionItem.ATTRIBUTE_COPYRIGHT);
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }
}
