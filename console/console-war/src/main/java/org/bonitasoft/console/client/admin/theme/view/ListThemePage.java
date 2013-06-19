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

import org.bonitasoft.web.rest.model.portal.theme.ThemeDefinition;
import org.bonitasoft.web.rest.model.portal.theme.ThemeItem;
import org.bonitasoft.web.toolkit.client.ClientApplicationURL;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.action.RedirectionAction;
import org.bonitasoft.web.toolkit.client.ui.action.popup.PopupAction;
import org.bonitasoft.web.toolkit.client.ui.component.Button;
import org.bonitasoft.web.toolkit.client.ui.component.Text;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTableActionSet;

/**
 * @author Gai Cuisha
 * 
 */
public class ListThemePage extends Page {

    /**
     * the token of this page
     */
    public static final String TOKEN = "themelistingadmin";

    protected static final String THEME_LIST = "themeList";

    protected static final String ADD_THEME = "addTheme";

    @Override
    public void defineTitle() {
        this.setTitle(_("Look'n'feel"));
    }

    @Override
    public String defineJsId() {
        return THEME_LIST;
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    public void buildView() {
        addBody(new Text("", new Button(new JsId(ADD_THEME), _("Add"), "", new PopupAction(UploadThemePage.TOKEN))),
                new ItemTable(Definitions.get(ThemeDefinition.TOKEN))
                        .addColumn(ThemeItem.ATTRIBUTE_ICON, "")
                        .addColumn(ThemeItem.ATTRIBUTE_NAME, _("Name"), true, true)
                        .addColumn(ThemeItem.ATTRIBUTE_AUTHOR, _("Author"))
                        .addColumn(ThemeItem.ATTRIBUTE_INSTALLEDBY, _("Installed by"))
                        .addColumn(ThemeItem.ATTRIBUTE_INSTALLEDDATE, _("Installed date"))
                        .addActions(new ItemTableActionSet() {

                            @Override
                            protected void defineActions(final IItem item) {
                                if (item.getAttributeValue(ThemeItem.ATTRIBUTE_ISDEFAULT).equals("false")) {
                                    this.addActionDelete("", Definitions.get(ThemeDefinition.TOKEN));
                                }
                                this.addAction(_("Apply"), "", new RedirectionAction(EditThemePage.TOKEN, ClientApplicationURL.Target.POPUP));
                            }
                        }).addGroupedDeleteAction("", Definitions.get(ThemeDefinition.TOKEN)));

    }

}
