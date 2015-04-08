package org.bonitasoft.web.rest.server.api.custom;

import org.restlet.data.Method;

/**
 * @author Laurent Leseigneur
 */
public interface CustomResourceDescriptor {

    String getPathTemplate();

    Method getMethod();

    String getPageName();

}
