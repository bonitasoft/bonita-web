package org.bonitasoft.console.common.server.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Paths;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ErrorPageServletTest {
    
    @Mock
    HttpServletRequest request;
    
    @Mock
    HttpServletResponse response;
    
    @Mock
    ServletContext sc;
    
    @Spy
    ErrorPageServlet errorServlet = new ErrorPageServlet();

    StringWriter stringWriter;

    @Before
    public void setUp() throws Exception {
        stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);
        File errorsHTMLFilePaths = Paths.get("src/main/webapp/WEB-INF/errors.html").toFile();
        InputStream errorPageInputStream = new FileInputStream(errorsHTMLFilePaths);
        when(sc.getResourceAsStream(ErrorPageServlet.ERROR_TEMPLATE_PATH)).thenReturn(errorPageInputStream);
        doReturn(sc).when(errorServlet).getServletContext();
    }
    
    @Test
    public void should_write_formatted_response() throws Exception {
        
        when(request.getPathInfo()).thenReturn("/404");
        when(request.getContextPath()).thenReturn("/bonita");

        errorServlet.doGet(request, response);
        
        assertThat(stringWriter.toString()).contains("Error 404");
        assertThat(stringWriter.toString()).contains("src=\"/bonita/portal/resource/app/appDirectoryBonita/error-404/content/?_l=fr&amp;app=appDirectoryBonita\"");
        assertThat(stringWriter.toString()).contains("width=\"100%\"");
    }
    
    @Test
    public void should_write_formatted_response_with_root_contextPath() throws Exception {
        
        when(request.getPathInfo()).thenReturn("/500");
        when(request.getContextPath()).thenReturn("/");

        errorServlet.doGet(request, response);
        
        assertThat(stringWriter.toString()).contains("src=\"/portal/resource/app/appDirectoryBonita/error-500/content/?_l=fr&amp;app=appDirectoryBonita\"");
    }
    
    @Test
    public void should_display_error_when_error_code_is_missing() throws Exception {
        
        when(request.getPathInfo()).thenReturn("");
        when(request.getContextPath()).thenReturn("/bonita");

        errorServlet.doGet(request, response);
        
        assertThat(stringWriter.toString()).contains("Status code missing from request.");
    }
}
