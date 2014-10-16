package org.bonitasoft.web.rest.server;


import org.bonitasoft.web.rest.server.api.bpm.flownode.TaskVariableResource;
import org.bonitasoft.web.rest.server.api.bpm.flownode.TimerEventTriggerResource;
import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class BonitaRestletAPIServlet extends Application {

    //    protected static List<String> OLD_APIs = Arrays.asList(
    //            "/bpm/activity",
    //            "/bpm/actor",
    //            "/bpm/actorMember",
    //            "/bpm/archivedActivity",
    //            "/bpm/archivedCase",
    //            "/bpm/archivedComment",
    //            "/bpm/archivedConnectorInstance",
    //            "/bpm/archiveddocument",
    //            "/bpm/archivedFlowNode",
    //            "/bpm/archivedHumanTask",
    //            "/bpm/archivedTask",
    //            "/bpm/archivedUserTask",
    //            "/bpm/case",
    //            "/bpm/caseDocument",
    //            "/bpm/caseVariable",
    //            "/bpm/category",
    //            "/bpm/comment",
    //            "/bpm/connectorInstance",
    //            "/bpm/delegation",
    //            "/bpm/document",
    //            "/bpm/document",
    //            "/bpm/flowNode",
    //            "/bpm/hiddenUserTask",
    //            "/bpm/humanTask",
    //            "/bpm/process",
    //            "/bpm/processCategory",
    //            "/bpm/processConnector",
    //            "/bpm/processConnectorDependency",
    //            "/bpm/processResolutionProblem",
    //            "/bpm/task",
    //            "/bpm/userTask",
    //            "/customuserinfo/definition",
    //            "/customuserinfo/user",
    //            "/customuserinfo/value",
    //            "/identity/group",
    //            "/identity/membership",
    //            "/identity/personalcontactdata",
    //            "/identity/professionalcontactdata",
    //            "/identity/role",
    //            "/identity/user",
    //            "/platform/platform",
    //            "/portal/profile",
    //            "/portal/profileEntry",
    //            "/portal/profileMember",
    //            "/system/i18nlocale",
    //            "/system/i18ntranslation",
    //            "/system/session",
    //            "/userXP/profile",
    //            "/userXP/profileEntry",
    //            "/userXP/profileMember"
    //            );

    /**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public synchronized Restlet createInboundRoot() {

        final Context context = getContext();
        final Router router = new Router(context);

        router.attach("/bpm/taskVariable", TaskVariableResource.class);
        router.attach("/bpm/taskVariable", TimerEventTriggerResource.class);

        return router;
    }

}
