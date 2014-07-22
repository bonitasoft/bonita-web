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
package org.bonitasoft.web.rest.model.document;

import java.util.Date;

import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.Item;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * Document item
 * 
 * @author Gai Cuisha, Fabio Lombardi
 * 
 */
public class DocumentItem extends Item {

	public DocumentItem() {
        super();
    }

    public DocumentItem(final IItem item) {
        super(item);
    }

    public static final String ATTRIBUTE_ID = "id";

    public static final String ATTRIBUTE_VERSION = "version";

    public static final String ATTRIBUTE_CASE_ID = "caseId";
    
    public static final String ATTRIBUTE_NAME = "name";

    public static final String ATTRIBUTE_SUBMITTED_BY_USER_ID = "createdBy";
    
    public static final String ATTRIBUTE_CREATION_DATE = "creationDate";

    public static final String ATTRIBUTE_HAS_CONTENT = "documentHasContent";

    public static final String ATTRIBUTE_CONTENT_FILENAME = "fileName";

    public static final String ATTRIBUTE_CONTENT_MIMETYPE = "contentMimeType";

    public static final String ATTRIBUTE_CONTENT_STORAGE_ID = "contentStorageId";

    public static final String ATTRIBUTE_UPLOAD_PATH = "uploadPath";
    
    public static final String ATTRIBUTE_URL = "URL";

    public void setId(final String id) {
        this.setAttribute(ATTRIBUTE_ID, id);
    }

    public void setVersion(final String version) {
        this.setAttribute(ATTRIBUTE_VERSION, version);
    }

    
    public void setCaseId(final String caseId) {
        this.setAttribute(ATTRIBUTE_CASE_ID, caseId);
    }

    
    public void setName(final String name) {
        this.setAttribute(ATTRIBUTE_NAME, name);
    }
    
    public void setSubmittedBy(final APIID userId) {
        this.setAttribute(ATTRIBUTE_SUBMITTED_BY_USER_ID, userId.toString());
    }

    
    public void setSubmittedBy(final Long userId) {
        this.setAttribute(ATTRIBUTE_SUBMITTED_BY_USER_ID, userId.toString());
    }

    public void setCreationDate(String creationDate) {
        this.setAttribute(ATTRIBUTE_CREATION_DATE, creationDate);
    }

    public void setCreationDate(final Date creationDate) {
        this.setAttribute(ATTRIBUTE_CREATION_DATE, creationDate);
    }
    
    public void setHasContent(final String hasContent) {
        this.setAttribute(ATTRIBUTE_HAS_CONTENT, hasContent);
    }
 
    public void setFileName(final String fileName) {
        this.setAttribute(ATTRIBUTE_CONTENT_FILENAME, fileName);
    }
    
    public void setMIMEType(final String MIMEType) {
        this.setAttribute(ATTRIBUTE_CONTENT_MIMETYPE, MIMEType);
    }

    public void setStorageId(final String storageId) {
        this.setAttribute(ATTRIBUTE_CONTENT_STORAGE_ID, storageId);
    }
    public void setUploadPath(final String uploadPath) {
        this.setAttribute(ATTRIBUTE_UPLOAD_PATH, uploadPath);
    }
    
    public String getUploadPath() {
    	return getAttributeValue(ATTRIBUTE_UPLOAD_PATH);
    }
    
    public void setURL(final String URL) {
        this.setAttribute(ATTRIBUTE_URL, URL);
    }
        
    public String getVersion() {
    	return getAttributeValue(ATTRIBUTE_VERSION);    	
    }
    
    public APIID getCaseId() {
    	return getAttributeValueAsAPIID(ATTRIBUTE_CASE_ID);
    }
    
    public String getName() {
    	return getAttributeValue(ATTRIBUTE_NAME);
    }
    public APIID getSubmittedBy() {
    	return getAttributeValueAsAPIID(ATTRIBUTE_SUBMITTED_BY_USER_ID);
    }
    
    public Date getCreationDate() {
    	return getAttributeValueAsDate(ATTRIBUTE_CREATION_DATE);
    }
    
    public boolean hasContent() {
    	return Boolean.parseBoolean(getAttributeValue(ATTRIBUTE_HAS_CONTENT));
    }
    
    public String getFileName() {
    	return getAttributeValue(ATTRIBUTE_CONTENT_FILENAME);
    }
    
    public String getMIMEType() {
    	return getAttributeValue(ATTRIBUTE_CONTENT_MIMETYPE);
    }
    
    public String getStorageId() {
    	return getAttributeValue(ATTRIBUTE_CONTENT_STORAGE_ID);
    }
    
    public String getURL() {
    	return getAttributeValue(ATTRIBUTE_URL);
    }

    @Override
    public ItemDefinition getItemDefinition() {
        return new DocumentDefinition();
    }

}
