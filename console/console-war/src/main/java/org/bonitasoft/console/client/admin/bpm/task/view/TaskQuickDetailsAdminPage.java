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
package org.bonitasoft.console.client.admin.bpm.task.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.bonitasoft.console.client.admin.bpm.accessor.IActivityAccessor;
import org.bonitasoft.console.client.common.component.snippet.CommentSectionSnippet;
import org.bonitasoft.console.client.common.metadata.MetadataTaskBuilder;
import org.bonitasoft.web.rest.model.bpm.flownode.ArchivedFlowNodeDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.FlowNodeDefinition;
import org.bonitasoft.web.rest.model.bpm.flownode.IActivityItem;
import org.bonitasoft.web.rest.model.bpm.flownode.IFlowNodeItem;
import org.bonitasoft.web.rest.model.bpm.flownode.IHumanTaskItem;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DescriptionAttributeReader;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.NameAttributeReader;
import org.bonitasoft.web.toolkit.client.ui.RawView;
import org.bonitasoft.web.toolkit.client.ui.action.ActionShowView;
import org.bonitasoft.web.toolkit.client.ui.component.Button;
import org.bonitasoft.web.toolkit.client.ui.component.Clickable;
import org.bonitasoft.web.toolkit.client.ui.component.Section;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemDetailsMetadata;
import org.bonitasoft.web.toolkit.client.ui.utils.DateFormat.FORMAT;

/**
 * @author Vincent Elcrin
 * 
 */
public class TaskQuickDetailsAdminPage extends ArchivableItemDetailsPage<IFlowNodeItem> {

    public final static String TOKEN = "taskquickdetailsadmin";    
    
    public static final List<String> PRIVILEGES = new ArrayList<String>();
    
    static {
        PRIVILEGES.add(TaskListingAdminPage.TOKEN);
    }

    public TaskQuickDetailsAdminPage() {
        super(FlowNodeDefinition.get(), ArchivedFlowNodeDefinition.get());
    }

    /**
     * Constructor which set archived parameter of the page.
     * 
     * @param archived
     */
    public TaskQuickDetailsAdminPage(final boolean archived) {
        this();
        setArchive(archived);
    }

    @Override
    protected void defineTitle(final IFlowNodeItem flowNode) {
        setTitle(new NameAttributeReader().read(flowNode));
        addDescription(new DescriptionAttributeReader().read(flowNode));
    }

    @Override
    protected void buildToolbar(final IFlowNodeItem flowNode) {
        addToolbarLink(moreDetailsButton(flowNode.getId()));
    }

    private Clickable moreDetailsButton(final APIID id) {
        return new Button("btn-more", _("More"), _("Show more details about this task"),
                new ActionShowView(createMoreDetailsPage(id)));
    }

    private RawView createMoreDetailsPage(APIID id) {
        TaskMoreDetailsAdminPage page = new TaskMoreDetailsAdminPage(isArchived());
        page.addParameter(PARAMETER_ITEM_ID, id.toString());
        return page;
    }

    @Override
    protected LinkedList<ItemDetailsMetadata> defineMetadatas(final IFlowNodeItem item) {
        final MetadataTaskBuilder metadatas = new MetadataTaskBuilder();
        metadatas.addCaseId(item, true);
        metadatas.addAppsName();
        metadatas.addDueDate(getArchivedDateFormat());
        metadatas.addType();
        if (item.isHumanTask()) {
            metadatas.addAssignedTo();
        }
        return metadatas.build();
    }

    protected FORMAT getArchivedDateFormat() {
        return isArchived() ? FORMAT.DISPLAY : FORMAT.DISPLAY_RELATIVE;
    }

    @Override
    protected void buildBody(final IFlowNodeItem item) {
        if (item.isActivity()) {
            addBody(getTaskTechnicalInformation(new IActivityAccessor(item)));
        }
        addBody(new CommentSectionSnippet(item.getCaseId())
                .setNbLinesByPage(3)
                .build());
    }

    protected Section getTaskTechnicalInformation(final IActivityAccessor activity) {
        if (activity.isArchived()) {
            return new ArchivedTaskTechnicalInformationSnippet(activity)
                    .build();
        } else {
            return new TaskTechnicalInformationSnippet(activity)
                    .build();
        }
    }

    @Override
    protected List<String> defineDeploys() {
        return Arrays.asList(IActivityItem.ATTRIBUTE_PROCESS_ID, IActivityItem.ATTRIBUTE_EXECUTED_BY_USER_ID, IHumanTaskItem.ATTRIBUTE_ASSIGNED_USER_ID);
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }
}
