package org.bonitasoft.web.rest.server.api.bpm.flownode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.flownode.TimerEventTriggerInstanceNotFoundException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.test.toolkit.organization.TestUserFactory;
import org.bonitasoft.web.rest.server.AbstractConsoleTest;
import org.junit.Test;

public class TimerEventTriggerResourceIT extends AbstractConsoleTest {

    // For Integration tests:
    TimerEventTriggerResource restResource = spy(new TimerEventTriggerResource());

    @Override
    public void consoleTestSetUp() throws Exception {
        doReturn(mock(HttpServletRequest.class)).when(restResource).getHttpRequest();
        final HttpSession session = mock(HttpSession.class);
        doReturn(session).when(restResource).getHttpSession();
        final APISession apiSession = TestUserFactory.getJohnCarpenter().getSession();
        //new APISessionImpl(14L, new Date(), 3000000L, "username", 1L, "default", 1L);
        doReturn(apiSession).when(session).getAttribute("apiSession");
        doReturn(0).when(restResource).getIntegerParameter(anyString(), anyBoolean());
        doReturn(0L).when(restResource).getLongParameter(anyString(), anyBoolean());
        doReturn("").when(restResource).getParameter(anyString(), anyBoolean());
        doReturn(Collections.emptyList()).when(restResource).getParameterAsList(anyString());
    }

    @Test
    public void searchTimerEventTriggersShouldReturnStatusCode200() throws IOException {
        assertThat(restResource.searchTimerEventTriggers().toString()).isEqualTo("[]");
    }

    @Test(expected = TimerEventTriggerInstanceNotFoundException.class)
    public void updateTimerEventTriggersShouldThrowExceptionIfTimerNotFound() throws Exception {
        doReturn("1").when(restResource).getAttribute(TimerEventTriggerResource.ID_PARAM_NAME);

        final TimerEventTrigger trigger = new TimerEventTrigger();
        trigger.setExecutionDate(System.currentTimeMillis());
        restResource.updateTimerEventTrigger(trigger);
    }

    @Test
    public void updateTimerEventTriggersShouldReturnStatusCode200() throws Exception {
        final long timerEventTriggerId = 1L;
        doReturn("" + timerEventTriggerId).when(restResource).getAttribute(TimerEventTriggerResource.ID_PARAM_NAME);
        final ProcessAPI processAPI = mock(ProcessAPI.class);
        doReturn(processAPI).when(restResource).getEngineProcessAPI();
        final Date date = new Date();
        doReturn(date).when(processAPI).updateExecutionDateOfTimerEventTriggerInstance(eq(timerEventTriggerId), any(Date.class));
        final TimerEventTrigger trigger = new TimerEventTrigger();
        trigger.setExecutionDate(System.currentTimeMillis());
        assertThat(restResource.updateTimerEventTrigger(trigger)).isEqualTo(date.getTime());
    }
}
