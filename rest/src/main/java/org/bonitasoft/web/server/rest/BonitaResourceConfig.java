package org.bonitasoft.web.server.rest;

import org.bonitasoft.web.server.rest.inject.EngineAPIsBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class BonitaResourceConfig extends ResourceConfig {

    public BonitaResourceConfig() {
        packages("org.bonitasoft.web.server.rest.resources", "org.bonitasoft.web.server.rest.exception");
        register(JacksonFeature.class);
        register(new EngineAPIsBinder());
    }
}
