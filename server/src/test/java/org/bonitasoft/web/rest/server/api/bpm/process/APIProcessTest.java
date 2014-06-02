package org.bonitasoft.web.rest.server.api.bpm.process;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.bonitasoft.test.toolkit.organization.TestUser;
import org.bonitasoft.test.toolkit.organization.TestUserFactory;
import org.bonitasoft.web.rest.model.ModelFactory;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.rest.server.AbstractConsoleTest;
import org.bonitasoft.web.rest.server.datastore.bpm.process.ProcessDatastore;
import org.bonitasoft.web.toolkit.client.ItemDefinitionFactory;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * @author Alexis Hassler
 */
public class APIProcessTest extends AbstractConsoleTest {

    private APIProcess apiProcess;

    private final ProcessDatastore processDatastore = mock(ProcessDatastore.class);

    @Rule
    public TemporaryFolder folderRule = new TemporaryFolder();

    @Override
    protected TestUser getInitiator() {
        return TestUserFactory.getJohnCarpenter();
    }

    @Override
    @Before
    public void consoleTestSetUp() {
        ItemDefinitionFactory.setDefaultFactory(new ModelFactory());
        apiProcess = spy(new APIProcess());
        apiProcess.setCaller(getAPICaller(getInitiator().getSession(), "API/bpm/process"));
        doReturn(processDatastore).when(apiProcess).getProcessDatastore();
        doReturn(null).when(apiProcess).uploadIcon(anyString());
    }

    @Test
    public void should_add_change_icon_path_when_specified() throws Exception {
        // Given
        ProcessItem processItem = mock(ProcessItem.class);
        when(processItem.getIcon()).thenReturn("Non empty");

        // When
        apiProcess.add(processItem);

        // Then
        verify(processItem, times(1)).setIcon(anyString());
    }

    @Test
    public void should_add_not_change_icon_path_when_not_specified() throws Exception {
        // Given
        ProcessItem processItem = mock(ProcessItem.class);
        when(processItem.getIcon()).thenReturn("");

        // When
        apiProcess.add(processItem);

        // Then
        verify(processItem, never()).setIcon(anyString());
    }

    @Test
    public void should_update_upload_icon_when_non_blank_icon_attribute() {
        // Given
        ProcessItem processItem = mock(ProcessItem.class);
        when(processItem.getIcon()).thenReturn("");
        when(processDatastore.get(any(APIID.class))).thenReturn(processItem);

        HashMap<String, String> attributes = new HashMap<String, String>();
        attributes.put(ProcessItem.ATTRIBUTE_ICON, "Non blank");

        // When
        apiProcess.update(APIID.makeAPIID(""), attributes);

        // Then
        verify(apiProcess).uploadIcon(anyString());
        verify(apiProcess, times(2)).getProcessDatastore();
    }

    @Test
    public void should_update_not_upload_icon_when_blank_icon_attribute() {
        // Given
        HashMap<String, String> attributes = new HashMap<String, String>();
        attributes.put(ProcessItem.ATTRIBUTE_ICON, "");

        // When
        apiProcess.update(APIID.makeAPIID(""), attributes);

        // Then
        verify(apiProcess, never()).uploadIcon(anyString());
        verify(apiProcess, times(1)).getProcessDatastore();
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
