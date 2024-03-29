package org.bonitasoft.console.common.server.page;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.bonitasoft.engine.api.PageAPI;
import org.bonitasoft.engine.page.PageURL;
import org.bonitasoft.engine.page.URLAdapterConstants;
import org.bonitasoft.engine.session.APISession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PageMappingServiceTest {

    @Spy
    PageMappingService pageMappingService;

    @Mock
    PageAPI pageAPI;

    @Mock
    HttpServletRequest hsRequest;

    @Mock
    HttpSession httpSession;

    @Mock
    APISession apiSession;

    @Before
    public void beforeEach() throws Exception {
        when(hsRequest.getSession()).thenReturn(httpSession);
        doReturn(pageAPI).when(pageMappingService).getPageAPI(apiSession);
        when(apiSession.getUserId()).thenReturn(1L);
        when(hsRequest.getContextPath()).thenReturn("/bonita");
        Map<String, String[]> params = new HashMap<>();
        params.put("key", new String[]{"value"});
        when(hsRequest.getParameterMap()).thenReturn(params);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void getPage() throws Exception {
        final PageURL pageURL = mock(PageURL.class);
        when(pageURL.getUrl()).thenReturn("/externalURL");
        when(pageURL.getPageId()).thenReturn(null);
        ArgumentCaptor<Map> contextCaptor = ArgumentCaptor.forClass(Map.class);
        when(pageAPI.resolvePageOrURL(eq("process/processName/processVersion"), anyMap(), eq(true))).thenReturn(pageURL);

        final PageReference returnedPageReference = pageMappingService.getPage(hsRequest, apiSession, "process/processName/processVersion", new Locale("en"),
                true);

        verify(pageAPI).resolvePageOrURL(eq("process/processName/processVersion"), contextCaptor.capture(), eq(true));
        Map<String, Serializable> capturedContext = contextCaptor.getValue();
        assertEquals("/bonita", capturedContext.get(URLAdapterConstants.CONTEXT_PATH));
        assertEquals("en", capturedContext.get(URLAdapterConstants.LOCALE));
        assertEquals("value", ((Map<String, String[]>)capturedContext.get(URLAdapterConstants.QUERY_PARAMETERS)).get("key")[0]);
        assertNotNull(returnedPageReference);
        assertNull(returnedPageReference.getPageId());
        assertEquals("/externalURL", returnedPageReference.getURL());
    }

}
