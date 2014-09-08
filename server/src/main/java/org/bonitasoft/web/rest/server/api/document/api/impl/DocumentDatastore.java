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
package org.bonitasoft.web.rest.server.api.document.api.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.activation.FileTypeMap;
import javax.activation.MimetypesFileTypeMap;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.console.common.server.preferences.properties.PropertiesFactory;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.document.Document;
import org.bonitasoft.engine.bpm.document.DocumentAttachmentException;
import org.bonitasoft.engine.bpm.document.DocumentException;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessInstanceNotFoundException;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.RetrieveException;
import org.bonitasoft.engine.exception.SearchException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.InvalidSessionException;
import org.bonitasoft.web.rest.model.document.DocumentItem;
import org.bonitasoft.web.rest.server.datastore.CommonDatastore;
import org.bonitasoft.web.rest.server.datastore.filter.Filters;
import org.bonitasoft.web.rest.server.datastore.utils.SearchOptionsCreator;
import org.bonitasoft.web.rest.server.datastore.utils.Sorts;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasAdd;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasGet;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasUpdate;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.data.APIID;

/**
 * Document data store
 * 
 * @author Yongtao Guo, Fabio Lombardi
 * 
 */
public class DocumentDatastore extends CommonDatastore<DocumentItem, Document>  implements DatastoreHasAdd<DocumentItem>, DatastoreHasGet<DocumentItem>, DatastoreHasUpdate<DocumentItem> {
	
	private static final String CREATE_NEW_DOCUMENT = "AddNewDocument";
	
	private static final String CREATE_NEW_VERSION_DOCUMENT = "AddNewVersionDocument";

	protected final WebBonitaConstantsUtils constants;

    protected final ProcessAPI processAPI;

    final long maxSizeForTenant;
    
    final FileTypeMap mimetypesFileTypeMap;

	protected SearchOptionsCreator searchOptionsCreator;
    
    /**
     * Default constructor.
     */    
    public DocumentDatastore(final APISession engineSession, final WebBonitaConstantsUtils constantsValue, final ProcessAPI processAPI) {
        super(engineSession);
        constants = constantsValue;
        this.processAPI = processAPI;
        maxSizeForTenant =  PropertiesFactory.getConsoleProperties(engineSession.getTenantId()).getMaxSize();
        mimetypesFileTypeMap = new MimetypesFileTypeMap();
    }

    @Override
    public DocumentItem get(final APIID id) {
        try {
            final Document documentItem = processAPI.getDocument(id.toLong());
            return convertEngineToConsoleItem(documentItem);
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }
    
    @Override
	public DocumentItem add(DocumentItem item) {

        long caseId = -1;
        try {
        	
            /* Necessary to avoid API break */
            if (item.getAttributeValue(DocumentItem.ATTRIBUTE_CASE_ID) != null) {
            	caseId = Long.valueOf(item.getAttributeValue(DocumentItem.ATTRIBUTE_CASE_ID));
            } else if (item.getAttributeValue(DocumentItem.PROCESSINSTANCE_ID) != null ) {
            	caseId = Long.valueOf(item.getAttributeValue(DocumentItem.PROCESSINSTANCE_ID));            	
            }
            
        } catch (NumberFormatException e) {
            throw new APIException("Error while attaching a new document. Request with bad case id value.");
        }
        
        /* -----------------------------*/
        
        final String documentName = item.getAttributeValue(DocumentItem.ATTRIBUTE_NAME);
        final String uploadPath = item.getAttributeValue(DocumentItem.ATTRIBUTE_UPLOAD_PATH);
        final String urlPath = item.getAttributeValue(DocumentItem.ATTRIBUTE_URL);
        String documentDescription = item.getAttributeValue(DocumentItem.ATTRIBUTE_DESCRIPTION);
        
        /* Necessary to avoid API break*/
        String operationType = CREATE_NEW_DOCUMENT;
        final String userSpecifiedOperationType = item.getAttributeValue(DocumentItem.DOCUMENT_CREATION_TYPE);
        if (userSpecifiedOperationType != null) {
        	operationType = userSpecifiedOperationType;
        }
        /* -----------------------------*/
        
        try {
            DocumentItem returnedItem = new DocumentItem();
            if (caseId != -1 && documentName != null) {
                if (uploadPath != null && !uploadPath.isEmpty()) {
                    returnedItem = attachDocument(caseId, documentName, uploadPath, operationType, documentDescription);
                } else if (urlPath != null && !urlPath.isEmpty()) {
                    returnedItem = attachDocumentFromUrl(caseId, documentName, urlPath, operationType, documentDescription);
                }
                return returnedItem;
            } else {
                throw new APIException("Error while attaching a new document. Request with bad param value.");
            }
        } catch (final Exception e) {
            throw new APIException(e);
        }
        
	}
    
    @Override
    public DocumentItem update(APIID id, Map<String, String> attributes) {    	
    	DocumentItem returnedItem = new DocumentItem();
    	try {
    		Document document = processAPI.getDocument(id.toLong());
    		final long caseId = document.getProcessInstanceId();
    		final String documentName = document.getName();
    		final String urlPath;
    		String documentDescription = null;
    		
			if (attributes.containsKey(DocumentItem.ATTRIBUTE_UPLOAD_PATH) || attributes.containsKey(DocumentItem.ATTRIBUTE_URL)) {
				
				if (attributes.get(DocumentItem.ATTRIBUTE_DESCRIPTION) != null) {
					documentDescription = attributes.get(DocumentItem.ATTRIBUTE_DESCRIPTION);
				}
				
				if (attributes.containsKey(DocumentItem.ATTRIBUTE_UPLOAD_PATH)) {
					urlPath = attributes.get(DocumentItem.ATTRIBUTE_UPLOAD_PATH);
					returnedItem = attachDocument(caseId, documentName, urlPath, CREATE_NEW_VERSION_DOCUMENT, documentDescription);
				} else {
					urlPath = attributes.get(DocumentItem.ATTRIBUTE_URL);
					returnedItem = attachDocumentFromUrl(caseId, documentName, urlPath, CREATE_NEW_VERSION_DOCUMENT, documentDescription);
				}
				return returnedItem;
			} else {
		        throw new APIException("Error while attaching a new document. Request with bad param value.");
		    }
    	} catch (final Exception e) {
            throw new APIException(e);
        }
    }
    
	public DocumentItem attachDocument(final long caseId, final String documentName, final String uploadPath, String operationType, String documentDescription) 
                throws BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException, DocumentException, IOException, ProcessInstanceNotFoundException, DocumentAttachmentException, InvalidSessionException, ProcessDefinitionNotFoundException, RetrieveException {

        DocumentItem item = new DocumentItem();
        String fileName = null;
        String mimeType = null;
        byte[] fileContent = null;
        
        final File theSourceFile = new File(uploadPath);
        if (theSourceFile.exists()) {
            if (theSourceFile.length() > maxSizeForTenant * 1048576) {
                final String errorMessage = "This document is exceeded " + maxSizeForTenant + "Mb";
                throw new DocumentException(errorMessage);
            }
            fileContent = DocumentUtil.getArrayByteFromFile(theSourceFile);
            if (theSourceFile.isFile()) {
                fileName = theSourceFile.getName();
                mimeType = mimetypesFileTypeMap.getContentType(theSourceFile);
            }
        }
        // Attach a new document to a case
        Document document = null;
        if (operationType.equals(CREATE_NEW_DOCUMENT)) {
        	document = processAPI.attachDocument(caseId, documentName, fileName, mimeType, fileContent, documentDescription);        	
        } else if (operationType.equals(CREATE_NEW_VERSION_DOCUMENT)) {
        	document = processAPI.attachNewDocumentVersion(caseId, documentName, fileName, mimeType, fileContent, documentDescription);
        } 
        item = convertEngineToConsoleItem(document);
        return item;
    }

    public DocumentItem attachDocumentFromUrl(final long caseId, final String documentName, final String url, String operationType, String documentDescription)
            throws InvalidSessionException, BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException, ProcessInstanceNotFoundException,
            DocumentAttachmentException, IOException, RetrieveException, ProcessDefinitionNotFoundException {

        DocumentItem item = new DocumentItem();
        final String fileName = DocumentUtil.getFileNameFromUrl(url);
        final String mimeType = DocumentUtil.getMimeTypeFromUrl(url);
        if (fileName != null) {
        	// Attach a new document to a case
        	 Document document = null;
             if (operationType.equals(CREATE_NEW_DOCUMENT)) {
             	document = processAPI.attachDocument(caseId, documentName, fileName, mimeType, url, documentDescription);        	
             } else if (operationType.equals(CREATE_NEW_VERSION_DOCUMENT)) {
             	document = processAPI.attachNewDocumentVersion(caseId, documentName, fileName, mimeType, url, documentDescription);
             } 
             item = convertEngineToConsoleItem(document);
        }
        return item;
    }
    
    @Override
    protected DocumentItem convertEngineToConsoleItem(final Document item) {
        if (item != null) {
            return new DocumentItemConverter().convert(item);
        }
        return null;
    }

    private List<DocumentItem> convertEngineToConsoleItem(List<Document> result) {
    	if (result != null) {
            return new DocumentItemConverter().convert(result);
        }
        return null;
    }
    
	public ItemSearchResult<DocumentItem> search(int page, int resultsByPage,
			String search, Map<String, String> filters, String orders) {
		return searchDocument(page, resultsByPage, search, filters, orders);
	}
    
	protected ItemSearchResult<DocumentItem> searchDocument(final int page, final int resultsByPage, final String search,
            final Map<String, String> filters, final String orders) {

		searchOptionsCreator = buildSearchOptionCreator(page, resultsByPage, search, filters, orders);

		try {
			SearchResult<Document> engineSearchResults = processAPI.searchDocuments(searchOptionsCreator.create());
			return new ItemSearchResult<DocumentItem>(page, resultsByPage, engineSearchResults.getCount(), convertEngineToConsoleItem(engineSearchResults.getResult()));
		} catch (SearchException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected SearchOptionsCreator buildSearchOptionCreator(final int page, final int resultsByPage, final String search, final Map<String, String> filters, final String orders) {
		SearchOptionsCreator searchOptionsCreator = new SearchOptionsCreator(page, resultsByPage, search, new Sorts(orders, getDocumentSearchAttributeConverter()), new Filters(filters, new DocumentFilterCreator(getDocumentSearchAttributeConverter())));
		return searchOptionsCreator;
	}

	private DocumentSearchAttributeConverter getDocumentSearchAttributeConverter() {
		return new DocumentSearchAttributeConverter();
	}

}
