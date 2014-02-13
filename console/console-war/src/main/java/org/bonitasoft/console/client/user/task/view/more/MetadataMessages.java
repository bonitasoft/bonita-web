/**
 * Copyright (C) 2014 BonitaSoft S.A.
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

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

public class MetadataMessages {

    String app_title() {
        return _("The app responsible for the creation of this task");
    }

    String app_label() {
        return _("Apps");
    }

    String state_title() {
        return _("The state of the task");
    }

    String state_label() {
        return _("State");
    }

    String app_version_title() {
        return _("Version of the app");
    }

    String app_version_label() {
        return _("Apps version");
    }

    String case_id_title() {
        return _("The id of the related case");
    }

    String case_id_label() {
        return _("Case");
    }

    String priority_title() {
        return _("The priority of the task");
    }

    String priority_label() {
        return _("Priority");
    }

    String assigned_to_title() {
        return _("The user name of the user to which the task is assigned");
    }

    String assigned_to_label() {
        return _("Assigned to");
    }

    String last_update_date_title() {
        return _("The date of the last modification");
    }

    String last_update_date_label() {
        return _("Last update date");
    }

    String assigned_date_title() {
        return _("The date when while the task has been assigned");
    }

    String assigned_date_label() {
        return _("Assigned date");
    }

    String due_date_title() {
        return _("The date while the task must be finished");
    }

    String due_date_label() {
        return _("Due date");
    }

    String no_description() {
        return _("No description.");
    }

}
