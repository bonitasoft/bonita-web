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
package org.bonitasoft.console.client.admin.bpm.task.action;

import java.util.List;

import org.bonitasoft.console.client.user.task.model.TaskAPI;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.action.ActionOnItemIds;

/**
 * @author Vincent Elcrin
 * 
 */
public class AssignTaskAndHistoryBackAction extends ActionOnItemIds {

    @Override
    protected void execute(List<APIID> taskIds) {
        TaskAPI.assign(taskIds, APIID.makeAPIID(this.getParameter("user_id")), new Action() {

            @Override
            public void execute() {
                ViewController.refreshCurrentPage();
                ViewController.closePopup();

            }
        });
    }

}
