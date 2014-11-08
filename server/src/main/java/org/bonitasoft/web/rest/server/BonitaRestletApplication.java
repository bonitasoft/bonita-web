package org.bonitasoft.web.rest.server;

import java.util.logging.Level;

import org.bonitasoft.web.rest.server.api.bpm.flownode.ActivityVariableResource;
import org.bonitasoft.web.rest.server.api.bpm.flownode.TimerEventTriggerResource;
import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.engine.Engine;
import org.restlet.routing.Router;

public class BonitaRestletApplication extends Application {

    /**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public synchronized Restlet createInboundRoot() {

        final Context context = getContext();
        final Router router = new Router(context);
        // GET an activityData:
        router.attach("/bpm/activityVariable/{" + ActivityVariableResource.ACTIVITYDATA_ACTIVITY_ID + "}/{" + ActivityVariableResource.ACTIVITYDATA_DATA_NAME
                + "}", ActivityVariableResource.class);
        // GET to search timer event triggers:
        router.attach("/bpm/timerEventTrigger", TimerEventTriggerResource.class);
        // PUT to update timer event trigger date:
        router.attach("/bpm/timerEventTrigger/{" + TimerEventTriggerResource.ID_PARAM_NAME + "}", TimerEventTriggerResource.class);

        return router;
    }

    @Override
    public void handle(final Request request, final Response response) {
        request.setLoggable(false);
        Engine.setLogLevel(Level.OFF);
        Engine.setRestletLogLevel(Level.OFF);
        // New Restlet APIs:
        super.handle(request, response);
    }

}
