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

import org.bonitasoft.console.client.common.component.button.MoreButton;
import org.bonitasoft.console.client.user.task.action.TaskClaimAction;
import org.bonitasoft.console.client.user.task.action.TaskRelaseAction;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.component.Button;
import org.bonitasoft.web.toolkit.client.ui.component.button.ButtonAction;
import org.bonitasoft.web.toolkit.client.ui.component.button.ButtonPrimaryAction;

/**
 * @author Vincent Elcrin
 * 
 */
public class TaskButtonFactory {

    public Button createPerformTaskButton(Action performTask) {
        return new ButtonPrimaryAction("btn-perform", _("Do it"), _("Done"), performTask);
    }

    public Button createMoreDetailsButton(final Action more) {
        return new MoreButton(_("Show more details about this task"), more);
    }

    public ButtonAction createUnassignedButton(TaskRelaseAction taskRelaseAction) {
        return new ButtonAction("btn-unassign", _("Release"), _("Unassign this task. Other allowed users will see it"),
                taskRelaseAction);
    }

    public ButtonAction createClaimButton(TaskClaimAction taskClaimAction) {
        return new ButtonAction("btn-assigntome", _("Take"),
                _("Assign this task to me. Other allowed users will no longer see it"), taskClaimAction);
    }
    
    public Button createRefreshButton(Action tableRefreshAction) {
        Button btn = new Button("btn-refresh", "",
                _("Refresh this table"), tableRefreshAction);

        btn.addClass("btn-refresh");
        return btn;
    }
}
