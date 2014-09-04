package org.bonitasoft.web.rest.server.api.document.api.impl;

import org.bonitasoft.engine.bpm.document.Document;
import org.bonitasoft.web.rest.model.document.DocumentItem;
import org.bonitasoft.web.rest.server.datastore.converter.ItemConverter;

public class DocumentItemConverter  extends ItemConverter<DocumentItem, Document> {

	@Override
    public DocumentItem convert(final Document engineItem) {
		final DocumentItem item = new DocumentItem();
        item.setId(String.valueOf(engineItem.getId()));
        item.setCaseId(String.valueOf(engineItem.getProcessInstanceId()));
        item.setName(engineItem.getName());
        item.setVersion(engineItem.getVersion());
        item.setDescription(engineItem.getDescription());
        item.setSubmittedBy(engineItem.getAuthor());
        item.setFileName(engineItem.getContentFileName());
        item.setCreationDate(engineItem.getCreationDate());
        item.setMIMEType(engineItem.getContentMimeType());
        item.setHasContent(String.valueOf(engineItem.hasContent()));
        item.setStorageId(engineItem.getContentStorageId());
        item.setURL(engineItem.getUrl());
        return item;
	}

}
