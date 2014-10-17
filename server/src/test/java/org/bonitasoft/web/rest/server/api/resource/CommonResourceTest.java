package org.bonitasoft.web.rest.server.api.resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.web.rest.server.framework.APIServletCall;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.junit.Test;

public class CommonResourceTest {

    @Test
    public void getParameterShouldNotVerifyNotNullIfNotMandatory() throws Exception {
        // given:
        final CommonResource spy = spy(new CommonResource());
        final String parameterName = "any string parameter name";
        final String parameterValue = "some value";
        doReturn(parameterValue).when(spy).getRequestParameter(anyString());

        // when:
        spy.getParameter(parameterName, false);

        // then:
        verify(spy, times(0)).verifyNotNullParameter(parameterValue, parameterName);
    }

    @Test
    public void getParameterShouldVerifyNotNullIfMandatory() throws Exception {
        // given:
        final CommonResource spy = spy(new CommonResource());
        doReturn(null).when(spy).getRequestParameter(anyString());
        doNothing().when(spy).verifyNotNullParameter(anyObject(), anyString());
        final String parameterName = "any string parameter name";

        // when:
        spy.getParameter(parameterName, true);

        // then:
        verify(spy).verifyNotNullParameter(null, parameterName);
    }

    @Test
    public void getSearchOrderMustRetrieveProperParameter() throws Exception {
        final CommonResource spy = spy(new CommonResource());
        doReturn("dummy sort value").when(spy).getParameter(anyString(), anyBoolean());
        spy.getSearchOrder();

        verify(spy).getParameter(APIServletCall.PARAMETER_ORDER, false);
    }

    @Test
    public void getSearchPageNumberMustRetrieveProperParameter() throws Exception {
        final CommonResource spy = spy(new CommonResource());
        doReturn(new Integer(88)).when(spy).getIntegerParameter(anyString(), anyBoolean());
        spy.getSearchPageNumber();

        verify(spy).getIntegerParameter(APIServletCall.PARAMETER_PAGE, false);
    }

    @Test
    public void getSearchPageSizeMustRetrieveProperParameter() throws Exception {
        final CommonResource spy = spy(new CommonResource());
        doReturn(new Integer(77)).when(spy).getIntegerParameter(anyString(), anyBoolean());
        spy.getSearchPageSize();

        verify(spy).getIntegerParameter(APIServletCall.PARAMETER_LIMIT, false);
    }

    @Test
    public void getSearchTermMustRetrieveProperParameter() throws Exception {
        final CommonResource spy = spy(new CommonResource());
        doReturn("lookFor").when(spy).getParameter(anyString(), anyBoolean());
        spy.getSearchTerm();

        verify(spy).getParameter(APIServletCall.PARAMETER_SEARCH, false);
    }

    @Test
    public void getMandatoryParameterShouldCheckNonNull() throws Exception {
        final CommonResource spy = spy(new CommonResource());
        final String parameterName = "name of the parameter";
        doNothing().when(spy).verifyNotNullParameter(any(Class.class), anyString());
        final String objectInParameterMap = "dummyString";
        doReturn(objectInParameterMap).when(spy).getRequestParameter(anyString());

        spy.getParameter(parameterName, true);

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

    @Test
    public void parseFilterShoulReturnNullIfListIsNull() throws Exception {
        assertThat(new CommonResource().parseFilters(null)).isNull();
    }

    @Test
    public void parseFilterShouldBuildExpectedMap() throws Exception {
        // given:
        final List<String> filters = new ArrayList<String>(2);
        filters.add("toto=17");
        filters.add("titi='EN_ECHEC'");

        // when:
        final Map<String, String> parseFilters = new CommonResource().parseFilters(filters);

        // then:
        assertThat(parseFilters.size()).isEqualTo(2);
        assertThat(parseFilters.get("toto")).isEqualTo("17");
        assertThat(parseFilters.get("titi")).isEqualTo("'EN_ECHEC'");
    }

    @Test
    public void parseFilterShouldBuildMapEvenIfNoValueForParam() throws Exception {
        // given:
        final List<String> filters = new ArrayList<String>(2);
        filters.add("nomatchingvalue=");

        // when:
        final Map<String, String> parseFilters = new CommonResource().parseFilters(filters);

        // then:
        assertThat(parseFilters.size()).isEqualTo(1);
        assertThat(parseFilters.get("nomatchingvalue")).isNull();
    }

    @Test
    public void getIntegerParameterShouldReturnNullIfgetParameterReturnsNull() throws Exception {
        // given:
        final CommonResource spy = spy(new CommonResource());
        doReturn(null).when(spy).getParameter(anyString(), anyBoolean());

        // then:
        assertThat(spy.getIntegerParameter("", false)).isNull();
        assertThat(spy.getIntegerParameter("", true)).isNull();
    }

    @Test
    public void getLongParameterShouldReturnNullIfgetParameterReturnsNull() throws Exception {
        // given:
        final CommonResource spy = spy(new CommonResource());
        doReturn(null).when(spy).getParameter(anyString(), anyBoolean());

        // then:
        assertThat(spy.getLongParameter("", false)).isNull();
        assertThat(spy.getLongParameter("", true)).isNull();
    }

    @Test
    public void buildSearchOptionsShouldCallAllGetxxxSearchParameterMethods() throws Exception {
        // given:
        final CommonResource spy = spy(new CommonResource());
        doReturn(null).when(spy).getSearchFilters();
        doReturn(null).when(spy).getSearchOrder();
        doReturn(0).when(spy).getSearchPageNumber();
        doReturn(10).when(spy).getSearchPageSize();
        doReturn(null).when(spy).getSearchTerm();

        // when:
        spy.buildSearchOptions();

        // then:
        verify(spy).getSearchFilters();
        verify(spy).getSearchOrder();
        verify(spy).getSearchPageNumber();
        verify(spy).getSearchPageSize();
        verify(spy).getSearchTerm();
    }
}
