package org.bonitasoft.web.rest.server.api.bpm.process;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.engine.bpm.process.ProcessInstanceState;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.ModelFactory;
import org.bonitasoft.web.rest.model.bpm.cases.CaseItem;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.rest.server.datastore.bpm.cases.CaseDatastore;
import org.bonitasoft.web.rest.server.datastore.bpm.process.ProcessDatastore;
import org.bonitasoft.web.rest.server.framework.APIServletCall;
import org.bonitasoft.web.toolkit.client.ItemDefinitionFactory;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Alexis Hassler
 * @author Celine Souchet
 */
@RunWith(MockitoJUnitRunner.class)
public class APIProcessTest {

    private APIProcess apiProcess;

    @Mock
    private APIServletCall caller;

    @Mock
    private HttpSession session;

    @Mock
    private APISession engineSession;

    @Mock
    private ProcessDatastore processDatastore;

    @Mock
    private CaseDatastore caseDatastore;

    @Mock
    private WebBonitaConstantsUtils webBonitaConstantsUtils;

    @Before
    public void consoleTestSetUp() throws IOException {
        ItemDefinitionFactory.setDefaultFactory(new ModelFactory());
        org.bonitasoft.console.common.server.i18n.I18n.getInstance();
        given(caller.getHttpSession()).willReturn(session);
        given(session.getAttribute("apiSession")).willReturn(engineSession);
        given(engineSession.getTenantId()).willReturn(1L);

        apiProcess = spy(new APIProcess());
        apiProcess.setCaller(caller);
        doReturn(processDatastore).when(apiProcess).getProcessDatastore();
        doReturn(caseDatastore).when(apiProcess).getCaseDatastore();
    }

    /**
     * Test method for {@link org.bonitasoft.web.rest.server.api.bpm.process.APIProcess#fillCounters(ProcessItem, List).
     */
    @Test
    public final void fillCounters_should_fill_number_of_failed_cases_when_counter_of_failed_cases_is_active() {
        // Given
        final APIID id = APIID.makeAPIID(78L);
        final ProcessItem item = mock(ProcessItem.class);
        doReturn(id).when(item).getId();

        final List<String> counters = Arrays.asList(ProcessItem.COUNTER_FAILED_CASES);

        final long numberOfFailedCases = 2L;
        final Map<String, String> filters = new HashMap<>();
        filters.put(CaseItem.FILTER_CALLER, "any");
        filters.put(CaseItem.ATTRIBUTE_PROCESS_ID, item.getId().toString());
        filters.put(CaseItem.FILTER_STATE, ProcessInstanceState.ERROR.name());
        doReturn(numberOfFailedCases).when(caseDatastore).count(null, null, filters);

        // When
        apiProcess.fillCounters(item, counters);

        // Then
        verify(item).setAttribute(ProcessItem.COUNTER_FAILED_CASES, numberOfFailedCases);
    }

    /**
     * Test method for {@link org.bonitasoft.web.rest.server.api.bpm.process.APIProcess#fillCounters(ProcessItem, List).
     */
    @Test
    public final void fillCounters_should_do_nothing_when_counter_of_failed_cases_is_not_active() {
        // Given
        final ProcessItem item = mock(ProcessItem.class);
        final List<String> counters = new ArrayList<>();

        // When
        apiProcess.fillCounters(item, counters);

        // Then
        verify(item, never()).setAttribute(anyString(), anyLong());
    }

    /**
     * Test method for {@link org.bonitasoft.web.rest.server.api.bpm.process.APIProcess#fillCounters(ProcessItem, List).
     */
    @Test
    public final void fillCounters_should_fill_number_of_open_cases_when_counter_of_open_cases_is_active() {
        // Given
        final APIID id = APIID.makeAPIID(78L);
        final ProcessItem item = mock(ProcessItem.class);
        doReturn(id).when(item).getId();

        final List<String> counters = Arrays.asList(ProcessItem.COUNTER_OPEN_CASES);
        final Map<String, String> filters = new HashMap<>();
        filters.put(CaseItem.FILTER_CALLER, "any");
        filters.put(CaseItem.ATTRIBUTE_PROCESS_ID, id.toString());

        final long numberOfOpenCases = 2L;
        doReturn(numberOfOpenCases).when(caseDatastore).count(null, null, filters);

        // When
        apiProcess.fillCounters(item, counters);

        // Then
        verify(item).setAttribute(ProcessItem.COUNTER_OPEN_CASES, numberOfOpenCases);
    }

    /**
     * Test method for {@link org.bonitasoft.web.rest.server.api.bpm.process.APIProcess#fillCounters(ProcessItem, List).
     */
    @Test
    public final void fillCounters_should_do_nothing_when_counter_of_open_cases_is_not_active() {
        // Given
        final ProcessItem item = mock(ProcessItem.class);
        final List<String> counters = new ArrayList<>();

        // When
        apiProcess.fillCounters(item, counters);

        // Then
        verify(item, never()).setAttribute(anyString(), anyLong());
    }
}
