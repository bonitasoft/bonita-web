package org.bonitasoft.web.rest.server.api.extension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import javax.servlet.http.HttpServletRequest;

import org.bonitasoft.console.common.server.page.RestApiRenderer;
import org.bonitasoft.engine.exception.BonitaException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.Request;
import org.restlet.data.Method;
import org.restlet.representation.Representation;

/**
 * @author Laurent Leseigneur
 */
@RunWith(MockitoJUnitRunner.class)
public class ApiExtensionResourceTest {

    public static final String RETURN_VALUE = "{'return':'value'}";
    public static final String CUSTOM_PAGE_NAME = "custom_pageName";
    public static final String GET = "GET";

    @Mock
    private ResourceExtensionDescriptor resourceExtensionDescriptor;

    @Mock
    private RestApiRenderer restApiRenderer;

    @Mock
    private Request request;

    @InjectMocks
    @Spy
    ApiExtensionResource apiExtensionResource;

    private Method method;


    @Before
    public void before() throws Exception {
        doReturn(request).when(apiExtensionResource).getRequest();

    }

    @Test
    public void should_handle_return_result() throws Exception {
        //given
        method = new Method(GET);
        doReturn(method).when(request).getMethod();
        doReturn(GET).when(resourceExtensionDescriptor).getMethod();
        doReturn(CUSTOM_PAGE_NAME).when(resourceExtensionDescriptor).getPageName();

        doReturn(RETURN_VALUE).when(restApiRenderer).handleRestApiCall(any(HttpServletRequest.class), eq(CUSTOM_PAGE_NAME));

        //when
        final Representation representation = apiExtensionResource.doHandle();

        //then
        assertThat(representation.getText()).as("should return response").isEqualTo(RETURN_VALUE);
    }

    @Test
    public void should_handle_return_empty_response() throws Exception {
        //given
        method = new Method(GET);
        doReturn(method).when(request).getMethod();
        doReturn(GET).when(resourceExtensionDescriptor).getMethod();
        doReturn(CUSTOM_PAGE_NAME).when(resourceExtensionDescriptor).getPageName();

        doReturn(null).when(restApiRenderer).handleRestApiCall(any(HttpServletRequest.class), eq(CUSTOM_PAGE_NAME));

        //when
        final Representation representation = apiExtensionResource.doHandle();

        //then
        assertThat(representation.getText()).as("should return response").isEqualTo("");
    }

    @Test
    public void should_handle_return_exception_message() throws Exception {
        //given
        method = new Method(GET);
        doReturn(method).when(request).getMethod();
        doReturn(GET).when(resourceExtensionDescriptor).getMethod();
        doReturn(CUSTOM_PAGE_NAME).when(resourceExtensionDescriptor).getPageName();

        doThrow(BonitaException.class).when(restApiRenderer).handleRestApiCall(any(HttpServletRequest.class), eq(CUSTOM_PAGE_NAME));

        //when
        final Representation representation = apiExtensionResource.doHandle();

        //then
        assertThat(representation.getText()).as("should return error message").isEqualTo("error while getting result");
    }


    @Test
    public void should_handle_wrong_method() throws Exception {
        //given
        method = new Method(GET);
        doReturn(method).when(request).getMethod();
        doReturn("POST").when(resourceExtensionDescriptor).getMethod();

        //when
        final Representation representation = apiExtensionResource.doHandle();

        //then
        assertThat(representation.getText()).as("should return page response").isEqualTo("method GET is not supported");
    }


}