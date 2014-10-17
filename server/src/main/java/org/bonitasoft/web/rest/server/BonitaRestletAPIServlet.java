package org.bonitasoft.web.rest.server;


import org.bonitasoft.web.rest.server.api.bpm.flownode.TaskVariableResource;
import org.bonitasoft.web.rest.server.api.bpm.flownode.TimerEventTriggerResource;
import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class BonitaRestletAPIServlet extends Application {

    /**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public synchronized Restlet createInboundRoot() {

        final Context context = getContext();
        final Router router = new Router(context);

        router.attach("/bpm/taskVariable", TaskVariableResource.class);
        // GET to search timer event triggers:
        router.attach("/bpm/timerEventTrigger", TimerEventTriggerResource.class);
        // PUT to update timer event trigger date:
        router.attach("/bpm/timerEventTrigger/{id}", TimerEventTriggerResource.class);

        return router;
    }

}
