package org.bonitasoft.web.rest.server.datastore.bpm.process;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bonitasoft.console.common.server.page.CustomPageService;
import org.bonitasoft.console.common.server.preferences.properties.CompoundPermissionsMapping;
import org.bonitasoft.console.common.server.utils.BonitaHomeFolderAccessor;
import org.bonitasoft.console.common.server.utils.PlatformManagementUtils;
import org.bonitasoft.console.common.server.utils.UnauthorizedFolderException;
import org.bonitasoft.engine.api.PageAPI;
import org.bonitasoft.engine.page.Page;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.rest.server.APITestWithMock;
import org.bonitasoft.web.rest.server.engineclient.ProcessEngineClient;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIForbiddenException;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class ProcessDatastoreTest extends APITestWithMock {

    private ProcessDatastore processDatastore;

    @Mock
    private APISession engineSession;

    @Mock
    private ProcessEngineClient processEngineClient;

    @Mock
    private BonitaHomeFolderAccessor tenantFolder;

    @Mock
    private PageAPI pageAPI;

    @Mock
    private CustomPageService customPageService;

    @Mock
    private CompoundPermissionsMapping compoundPermissionsMapping;

    @Mock
    private SearchResult<Page> searchResult;

    @Mock
    private PlatformManagementUtils platformManagementUtils;

    private final ProcessItem processItem = new ProcessItem();

    @Before
    public void setUp() throws Exception {
        processDatastore = spy(new ProcessDatastore(engineSession));
        doReturn(tenantFolder).when(processDatastore).getTenantFolder();
        doReturn(platformManagementUtils).when(processDatastore).getPlatformManagementUtils();
        doReturn(processEngineClient).when(processDatastore).getProcessEngineClient();
        doReturn(customPageService).when(processDatastore).getCustomPageService();
        doReturn(pageAPI).when(processDatastore).getPageAPI();
        doReturn(compoundPermissionsMapping).when(processDatastore).getCompoundPermissionsMapping();
        doReturn(searchResult).when(pageAPI).searchPages(any(SearchOptions.class));
    }

    @Test(expected = APIForbiddenException.class)
    public void it_throws_an_exception_adding_a_bar_file_with_unauthorized_path() throws IOException {
        // Given
        doThrow(new UnauthorizedFolderException("")).when(tenantFolder).getTempFile(any(String.class), any(Long.class));

        // When
        processDatastore.add(processItem);

    }

    @Test(expected = APIException.class)
    public void it_throws_an_exception_adding_a_bar_file_with_ioException() throws IOException {
        // Given
        doThrow(new IOException("")).when(tenantFolder).getTempFile(any(String.class), any(Long.class));


        // When
        try {
            processDatastore.add(processItem);
        } catch (final APIForbiddenException e) {
            fail("Exception is not supposed to be an APIForbiddenException");
        }
    }

    @Test
    public void it_removes_the_pages_when_deleting_a_process() throws IOException {

        final Page page1 = mock(Page.class);
        doReturn("page1").when(page1).getName();
        final Page page2 = mock(Page.class);
        doReturn("page2").when(page2).getName();
        Arrays.asList(page1, page2);
        doReturn(Arrays.asList(page1, page2)).when(searchResult).getResult();
        doReturn(2L).when(searchResult).getCount();

        final APIID id = APIID.makeAPIID(2L);
        processDatastore.delete(Arrays.asList(id));

        verify(processDatastore).removeProcessPagesFromHome(id);
        verify(customPageService, times(1)).removePage(engineSession, page1);
        verify(compoundPermissionsMapping, times(1)).removeProperty("page1");
        verify(customPageService, times(1)).removePage(engineSession, page2);
        verify(compoundPermissionsMapping, times(1)).removeProperty("page2");
    }

    @Test
    public void it_removes_the_pages_when_deleting_a_process_with_pagination() throws IOException {

        final long nbOfPages = 130L;
        final Page page = mock(Page.class);
        doReturn("page").when(page).getName();
        final List<Page> pages = new ArrayList<Page>();
        for (int i = 0; i < nbOfPages; i++) {
            pages.add(page);
        }
        doReturn(pages).when(searchResult).getResult();
        doReturn(nbOfPages).when(searchResult).getCount();

        final APIID id = APIID.makeAPIID(2L);
        processDatastore.delete(Arrays.asList(id));

        verify(processDatastore).removeProcessPagesFromHome(id);
        verify(customPageService, times((int) nbOfPages)).removePage(engineSession, page);
        verify(compoundPermissionsMapping, times((int) nbOfPages)).removeProperty("page");
    }

    @Test
    public void should_retrieve_the_autologin_configuration_when_updating_the_process_state() throws Exception {

        final long tenantId = 1L;
        doReturn(tenantId).when(engineSession).getTenantId();
        final APIID id = APIID.makeAPIID(2L);
        final Map<String, String> attributes = new HashMap<String, String>();
        attributes.put(ProcessItem.ATTRIBUTE_ACTIVATION_STATE, ProcessItem.VALUE_ACTIVATION_STATE_ENABLED);

        processDatastore.update(id, attributes);

        verify(platformManagementUtils).retrieveAutologinConfiguration(tenantId);;
    }
}
