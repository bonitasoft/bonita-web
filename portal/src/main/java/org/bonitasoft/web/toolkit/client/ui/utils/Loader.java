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
package org.bonitasoft.web.toolkit.client.ui.utils;

import static com.google.gwt.query.client.GQuery.$;

/**
 * @author Julien Mege
 * 
 */
public final class Loader {
	//Warning: in case of refactor, be sure to modify the ReportMoreDetailsAdminPage following native methode, 
	//  showLoader() and hideLoader() to avoid java script error in the reports
    public static final String MAIN_LOADER_ID = "initloader";
   
    public static void showLoader() {
    	$("#"+MAIN_LOADER_ID).show();
    }
    
    public static void hideLoader() {
    	$("#"+MAIN_LOADER_ID).hide();
    }
    
 }
