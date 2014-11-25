/**
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation
 * version 2.1 of the License.
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth
 * Floor, Boston, MA 02110-1301, USA.
 **/
package org.bonitasoft.forms.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.login.LoginManager;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.forms.client.model.exception.SessionTimeoutException;
import org.bonitasoft.forms.server.api.FormAPIFactory;
import org.bonitasoft.forms.server.api.IFormDefinitionAPI;
import org.bonitasoft.forms.server.exception.ApplicationFormDefinitionNotFoundException;
import org.bonitasoft.forms.server.exception.FormNotFoundException;
import org.bonitasoft.forms.server.exception.FormServiceProviderNotFoundException;
import org.bonitasoft.forms.server.exception.InvalidFormDefinitionException;
import org.bonitasoft.forms.server.exception.NoCredentialsInSessionException;
import org.bonitasoft.forms.server.provider.FormServiceProvider;
import org.bonitasoft.forms.server.provider.impl.util.FormServiceProviderFactory;
import org.bonitasoft.forms.server.provider.impl.util.FormServiceProviderUtil;
import org.bonitasoft.forms.server.util.LocaleUtil;
import org.bonitasoft.web.toolkit.client.common.json.JSonSerializer;
import org.w3c.dom.Document;

/**
 * Servlet allowing retrieve the list of forms and pre-load a form in the cache
 *
 * @author Anthony Birembaut
 */
public class FormsCacheServlet extends HttpServlet {

    /**
     * UID
     */
    private static final long serialVersionUID = 5209416978166786895L;

    /**
     * locale Util
     */
    protected final LocaleUtil localeUtil = new LocaleUtil();

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(FormsCacheServlet.class.getName());

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

        final String processIDStr = request.getParameter(FormServiceProviderUtil.PROCESS_UUID);

        if (processIDStr == null) {
            final String errorMessage = "Error while using the servlet FormsCacheServlet to get a list of forms: the parameter "
                    + FormServiceProviderUtil.PROCESS_UUID
                    + " is undefined.";
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, errorMessage);
            }
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, errorMessage);
        } else {
            final Map<String, Object> urlContext = new HashMap<String, Object>();
            urlContext.put(FormServiceProviderUtil.PROCESS_UUID, processIDStr);
            final String localeStr = localeUtil.getLocale(request);
            final Locale userLocale = localeUtil.resolveLocale(localeStr);

            try {
                final Map<String, Object> context = initContext(request, urlContext, userLocale);
                final IFormDefinitionAPI definitionAPI = getDefinitionAPI(request, context, localeStr);
                final List<String> formIDs = definitionAPI.getFormsList(context);
                response.setContentType("text/html");
                response.setCharacterEncoding("UTF-8");
                final PrintWriter printWriter = response.getWriter();
                printWriter.print(JSonSerializer.serializeCollection(formIDs));
                printWriter.close();
            } catch (final FormNotFoundException e) {
                final String errorMessage = "Cannot find any form definition for process " + processIDStr;
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.log(Level.INFO, errorMessage, e);
                }
                response.sendError(HttpServletResponse.SC_NOT_FOUND, errorMessage);
            } catch (final NoCredentialsInSessionException e) {
                final String errorMessage = "Cannot find the API session in the HTTP Session.";
                if (LOGGER.isLoggable(Level.FINEST)) {
                    LOGGER.log(Level.FINEST, errorMessage, e);
                }
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, errorMessage);
            } catch (final SessionTimeoutException e) {
                final String errorMessage = "The session has timed out.";
                if (LOGGER.isLoggable(Level.FINEST)) {
                    LOGGER.log(Level.FINEST, errorMessage, e);
                }
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, errorMessage);
            } catch (final Exception e) {
                final String errorMessage = "Error while using the servlet FormsCacheServlet to get a list of forms.";
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, errorMessage, e);
                }
                throw new ServletException(errorMessage);
            }
        }
    }

    @Override
    protected void doPut(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

        String[] pathInfo = null;
        if (request.getPathInfo() != null && request.getPathInfo().startsWith("/")) {
            pathInfo = request.getPathInfo().split("/");
        }
        if (pathInfo == null || pathInfo.length < 3) {
            final String errorMessage = "Error while using the servlet FormsCacheServlet to load a forms: a process ID and a form ID are expected in the Path of the URL";
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, errorMessage);
            }
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, errorMessage);
        } else {
            final String processIDStr = pathInfo[1];
            final String formID = pathInfo[2];
            final Map<String, Object> urlContext = new HashMap<String, Object>();
            urlContext.put(FormServiceProviderUtil.PROCESS_UUID, processIDStr);
            final String localeStr = localeUtil.getLocale(request);
            final Locale userLocale = localeUtil.resolveLocale(localeStr);
            try {
                final Map<String, Object> context = initContext(request, urlContext, userLocale);
                final IFormDefinitionAPI definitionAPI = getDefinitionAPI(request, context, localeStr);
                definitionAPI.cacheForm(formID, context);
            } catch (final FormNotFoundException e) {
                final String errorMessage = "Cannot find any form definition for process " + processIDStr;
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.log(Level.INFO, errorMessage, e);
                }
                response.sendError(HttpServletResponse.SC_NOT_FOUND, errorMessage);
            } catch (final ApplicationFormDefinitionNotFoundException e) {
                final String errorMessage = "Cannot find any form definition for process " + processIDStr + " and form " + formID;
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.log(Level.INFO, errorMessage, e);
                }
                response.sendError(HttpServletResponse.SC_NOT_FOUND, errorMessage);
            } catch (final NoCredentialsInSessionException e) {
                final String errorMessage = "Cannot find the API session in the HTTP Session.";
                if (LOGGER.isLoggable(Level.FINEST)) {
                    LOGGER.log(Level.FINEST, errorMessage, e);
                }
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, errorMessage);
            } catch (final SessionTimeoutException e) {
                final String errorMessage = "The session has timed out.";
                if (LOGGER.isLoggable(Level.FINEST)) {
                    LOGGER.log(Level.FINEST, errorMessage, e);
                }
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, errorMessage);
            } catch (final Exception e) {
                final String errorMessage = "Error while using the servlet FormsCacheServlet to get a list of forms.";
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, errorMessage, e);
                }
                throw new ServletException(errorMessage);
            }
        }
    }

    protected IFormDefinitionAPI getDefinitionAPI(final HttpServletRequest request, final Map<String, Object> context, final String localeStr)
            throws NoCredentialsInSessionException, FormServiceProviderNotFoundException, FormNotFoundException, IOException,
            InvalidFormDefinitionException, SessionTimeoutException {
        final long tenantID = getTenantID(request);
        final FormServiceProvider formServiceProvider = FormServiceProviderFactory.getFormServiceProvider(tenantID);
        final Document document = formServiceProvider.getFormDefinitionDocument(context);
        final Date deployementDate = formServiceProvider.getDeployementDate(context);
        final IFormDefinitionAPI definitionAPI = FormAPIFactory.getFormDefinitionAPI(tenantID, document, deployementDate, localeStr);
        return definitionAPI;
    }

    protected Map<String, Object> initContext(final HttpServletRequest request, final Map<String, Object> urlContext, final Locale userLocale)
            throws NoCredentialsInSessionException {
        final HttpSession httpSession = request.getSession();
        final APISession apiSession = (APISession) httpSession.getAttribute(LoginManager.API_SESSION_PARAM_KEY);
        if (apiSession != null) {
            final Map<String, Object> context = new HashMap<String, Object>();
            context.put(FormServiceProviderUtil.URL_CONTEXT, urlContext);
            context.put(FormServiceProviderUtil.LOCALE, userLocale);
            context.put(FormServiceProviderUtil.API_SESSION, apiSession);
            return context;
        } else {
            throw new NoCredentialsInSessionException();
        }
    }

    protected long getTenantID(final HttpServletRequest request) {
        final HttpSession httpSession = request.getSession();
        final APISession aAPISession = (APISession) httpSession.getAttribute(LoginManager.API_SESSION_PARAM_KEY);
        return aAPISession.getTenantId();
    }
}
