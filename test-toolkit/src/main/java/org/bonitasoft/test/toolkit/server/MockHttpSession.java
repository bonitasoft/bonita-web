/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.test.toolkit.server;

import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

/**
 * @author Ruiheng Fan
 * 
 */
public class MockHttpSession implements HttpSession {

    Map<String, Object> attributesMap = new HashMap<String, Object>();

    String id = new Date().getTime() + "";

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpSession#getCreationTime()
     */
    @Override
    public long getCreationTime() {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpSession#getId()
     */
    @Override
    public String getId() {
        return this.id;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpSession#getLastAccessedTime()
     */
    @Override
    public long getLastAccessedTime() {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpSession#getServletContext()
     */
    @Override
    public ServletContext getServletContext() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpSession#setMaxInactiveInterval(int)
     */
    @Override
    public void setMaxInactiveInterval(final int interval) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpSession#getMaxInactiveInterval()
     */
    @Override
    public int getMaxInactiveInterval() {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpSession#getSessionContext()
     */
    @Override
    public HttpSessionContext getSessionContext() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpSession#getAttribute(java.lang.String)
     */
    @Override
    public Object getAttribute(final String name) {
        return this.attributesMap.get(name);
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpSession#getValue(java.lang.String)
     */
    @Override
    public Object getValue(final String name) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpSession#getAttributeNames()
     */
    @Override
    public Enumeration getAttributeNames() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpSession#getValueNames()
     */
    @Override
    public String[] getValueNames() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpSession#setAttribute(java.lang.String, java.lang.Object)
     */
    @Override
    public void setAttribute(final String name, final Object value) {
        this.attributesMap.put(name, value);
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpSession#putValue(java.lang.String, java.lang.Object)
     */
    @Override
    public void putValue(final String name, final Object value) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpSession#removeAttribute(java.lang.String)
     */
    @Override
    public void removeAttribute(final String name) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpSession#removeValue(java.lang.String)
     */
    @Override
    public void removeValue(final String name) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpSession#invalidate()
     */
    @Override
    public void invalidate() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.http.HttpSession#isNew()
     */
    @Override
    public boolean isNew() {
        // TODO Auto-generated method stub
        return false;
    }

}
