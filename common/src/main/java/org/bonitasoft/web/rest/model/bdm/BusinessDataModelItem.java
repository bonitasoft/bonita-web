/*******************************************************************************
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.model.bdm;

import java.io.Serializable;


public class BusinessDataModelItem implements Serializable {

    /**
     * UID
     */
    private static final long serialVersionUID = -6119736077951213751L;
    
    private String fileUpload;

    
    public String getFileUpload() {
        return fileUpload;
    }

    
    public void setFileUpload(String fileUpload) {
        this.fileUpload = fileUpload;
    }
    
}
