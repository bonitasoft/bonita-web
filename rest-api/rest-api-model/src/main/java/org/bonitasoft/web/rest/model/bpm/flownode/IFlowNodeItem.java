/**
 * Copyright (C) 2013 BonitaSoft S.A.
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
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.template.ItemHasDualDescription;
import org.bonitasoft.web.toolkit.client.data.item.template.ItemHasDualName;
import org.bonitasoft.web.toolkit.client.data.item.template.ItemHasUniqueId;

/**
 * @author Séverin Moussel
 * 
 */
public interface IFlowNodeItem extends IItem, ItemHasUniqueId, ItemHasDualName, ItemHasDualDescription {

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ATTRIBUTES
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static String ATTRIBUTE_PROCESS_ID = "processId";

    public static String ATTRIBUTE_CASE_ID = "caseId";

    public static String ATTRIBUTE_STATE = "state";

    public static String ATTRIBUTE_TYPE = "type";

    public static String ATTRIBUTE_EXECUTED_BY_USER_ID = "executedBy";

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ATTRIBUTES VALUES
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final String VALUE_STATE_FAILED = "failed";

    public static final String VALUE_STATE_READY = "ready";

    public static final String VALUE_STATE_COMPLETED = "completed";

    public static final String VALUE_STATE_SKIPPED = "skipped";

    public static final String VALUE_STATE_ABORTED = "aborted";

    // TODO replay is the SP feature only
    public static final String VALUE_STATE_REPLAY = "replay";

    public static final String VALUE_TYPE_TASK = "TASK";

    public static final String VALUE_TYPE_AUTOMATIC_TASK = "AUTOMATIC_TASK";

    public static final String VALUE_ICON_AUTOMATIC_TASK = "/default/icon_automaticTask.png";

    public static final String VALUE_TYPE_USER_TASK = "USER_TASK";

    public static final String VALUE_TYPE_MANUAL_TASK = "MANUAL_TASK";

    public static final String VALUE_TYPE_CALL_ACTIVITY = "CALL_ACTIVITY";

    public static final String VALUE_ICON_CALL_ACTIVITY = "/default/icon_call_activity_task.png";

    public static final String VALUE_TYPE_LOOP_ACTIVITY = "LOOP_ACTIVITY";

    public static final String VALUE_ICON_LOOP_ACTIVITY = "/default/icon_loop_task.png";

    public static final String VALUE_TYPE_MULTI_INSTANCE_ACTIVITY = "MULTI_INSTANCE_ACTIVITY";

    public static final String VALUE_ICON_MULTI_INSTANCE_ACTIVITY = "/default/icon_multi_instance_task.png";

    public static final String VALUE_TYPE_SUB_PROCESS_ACTIVITY = "SUB_PROCESS_ACTIVITY";

    public static final String VALUE_TYPE_GATEWAY = "GATEWAY";

    public static final String VALUE_TYPE_START_EVENT = "EVENT";

    public static final String VALUE_TYPE_INTERMEDIATE_CATCH_EVENT = "INTERMEDIATE_CATCH_EVENT";

    public static final String VALUE_TYPE_BOUNDARY_EVENT = "BOUNDARY_EVENT";

    public static final String VALUE_TYPE_INTERMEDIATE_THROW_EVENT = "";

    public static final String VALUE_TYPE_END_EVENT = "END_EVENT";

    public void setProcessId(final APIID id);

    public void setProcessId(final String id);

    public void setProcessId(final Long id);

    public APIID getProcessId();

    public void setCaseId(final APIID id);

    public void setCaseId(final String id);

    public void setCaseId(final Long id);

    public APIID getCaseId();

    public void setState(final String state);

    public String getState();

    public void setType(final String type);

    public String getType();

    public void setExecutedByUserId(final APIID id);

    public void setExecutedByUserId(final String id);

    public void setExecutedByUserId(final Long id);

    public APIID getExecutedByUserId();

    public UserItem getExecutedByUser();

    public ProcessItem getProcess();

    public CaseItem getCase();

    public boolean isUserTask();

    public boolean isAutomaticTask();

    public boolean isBoundaryEvent();

    public boolean isCallActivity();

    public boolean isEndEvent();

    public boolean isGateway();

    public boolean isHumanTask();

    public boolean isActivity();

    public boolean isIntermediateCatchEvent();

    public boolean isIntermediateThrowEvent();

    public boolean isLoopActivity();

    public boolean isManualTask();

    public boolean isStartEvent();

    public boolean isArchived();

    public boolean isAborted();

    /**
     * @return
     */
    boolean isSubProcessActivity();

    /**
     * @return
     */
    boolean isMultiInsatnceActivity();

    public String ensureName();

    public String ensureDescription();

}
