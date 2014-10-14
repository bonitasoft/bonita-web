package org.bonitasoft.web.rest.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.bonitasoft.web.rest.server.api.bpm.flownode.TaskVariableResource;
import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.engine.Engine;
import org.restlet.ext.servlet.ServletUtils;
import org.restlet.routing.Router;

public class BonitaRestletAPIServlet extends Application {

    protected static List<String> OLD_APIs = new ArrayList<String>(80);
    static {
        OLD_APIs.addAll(Arrays.asList(
                "/bpm/activity",
                "/bpm/actor",
                "/bpm/actorMember",
                "/bpm/archivedActivity",
                "/bpm/archivedCase",
                "/bpm/archivedComment",
                "/bpm/archivedConnectorInstance",
                "/bpm/archiveddocument",
                "/bpm/archivedFlowNode",
                "/bpm/archivedHumanTask",
                "/bpm/archivedTask",
                "/bpm/archivedUserTask",
                "/bpm/case",
                "/bpm/caseDocument",
                "/bpm/caseVariable",
                "/bpm/category",
                "/bpm/comment",
                "/bpm/connectorInstance",
                "/bpm/delegation",
                "/bpm/document",
                "/bpm/document",
                "/bpm/flowNode",
                "/bpm/hiddenUserTask",
                "/bpm/humanTask",
                "/bpm/process",
                "/bpm/processCategory",
                "/bpm/processConnector",
                "/bpm/processConnectorDependency",
                "/bpm/processResolutionProblem",
                "/bpm/task",
                "/bpm/userTask",
                "/customuserinfo/definition",
                "/customuserinfo/user",
                "/customuserinfo/value",
                "/identity/group",
                "/identity/membership",
                "/identity/personalcontactdata",
                "/identity/professionalcontactdata",
                "/identity/role",
                "/identity/user",
                "/platform/platform",
                "/portal/profile",
                "/portal/profileEntry",
                "/portal/profileMember",
                "/system/i18nlocale",
                "/system/i18ntranslation",
                "/system/session",
                "/userXP/profile",
                "/userXP/profileEntry",
                "/userXP/profileMember"
                ));
    }

    /**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public synchronized Restlet createInboundRoot() {

        final Context context = getContext();
        final Router router = new Router(context);

        router.attach("/bpm/taskVariable", TaskVariableResource.class);
        return router;
    }

    @Override
    public void handle(final Request request, final Response response) {
        // Old rest APIs:
        final HttpServletRequest httpRequest = getHttpRequest(request);
        final String pathInfo = httpRequest.getPathInfo();
        if (isV1RestApi(pathInfo)) {
            //should we disable it?
            Engine.setLogLevel(Level.OFF);
            Engine.setRestletLogLevel(Level.OFF);
            try {
                //ServletUtils.getResponse(response).sendRedirect("/APIv1");
                final ServletResponse httpResponse = getHttpResponse(response);
                //httpRequest.setCharacterEncoding("UTF-8");
                httpResponse.setCharacterEncoding("UTF-8");
                final RequestDispatcher rd = httpRequest.getRequestDispatcher("/APIv1");
                rd.include(httpRequest, httpResponse);
            } catch (final IOException e) {
                e.printStackTrace();
            } catch (final ServletException e) {
                e.printStackTrace();
            }

            return;
        }
        // New Restlet APIs:
        super.handle(request, response);
    }

    private boolean isV1RestApi(final String pathInfo) {
        for (final String url : OLD_APIs) {
            if (pathInfo.startsWith(url)) {
                return true;
            }
        }
        return false;
    }

    /**
     * gets the servlet response from the restlet response
     *
     * @param response
     * @return ServletResponse
     */
    private ServletResponse getHttpResponse(final Response response) {
        return ServletUtils.getResponse(response);
    }

    /**
     * gets the httpRequest from the restlet request
     *
     * @param request
     * @return HttpServletRequest
     */
    private HttpServletRequest getHttpRequest(final Request request) {
        return ServletUtils.getRequest(request);
    }

}
