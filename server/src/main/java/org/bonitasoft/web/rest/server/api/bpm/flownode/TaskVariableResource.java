package org.bonitasoft.web.rest.server.api.bpm.flownode;

import org.bonitasoft.engine.profile.Profile;
import org.bonitasoft.engine.profile.ProfileNotFoundException;
import org.bonitasoft.web.rest.server.api.resource.CommonResource;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.restlet.resource.Get;

public class TaskVariableResource extends CommonResource {

    @Get("json")
    public String getTaskVariable() {

        try {
            final Profile engineReturn = getProfile();
            return toJson(engineReturn);
        } catch (final Exception e) {
            throw new APIException(e);
        }
    }

    private Profile getProfile() throws ProfileNotFoundException {
        System.err.println("*************************************");
        System.err.println("!! return ALWAYS profile with ID 1 !!");
        System.err.println("*************************************");
        return getEngineProfileAPI().getProfile(1L);
    }
}
