package org.bonitasoft.forms.server;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.login.LoginManager;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.forms.server.api.IFormDefinitionAPI;
import org.bonitasoft.forms.server.exception.ApplicationFormDefinitionNotFoundException;
import org.bonitasoft.forms.server.exception.FormNotFoundException;
import org.bonitasoft.forms.server.provider.impl.util.FormServiceProviderUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;



@RunWith(MockitoJUnitRunner.class)
public class FormsCacheServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    @Mock
    private APISession apiSession;
    @Mock
    private IFormDefinitionAPI formDefinitionAPI;
    @Mock
    private PrintWriter printWriter;
    @Spy
    private final FormsCacheServlet formsCacheServlet = spy(new FormsCacheServlet());

    @Test
    public void should_get_return_list_of_forms() throws Exception {

        doReturn("12").when(request).getParameter(FormServiceProviderUtil.PROCESS_UUID);
        doReturn(session).when(request).getSession();
        doReturn(apiSession).when(session).getAttribute(LoginManager.API_SESSION_PARAM_KEY);
        doReturn(Locale.ENGLISH).when(request).getLocale();
        doReturn(formDefinitionAPI).when(formsCacheServlet).getDefinitionAPI(any(HttpServletRequest.class), anyMap(), anyString());
        final List<String> formIDs = new ArrayList<String>();
        formIDs.add("processName--1.0$ENTRY");
        formIDs.add("processName--1.0-activityName$ENTRY");
        doReturn(formIDs).when(formDefinitionAPI).getFormsList(anyMap());
        doReturn(1l).when(formsCacheServlet).getTenantID(request);
        final StringWriter stringWriter = new StringWriter();
        doReturn(new PrintWriter(stringWriter)).when(response).getWriter();
        stringWriter.close();

        formsCacheServlet.doGet(request, response);

        assertThat(stringWriter.toString(), is("[\"processName--1.0$ENTRY\",\"processName--1.0-activityName$ENTRY\"]"));
    }

    @Test
    public void should_put_call_cacheForm() throws Exception {

        doReturn("/12/processName--1.0$ENTRY").when(request).getPathInfo();
        doReturn(session).when(request).getSession();
        doReturn(apiSession).when(session).getAttribute(LoginManager.API_SESSION_PARAM_KEY);
        doReturn(Locale.ENGLISH).when(request).getLocale();
        final Map<String, Object> context = new HashMap<String, Object>();
        doReturn(context).when(formsCacheServlet).initContext(any(HttpServletRequest.class), anyMap(), any(Locale.class));
        doReturn(formDefinitionAPI).when(formsCacheServlet).getDefinitionAPI(any(HttpServletRequest.class), anyMap(), anyString());
        doReturn(1l).when(formsCacheServlet).getTenantID(request);

        formsCacheServlet.doPut(request, response);

        verify(formDefinitionAPI, times(1)).cacheForm("processName--1.0$ENTRY", context);
    }

    @Test
    public void should_put_call_send_error_status_code_when_form_definition_not_found() throws Exception {

        doReturn("/12/processName--1.0$ENTRY").when(request).getPathInfo();
        doReturn(session).when(request).getSession();
        doReturn(apiSession).when(session).getAttribute(LoginManager.API_SESSION_PARAM_KEY);
        doReturn(Locale.ENGLISH).when(request).getLocale();
        final Map<String, Object> context = new HashMap<String, Object>();
        doReturn(context).when(formsCacheServlet).initContext(any(HttpServletRequest.class), anyMap(), any(Locale.class));
        doThrow(FormNotFoundException.class).when(formsCacheServlet).getDefinitionAPI(any(HttpServletRequest.class), anyMap(), anyString());
        doReturn(1l).when(formsCacheServlet).getTenantID(request);

        formsCacheServlet.doPut(request, response);

        verify(response, times(1)).sendError(eq(HttpServletResponse.SC_NOT_FOUND), anyString());
    }

    @Test
    public void should_put_call_send_error_status_code_when_form_not_found() throws Exception {

        doReturn("/12/processName--1.0$ENTRY").when(request).getPathInfo();
        doReturn(session).when(request).getSession();
        doReturn(apiSession).when(session).getAttribute(LoginManager.API_SESSION_PARAM_KEY);
        doReturn(Locale.ENGLISH).when(request).getLocale();
        final Map<String, Object> context = new HashMap<String, Object>();
        doReturn(context).when(formsCacheServlet).initContext(any(HttpServletRequest.class), anyMap(), any(Locale.class));
        doReturn(formDefinitionAPI).when(formsCacheServlet).getDefinitionAPI(any(HttpServletRequest.class), anyMap(), anyString());
        doReturn(1l).when(formsCacheServlet).getTenantID(request);
        doThrow(ApplicationFormDefinitionNotFoundException.class).when(formDefinitionAPI).cacheForm(anyString(), anyMap());

        formsCacheServlet.doPut(request, response);

        verify(response, times(1)).sendError(eq(HttpServletResponse.SC_NOT_FOUND), anyString());
    }

    @Test
    public void should_get_call_send_error_status_code_when_form_definition_not_found() throws Exception {

        doReturn("12").when(request).getParameter(FormServiceProviderUtil.PROCESS_UUID);
        doReturn(session).when(request).getSession();
        doReturn(apiSession).when(session).getAttribute(LoginManager.API_SESSION_PARAM_KEY);
        doReturn(Locale.ENGLISH).when(request).getLocale();
        doThrow(FormNotFoundException.class).when(formsCacheServlet).getDefinitionAPI(any(HttpServletRequest.class), anyMap(), anyString());
        doReturn(1l).when(formsCacheServlet).getTenantID(request);

        formsCacheServlet.doGet(request, response);

        verify(response, times(1)).sendError(eq(HttpServletResponse.SC_NOT_FOUND), anyString());
    }
}
