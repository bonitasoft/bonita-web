package org.bonitasoft.web.rest.server.api.document.api.impl;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.web.rest.model.document.DocumentItem;
import org.bonitasoft.web.rest.server.datastore.converter.AttributeConverter;
import org.bonitasoft.web.toolkit.client.common.util.MapUtil;

public class DocumentSearchAttributeConverter implements AttributeConverter {

	private final Map<String, String> mapping;

    public DocumentSearchAttributeConverter() {
        mapping = createMapping();
    }

    private Map<String, String> createMapping() {
        final Map<String, String> mapping = new HashMap<String, String>();
        mapping.put(DocumentItem.ATTRIBUTE_ID, DocumentItem.ATTRIBUTE_ID);
        mapping.put(DocumentItem.ATTRIBUTE_SUBMITTED_BY_USER_ID, "documentAuthor");
        mapping.put(DocumentItem.ATTRIBUTE_NAME, DocumentItem.ATTRIBUTE_NAME);
        mapping.put(DocumentItem.ATTRIBUTE_CREATION_DATE, DocumentItem.ATTRIBUTE_CREATION_DATE);
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
