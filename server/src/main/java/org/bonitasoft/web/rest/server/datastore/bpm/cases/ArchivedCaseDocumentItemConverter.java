package org.bonitasoft.web.rest.server.datastore.bpm.cases;

import org.bonitasoft.engine.bpm.document.ArchivedDocument;
import org.bonitasoft.web.rest.model.bpm.cases.ArchivedCaseDocumentItem;
import org.bonitasoft.web.rest.server.datastore.converter.ItemConverter;

public class ArchivedCaseDocumentItemConverter extends ItemConverter<ArchivedCaseDocumentItem, ArchivedDocument> {

    @Override
    public ArchivedCaseDocumentItem convert(final ArchivedDocument engineItem) {
        final ArchivedCaseDocumentItem item = new ArchivedCaseDocumentItem();
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
        item.setSourceObjectId(engineItem.getSourceObjectId());
        item.setArchivedDate(engineItem.getArchiveDate());
        return item;
    }
}
