package org.bonitasoft.web.rest.server.api.document.api.impl;

import java.io.Serializable;

import org.bonitasoft.web.rest.server.datastore.converter.AttributeConverter;
import org.bonitasoft.web.rest.server.datastore.filter.Field;
import org.bonitasoft.web.rest.server.datastore.filter.Filter;
import org.bonitasoft.web.rest.server.datastore.filter.FilterCreator;
import org.bonitasoft.web.rest.server.datastore.filter.StrValue;
import org.bonitasoft.web.rest.server.datastore.organization.UserSearchAttributeConverter;

public class DocumentFilterCreator implements FilterCreator {
    
	private DocumentSearchAttributeConverter converter;

    public DocumentFilterCreator(DocumentSearchAttributeConverter converter) {
        this.converter = converter;
    }
	@Override
	public Filter<? extends Serializable> create(String attribute, String value) {
		return new Filter<String>(new Field(attribute, converter), new StrValue(value));
	}

}
