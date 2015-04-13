package org.bonitasoft.web.rest.server.api.extension;

/**
 * @author Laurent Leseigneur
 */
public interface ResourceExtensionDescriptor {

    String getPathTemplate();

    String getMethod();

    String getPageName();

}
