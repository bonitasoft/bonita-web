/*******************************************************************************
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.server;

import static org.assertj.core.api.Assertions.assertThat;

import org.bonitasoft.web.rest.server.FinderFactory.BusinessDataReferenceResourceFinder;
import org.bonitasoft.web.rest.server.FinderFactory.BusinessDataReferencesResourceFinder;
import org.bonitasoft.web.rest.server.FinderFactory.BusinessDataResourceFinder;
import org.bonitasoft.web.rest.server.api.bdm.BusinessDataQueryResource;
import org.bonitasoft.web.rest.server.api.bdm.BusinessDataReferenceResource;
import org.bonitasoft.web.rest.server.api.bdm.BusinessDataReferencesResource;
import org.bonitasoft.web.rest.server.api.bdm.BusinessDataResource;
import org.junit.Before;
import org.junit.Test;
import org.restlet.resource.Finder;
import org.restlet.resource.ServerResource;

public class FinderFactoryTest {

    private FinderFactory factory;

    @Before
    public void setUp() {
        factory = new FinderFactory();
    }

    @Test
    public void should_return_BusinessDataQueryFinder_for_BusinessDataQuery() throws Exception {

        final Finder finder = factory.create(BusinessDataQueryResource.class);

        assertThat(finder).isInstanceOf(FinderFactory.BusinessDataQueryResourceFinder.class);
    }

    @Test
    public void should_return_BusinessDataResourceFinder_for_BusinessDataResource() throws Exception {

        final Finder finder = factory.create(BusinessDataResource.class);

        assertThat(finder).isInstanceOf(BusinessDataResourceFinder.class);
    }

    @Test
    public void should_return_BusinessDataReferenceResourceFinder_for_BusinessDataReferenceResource() throws Exception {

        final Finder finder = factory.create(BusinessDataReferenceResource.class);

        assertThat(finder).isInstanceOf(BusinessDataReferenceResourceFinder.class);
    }

    @Test
    public void should_return_BusinessDataReferencesResourceFinder_for_BusinessDataReferencesResource() throws Exception {

        final Finder finder = factory.create(BusinessDataReferencesResource.class);

        assertThat(finder).isInstanceOf(BusinessDataReferencesResourceFinder.class);
    }

    @Test(expected = RuntimeException.class)
    public void should_throw_RuntimeException_for_a_not_supported_class() throws Exception {

        factory.create(NotSupportedResource.class);
    }

    private class NotSupportedResource extends ServerResource {

    }
}
