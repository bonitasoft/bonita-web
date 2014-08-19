package org.bonitasoft.web.server.rest.inject;

import org.bonitasoft.engine.api.ProcessAPI;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class EngineAPIsBinder extends AbstractBinder {

    @Override
    protected void configure() {
        bindFactory(ProcessAPIFactory.class).to(ProcessAPI.class);
    }

}
