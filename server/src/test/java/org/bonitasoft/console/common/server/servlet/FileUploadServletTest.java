package org.bonitasoft.console.common.server.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class FileUploadServletTest {

    @Mock
    HttpServletRequest request;

    @Spy
    TenantFileUploadServlet fileUploadServlet = new TenantFileUploadServlet();

    @Test
    public void generateResponseJson_should_return_valid_json() throws Exception {
        final File uploadedFile = mock(File.class);
        when(uploadedFile.getName()).thenReturn("uploadedFile.txt");
        when(fileUploadServlet.getServletConfig()).thenReturn(mock(ServletConfig.class));
        when(fileUploadServlet.getInitParameter(FileUploadServlet.RETURN_ORIGINAL_FILENAME_PARAM)).thenReturn("true");
        when(fileUploadServlet.getInitParameter(FileUploadServlet.RETURN_FULL_SERVER_PATH_PARAM)).thenReturn("false");
        fileUploadServlet.init();

        final JSONObject jsonResponse = new JSONObject(fileUploadServlet.generateResponseJson(request, "originalFileName", uploadedFile));

        assertThat(jsonResponse.getString(FileUploadServlet.FILE_NAME_RESPONSE_ATTRIBUTE)).isEqualTo("originalFileName");
        assertThat(jsonResponse.getString(FileUploadServlet.TEMP_PATH_RESPONSE_ATTRIBUTE)).isEqualTo("uploadedFile.txt");
    }

    @Test
    public void generateResponseString_should_return_valid_text() throws Exception {
        final File uploadedFile = mock(File.class);
        when(uploadedFile.getName()).thenReturn("uploadedFile.txt");
        when(fileUploadServlet.getServletConfig()).thenReturn(mock(ServletConfig.class));
        when(fileUploadServlet.getInitParameter(FileUploadServlet.RETURN_ORIGINAL_FILENAME_PARAM)).thenReturn("true");
        when(fileUploadServlet.getInitParameter(FileUploadServlet.RETURN_FULL_SERVER_PATH_PARAM)).thenReturn("false");
        fileUploadServlet.init();

        final String responseString = fileUploadServlet.generateResponseString(request, "originalFileName", uploadedFile);

        assertThat(responseString).isEqualTo("uploadedFile.txt::originalFileName");
    }
}
