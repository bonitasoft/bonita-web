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
package org.bonitasoft.web.toolkit.server.servlet;

import org.bonitasoft.web.toolkit.client.common.CommonDateFormater;
import org.bonitasoft.web.toolkit.client.common.exception.api.*;
import org.bonitasoft.web.toolkit.client.common.json.JSonSerializer;
import org.bonitasoft.web.toolkit.client.data.item.Item;
import org.bonitasoft.web.toolkit.server.ServiceNotFoundException;
import org.bonitasoft.web.toolkit.server.ServletCall;
import org.bonitasoft.web.toolkit.server.utils.LocaleUtils;
import org.bonitasoft.web.toolkit.server.utils.ServerDateFormater;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n.LOCALE;

/**
 * @author Séverin Moussel
 * 
 */
public abstract class ToolkitHttpServlet extends HttpServlet {

    private static final long serialVersionUID = -8470006030459575773L;

    /**
     * Console logger
     */
    protected static final Logger LOGGER = Logger.getLogger(ToolkitHttpServlet.class.getName());

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CATCH ALL EXCEPTIONS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public ToolkitHttpServlet() {
        super();
        initializeToolkit();
    }

    /**
     * Initialize
     * 
     * @see HttpServlet#service(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
     */
    @Override
    protected final void service(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        try {
            super.service(req, resp);
        } catch (final Exception e) {
            catchAllExceptions(retrieveLowestAPIException(e), req, resp);
        }
    }

    private Throwable retrieveLowestAPIException(final Throwable e) {
        Throwable lowest = e;

        while (lowest.getCause() != null && lowest.getCause() instanceof APIException) {
            lowest = lowest.getCause();
        }

        return lowest;
    }

    /**
     * Output an exception in JSon.
     * 
     * @param e
     *            The exception to output
     * @param resp
     *            The response to fill
     * @param httpStatusCode
     *            The status code to return
     */
    protected final void outputException(final Throwable e, final HttpServletRequest req, final HttpServletResponse resp, final int httpStatusCode) {

        resp.setStatus(httpStatusCode);
        resp.setContentType("application/json;charset=UTF-8");

        try {
            final PrintWriter output = resp.getWriter();
            if(e instanceof APIException) {
                setLocalization((APIException) e, LocaleUtils.getUserLocale(req));
            }

            output.print(e == null ? "" : JSonSerializer.serialize(e));
            output.flush();
        } catch (final Exception e2) {
            throw new APIException(e2);
        }
    }

    private void setLocalization(APIException localizable, String locale) {
        if(locale != null && !locale.isEmpty()) {
            localizable.setLocale(LOCALE.valueOf(locale));
        }
    }

    /**
     * Initialize the toolkit
     */
    protected void initializeToolkit() {
        Item.setApplyInputModifiersByDefault(false);
        Item.setApplyValidatorsByDefault(false);
        Item.setApplyOutputModifiersByDefault(false);
        Item.setApplyValidatorMandatoryByDefault(false);

        CommonDateFormater.setDateFormater(new ServerDateFormater());
    }

    /**
     * @param req
     *            The request called
     * @param resp
     *            The response to send
     */
    protected void catchAllExceptions(final Throwable exception, final HttpServletRequest req, final HttpServletResponse resp) {
        if (exception instanceof APIMethodNotAllowedException) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, exception.getMessage(), exception);
            }
            outputException(exception, req, resp, HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        } else if (exception instanceof APINotFoundException) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, exception.getMessage(), exception);
            }
            outputException(exception, req, resp, HttpServletResponse.SC_NOT_FOUND);
        } else if (exception instanceof ServiceNotFoundException) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, exception.getMessage(), exception);
            }
            outputException(exception, req, resp, HttpServletResponse.SC_NOT_FOUND);
        } else if (exception instanceof APIItemNotFoundException) {
            outputException(null, req, resp, HttpServletResponse.SC_NOT_FOUND);
        } else if (exception instanceof APIForbiddenException) {
            outputException(exception, req, resp, HttpServletResponse.SC_FORBIDDEN);
        } else {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, exception.getMessage(), exception);
            }
            outputException(exception, req, resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // DEFINE SERVLET
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected abstract ServletCall defineServletCall(final HttpServletRequest req, final HttpServletResponse resp);

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // INITIATE CALL
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected final void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        defineServletCall(req, resp).doGet();
    }

    @Override
    protected final void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        defineServletCall(req, resp).doPost();
    }

    @Override
    protected final void doPut(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        defineServletCall(req, resp).doPut();
    }

    @Override
    protected final void doDelete(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        defineServletCall(req, resp).doDelete();
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // LOCKING OVERRIDES
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected final long getLastModified(final HttpServletRequest req) {
        return super.getLastModified(req);
    }

    @Override
    protected final void doHead(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        super.doHead(req, resp);
    }

    @Override
    protected final void doOptions(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        super.doOptions(req, resp);
    }

    @Override
    protected final void doTrace(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        super.doTrace(req, resp);
    }

    @Override
    public final void service(final ServletRequest req, final ServletResponse res) throws ServletException, IOException {
        super.service(req, res);
    }
}
