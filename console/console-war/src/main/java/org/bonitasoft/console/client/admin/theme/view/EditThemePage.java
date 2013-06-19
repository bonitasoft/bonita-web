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
package org.bonitasoft.console.client.admin.theme.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.console.client.admin.theme.action.ApplyThemeAction;
import org.bonitasoft.web.rest.model.portal.theme.ThemeDefinition;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.component.form.Form;

/**
 * @author Gai Cuisha
 * 
 */
public class EditThemePage extends Page {

    /**
     * the token of this page
     */
    public static final String TOKEN = "EditTheme";

    @Override
    public void defineTitle() {
        this.setTitle(_("Apply this look'n'feel?"));
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    public void buildView() {
        final ItemDefinition itemDef = Definitions.get(ThemeDefinition.TOKEN);
        final String itemId = this.getParameter("id");
        final Form form = new Form(new JsId(TOKEN));
        form.addHiddenEntry("id", itemId);
        form.addButton(new JsId("apply"), _("Apply"), _("Apply"), new ApplyThemeAction(itemDef));
        addBody(form);
    }
}
