/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.rest.model.bpm.cases;

import java.util.Date;

import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.Item;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.template.ItemHasLastUpdateDate;
import org.bonitasoft.web.toolkit.client.data.item.template.ItemHasUniqueId;

/**
 * process instance item
 * 
 * @author Haojie Yuan
 */
public class CaseItem extends Item implements ItemHasLastUpdateDate, ItemHasUniqueId {
    
    public static final String ATTRIBUTE_VARIABLES = "variables";
    
    public CaseItem() {
        super();
    }

    public CaseItem(final IItem item) {
        super(item);
    }

    public static final String ATTRIBUTE_STATE = "state";

    public static final String ATTRIBUTE_START_DATE = "start";

    public static final String ATTRIBUTE_STARTED_BY_USER_ID = "started_by";

    public static final String ATTRIBUTE_END_DATE = "end_date";

    public static final String ATTRIBUTE_PROCESS_ID = "processDefinitionId";

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ATTRIBUTES VALUES
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // see ProcessInstanceState enum
    public static final String VALUE_STATE_INITIALIZING = "0";

    public static final String VALUE_STATE_STARTED = "1";

    public static final String VALUE_STATE_SUSPENDED = "2";

    public static final String VALUE_STATE_CANCELLED = "3";

    public static final String VALUE_STATE_ABORTED = "4";

    public static final String VALUE_STATE_COMPLETING = "5";

    public static final String VALUE_STATE_COMPLETED = "6";

    public static final String VALUE_STATE_ERROR = "7";

    public static final String VALUE_STATE_TO_MIGRATE = "8";

    public static final String VALUE_STATE_READY_FOR_MIGRATION = "9";

    public static final String VALUE_STATE_MIGRATING = "10";

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // FILTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final String FILTER_USER_ID = "user_id";

    public static final String FILTER_SUPERVISOR_ID = "supervisor_id";

    public static final String FILTER_TEAM_MANAGER_ID = "team_manager_id";

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GETTERS AND SETTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // DEPLOYS

    public UserItem getStartedByUser() {
        return new UserItem(getDeploy(ATTRIBUTE_STARTED_BY_USER_ID));
    }

    public ProcessItem getProcess() {
        return new ProcessItem(getDeploy(ATTRIBUTE_PROCESS_ID));
    }

    // GETTERS

    @Override
    public Date getLastUpdateDate() {
        return getAttributeValueAsDate(ATTRIBUTE_LAST_UPDATE_DATE);
    }

    public String getState() {
        return getAttributeValue(ATTRIBUTE_STATE);
    }

    public Date getStartDate() {
        return getAttributeValueAsDate(ATTRIBUTE_START_DATE);
    }

    public APIID getStartedByUserId() {
        return getAttributeValueAsAPIID(ATTRIBUTE_STARTED_BY_USER_ID);
    }

    public Date getEndDate() {
        return getAttributeValueAsDate(ATTRIBUTE_END_DATE);
    }

    public APIID getProcessId() {
        return getAttributeValueAsAPIID(ATTRIBUTE_PROCESS_ID);
    }

    // SETTERS

    @Override
    public void setId(final Long id) {
        setAttribute(ATTRIBUTE_ID, id);
    }

    @Override
    public void setId(final String id) {
        setAttribute(ATTRIBUTE_ID, id);
    }

    @Override
    public void setLastUpdateDate(final String date) {
        setAttribute(ATTRIBUTE_LAST_UPDATE_DATE, date);
    }

    @Override
    public void setLastUpdateDate(final Date date) {
        setAttribute(ATTRIBUTE_LAST_UPDATE_DATE, date);
    }

    public void setState(final String state) {
        setAttribute(ATTRIBUTE_STATE, state);
    }

    /**
     * 
     * @param date
     *            Must be SQL formated date
     */
    public void setStartDate(final String date) {
        setAttribute(ATTRIBUTE_START_DATE, date);
    }

    public void setStartDate(final Date date) {
        setAttribute(ATTRIBUTE_START_DATE, date);
    }

    public void setStartedByUserId(final Long userId) {
        setAttribute(ATTRIBUTE_STARTED_BY_USER_ID, userId);
    }

    public void setStartedByUserId(final APIID userId) {
        setAttribute(ATTRIBUTE_STARTED_BY_USER_ID, userId);
    }

    public void setStartedByUserId(final String userId) {
        setAttribute(ATTRIBUTE_STARTED_BY_USER_ID, userId);
    }

    public void setEndDate(final String date) {
        setAttribute(ATTRIBUTE_END_DATE, date);
    }

    public void setEndDate(final Date date) {
        setAttribute(ATTRIBUTE_END_DATE, date);
    }

    public void setProcessId(final Long processId) {
        setProcessId(APIID.makeAPIID(processId));
    }

    public void setProcessId(final String processId) {
        setAttribute(ATTRIBUTE_PROCESS_ID, processId);
    }

    public void setProcessId(final APIID processId) {
        setAttribute(ATTRIBUTE_PROCESS_ID, processId);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // UTILS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public ItemDefinition getItemDefinition() {
        return new CaseDefinition();
    }

}
