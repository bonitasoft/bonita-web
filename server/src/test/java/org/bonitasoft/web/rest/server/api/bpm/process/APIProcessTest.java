package org.bonitasoft.web.rest.server.api.bpm.process;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstants;
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
import org.bonitasoft.web.toolkit.client.common.exception.api.APIForbiddenException;
import org.bonitasoft.web.toolkit.client.common.util.StringUtil;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
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

    @Rule
    public TemporaryFolder folderRule = new TemporaryFolder();

    private String savedBonitaHomeProperty;
    
    @Before
    public void consoleTestSetUp() {
        ItemDefinitionFactory.setDefaultFactory(new ModelFactory());
        org.bonitasoft.console.common.server.i18n.I18n.getInstance();
        given(caller.getHttpSession()).willReturn(session);
        given(session.getAttribute("apiSession")).willReturn(engineSession);
        given(engineSession.getTenantId()).willReturn(1L);
        savedBonitaHomeProperty = System.getProperty(WebBonitaConstants.BONITA_HOME);
        System.setProperty(WebBonitaConstants.BONITA_HOME, folderRule.getRoot().getPath());

        apiProcess = spy(new APIProcess());
        apiProcess.setCaller(caller);
        doReturn(processDatastore).when(apiProcess).getProcessDatastore();
        doReturn(caseDatastore).when(apiProcess).getCaseDatastore();
        doReturn(webBonitaConstantsUtils).when(apiProcess).getWebBonitaConstantsUtils();
        doNothing().when(apiProcess).deleteOldIconFile(any(APIID.class));
        doReturn(null).when(apiProcess).uploadIcon("Non empty");
        doReturn(null).when(apiProcess).uploadIcon("");
    }

    @After
    public void teardown() throws Exception {
        if (StringUtil.isBlank(savedBonitaHomeProperty)) {
            System.clearProperty(WebBonitaConstants.BONITA_HOME);
        } else {
            System.setProperty(WebBonitaConstants.BONITA_HOME, savedBonitaHomeProperty);
        }
    }
    
    @Test
    public void add_should_add_change_icon_path_when_specified() {
        // Given
        final ProcessItem processItem = mock(ProcessItem.class);
        when(processItem.getIcon()).thenReturn("Non empty");

        // When
        apiProcess.add(processItem);

        // Then
        verify(processItem, times(1)).setIcon(anyString());
    }

    @Test
    public void add_should_add_not_change_icon_path_when_not_specified() {
        // Given
        final ProcessItem processItem = mock(ProcessItem.class);
        when(processItem.getIcon()).thenReturn("");

        // When
        apiProcess.add(processItem);

        // Then
        verify(processItem, never()).setIcon(anyString());
    }

    @Test
    public void update_should_upload_icon_when_non_blank_icon_attribute() {
        // Given
        final APIID apiid = APIID.makeAPIID("");
        final ProcessItem processItem = mock(ProcessItem.class);
        when(processItem.getIcon()).thenReturn("");
        when(processDatastore.get(apiid)).thenReturn(processItem);

        final Map<String, String> attributes = new HashMap<String, String>();
        attributes.put(ProcessItem.ATTRIBUTE_ICON, "Non empty");

        // When
        apiProcess.update(apiid, attributes);

        // Then
        verify(apiProcess).uploadIcon(anyString());
        verify(apiProcess).deleteOldIconFile(apiid);
        verify(apiProcess, times(1)).getProcessDatastore();
    }

    @Test
    public void update_should_not_upload_icon_when_blank_icon_attribute() {
        // Given
        final Map<String, String> attributes = new HashMap<String, String>();
        attributes.put(ProcessItem.ATTRIBUTE_ICON, "");

        // When
        apiProcess.update(APIID.makeAPIID(""), attributes);

        // Then
        verify(apiProcess, never()).uploadIcon(anyString());
        verify(apiProcess, times(1)).getProcessDatastore();
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
        final Map<String, String> filters = new HashMap<String, String>();
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
        final List<String> counters = new ArrayList<String>();

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
        final Map<String, String> filters = new HashMap<String, String>();
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
        final List<String> counters = new ArrayList<String>();

        // When
        apiProcess.fillCounters(item, counters);

        // Then
        verify(item, never()).setAttribute(anyString(), anyLong());
    }

    @Test(expected = APIForbiddenException.class)
    public void it_throws_an_exception_updating_icon_with_unauthorized_path() throws IOException {
        // Given
        final Map<String, String> attributes = new HashMap<String, String>();
        attributes.put(ProcessItem.ATTRIBUTE_ICON, "." + File.separator + "doc.jpg");

        // When
        apiProcess.update(APIID.makeAPIID(""), attributes);
    }

    @Test(expected = APIForbiddenException.class)
    public void it_throws_an_exception_adding_icon_with_unauthorized_path() throws IOException {
        // Given
        final ProcessItem processItem = mock(ProcessItem.class);
        when(processItem.getIcon()).thenReturn("." + File.separator + "doc.jpg");

        // When
        apiProcess.add(processItem);
    }


    // TODO finish this test by modifiying the process api to return a folder which can be mocked
    // @Test
    // public void should_update_do_something_when_blabla() throws IOException {
    // // Given
    // String iconName = "NonEmpty";
    // doReturn(folderRule.getRoot()).when(apiProcess).getIconsFolder();
    //
    // File iconFile = folderRule.newFile(iconName);
    // assertThat(iconFile).exists(); // ...
    //
    // ProcessItem processItem = mock(ProcessItem.class);
    // when(processItem.getIcon()).thenReturn(iconName);
    // when(processDatastore.get(any(APIID.class))).thenReturn(processItem);
    //
    // HashMap<String, String> attributes = new HashMap<String, String>();
    // attributes.put(ProcessItem.ATTRIBUTE_ICON, "Non blank");
    //
    // // When
    // apiProcess.update(APIID.makeAPIID(""), attributes);
    //
    // // Then
    // assertThat(iconFile).doesNotExist();
    // }
}
