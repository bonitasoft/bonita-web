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
package org.bonitasoft.console.client.user.task.view;

import static org.bonitasoft.web.toolkit.client.ViewController.showPopup;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.web.rest.model.bpm.flownode.HumanTaskItem;
import org.bonitasoft.web.rest.model.bpm.flownode.IHumanTaskItem;
import org.bonitasoft.web.rest.model.bpm.flownode.TaskItem;
import org.bonitasoft.web.toolkit.client.Session;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DescriptionAttributeReader;
import org.bonitasoft.web.toolkit.client.data.item.template.ItemHasDualName;
import org.bonitasoft.web.toolkit.client.ui.page.ItemNotFoundPopup;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemQuickDetailsPage;

/**
 * @author Séverin Moussel, Vincent Elcrin
 *
 */
public abstract class AbstractTaskDetailsPage<T extends IHumanTaskItem> extends ItemQuickDetailsPage<T> implements PluginTask {

    public AbstractTaskDetailsPage(final ItemDefinition itemDefinition) {
        super(itemDefinition);
    }

    @Override
    protected void defineTitle(final T item) {
        this.setTitle(item.getAttributeValue(ItemHasDualName.ATTRIBUTE_DISPLAY_NAME));
        addDescription(new DescriptionAttributeReader(HumanTaskItem.ATTRIBUTE_DISPLAY_DESCRIPTION,
                HumanTaskItem.ATTRIBUTE_DESCRIPTION).read(item));
    }

    @Override
    protected List<String> defineDeploys() {
        final ArrayList<String> deploys = new ArrayList<String>();
        deploys.add(TaskItem.ATTRIBUTE_PROCESS_ID);
        deploys.add(TaskItem.ATTRIBUTE_ROOT_CONTAINER_ID);
        return deploys;
    }

    @Override
    protected void onItemNotFound(final APIID itemId) {
        showPopup(new ItemNotFoundPopup(TasksListingPage.TOKEN));
    }

    @Override
    public String getPluginToken() {
        return PLUGIN_TOKEN;
    }

    protected boolean isTaskAssignedToOtherUser(final T item) {
        return item.getAssignedId() != null && !item.getAssignedId().equals(Session.getUserId());
    }

    protected boolean isTaskAssignedToCurrentUser(final T item) {
        return Session.getUserId().equals(item.getAssignedId());
    }
}
