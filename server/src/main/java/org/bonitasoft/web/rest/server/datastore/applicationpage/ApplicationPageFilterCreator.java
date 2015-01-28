/*******************************************************************************
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.server.datastore.applicationpage;

import java.io.Serializable;

import org.bonitasoft.web.rest.server.datastore.filter.Field;
import org.bonitasoft.web.rest.server.datastore.filter.Filter;
import org.bonitasoft.web.rest.server.datastore.filter.FilterCreator;
import org.bonitasoft.web.rest.server.datastore.filter.StrValue;

public class ApplicationPageFilterCreator implements FilterCreator {

    private final ApplicationPageSearchDescriptorConverter converter;

    public ApplicationPageFilterCreator(final ApplicationPageSearchDescriptorConverter converter) {
        this.converter = converter;
    }

    @Override
    public Filter<? extends Serializable> create(final String attribute, final String value) {
        return new Filter<String>(new Field(attribute, converter), new StrValue(value));
    }

}
