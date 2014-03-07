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
package org.bonitasoft.console.client.user.task.view.more;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bonitasoft.console.client.common.component.snippet.CommentSectionSnippet;
import org.bonitasoft.console.client.user.task.view.AbstractTaskDetailsPage;
import org.bonitasoft.web.rest.model.bpm.flownode.HumanTaskItem;
import org.bonitasoft.web.rest.model.bpm.flownode.IHumanTaskItem;
import org.bonitasoft.web.rest.model.bpm.flownode.TaskItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.ui.CssClass;
import org.bonitasoft.web.toolkit.client.ui.component.button.ButtonBack;
import org.bonitasoft.web.toolkit.client.ui.component.core.UiComponent;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemDetailsMetadata;

/**
 * @author Colin PUY
 * 
 */
public abstract class AbstractMoreTaskDetailPage<T extends IHumanTaskItem> extends AbstractTaskDetailsPage<T> {

    public AbstractMoreTaskDetailPage(ItemDefinition itemDefinition, T humanTask) {
        this(itemDefinition);
        addParameter(PARAMETER_ITEM_ID, humanTask.getId().toString());
    }

    public AbstractMoreTaskDetailPage(ItemDefinition itemDefinition) {
        super(itemDefinition);
        addClass(CssClass.MORE_DETAILS);
    }

    @Override
    protected List<String> defineDeploys() {
        final List<String> deploys = new ArrayList<String>(super.defineDeploys());
        deploys.add(HumanTaskItem.ATTRIBUTE_ASSIGNED_USER_ID);
        deploys.add(TaskItem.ATTRIBUTE_EXECUTED_BY_USER_ID);
        return deploys;
    }

    @Override
    protected void buildToolbar(T item) {
        addToolbarLink(new ButtonBack());
    }

    @Override
    protected void buildMetadatas(T item) {
        addBody(new UiComponent(new HumanTaskMetadataView(item)));
    }

    @Override
    protected boolean isDescriptionBeforeMetadatas() {
        return false;
    }

    @Override
    protected LinkedList<ItemDetailsMetadata> defineMetadatas(T item) {
        return null;
    }

    @Override
    protected void buildBody(final T item) {
        addBody(new CommentSectionSnippet(item.getCaseId()).build());
    }
}
