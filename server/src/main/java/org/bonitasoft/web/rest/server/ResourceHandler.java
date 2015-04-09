package org.bonitasoft.web.rest.server;

import org.bonitasoft.web.rest.server.api.bpm.flownode.ContextResultElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fabiolombardi on 09/04/2015.
 */
public class ResourceHandler {

    List<ResourceFinder> resourceFinders = new ArrayList<>();

    public void addResource(ResourceFinder finder) {
        resourceFinders.add(finder);
    }

    public ResourceFinder getResourceFinderFor(Serializable object) {
        for (ResourceFinder resourceFinder : resourceFinders) {
            if(resourceFinder.handlesResource(object)){
                return resourceFinder;
            }
        }
        return null;
    }

    public Serializable getContextResultElement(Serializable object) {
        ResourceFinder resourceFinderFor = getResourceFinderFor(object);
        if (resourceFinderFor != null) {
            return resourceFinderFor.getContextResultElement(object);
        }
        return object;
    }
}
