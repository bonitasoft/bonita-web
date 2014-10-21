package org.bonitasoft.web.rest.server.api.bpm.flownode;

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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.flownode.TimerEventTriggerInstance;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.impl.APISessionImpl;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.junit.Test;

public class TimerEventTriggerResourceTest {

    @Test
    public void searchTimerEventTriggersShouldCallEngine() throws Exception {
        // given:
        final TimerEventTriggerResource spy = spy(new TimerEventTriggerResource());
        final SearchOptions searchOptions = mock(SearchOptions.class);
        doReturn(1L).when(spy).getLongParameter(anyString(), anyBoolean());
        doReturn(searchOptions).when(spy).buildSearchOptions();
        doReturn(new ArrayList<TimerEventTriggerInstance>()).when(spy).runEngineSearch(anyLong(), eq(searchOptions));

        // when:
        spy.searchTimerEventTriggers();

        // then:
        verify(spy).runEngineSearch(1L, searchOptions);
    }

    @Test(expected = APIException.class)
    public void searchTimerEventTriggersShouldThrowExceptionIfParameterNotFound() throws Exception {
        // given:
        final TimerEventTriggerResource spy = spy(new TimerEventTriggerResource());
        doReturn(null).when(spy).getParameter(anyString(), anyBoolean());

        // when:
        spy.searchTimerEventTriggers();
    }

    @Test(expected = APIException.class)
    public void updateShouldHandleNullID() throws Exception {
        final TimerEventTriggerResource spy = spy(new TimerEventTriggerResource());
        doReturn(Collections.EMPTY_MAP).when(spy).getRequestAttributes();

        spy.updateTimerEventTrigger(null);
    }

    @Test
    public void updateTimerEventTriggersShouldReturnStatusEngineReturnedDate() throws Exception {
        final TimerEventTriggerResource restResource = spy(new TimerEventTriggerResource());

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
        final ProcessAPI processAPI = mock(ProcessAPI.class);
        doReturn(processAPI).when(restResource).getEngineProcessAPI();
        final Date date = new Date();
        final TimerEventTrigger timerEventTrigger = new TimerEventTrigger();
        timerEventTrigger.setExecutionDate(date.getTime());
        doReturn(date).when(processAPI).updateExecutionDateOfTimerEventTriggerInstance(eq(timerEventTriggerId), any(Date.class));
        assertThat(restResource.updateTimerEventTrigger(timerEventTrigger)).isEqualTo(date.getTime());
    }
}
