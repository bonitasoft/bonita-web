/*******************************************************************************
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 *      BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 *      or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.console.client.admin.bpm.task.action;

import java.util.List;

import org.bonitasoft.console.client.user.task.model.TaskAPI;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.ui.action.ActionOnItemIds;


/**
 * @author Fabio Lombardi
 *
 */
public class UnassignAndAssignTask extends ActionOnItemIds {

    @Override
    protected void execute(List<APIID> taskIds) {
        final AssignTaskAndHistoryBackAction action = new AssignTaskAndHistoryBackAction();
        TaskAPI.release(taskIds, action);
        action.setParameters(getParameters());
        action.execute();
    }

    
}
