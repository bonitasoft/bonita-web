package org.bonitasoft.console.common.server.page;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.utils.SessionUtil;
import org.bonitasoft.engine.api.PageAPI;
import org.bonitasoft.engine.page.AuthorizationRuleConstants;
import org.bonitasoft.engine.page.PageURL;
import org.bonitasoft.engine.page.URLAdapterConstants;
import org.bonitasoft.engine.session.APISession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
    }

    @Test
    public void getPage() throws Exception {
        final PageURL pageURL = mock(PageURL.class);
        when(pageURL.getUrl()).thenReturn("/externalURL");
        when(pageURL.getPageId()).thenReturn(null);
        final Map<String, Serializable> context = new HashMap<>();
        context.put(URLAdapterConstants.QUERY_PARAMETERS, (Serializable) hsRequest.getParameterMap());
        context.put(AuthorizationRuleConstants.IS_ADMIN, false);
        context.put(URLAdapterConstants.LOCALE, "en");
        context.put(URLAdapterConstants.CONTEXT_PATH, "/bonita");
        when(pageAPI.resolvePageOrURL("process/processName/processVersion", context, true)).thenReturn(pageURL);
        final Set<String> userPermissions = new HashSet<>();
        when(httpSession.getAttribute(SessionUtil.PERMISSIONS_SESSION_PARAM_KEY)).thenReturn(userPermissions);

        final PageReference returnedPageReference = pageMappingService.getPage(hsRequest, apiSession, "process/processName/processVersion", new Locale("en"),
                true);

        assertNotNull(returnedPageReference);
        assertEquals(null, returnedPageReference.getPageId());
        assertEquals("/externalURL", returnedPageReference.getURL());
    }

}
