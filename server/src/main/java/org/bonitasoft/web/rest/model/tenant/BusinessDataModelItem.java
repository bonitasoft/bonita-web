/*******************************************************************************
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.model.tenant;

import org.bonitasoft.web.toolkit.client.data.item.Item;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;


public class BusinessDataModelItem extends Item {

    public static final String ATTRIBUTE_FILE_UPLOAD_PATH = "fileUpload";
    
    @Override
    public ItemDefinition<BusinessDataModelItem> getItemDefinition() {
        return new BusinessDataModelDefinition();
    }

    public String getFileUploadPath() {
        return getAttributeValue(ATTRIBUTE_FILE_UPLOAD_PATH);
    }
}
