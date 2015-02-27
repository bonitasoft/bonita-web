/*******************************************************************************
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.server.utils;

import org.bonitasoft.web.rest.server.api.resource.CommonResource;
import org.restlet.resource.Get;

public class FakeResource extends CommonResource {

    private final FakeService fakeService;

    public FakeResource(final FakeService fakeService) {
        this.fakeService = fakeService;
    }

    @Get
    public String doSomething() throws Exception {
        return fakeService.saySomething();
    }

    public static interface FakeService {

        public String saySomething() throws Exception;
    }
}
