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
package org.bonitasoft.console.common.server.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
     * Theme parameter
     */
    public static final String THEME_PARAM = "theme";

    /**
     * the GWT debug mode param
     */
    public static final String GWT_DEBUG_PARAM = "gwt.codesvr";

    /**
     * Ticket parameter for SSO
     */
    public static final String TICKET_PARAM = "ticket";

    /**
     * console home page keyword
     */
    public static final String HOMEPAGE = "homepage";

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
            final List<String> supportedParameterKeysList = Arrays.asList(FORM_LOCALE_URL_PARAM, TENANT_PARAM, THEME_PARAM,
                    GWT_DEBUG_PARAM, TOKEN_URL_PARAM, TICKET_PARAM);
            final Set<String> parameterKeys = new HashSet<String>(parameters.keySet());
            parameterKeys.removeAll(supportedParameterKeysList);
            if (!parameterKeys.isEmpty()) {
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
                response.setContentType(StandardCharsets.UTF_8.name());
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
            queryString.append(URLEncoder.encode(values[0], StandardCharsets.UTF_8.name()));
        } else if (values.length > 1) {
            final StringBuilder valuesList = new StringBuilder();
            for (final String value : values) {
                if (valuesList.length() > 0) {
                    valuesList.append(",");
                }
                valuesList.append(URLEncoder.encode(value, StandardCharsets.UTF_8.name()));
            }
            queryString.append(valuesList);
        }
    }

    @Override
    public void destroy() {
    }
}
