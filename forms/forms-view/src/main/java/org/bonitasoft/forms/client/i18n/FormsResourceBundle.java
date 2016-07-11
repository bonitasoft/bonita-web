/**
 * Copyright (C) 2009 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.forms.client.i18n;

import com.google.gwt.core.client.GWT;

/**
 * Form labels and constants accessor for i18n
 * 
 * @author Anthony Birembaut
 */
public class FormsResourceBundle {
    
    protected static final FormsMessages MESSAGES = (FormsMessages) GWT.create(FormsMessages.class);
    
    protected static final FormsErrors ERRORS = (FormsErrors) GWT.create(FormsErrors.class);
    
    public static FormsMessages getMessages(){
        return MESSAGES;
    }
    
    public static FormsErrors getErrors() {
        return ERRORS;
    }
}
