package org.bonitasoft.web.rest.server.api.custom;

import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;

import ro.isdc.wro.http.support.RedirectedStreamServletResponseWrapper;

/**
 * @author Laurent Leseigneur
 */
public class CustomHttpServletResponse extends RedirectedStreamServletResponseWrapper{
    /**
     * @param outputStream the stream where the response will be written.
     * @param response
     */
    public CustomHttpServletResponse(OutputStream outputStream, HttpServletResponse response) {
        super(outputStream, response);
    }
}
