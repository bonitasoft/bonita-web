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
package org.bonitasoft.web.rest.api.model.bpm.cases;

import java.util.Date;

import org.bonitasoft.web.rest.api.model.bpm.flownode.TaskItem;
import org.bonitasoft.web.rest.api.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.Item;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.template.ItemHasUniqueId;

/**
 * @author Vincent Elcrin
 * 
 */
public class CommentItem extends Item implements ItemHasUniqueId {

    public CommentItem() {
        super();
    }

    public CommentItem(final IItem item) {
        super(item);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ATTRIBUTES NAMES
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final String ATTRIBUTE_TENANT_ID = "tenantId";

    public static final String ATTRIBUTE_USER_ID = "userId";

    public static final String ATTRIBUTE_PROCESS_INSTANCE_ID = "processInstanceId";

    public static final String ATTRIBUTE_POST_DATE = "postDate";

    public static final String ATTRIBUTE_CONTENT = "content";

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ATTRIBUTES VALUES
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // FILTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final String FILTER_TEAM_MANAGER_ID = "team_manager_id";

    public static final String FILTER_SUPERVISOR_ID = "supervisor_id";

    public static final String FILTER_USER_ID = "user_id";

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // COUNTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GETTERS AND SETTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // GETTERS

    public APIID getTenantId() {
        return APIID.makeAPIID(this.getAttributeValue(ATTRIBUTE_TENANT_ID));
    }

    public APIID getUserId() {
        return APIID.makeAPIID(this.getAttributeValue(ATTRIBUTE_USER_ID));
    }

    public APIID getProcessInstanceId() {
        return APIID.makeAPIID(this.getAttributeValue(ATTRIBUTE_PROCESS_INSTANCE_ID));
    }

    public String getPostDate() {
        return this.getAttributeValue(ATTRIBUTE_POST_DATE);
    }

    public String getContent() {
        return this.getAttributeValue(ATTRIBUTE_CONTENT);
    }

    // SETTERS

    @Override
    public void setId(final String id) {
        this.setAttribute(ATTRIBUTE_ID, id);
    }

    @Override
    public void setId(final Long id) {
        this.setAttribute(ATTRIBUTE_ID, APIID.makeAPIID(id));
    }

    public void setTenantId(final String id) {
        this.setAttribute(ATTRIBUTE_TENANT_ID, id);
    }

    public void setTenantId(final Long id) {
        this.setAttribute(ATTRIBUTE_TENANT_ID, APIID.makeAPIID(id));
    }

    public void setTenantId(final APIID id) {
        this.setAttribute(ATTRIBUTE_TENANT_ID, id);
    }

    public void setUserId(final String id) {
        this.setAttribute(ATTRIBUTE_USER_ID, id);
    }

    public void setUserId(final Long id) {
        this.setAttribute(ATTRIBUTE_USER_ID, APIID.makeAPIID(id));
    }

    public void setUserId(final APIID id) {
        this.setAttribute(ATTRIBUTE_USER_ID, id);
    }

    public void setProcessInstanceId(final String id) {
        this.setAttribute(ATTRIBUTE_PROCESS_INSTANCE_ID, id);
    }

    public void setProcessInstanceId(final Long id) {
        this.setAttribute(ATTRIBUTE_PROCESS_INSTANCE_ID, APIID.makeAPIID(id));
    }

    public void setProcessInstanceId(final APIID id) {
        this.setAttribute(ATTRIBUTE_PROCESS_INSTANCE_ID, id);
    }

    public void setPostDate(final String date) {
        this.setAttribute(ATTRIBUTE_POST_DATE, date);
    }

    public void setPostDate(final Long date) {
        setPostDate(new Date(date));
    }

    public void setPostDate(final Date date) {
        this.setAttribute(ATTRIBUTE_POST_DATE, date);
    }

    public void setContent(final String content) {
        this.setAttribute(ATTRIBUTE_CONTENT, content);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // DEPLOYS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public UserItem getUser() {
        return new UserItem(getDeploy(ATTRIBUTE_USER_ID));
    }

    public TaskItem getProcessInstance() {
        return new TaskItem(getDeploy(ATTRIBUTE_PROCESS_INSTANCE_ID));
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // UTILS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public ItemDefinition getItemDefinition() {
        return Definitions.get(CommentDefinition.TOKEN);
    }
}
