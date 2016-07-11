/**
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.console.common.server;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

/**
 * @author Fabio Lombardi
 *
 */

// DO NOT DELETE: Even if it could seem useless, we use this class in order to add profile description values inside Crowdin
public class ProfileTypesI18N {
    // /////////////////////////////////////////
    // // USER
    // /////////////////////////////////////////
    protected static final String USER = _("User");

    protected static final String USER_DESCRIPTION = _("The user can view and perform tasks and can start a new case of a process.");

    // /////////////////////////////////////////
    // // ADMINISTRATOR
    // /////////////////////////////////////////
    protected static final String ADMINISTRATOR = _("Administrator");

    protected static final String ADMINISTRATOR_DESCRIPTION = _("The administrator can install a process, manage the organization, and handle some errors (for example, by replaying a task).");

    // /////////////////////////////////////////
    // // PROCESS MANAGER
    // /////////////////////////////////////////
    protected static final String PROCESS_MANAGER = _("Process manager");

    protected static final String PROCESS_MANAGER_DESCRIPTION = _("The Process manager can supervise designated processes, and manage cases and tasks of those processes.");
}

