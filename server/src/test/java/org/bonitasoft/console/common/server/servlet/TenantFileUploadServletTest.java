package org.bonitasoft.console.common.server.servlet;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.bonitasoft.console.common.server.preferences.properties.ConsoleProperties;
import org.bonitasoft.engine.session.APISession;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TenantFileUploadServletTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Spy
    protected TenantFileUploadServlet fileUploadServlet;
    private ConsoleProperties consoleProperties;
    private HttpServletRequest request;

    @Before
    public void setUp() throws Exception {
        request = mock(HttpServletRequest.class);
        final HttpSession session = mock(HttpSession.class);
        final APISession apiSession = mock(APISession.class);
        consoleProperties = mock(ConsoleProperties.class);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("apiSession")).thenReturn(apiSession);
        doReturn(consoleProperties).when(fileUploadServlet).getConsoleProperties(123);
        when(apiSession.getTenantId()).thenReturn(123L);
    }

    @Test
    public void should_throw_fileTooBigException_when_file_is_bigger_in_than_conf_file() throws Exception {
        final ServletFileUpload serviceFileUpload = mock(ServletFileUpload.class);

        when(consoleProperties.getMaxSize()).thenReturn(1L); // 1Mb
        fileUploadServlet.setUploadSizeMax(serviceFileUpload, request);
        verify(serviceFileUpload).setFileSizeMax(1048576);
    }

    @Test
    public void should_set_413_status_code_with_empty_body_when_file_creates_OOMError() throws Exception {
        final ServletFileUpload serviceFileUpload = mock(ServletFileUpload.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final PrintWriter printer = mock(PrintWriter.class);

        //manage spy
        fileUploadServlet.uploadDirectoryPath = tempFolder.getRoot().getAbsolutePath();
        fileUploadServlet.checkUploadedFileSize = true;
        doNothing().when(fileUploadServlet).defineUploadDirectoryPath(request);
        doReturn(serviceFileUpload).when(fileUploadServlet).createServletFileUpload(any(FileItemFactory.class));

        when(serviceFileUpload.parseRequest(request)).thenThrow(new OutOfMemoryError());
        when(request.getMethod()).thenReturn("post");
        when(request.getContentType()).thenReturn("multipart/");
        when(response.getWriter()).thenReturn(printer);

        fileUploadServlet.doPost(request, response);

        verify(response).setStatus(HttpURLConnection.HTTP_ENTITY_TOO_LARGE);
        verify(printer, never()).print(anyString());
        verify(printer, never()).flush();
    }

    @Test
    public void should_set_413_status_code_with_empty_body_when_file_is_too_big() throws Exception {
        final ServletFileUpload serviceFileUpload = mock(ServletFileUpload.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final PrintWriter printer = mock(PrintWriter.class);

        //manage spy
        fileUploadServlet.uploadDirectoryPath = tempFolder.getRoot().getAbsolutePath();
        fileUploadServlet.checkUploadedFileSize = true;
        doNothing().when(fileUploadServlet).defineUploadDirectoryPath(request);
        doReturn(serviceFileUpload).when(fileUploadServlet).createServletFileUpload(any(FileItemFactory.class));

        final FileSizeLimitExceededException exception = new FileSizeLimitExceededException(
                format("The field %s exceeds its maximum permitted size of %s bytes.",
                        "uploadedFile.zip", Long.valueOf(0)),
                        20 * 1048576L, 0);
        exception.setFileName("uploadedFile.zip");
        when(serviceFileUpload.parseRequest(request)).thenThrow(exception);
        when(request.getMethod()).thenReturn("post");
        when(request.getContentType()).thenReturn("multipart/");
        when(response.getWriter()).thenReturn(printer);

        fileUploadServlet.doPost(request, response);

        verify(response).setStatus(HttpURLConnection.HTTP_ENTITY_TOO_LARGE);
        verify(printer, never()).print(anyString());
        verify(printer, never()).flush();
    }

    @Test
    public void should_set_413_status_code_with_json_body_when_file_is_too_big_and_json_is_supported() throws Exception {
        final ServletFileUpload serviceFileUpload = mock(ServletFileUpload.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final PrintWriter printer = mock(PrintWriter.class);

        //manage spy
        fileUploadServlet.uploadDirectoryPath = tempFolder.getRoot().getAbsolutePath();
        fileUploadServlet.checkUploadedFileSize = true;
        fileUploadServlet.responseContentType = "json";
        doNothing().when(fileUploadServlet).defineUploadDirectoryPath(request);
        doReturn(serviceFileUpload).when(fileUploadServlet).createServletFileUpload(any(FileItemFactory.class));

        final FileSizeLimitExceededException exception = new FileSizeLimitExceededException(
                format("The field %s exceeds its maximum permitted size of %s bytes.",
                        "uploadedFile.zip", Long.valueOf(0)),
                        20 * 1048576L, 0);
        exception.setFileName("uploadedFile.zip");
        when(serviceFileUpload.parseRequest(request)).thenThrow(exception);
        when(request.getMethod()).thenReturn("post");
        when(request.getContentType()).thenReturn("multipart/");
        when(response.getWriter()).thenReturn(printer);

        fileUploadServlet.doPost(request, response);

        verify(response).setStatus(HttpURLConnection.HTTP_ENTITY_TOO_LARGE);
        final ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(printer).print(captor.capture());
        assertThat(captor.getValue())
        .contains("\"statusCode\":413")
        .contains("\"message\":\"uploadedFile.zip is 20971520 large, limit is set to 0Mb\"")
        .contains("\"type\":\"EntityTooLarge\"");
        verify(printer).flush();
    }
}
