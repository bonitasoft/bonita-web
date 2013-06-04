/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 */
package org.bonitasoft.forms.server.filter;

import java.io.IOException;
import java.util.Arrays;
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
 * This filter transform the URL parameters into Hash parameters
 *
 * @author Anthony Birembaut
 */
@Deprecated
public class ParametersToHashFilter implements Filter {
    
    /**
     * the URL param for the locale to use
     */
    protected static final String LOCALE_URL_PARAM = "locale";
    
    /**
     * user's domain URL parameter
     */
    public static final String DOMAIN_PARAM = "domain";
    
    /**
     * user XP's UI mode parameter
     */
    public static final String UI_MODE_PARAM = "ui";
    
    /**
     * the GWT debug mode param
     */
    public static final String GWT_DEBUG_PARAM = "gwt.codesvr";
	
    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(ParametersToHashFilter.class.getName());

    public void init(final FilterConfig filterConfig) throws ServletException {
    }

    @SuppressWarnings("unchecked")
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        try {
            final HttpServletRequest httpServletRequest = (HttpServletRequest)request;
            final HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        	final Map<String, String[]> parameters = httpServletRequest.getParameterMap();
        	final List<String> supportedParameterKeysList = Arrays.asList(LOCALE_URL_PARAM, DOMAIN_PARAM, UI_MODE_PARAM, GWT_DEBUG_PARAM);
        	final Set<String> parameterKeys = new HashSet<String>(parameters.keySet());
        	parameterKeys.removeAll(supportedParameterKeysList);
        	if (!parameterKeys.isEmpty()) {
            	final StringBuilder hashString = new StringBuilder();
                final StringBuffer requestURL = httpServletRequest.getRequestURL();
                final int anchorIndex = requestURL.indexOf("#");
                if (anchorIndex > 0) {
                    hashString.append(requestURL.substring(anchorIndex + 1));
                }
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
                httpServletResponse.sendRedirect(redirectionURL.toString());
            }
        } catch (final Exception e) {
            LOGGER.log(Level.SEVERE, "Error while setting the token in session");
            throw new ServletException(e);
        }
        filterChain.doFilter(request, response);
    }
    
    protected void buildQueryString(final StringBuilder queryString, final String key, final String[] values) {
        if (queryString.length() > 0) {
            queryString.append("&");
        }
        queryString.append(key);
        queryString.append("=");
        if (values.length == 1) {
            queryString.append(values[0]);
        } else if (values.length > 1) {
            final StringBuilder valuesList = new StringBuilder();
            for (final String value : values) {
                if (valuesList.length() > 0) {
                    valuesList.append(",");
                }
                valuesList.append(value);
            }
            queryString.append(valuesList);
        }
    }
    
    public void destroy() {
    }
}
