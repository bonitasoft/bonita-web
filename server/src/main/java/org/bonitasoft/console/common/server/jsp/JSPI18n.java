/**
 * Copyright (C) 2014 BonitaSoft S.A.
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
package org.bonitasoft.console.common.server.jsp;

import org.bonitasoft.console.common.server.i18n.I18n;
import org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n.LOCALE;

public class JSPI18n {

	private LOCALE locale;
	private JSPUtils jspUtils;

	public JSPI18n(JSPUtils jspUtils) {
		this.jspUtils = jspUtils;
		this.locale = loadLocale();

		// initialize I18n instance
		I18n.getInstance();
	}
	
	public String _(String message) {
    	return I18n._(message, locale);
    }
    
    public LOCALE getLocale() {
		return locale;
	}
    
    private LOCALE loadLocale() {
    	String locale = jspUtils.getParameter("_l", "");
    	if (locale.isEmpty()) {
    		locale = jspUtils.getSessionOrCookie("BOS_Locale", "en_US");
    	}
        locale = "default".equals(locale) ? "en_US" : locale;
        return LOCALE.valueOf(locale);
    }
}
