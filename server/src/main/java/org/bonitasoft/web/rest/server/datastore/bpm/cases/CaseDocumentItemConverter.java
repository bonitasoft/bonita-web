package org.bonitasoft.web.rest.server.datastore.bpm.cases;

import org.bonitasoft.engine.bpm.document.Document;
import org.bonitasoft.web.rest.model.bpm.cases.CaseDocumentItem;
import org.bonitasoft.web.rest.server.datastore.converter.ItemConverter;

public class CaseDocumentItemConverter extends ItemConverter<CaseDocumentItem, Document> {

    @Override
    public CaseDocumentItem convert(final Document engineItem) {
        final CaseDocumentItem item = new CaseDocumentItem();
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
        item.setIndex(engineItem.getIndex());
        return item;
    }
}
