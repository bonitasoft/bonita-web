package org.bonitasoft.console.common.server.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.forms.server.accessor.DefaultFormsProperties;
import org.bonitasoft.forms.server.exception.FileTooBigException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TenantFileUploadServletTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

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
        when(formsProperties.getAttachmentMaxSize()).thenReturn(10L); // 10Mb
        fileUploadServlet.checkUploadSize(request, item);
    }

    @Test
    public void doPost_with_checkSize_should_set_413_status_code_with_empty_body_when_file_is_too_big() throws Exception {
        doNothing().when(fileUploadServlet).defineUploadDirectoryPath(request);
        final ServletFileUpload serviceFileUpload = mock(ServletFileUpload.class);
        doReturn(serviceFileUpload).when(fileUploadServlet).createServletFileUpload(any(FileItemFactory.class));
        item = mock(FileItem.class);
        when(item.getSize()).thenReturn(20 * 1048576L);
        when(serviceFileUpload.parseRequest(request)).thenReturn(Arrays.asList(item));
        fileUploadServlet.uploadDirectoryPath = tempFolder.getRoot().getAbsolutePath();
        fileUploadServlet.checkUploadedFileSize = true;
        when(request.getMethod()).thenReturn("post");
        when(request.getContentType()).thenReturn("multipart/");
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final PrintWriter printer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printer);
        fileUploadServlet.doPost(request, response);
        verify(item).getSize();
        verify(response).setStatus(HttpURLConnection.HTTP_ENTITY_TOO_LARGE);
        verify(printer, never()).print(anyString());
        verify(printer, never()).flush();
    }

    @Test
    public void doPost_with_json_contentType_and_checkSize_should_set_413_status_code_with_json_body_when_file_is_too_big() throws Exception {
        doNothing().when(fileUploadServlet).defineUploadDirectoryPath(request);
        final ServletFileUpload serviceFileUpload = mock(ServletFileUpload.class);
        doReturn(serviceFileUpload).when(fileUploadServlet).createServletFileUpload(any(FileItemFactory.class));
        item = mock(FileItem.class);
        when(item.getSize()).thenReturn(20 * 1048576L);
        when(serviceFileUpload.parseRequest(request)).thenReturn(Arrays.asList(item));
        fileUploadServlet.uploadDirectoryPath = tempFolder.getRoot().getAbsolutePath();
        fileUploadServlet.checkUploadedFileSize = true;
        fileUploadServlet.responseContentType = "json";
        when(request.getMethod()).thenReturn("post");
        when(request.getContentType()).thenReturn("multipart/");
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final PrintWriter printer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printer);
        fileUploadServlet.doPost(request, response);
        verify(item).getSize();
        verify(response).setStatus(HttpURLConnection.HTTP_ENTITY_TOO_LARGE);
        verify(printer).print("{\"type\":\"error\", \"message\": \"null is too big, limit is set to 0Mb\"}");
        verify(printer).flush();
    }

}
