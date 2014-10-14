package org.bonitasoft.web.rest.server.api.resource;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.bonitasoft.web.rest.server.framework.APIServletCall;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.junit.Test;

public class CommonResourceTest {

    @Test
    public void getSearchPageNumberMustRetrieveProperParameter() throws Exception {
        final CommonResource spy = spy(new CommonResource());
        doReturn(new Integer(88)).when(spy).getIntegerParameter(anyString());
        spy.getSearchPageNumber();

        verify(spy).getIntegerParameter(APIServletCall.PARAMETER_PAGE);
    }

    @Test
    public void getSearchPageSizeMustRetrieveProperParameter() throws Exception {
        final CommonResource spy = spy(new CommonResource());
        doReturn(new Integer(77)).when(spy).getIntegerParameter(anyString());
        spy.getSearchPageSize();

        verify(spy).getIntegerParameter(APIServletCall.PARAMETER_LIMIT);
    }

    @Test
    public void getSearchTermMustRetrieveProperParameter() throws Exception {
        final CommonResource spy = spy(new CommonResource());
        doReturn("lookFor").when(spy).getParameter(anyString());
        spy.getSearchTerm();

        verify(spy).getParameter(APIServletCall.PARAMETER_SEARCH);
    }

    @Test
    public void getMandatoryParameterShouldCheckNonNull() throws Exception {
        final CommonResource spy = spy(new CommonResource());
        final String parameterName = "name of the parameter";
        doNothing().when(spy).verifyNotNullParameter(any(Class.class), anyString());
        final String objectInParameterMap = "dummyString";
        doReturn(objectInParameterMap).when(spy).getParameter(anyString());

        spy.getMandatoryParameter(Object.class, parameterName);

        verify(spy).verifyNotNullParameter(objectInParameterMap, parameterName);
    }

    @Test(expected = APIException.class)
    public void nullMandatoryParameterIsForbidden() throws Exception {
        new CommonResource().verifyNotNullParameter(null, "unused");
    }

    @Test
    public void notNullMandatoryParameterIsForbidden() throws Exception {
        new CommonResource().verifyNotNullParameter(new Object(), "unused");
        // no Exception
    }

}
