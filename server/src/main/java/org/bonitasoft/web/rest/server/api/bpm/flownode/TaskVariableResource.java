package org.bonitasoft.web.rest.server.api.bpm.flownode;

import org.bonitasoft.engine.api.ProfileAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.profile.Profile;
import org.bonitasoft.engine.profile.ProfileNotFoundException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.ext.servlet.ServletUtils;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class TaskVariableResource extends ServerResource {

    private APISession sessionSingleton = null;

    /**
     * Get the session to access the engine SDK
     */
    protected APISession getEngineSession() {
        if (sessionSingleton == null) {
            sessionSingleton = (APISession) ServletUtils.getRequest(getRequest()).getSession().getAttribute("apiSession");
        }
        return sessionSingleton;
    }

    @SuppressWarnings("rawtypes")
    @Get
    public Representation doGet() {

        String jsonString = "";
        //        final Map<String, Object> params = getRequestAttributes();

        try {
            final Profile engineReturn = getProfile();
            jsonString = toJson(engineReturn);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return new StringRepresentation(jsonString, MediaType.APPLICATION_JSON);
    }

    private String toJson(final Profile p) {
        final JSONObject jsonObj = new JSONObject(p);
        return jsonObj.toString();

    }

    //    protected ProcessAPI getEngineProcessAPI() {
    //        try {
    //            return TenantAPIAccessor.getProcessAPI(getEngineSession());
    //        } catch (final Exception e) {
    //            throw new APIException(e);
    //        }
    //    }

    protected ProfileAPI getEngineProfileAPI() {
        try {
            return TenantAPIAccessor.getProfileAPI(getEngineSession());
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    private Profile getProfile() throws ProfileNotFoundException {
        return getEngineProfileAPI().getProfile(1L);
    }
}
