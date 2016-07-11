/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.console.common.server.utils;

import java.util.Set;

import javax.servlet.http.HttpSession;

import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.user.User;

/**
 * @author Ruiheng.Fan
 * @author Baptiste Mesta
 */
public class SessionUtil {

    /**
     * the session param for the engine API session
     */
    public static final String API_SESSION_PARAM_KEY = "apiSession";

    /**
     * the session param for the user
     */
    public static final String USER_SESSION_PARAM_KEY = "user";

    /**
     * the session param for the username
     */
    public static final String USERNAME_SESSION_PARAM = "username";

    /**
     * the session param for the permissions
     */
    public static final String PERMISSIONS_SESSION_PARAM_KEY = "permissions";

    public static void sessionLogin(final User user, final APISession apiSession, final Set<String> permissions, final HttpSession session) {
        session.setAttribute(USERNAME_SESSION_PARAM, user.getUsername());
        session.setAttribute(USER_SESSION_PARAM_KEY, user);
        session.setAttribute(API_SESSION_PARAM_KEY, apiSession);
        session.setAttribute(PERMISSIONS_SESSION_PARAM_KEY, permissions);
    }

    public static void sessionLogout(final HttpSession session) {
        session.removeAttribute(API_SESSION_PARAM_KEY);
        session.removeAttribute(USERNAME_SESSION_PARAM);
        session.removeAttribute(USER_SESSION_PARAM_KEY);
        session.removeAttribute(PERMISSIONS_SESSION_PARAM_KEY);
        session.invalidate();
    }
}
