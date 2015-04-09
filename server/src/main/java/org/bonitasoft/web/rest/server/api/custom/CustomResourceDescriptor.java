package org.bonitasoft.web.rest.server.api.custom;

/**
 * @author Laurent Leseigneur
 */
public interface CustomResourceDescriptor {

    String getPathTemplate();

    String getMethod();

    String getPageName();

}
