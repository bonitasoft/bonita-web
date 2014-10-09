package org.bonitasoft.web.rest.server.api.document.api.impl;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.engine.bpm.document.DocumentsSearchDescriptor;
import org.bonitasoft.web.rest.model.bpm.cases.CaseDocumentItem;
import org.bonitasoft.web.rest.server.datastore.converter.AttributeConverter;
import org.bonitasoft.web.toolkit.client.common.util.MapUtil;

public class CaseDocumentSearchAttributeConverter implements AttributeConverter {

    private final Map<String, String> mapping;

    public CaseDocumentSearchAttributeConverter() {
        mapping = createMapping();
    }

    private Map<String, String> createMapping() {
        final Map<String, String> mapping = new HashMap<String, String>();
        mapping.put(CaseDocumentItem.ATTRIBUTE_ID, CaseDocumentItem.ATTRIBUTE_ID);
        mapping.put(CaseDocumentItem.ATTRIBUTE_SUBMITTED_BY_USER_ID, DocumentsSearchDescriptor.DOCUMENT_AUTHOR);
        mapping.put(CaseDocumentItem.ATTRIBUTE_NAME, DocumentsSearchDescriptor.DOCUMENT_NAME);
        mapping.put(CaseDocumentItem.ATTRIBUTE_CREATION_DATE, DocumentsSearchDescriptor.DOCUMENT_CREATIONDATE);
        mapping.put(CaseDocumentItem.ATTRIBUTE_DESCRIPTION, DocumentsSearchDescriptor.DOCUMENT_DESCRIPTION);
        mapping.put(CaseDocumentItem.ATTRIBUTE_INDEX, DocumentsSearchDescriptor.LIST_INDEX);
        return mapping;
    }

    @Override
    public String convert(final String attribute) {
        return MapUtil.getMandatory(mapping, attribute);
    }

    protected final void extendsMapping(final Map<String, String> extension) {
        mapping.putAll(extension);
    }

}
