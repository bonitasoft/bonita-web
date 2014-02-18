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
package org.bonitasoft.web.rest.model.bpm.flownode;

import org.bonitasoft.web.rest.model.bpm.cases.CaseItem;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.common.util.StringUtil;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.Item;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Séverin Moussel
 * 
 */
public class FlowNodeItem extends Item implements IFlowNodeItem {

    public FlowNodeItem() {
        super();
    }

    public FlowNodeItem(final IItem item) {
        super(item);
    }

    @Override
    public final void setDescription(final String description) {
        this.setAttribute(ATTRIBUTE_DESCRIPTION, description);
    }

    @Override
    public final void setDisplayDescription(final String displayDescription) {
        this.setAttribute(ATTRIBUTE_DISPLAY_DESCRIPTION, displayDescription);
    }

    @Override
    public final String getDescription() {
        return getAttributeValue(ATTRIBUTE_DESCRIPTION);
    }

    @Override
    public final String getDisplayDescription() {
        return getAttributeValue(ATTRIBUTE_DISPLAY_DESCRIPTION);
    }

    @Override
    public String ensureDescription() {
        if(StringUtil.isBlank(getDisplayDescription())) {
            return getDescription();
        }
        return getDisplayDescription();
    }

    @Override
    public final void setName(final String name) {
        this.setAttribute(ATTRIBUTE_NAME, name);
    }

    @Override
    public final void setDisplayName(final String displayName) {
        this.setAttribute(ATTRIBUTE_DISPLAY_NAME, displayName);
    }

    @Override
    public final String getName() {
        return getAttributeValue(ATTRIBUTE_NAME);
    }

    @Override
    public final String getDisplayName() {
        return getAttributeValue(ATTRIBUTE_DISPLAY_NAME);
    }

    @Override
    public String ensureName() {
        if(StringUtil.isBlank(getDisplayName())) {
            return getName();
        }
        return getDisplayName();
    }

    @Override
    public final void setId(final String id) {
        this.setAttribute(ATTRIBUTE_ID, id);
    }

    @Override
    public final void setId(final Long id) {
        this.setAttribute(ATTRIBUTE_ID, id);
    }

    @Override
    public final void setProcessId(final APIID id) {
        setAttribute(ATTRIBUTE_PROCESS_ID, id);
    }

    @Override
    public final void setProcessId(final String id) {
        setAttribute(ATTRIBUTE_PROCESS_ID, id);
    }

    @Override
    public final void setProcessId(final Long id) {
        setAttribute(ATTRIBUTE_PROCESS_ID, id);
    }

    @Override
    public final APIID getProcessId() {
        return getAttributeValueAsAPIID(ATTRIBUTE_PROCESS_ID);
    }

    @Override
    public final void setCaseId(final APIID id) {
        setAttribute(ATTRIBUTE_CASE_ID, id);
    }

    @Override
    public final void setCaseId(final String id) {
        setAttribute(ATTRIBUTE_CASE_ID, id);
    }

    @Override
    public final void setCaseId(final Long id) {
        setAttribute(ATTRIBUTE_CASE_ID, id);
    }

    @Override
    public final APIID getCaseId() {
        return getAttributeValueAsAPIID(ATTRIBUTE_CASE_ID);
    }

    @Override
    public final void setState(final String state) {
        setAttribute(ATTRIBUTE_STATE, state);
    }

    @Override
    public final String getState() {
        return getAttributeValue(ATTRIBUTE_STATE);
    }

    @Override
    public final void setType(final String type) {
        setAttribute(ATTRIBUTE_TYPE, type);
    }

    @Override
    public final String getType() {
        return getAttributeValue(ATTRIBUTE_TYPE);
    }

    @Override
    public final void setExecutedByUserId(final APIID id) {
        setAttribute(ATTRIBUTE_EXECUTED_BY_USER_ID, id);
    }

    @Override
    public final void setExecutedByUserId(final String id) {
        setAttribute(ATTRIBUTE_EXECUTED_BY_USER_ID, id);
    }

    @Override
    public final void setExecutedByUserId(final Long id) {
        setAttribute(ATTRIBUTE_EXECUTED_BY_USER_ID, id);
    }

    @Override
    public final APIID getExecutedByUserId() {
        return getAttributeValueAsAPIID(ATTRIBUTE_EXECUTED_BY_USER_ID);
    }

    @Override
    public final UserItem getExecutedByUser() {
        return new UserItem(getDeploy(ATTRIBUTE_EXECUTED_BY_USER_ID));
    }

    @Override
    public final ProcessItem getProcess() {
        return new ProcessItem(getDeploy(ATTRIBUTE_PROCESS_ID));
    }

    @Override
    public final CaseItem getCase() {
        return new CaseItem(getDeploy(ATTRIBUTE_CASE_ID));
    }

    @Override
    public ItemDefinition getItemDefinition() {
        return FlowNodeDefinition.get();
    }

    @Override
    public final boolean isUserTask() {
        return VALUE_TYPE_USER_TASK.equals(getType());
    }

    @Override
    public boolean isAutomaticTask() {
        return VALUE_TYPE_AUTOMATIC_TASK.equals(getType());
    }

    @Override
    public final boolean isBoundaryEvent() {
        return VALUE_TYPE_BOUNDARY_EVENT.equals(getType());
    }

    @Override
    public final boolean isCallActivity() {
        return VALUE_TYPE_CALL_ACTIVITY.equals(getType());
    }

    @Override
    public final boolean isEndEvent() {
        return VALUE_TYPE_END_EVENT.equals(getType());
    }

    @Override
    public final boolean isGateway() {
        return VALUE_TYPE_GATEWAY.equals(getType());
    }

    @Override
    public final boolean isHumanTask() {
        return isManualTask() || isUserTask();
    }

    @Override
    public final boolean isIntermediateCatchEvent() {
        return VALUE_TYPE_INTERMEDIATE_CATCH_EVENT.equals(getType());
    }

    @Override
    public final boolean isIntermediateThrowEvent() {
        return VALUE_TYPE_INTERMEDIATE_THROW_EVENT.equals(getType());
    }

    @Override
    public final boolean isLoopActivity() {
        return VALUE_TYPE_LOOP_ACTIVITY.equals(getType());
    }

    @Override
    public final boolean isSubProcessActivity() {
        return VALUE_TYPE_SUB_PROCESS_ACTIVITY.equals(getType());
    }

    @Override
    public final boolean isMultiInsatnceActivity() {
        return VALUE_TYPE_MULTI_INSTANCE_ACTIVITY.equals(getType());
    }

    @Override
    public final boolean isManualTask() {
        return VALUE_TYPE_MANUAL_TASK.equals(getType());
    }

    @Override
    public final boolean isStartEvent() {
        return VALUE_TYPE_START_EVENT.equals(getType());
    }

    @Override
    public boolean isArchived() {
        // Override this in archived activities to return true
        return false;
    }

    @Override
    public boolean isAborted() {
        return VALUE_STATE_ABORTED.equals(getState());
    }

    @Override
    public boolean isActivity() {
        return isAutomaticTask()
                || isCallActivity()
                || isHumanTask()
                || isLoopActivity()
                || isSubProcessActivity()
                || isMultiInsatnceActivity();
    }

}
