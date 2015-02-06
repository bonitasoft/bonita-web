/**
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.server.api.system;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Fabio Lombardi
 *
 */
public class SHA1Generator {
    
    private MessageDigest generator;
    
    public SHA1Generator(){
        
        try {
            this.generator =  MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            // Should never appear
        }
        
    }
    
    public String getHash(String stringToHash) {
        return getStringFromBytes(generator.digest(stringToHash.getBytes()));
    }
    
    public static String getStringFromBytes(byte[] b) {
        char hexDigit[] = {'0', '1', '2', '3', '4', '5', '6', '7',
                           '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        StringBuffer buf = new StringBuffer();
        for (int j=0; j<b.length; j++) {
           buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
           buf.append(hexDigit[b[j] & 0x0f]);
        }
        return buf.toString();
    }

}
