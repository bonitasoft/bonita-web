/**
 * Copyright (C) 2013 BonitaSoft S.A.
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
package org.bonitasoft.console.common.server.login.servlet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author Paul AMAR
 *
 */
public class URLProtector {

    protected List<String> tokens = Arrays.asList("https", "http", "www", "//", "\\.", ":");
        
    public String protectRedirectUrl(String redirectUrl) {
        if (!redirectUrl.startsWith("bonita/portal")) {
            return removeTokenFromUrl(redirectUrl, new ArrayList<String>(tokens));            
        } 
        return redirectUrl;
    }
    
    private String removeTokenFromUrl(String redirectUrl, List<String> tokens){
        if (tokens.size() > 0) {
            return removeTokenFromUrl(redirectUrl.replaceAll(tokens.remove(0), ""), tokens);
        }
        return redirectUrl;
    }
    
}
