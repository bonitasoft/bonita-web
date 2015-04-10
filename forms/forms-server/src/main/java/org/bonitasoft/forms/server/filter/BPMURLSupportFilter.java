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
 *
 */
package org.bonitasoft.forms.server.filter;

import static org.bonitasoft.web.rest.server.framework.utils.SearchOptionsBuilderUtil.computeIndex;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.CharEncoding;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfo;
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfoSearchDescriptor;
import org.bonitasoft.engine.search.Order;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.forms.client.model.exception.ForbiddenProcessAccessException;
import org.bonitasoft.forms.server.api.FormAPIFactory;
import org.bonitasoft.forms.server.api.IFormWorkflowAPI;
import org.bonitasoft.forms.server.exception.NoCredentialsInSessionException;

/**
 * This filter transform the regular URL parameters into Hash parameters, with a generated formID.
 *
 * @author Chong Zhao
 */
public class BPMURLSupportFilter implements Filter {

    /**
     * the URL param for the form locale to use
     */
    protected static final String FORM_LOCALE_URL_PARAM = "locale";

    /**
     * the URL param for the token
     */
    public static final String TOKEN_URL_PARAM = "token";

    /**
     * user's domain URL parameter
     */
    public static final String TENANT_PARAM = "tenant";

    /**
     * user XP's UI mode parameter
     */
    public static final String UI_MODE_PARAM = "ui";

    /**
     * Theme parameter
     */
    public static final String THEME_PARAM = "theme";

    /**
     * ui parameter
     */
    public static final String UI_PARAM = "ui";

    /**
     * the GWT debug mode param
     */
    public static final String GWT_DEBUG_PARAM = "gwt.codesvr";

    /**
     * autologin parameter
     */
    public static final String AUTO_LOGIN_PARAM = "autologin";

    /**
     * old pattern task parameter
     */
    public static final String TASK_PARAM = "task";

    /**
     * old pattern process parameter
     */
    public static final String PROCESS_PARAM = "process";

    /**
     * old pattern process parameter
     */
    public static final String PROCESS_NAME_PARAM = "processName";

    /**
     * old pattern instance parameter
     */
    public static final String INSTANCE_PARAM = "instance";

    /**
     * old pattern recap parameter
     */
    public static final String RECAP_PARAM = "recap";

    /**
     * form type: entry
     */
    public static final String ENTRY_FORM = "entry";

    /**
     * form type: view
     */
    public static final String VIEW_FORM = "view";

    /**
     * form type: recap
     */
    public static final String OVERVIEW_FORM = "recap";

    /**
     * form id parameter
     */
    public static final String FORM_ID_PARAM = "form";

    /**
     * form id separator
     */
    public static final String FORM_ID_SEPARATOR = "$";

    /**
     * application url prefix
     */
    public static final String APPLICATION_PREFIX = "/application";

    /**
     * console home page keyword
     */
    public static final String HOMEPAGE = "homepage";

    /**
     * console form mode
     */
    public static final String UI_FORM = "form";

    /**
     * The engine API session param key name
     */
    public static final String API_SESSION_PARAM_KEY = "apiSession";

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(BPMURLSupportFilter.class.getName());

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
    }

    @Override
    @SuppressWarnings("unchecked")
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        try {
            final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            final HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            final Map<String, String[]> parameters = new HashMap<String, String[]>(httpServletRequest.getParameterMap());
            final List<String> supportedParameterKeysList = Arrays.asList(FORM_LOCALE_URL_PARAM, TENANT_PARAM, UI_MODE_PARAM, THEME_PARAM,
                    GWT_DEBUG_PARAM, TOKEN_URL_PARAM, AUTO_LOGIN_PARAM);
            final Set<String> parameterKeys = new HashSet<String>(parameters.keySet());
            parameterKeys.removeAll(supportedParameterKeysList);
            if (!parameterKeys.isEmpty()) {
                if (parameterKeys.contains(PROCESS_NAME_PARAM)) {
                    final long processDefinitionID = getProcessIDOfLatestVersion(parameters.get(PROCESS_NAME_PARAM), httpServletRequest);
                    if (processDefinitionID != -1) {
                        final String[] parameterValues = new String[1];
                        parameterValues[0] = String.valueOf(processDefinitionID);
                        parameters.put(PROCESS_PARAM, parameterValues);
                    }
                    parameters.remove(PROCESS_NAME_PARAM);
                }
                if (!parameterKeys.contains(FORM_ID_PARAM) && isForm(httpServletRequest)) {
                    final String formID[] = getFormIDFromRegularURLParameters(parameters, httpServletRequest);
                    if (formID != null && formID.length > 0) {
                        parameters.put(FORM_ID_PARAM, formID);
                    }
                }
                final StringBuilder hashString = new StringBuilder();
                final StringBuilder queryString = new StringBuilder();
                for (final Entry<String, String[]> parameter : parameters.entrySet()) {
                    final String key = parameter.getKey();
                    final String[] values = parameter.getValue();
                    if (supportedParameterKeysList.contains(key)) {
                        buildQueryString(queryString, key, values);
                    } else {
                        buildQueryString(hashString, key, values);
                    }
                }
                final StringBuilder redirectionURL = new StringBuilder();
                redirectionURL.append(httpServletRequest.getRequestURI());
                if (queryString.length() > 0) {
                    redirectionURL.append("?");
                    redirectionURL.append(queryString);
                }
                if (hashString.length() > 0) {
                    redirectionURL.append("#");
                    redirectionURL.append(hashString);
                }
                final String encodeRedirectURL = httpServletResponse.encodeRedirectURL(redirectionURL.toString());
                httpServletResponse.sendRedirect(encodeRedirectURL);
            } else {
                response.setContentType(CharEncoding.UTF_8);
                filterChain.doFilter(request, response);
            }
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "Error while parsing the regular parameters into hash parameters.");
            throw new ServletException(e);
        }
    }

    protected void buildQueryString(final StringBuilder queryString, final String key, final String[] values) throws UnsupportedEncodingException {
        if (queryString.length() > 0) {
            queryString.append("&");
        }
        queryString.append(key);
        queryString.append("=");
        if (values.length == 1) {
            queryString.append(URLEncoder.encode(values[0], CharEncoding.UTF_8));
        } else if (values.length > 1) {
            final StringBuilder valuesList = new StringBuilder();
            for (final String value : values) {
                if (valuesList.length() > 0) {
                    valuesList.append(",");
                }
                valuesList.append(URLEncoder.encode(value, CharEncoding.UTF_8));
            }
            queryString.append(valuesList);
        }
    }

    /**
     * Get the form id for any form.
     *
     * @param parameters
     *            The regular parameters of current URL.
     * @param request
     *            The current http servlet request.
     * @return formID
     */
    protected String[] getFormIDFromRegularURLParameters(final Map<String, String[]> parameters, final HttpServletRequest request) {
        final String taskIDStr[] = parameters.get(TASK_PARAM);
        final String processIDStr[] = parameters.get(PROCESS_PARAM);
        final String instanceIDStr[] = parameters.get(INSTANCE_PARAM);
        final String recapParam[] = parameters.get(RECAP_PARAM);
        String formType = null;
        final String formID[] = new String[1];
        boolean isRecap = false;
        if (recapParam != null) {
            isRecap = Boolean.parseBoolean(recapParam[0]);
        }
        final StringBuffer tempFormID = new StringBuffer();
        try {
            final APISession session = getAPISession(request);
            if (taskIDStr != null) {
                final IFormWorkflowAPI workflowAPI = FormAPIFactory.getFormWorkflowAPI();
                final long activityInstanceID = Long.parseLong(taskIDStr[0]);
                final String activityDefinitionUUID = workflowAPI.getActivityDefinitionUUIDFromActivityInstanceID(session, activityInstanceID);

                if (isRecap) {
                    formType = OVERVIEW_FORM;
                } else {
                    final boolean isTaskReady = workflowAPI.isTaskReady(session, activityInstanceID);
                    formType = isTaskReady ? ENTRY_FORM : VIEW_FORM;
                }
                tempFormID.append(activityDefinitionUUID);
                tempFormID.append(FORM_ID_SEPARATOR);
                tempFormID.append(formType);

            } else if (processIDStr != null) {
                tempFormID.append(processIDStr[0]);
                tempFormID.append(FORM_ID_SEPARATOR);
                tempFormID.append(ENTRY_FORM);

            } else if (instanceIDStr != null) {
                final IFormWorkflowAPI workflowAPI = FormAPIFactory.getFormWorkflowAPI();
                final long processInstanceID = Long.parseLong(instanceIDStr[0]);
                final long processDefinitionID = workflowAPI.getProcessDefinitionIDFromProcessInstanceID(session, processInstanceID);
                tempFormID.append(processDefinitionID);
                tempFormID.append(FORM_ID_SEPARATOR);
                formType = isRecap ? OVERVIEW_FORM : VIEW_FORM;
                tempFormID.append(formType);

            } else {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, "No regular variables available in the URL for generating form id.");
                }
            }
            formID[0] = tempFormID.toString();
            return formID;

        } catch (final Exception e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, "Cannot get the formID with current URL parameters.", e);
            }
            return null;

        }
    }

    /**
     * Get the latest version of a given process name.
     *
     * @param processName
     *            A regular URL parameter
     * @param request
     *            The current http servlet request.
     * @return the ID of the latest version of the process
     * @throws ServletException
     */
    protected long getProcessIDOfLatestVersion(final String[] processName, final HttpServletRequest request) throws ServletException {
        String errorMessage = null;
        long processUUID = -1;
        try {
            final APISession session = getAPISession(request);
            final long userId = session.getUserId();
            final ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(session);
            final SearchOptionsBuilder builder = buildSearchOptions(0, 100, ProcessDeploymentInfoSearchDescriptor.DEPLOYMENT_DATE + " DESC", null);
            builder.filter(ProcessDeploymentInfoSearchDescriptor.NAME, processName[0]);
            final SearchResult<ProcessDeploymentInfo> deploymentInfoResult = processAPI.searchProcessDeploymentInfos(userId, builder.done());
            if (deploymentInfoResult != null && deploymentInfoResult.getCount() > 0) {
                processUUID = deploymentInfoResult.getResult().get(0).getProcessId();
            } else {
                errorMessage = "Access denied: you do not have the privileges to see this page.";
                throw new ForbiddenProcessAccessException();
            }
            return processUUID;
        } catch (final Exception e) {
            if (errorMessage == null) {
                errorMessage = "Error while getting the ID of the latest version process -- " + processName;
            }
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new ServletException(errorMessage, e);
        }
    }

    /**
     * Is console view mode
     *
     * @param request
     * @return isConsole flag
     */
    protected boolean isForm(final HttpServletRequest request) {
        final String ui = request.getParameter(UI_PARAM);
        return UI_FORM.equals(ui);
    }

    /**
     * Retrieve the API session from the HTTP session
     *
     * @param request
     *            the HTTP request
     * @return the tenantID
     * @throws NoCredentialsInSessionException
     */
    protected APISession getAPISession(final HttpServletRequest request) throws NoCredentialsInSessionException {
        final HttpSession session = request.getSession();
        // FIXME: Retrieve the API session in the user object
        final APISession apiSession = (APISession) session.getAttribute(API_SESSION_PARAM_KEY);
        if (apiSession == null) {
            final String errorMessage = "There is API session in the HTTP session.";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage);
            }
            throw new NoCredentialsInSessionException(errorMessage);
        }
        return apiSession;
    }

    /**
     * build SearchOptionsBuilder
     *
     * @param pageIndex
     * @param numberOfResults
     * @param sort
     * @param search
     * @return SearchOptionsBuilder object
     */
    protected SearchOptionsBuilder buildSearchOptions(final int pageIndex, final int numberOfResults, final String sort, final String search) {
        final SearchOptionsBuilder builder = new SearchOptionsBuilder(computeIndex(pageIndex, numberOfResults), numberOfResults);
        if (sort != null) {
            final String[] order = sort.split(" ");
            if (order.length == 2) {
                builder.sort(order[0], Order.valueOf(order[1].toUpperCase()));
            }
        }
        if (search != null && !search.isEmpty()) {
            builder.searchTerm(search);
        }
        return builder;
    }

    @Override
    public void destroy() {
    }
}
