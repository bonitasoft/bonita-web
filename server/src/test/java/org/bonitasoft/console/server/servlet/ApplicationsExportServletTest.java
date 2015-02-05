package org.bonitasoft.console.server.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.bonitasoft.console.common.server.i18n.I18n;
import org.bonitasoft.engine.api.ApplicationAPI;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.InvalidSessionException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class ApplicationsExportServletTest {

    @Mock
    APISession session;

    @Mock
    private ApplicationAPI applicationAPI;

    @Spy
    private ApplicationsExportServlet spiedApplicationsExportServlet;

    @BeforeClass
    public static void initEnvironnement() {
        I18n.getInstance();
    }

    @Before
    public void init() throws InvalidSessionException, BonitaHomeNotSetException, ServerAPIException, UnknownAPITypeException {
        MockitoAnnotations.initMocks(this);
        Mockito.doReturn(applicationAPI).when(spiedApplicationsExportServlet).getApplicationAPI(any(APISession.class));
    }

    @Test
    public void should_call_engine_export_with_the_good_id() throws Exception {

        spiedApplicationsExportServlet.exportResources(new long[] { 1 }, session);

        verify(applicationAPI, times(1)).exportApplications(new long[] { 1 });
    }

    @Test
    public void should_logger_log_with_name_of_the_Servlet() throws Exception {
        assertThat(spiedApplicationsExportServlet.getLogger().getName()).isEqualTo("org.bonitasoft.console.server.servlet.ApplicationsExportServlet");
    }

    @Test
    public void should_getFileExportName_return_a_valide_file_name() throws Exception {
        assertThat(spiedApplicationsExportServlet.getFileExportName()).isEqualTo("Application_Data.xml");
    }

}
