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
package org.bonitasoft.console.client.user.task.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.Iterator;
import java.util.LinkedList;

import org.bonitasoft.console.client.data.item.attribute.reader.DeployedUserReader;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.toolkit.client.ui.CssClass;
import org.bonitasoft.web.toolkit.client.ui.component.button.ButtonBack;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemDetailsAction;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemDetailsMetadata;

/**
 * @author Séverin Moussel
 * 
 */
public class ProcessMoreDetailsPage extends ProcessQuickDetailsPage implements PluginProcess {

    public final static String TOKEN = "processmoredetails";

    /**
     * Default Constructor.
     */
    public ProcessMoreDetailsPage() {
        addClass(CssClass.MORE_DETAILS);
    }

    @Override
    public String defineToken() {// to put the moredetailsclass to make a difference with quickdetails
        return TOKEN;
    }

    @Override
    protected boolean isDescriptionBeforeMetadatas() {
        return false;
    }

    @Override
    protected LinkedList<ItemDetailsMetadata> defineMetadatas(final ProcessItem item) {
        final LinkedList<ItemDetailsMetadata> metadatas = super.defineMetadatas(item);

        metadatas.add(new ItemDetailsMetadata(ProcessItem.ATTRIBUTE_VERSION, _("Version"), _("The version of the app")));
        metadatas.add(new ItemDetailsMetadata(ProcessItem.ATTRIBUTE_DEPLOYMENT_DATE, _("Installed on"), _("The date while this app has been installed")));
        metadatas.add(new ItemDetailsMetadata(
                new DeployedUserReader(ProcessItem.ATTRIBUTE_DEPLOYED_BY_USER_ID),
                _("Installed by"),
                _("The user who has installed this app")));
        metadatas.add(new ItemDetailsMetadata(ProcessItem.ATTRIBUTE_LAST_UPDATE_DATE, _("Last updated on"), _("The date while the app has been updated")));

        return metadatas;
    }

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.console.client.user.task.view.ProcessQuickDetailsPage#defineActions(org.bonitasoft.console.client.process.model.ProcessItem)
     */
    @Override
    protected LinkedList<ItemDetailsAction> defineActions(final ProcessItem item) {
        final LinkedList<ItemDetailsAction> superActions = super.defineActions(item);
        final LinkedList<ItemDetailsAction> actions = new LinkedList<ItemDetailsAction>(superActions);
        ProcessMoreDetailsPage.this.addToolbarLink(new ButtonBack());

        /*
         * Find and remove more button from the actions
         */
        final Iterator<ItemDetailsAction> it = superActions.iterator();
        while (it.hasNext()) {
            final ItemDetailsAction action = it.next();
            if (_("More").equals(action.getLabel())) {
                actions.remove(action);
                break;
            }
        }

        return actions;
    }

    @Override
    public String getPluginToken() {
        return PLUGIN_TOKEN;
    }

}
