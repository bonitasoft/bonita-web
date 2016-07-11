package org.bonitasoft.web.rest.server.api.bpm.flownode;

import static net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.flownode.TimerEventTriggerInstance;
import org.bonitasoft.engine.bpm.flownode.impl.internal.TimerEventTriggerInstanceImpl;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.search.impl.SearchResultImpl;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.impl.APISessionImpl;
import org.bonitasoft.web.rest.server.BonitaRestletApplication;
import org.bonitasoft.web.rest.server.utils.RestletTest;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.Response;
import org.restlet.resource.ServerResource;

@RunWith(MockitoJUnitRunner.class)
public class TimerEventTriggerResourceTest extends RestletTest {

    @Mock
    ProcessAPI processAPI;

    private TimerEventTriggerResource restResource;

    @Before
    public void initializeMocks() {
        restResource = spy(new TimerEventTriggerResource(processAPI));
    }

    @Override
    protected ServerResource configureResource() {
        return new TimerEventTriggerResource(processAPI);
    }

    @Test
    public void searchTimerEventTriggersShouldCallEngine() throws Exception {
        // given:
        final SearchOptions searchOptions = mock(SearchOptions.class);
        doReturn(1L).when(restResource).getLongParameter(anyString(), anyBoolean());
        doReturn(searchOptions).when(restResource).buildSearchOptions();
        doReturn(new ArrayList<TimerEventTriggerInstance>()).when(restResource).runEngineSearch(anyLong(), eq(searchOptions));

        // when:
        restResource.searchTimerEventTriggers();

        // then:
        verify(restResource).runEngineSearch(1L, searchOptions);
    }

    @Test(expected = APIException.class)
    public void searchTimerEventTriggersShouldThrowExceptionIfParameterNotFound() throws Exception {
        // given:
        doReturn(null).when(restResource).getParameter(anyString(), anyBoolean());

        // when:
        restResource.searchTimerEventTriggers();
    }

    @Test(expected = APIException.class)
    public void updateShouldHandleNullID() throws Exception {
        doReturn(Collections.EMPTY_MAP).when(restResource).getRequestAttributes();

        restResource.updateTimerEventTrigger(null);
    }

    @Test
    public void updateTimerEventTriggersShouldReturnStatusEngineReturnedDate() throws Exception {

        doReturn(mock(HttpServletRequest.class)).when(restResource).getHttpRequest();
        final HttpSession session = mock(HttpSession.class);
        doReturn(session).when(restResource).getHttpSession();
        final APISession apiSession = new APISessionImpl(14L, new Date(), 3000000L, "username", 1L, "default", 1L);
        doReturn(apiSession).when(session).getAttribute("apiSession");
        doReturn(0).when(restResource).getIntegerParameter(anyString(), anyBoolean());
        doReturn(0L).when(restResource).getLongParameter(anyString(), anyBoolean());
        doReturn("").when(restResource).getParameter(anyString(), anyBoolean());
        doReturn(Collections.emptyList()).when(restResource).getParameterAsList(anyString());

        final long timerEventTriggerId = 1L;
        doReturn("" + timerEventTriggerId).when(restResource).getAttribute(TimerEventTriggerResource.ID_PARAM_NAME);
        final Date date = new Date();
        final TimerEventTrigger timerEventTrigger = new TimerEventTrigger();
        timerEventTrigger.setExecutionDate(date.getTime());
        doReturn(date).when(processAPI).updateExecutionDateOfTimerEventTriggerInstance(eq(timerEventTriggerId), any(Date.class));
        assertThat(restResource.updateTimerEventTrigger(timerEventTrigger).getExecutionDate()).isEqualTo(timerEventTrigger.getExecutionDate());
    }

    @Test
    public void should_TimerEventTrigger_return_number_as_strings() throws Exception {
        //given
        final SearchResult<TimerEventTriggerInstance> searchResult = createSearchResult();
        doReturn(searchResult).when(processAPI).searchTimerEventTriggerInstances(anyLong(), any(SearchOptions.class));

        //when
        final Response response = request(BonitaRestletApplication.BPM_TIMER_EVENT_TRIGGER_URL + "?caseId=2&p=0&c=10").get();

        //then
        assertJsonEquals(getJson("timerEventTriggerInstances.json"), response.getEntityAsText());
    }

    @Test
    public void should_update_TimerEventTrigger_return_number_as_strings() throws Exception {
        //given
        doReturn(new Date(Long.MAX_VALUE)).when(processAPI).updateExecutionDateOfTimerEventTriggerInstance(anyLong(), any(Date.class));

        //when
        final Response response = request(BonitaRestletApplication.BPM_TIMER_EVENT_TRIGGER_URL + "/2").put("{\"executionDate\": 9223372036854775807}");

        //then
        assertJsonEquals(getJson("timerEventTriggerInstance.json"), response.getEntityAsText());
    }

    private SearchResult<TimerEventTriggerInstance> createSearchResult() {
        final TimerEventTriggerInstance timerEventTriggerInstance1 = new TimerEventTriggerInstanceImpl(1L, 2L, "name", new Date(123L));
        final List<TimerEventTriggerInstance> instances = new ArrayList<>();
        instances.add(timerEventTriggerInstance1);

        return new SearchResultImpl<>(1L, instances);

    }
}
