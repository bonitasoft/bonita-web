package org.bonitasoft.console.common.server.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;


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

    @Test
     public void getExtension_should_return_proper_extension() throws IOException {
        // given
        String filename = "C:\\Users\\Desktop\\process.bar";

        // when
        String extension = fileUploadServlet.getExtension(filename);

        // then
        assertThat(extension).isEqualTo(".bar");
    }

    @Test
    public void getExtension_should_return_an_empty_extension() throws IOException {
        // given
        String filename = "C:\\Users\\Desktop\\process";

        // when
        String extension = fileUploadServlet.getExtension(filename);

        // then
        assertThat(extension).isEqualTo("");
    }

    @Test
    public void getExtension_should_return_a_proper_extension_without_taking_care_of_dots() throws IOException {
        // given
        String filename = "C:\\Users\\Deskt.op\\proc.ess.bar";

        // when
        String extension = fileUploadServlet.getExtension(filename);

        // then
        assertThat(extension).isEqualTo(".bar");
    }

    @Test
    public void getExtension_should_return_proper_extension_for_short_filename() throws IOException {
        // given
        String filename = "process.bar";

        // when
        String extension = fileUploadServlet.getExtension(filename);

        // then
        assertThat(extension).isEqualTo(".bar");
    }

    @Test
    public void getExtension_should_return_proper_extension_for_linux_like_paths() throws IOException {
        // given
        String filename = "/Users/Deskt.op/proc.ess.bar";

        // when
        String extension = fileUploadServlet.getExtension(filename);

        // then
        assertThat(extension).isEqualTo(".bar");
    }

    @Test
    public void getExtension_should_return_an_empty_extension_for_parent_folder_filename() throws IOException {
        // given
        String filename = "../../../";

        // when
        String extension = fileUploadServlet.getExtension(filename);

        // then
        assertThat(extension).isEqualTo("");
    }

    @Test
    public void getFilenameLastSegment_should_return_proper_filename() {
        // given
        String filename = "C:\\Users\\Desktop\\process.bar";

        // when
        String filenameLastSegment = fileUploadServlet.getFilenameLastSegment(filename);

        // then
        assertThat(filenameLastSegment).isEqualTo("process.bar");
    }

    @Test
    public void getFilenameLastSegment_should_return_proper_filename_for_linux_paths() {
        // given
        String filename = "/Users/Deskt.op/process.bar";

        // when
        String filenameLastSegment = fileUploadServlet.getFilenameLastSegment(filename);

        // then
        assertThat(filenameLastSegment).isEqualTo("process.bar");
    }

    @Test
    public void getFilenameLastSegment_should_return_an_empty_filename_for_parent_folder_filename() throws IOException {
        // given
        String filename = "../../../";

        // when
        String filenameLastSegment = fileUploadServlet.getFilenameLastSegment(filename);

        // then
        assertThat(filenameLastSegment).isEqualTo("");
    }
}
