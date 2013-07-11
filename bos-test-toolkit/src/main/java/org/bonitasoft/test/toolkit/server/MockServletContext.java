/**
 * Copyright (C) 2010 BonitaSoft S.A.
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
package org.bonitasoft.test.toolkit.server;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * @author Nicolas Chabanoles
 * 
 */
public class MockServletContext implements ServletContext {

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletContext#getAttribute(java.lang.String)
     */
    @Override
    public Object getAttribute(final String arg0) {
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletContext#getAttributeNames()
     */
    @Override
    public Enumeration getAttributeNames() {
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletContext#getContext(java.lang.String)
     */
    @Override
    public ServletContext getContext(final String arg0) {
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletContext#getContextPath()
     */
    @Override
    public String getContextPath() {
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletContext#getInitParameter(java.lang.String)
     */
    @Override
    public String getInitParameter(final String arg0) {
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletContext#getInitParameterNames()
     */
    @Override
    public Enumeration getInitParameterNames() {
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletContext#getMajorVersion()
     */
    @Override
    public int getMajorVersion() {
        return 0;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletContext#getMimeType(java.lang.String)
     */
    @Override
    public String getMimeType(final String arg0) {
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletContext#getMinorVersion()
     */
    @Override
    public int getMinorVersion() {
        return 0;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletContext#getNamedDispatcher(java.lang.String)
     */
    @Override
    public RequestDispatcher getNamedDispatcher(final String arg0) {
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletContext#getRealPath(java.lang.String)
     */
    @Override
    public String getRealPath(final String arg0) {
        return new File("src" + File.separator + "test" + File.separator + "resources").getAbsolutePath() + arg0;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletContext#getRequestDispatcher(java.lang.String)
     */
    @Override
    public RequestDispatcher getRequestDispatcher(final String arg0) {
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletContext#getResource(java.lang.String)
     */
    @Override
    public URL getResource(final String arg0) throws MalformedURLException {
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletContext#getResourceAsStream(java.lang.String)
     */
    @Override
    public InputStream getResourceAsStream(final String arg0) {
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletContext#getResourcePaths(java.lang.String)
     */
    @Override
    public Set getResourcePaths(final String arg0) {
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletContext#getServerInfo()
     */
    @Override
    public String getServerInfo() {
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletContext#getServlet(java.lang.String)
     */
    @Override
    public Servlet getServlet(final String arg0) throws ServletException {
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletContext#getServletContextName()
     */
    @Override
    public String getServletContextName() {
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletContext#getServletNames()
     */
    @Override
    public Enumeration getServletNames() {
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletContext#getServlets()
     */
    @Override
    public Enumeration getServlets() {
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletContext#log(java.lang.String)
     */
    @Override
    public void log(final String arg0) {

    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletContext#log(java.lang.Exception, java.lang.String)
     */
    @Override
    public void log(final Exception arg0, final String arg1) {

    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletContext#log(java.lang.String, java.lang.Throwable)
     */
    @Override
    public void log(final String arg0, final Throwable arg1) {

    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletContext#removeAttribute(java.lang.String)
     */
    @Override
    public void removeAttribute(final String arg0) {

    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.ServletContext#setAttribute(java.lang.String, java.lang.Object)
     */
    @Override
    public void setAttribute(final String arg0, final Object arg1) {

    }

}
