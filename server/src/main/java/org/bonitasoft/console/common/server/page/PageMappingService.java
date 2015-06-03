/**
 * Copyright (C) 2015 BonitaSoft S.A.
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
package org.bonitasoft.console.common.server.page;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.utils.SessionUtil;
import org.bonitasoft.engine.api.PageAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.page.AuthorizationRuleConstants;
import org.bonitasoft.engine.page.PageURL;
import org.bonitasoft.engine.page.URLAdapterConstants;
import org.bonitasoft.engine.session.APISession;

public class PageMappingService {

    protected static final String PROCESS_DEPLOY = "process_deploy";

    public PageReference getPage(final HttpServletRequest request, final APISession apiSession, final String mappingKey, final Locale locale,
            final boolean executeAuthorizationRules) throws BonitaException {
        final Map<String, Serializable> context = new HashMap<>();
        context.put(URLAdapterConstants.QUERY_PARAMETERS, (Serializable) request.getParameterMap());
        context.put(AuthorizationRuleConstants.IS_ADMIN, isLoggedUserAdmin(request));
        context.put(URLAdapterConstants.LOCALE, locale.toString());
        context.put(URLAdapterConstants.CONTEXT_PATH, request.getContextPath());
        final PageAPI pageAPI = getPageAPI(apiSession);
        final PageURL pageURL = pageAPI.resolvePageOrURL(mappingKey, context, executeAuthorizationRules);
        return new PageReference(pageURL.getPageId(), pageURL.getUrl());
    }

    protected boolean isLoggedUserAdmin(final HttpServletRequest request) {
        final HttpSession session = request.getSession();
        @SuppressWarnings("unchecked")
        final Set<String> userPermissions = (Set<String>) session.getAttribute(SessionUtil.PERMISSIONS_SESSION_PARAM_KEY);
        return userPermissions.contains(PROCESS_DEPLOY);
    }

    protected PageAPI getPageAPI(final APISession apiSession) throws BonitaHomeNotSetException, ServerAPIException,
            UnknownAPITypeException {
        return TenantAPIAccessor.getCustomPageAPI(apiSession);
    }
}
