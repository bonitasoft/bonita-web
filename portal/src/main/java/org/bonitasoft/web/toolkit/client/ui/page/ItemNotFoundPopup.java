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
package org.bonitasoft.web.toolkit.client.ui.page;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.action.HistoryBackAction;
import org.bonitasoft.web.toolkit.client.ui.action.RedirectionAction;
import org.bonitasoft.web.toolkit.client.ui.component.Button;
import org.bonitasoft.web.toolkit.client.ui.component.Paragraph;

/**
 * @author Vincent Elcrin
 *
 */
public class ItemNotFoundPopup extends Page {

    public final static String TOKEN = "itemnotfound";

    public final static String PARAMETER_REDIRECTION_TARGET = "target";

    public ItemNotFoundPopup() {
        // empty constructor for view registration
    }

    public ItemNotFoundPopup(final String redirectionTargetToken) {
        addParameter(PARAMETER_REDIRECTION_TARGET, redirectionTargetToken);
    }

    @Override
    public void defineTitle() {
        setTitle(_("Item not found"));
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    public void buildView() {
        addBody(new Paragraph(_("Oups... This item is not available anymore.\nYou will be redirected to a safest place.")));
        addFoot(new Button(_("OK"), _("You will be rediected"), getRedirectionAction()));
    }

    protected Action getRedirectionAction() {
        if (getParameter(PARAMETER_REDIRECTION_TARGET) != null) {
            return new RedirectionAction(getParameter(PARAMETER_REDIRECTION_TARGET));
        } else {
            return new HistoryBackAction();
        }
    }
}
