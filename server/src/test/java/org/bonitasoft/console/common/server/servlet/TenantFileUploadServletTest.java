package org.bonitasoft.console.common.server.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.forms.server.accessor.DefaultFormsProperties;
import org.bonitasoft.forms.server.exception.FileTooBigException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TenantFileUploadServletTest {

    @Spy
    protected TenantFileUploadServlet fileUploadServlet;
    private DefaultFormsProperties formsProperties;
    private HttpServletRequest request;
    private FileItem item;

    @Before
    public void setUp() throws Exception {
        request = mock(HttpServletRequest.class);
        final HttpSession session = mock(HttpSession.class);
        item = mock(FileItem.class);
        final APISession apiSession = mock(APISession.class);
        formsProperties = mock(DefaultFormsProperties.class);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("apiSession")).thenReturn(apiSession);
        doReturn(formsProperties).when(fileUploadServlet).getDefaultFormProperties(123);
        when(apiSession.getTenantId()).thenReturn(123L);
        when(item.getName()).thenReturn("Some uploaded File.Txt");
    }

    /**
     * @throws Exception
     */
    @Test
    public void checkUploadSize_should_throw_fileTooBigException_when_file_is_bigger_in_than_conf_file() throws Exception {
        when(item.getSize()).thenReturn(10000000L);
        when(formsProperties.getAttachmentMaxSize()).thenReturn(1L); // 1Mb

        try {
            fileUploadServlet.checkUploadSize(request, item);
        } catch (final FileTooBigException e) {
            assertThat(e).hasMessage("file Some uploaded File.Txt too big !");
            return;
        }
        fail("Expected FileTooBigException but was not sent...");
    }

    @Test
    public void checkUploadSize_should_do_nothing_when_file_is_not_bigger_than_in_conf_file() throws Exception {
        when(item.getSize()).thenReturn(1000000L);
        when(formsProperties.getAttachmentMaxSize()).thenReturn(10L); // 1Mb
        fileUploadServlet.checkUploadSize(request, item);
    }

}
