/*******************************************************************************
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.server.utils;

import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.resource.Finder;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Router;

public class RestletAppBuilder {

    public static RestletAppBuilder aTestApp() {
        return new RestletAppBuilder();
    }
    
    public Application attach(String pathTemplate, final ServerResource resource) {
        Application app = new Application();
        Router router = new Router();
        router.attach(pathTemplate, new Finder() {
            @Override
            public ServerResource create(Request request, Response response) {
               return resource;
            }
        });
        app.setInboundRoot(router);
        return app;
    }
}
