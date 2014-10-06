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

import org.bonitasoft.web.rest.model.identity.UserItem;
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

    public static final String ATTRIBUTE_NAME = "documentName";

    public static final String ATTRIBUTE_DESCRIPTION = "documentDescription";

    public static final String ATTRIBUTE_SUBMITTED_BY_USER_ID = "submittedBy";

    public static final String ATTRIBUTE_CREATION_DATE = "documentCreationDate";

    public static final String ATTRIBUTE_HAS_CONTENT = "documentHasContent";

    public static final String ATTRIBUTE_CONTENT_FILENAME = "documentContentFileName";

    public static final String ATTRIBUTE_CONTENT_MIMETYPE = "documentContentMimeType";

    public static final String ATTRIBUTE_CONTENT_STORAGE_ID = "documentContentStorageId";

    public static final String ATTRIBUTE_UPLOAD_PATH = "documentUpload";

    public static final String ATTRIBUTE_URL = "documentURL";

    public static final String ATTRIBUTE_INDEX = "index";

    /* Attribute kept here for avoiding API break */

    public static final String DOCUMENT_CREATION_TYPE = "DOCUMENT_CREATION_TYPE";

    public static final String PROCESSINSTANCE_NAME = "processinstanceName";

    public static final String PROCESS_DISPLAY_NAME = "processDisplayName";

    public static final String PROCESS_VERSION = "processinstanceVersion";

    public static final String PROCESSINSTANCE_ID = "processinstanceId";

    public static final String DOCUMENT_AUTHOR = "documentAuthor";

    /* -------------------------------------------- */

    public void setId(final String id) {
        this.setAttribute(ATTRIBUTE_ID, id);
    }

    public void setVersion(final String version) {
        this.setAttribute(ATTRIBUTE_VERSION, version);
    }


    public void setCaseId(final String caseId) {
        this.setAttribute(ATTRIBUTE_CASE_ID, caseId);
        this.setAttribute(PROCESSINSTANCE_ID, caseId);
    }


    public void setName(final String name) {
        this.setAttribute(ATTRIBUTE_NAME, name);
    }

    public void setDescription(final String description) {
        setAttribute(ATTRIBUTE_DESCRIPTION, description);
    }

    public void setSubmittedBy(final APIID userId) {
        this.setAttribute(ATTRIBUTE_SUBMITTED_BY_USER_ID, userId.toString());
        this.setAttribute(DOCUMENT_AUTHOR, userId);
    }

    public void setSubmittedBy(final Long userId) {
        this.setAttribute(ATTRIBUTE_SUBMITTED_BY_USER_ID, userId.toString());
        this.setAttribute(DOCUMENT_AUTHOR, userId.toString());
    }

    public void setCreationDate(final String creationDate) {
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

    public void setIndex(final String index) {
        this.setAttribute(ATTRIBUTE_INDEX, index);
    }

    public void setIndex(final int index) {
        this.setAttribute(ATTRIBUTE_INDEX, index);
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

    public String getDescription() {
        return getAttributeValue(ATTRIBUTE_DESCRIPTION);
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

    public String getIndex() {
        return getAttributeValue(ATTRIBUTE_INDEX);
    }

    @Override
    public ItemDefinition getItemDefinition() {
        return new DocumentDefinition();
    }

    public UserItem getSubmittedByUser() {
        return (UserItem) getDeploy(ATTRIBUTE_SUBMITTED_BY_USER_ID);
    }

    /* Methods kept here to avoid API break */

    public void setProcessInstanceId(final String caseId) {
        this.setAttribute(PROCESSINSTANCE_ID, caseId);
    }

    public void setCaseName(final String caseName) {
        this.setAttribute(PROCESSINSTANCE_NAME, caseName);
    }

    public void setProcessDisplayName(final String caseName) {
        this.setAttribute(PROCESS_DISPLAY_NAME, caseName);
    }

    public void setProcessVersion(final String caseVersion) {
        this.setAttribute(PROCESS_VERSION, caseVersion);
    }

    public void setDocumentAuthor(final Long userId) {
        this.setAttribute(DOCUMENT_AUTHOR, userId);
    }

    public UserItem getAuthorByUser() {
        return (UserItem) getDeploy(DOCUMENT_AUTHOR);
    }

}
