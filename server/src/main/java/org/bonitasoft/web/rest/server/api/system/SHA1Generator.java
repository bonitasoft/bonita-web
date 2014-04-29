/*******************************************************************************
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 *      BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 *      or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
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
