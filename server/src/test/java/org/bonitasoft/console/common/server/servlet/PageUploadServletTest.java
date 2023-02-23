package org.bonitasoft.console.common.server.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

import java.io.File;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.bonitasoft.console.common.server.preferences.properties.ResourcesPermissionsMapping;
import org.bonitasoft.engine.session.APISession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

@RunWith(MockitoJUnitRunner.class)
public class PageUploadServletTest {

    @Mock
    HttpServletRequest request;

    @Mock
    APISession apiSession;

    @Mock
    ResourcesPermissionsMapping resourcesPermissionsMapping;

    @Spy
    PageUploadServlet pageUploadServlet = new PageUploadServlet();

    @Test
    public void should_getPermissions_work_with_valid_zip() throws Exception {

        final File zipFile = new File(getClass().getResource("/pageWithPermissions.zip").toURI());

        doReturn(apiSession).when(pageUploadServlet).getAPISession(request);
        doReturn("edit").when(request).getParameter("action");
        final Set<String> permissionsSet = new HashSet<String>();
        permissionsSet.add("Organisation visualization");
        permissionsSet.add("Organisation management");
        doReturn(permissionsSet).when(pageUploadServlet).getPagePermissions(request, zipFile, false);

        final String[] permissions = pageUploadServlet.getPermissions(request, zipFile);

        assertThat(permissions).containsExactlyInAnyOrder("Organisation visualization" , "Organisation management");
    }

    @Test
    public void should_getPermissions_work_with_invalid_zip() throws Exception {

        final File zipFile = new File(getClass().getResource("/pageWithPermissions.zip").toURI());

        doReturn(apiSession).when(pageUploadServlet).getAPISession(request);
        doReturn("add").when(request).getParameter("action");
        final Set<String> permissionsSet = new HashSet<String>();
        doReturn(permissionsSet).when(pageUploadServlet).getPagePermissions(request, zipFile, true);

        final String[] permissions = pageUploadServlet.getPermissions(request, zipFile);

        assertThat(permissions).isNotNull();
        assertThat(permissions).isEmpty();
    }

    @Test
    public void should_generateResponseString_work_with_permissions() throws Exception {

        final File zipFile = new File(getClass().getResource("/pageWithPermissions.zip").toURI());
        final String[] permissions = new String[] {"Organisation visualization" , "Organisation management"};
        doReturn(permissions).when(pageUploadServlet).getPermissions(request, zipFile);

        final String responseString = pageUploadServlet.generateResponseString(request, "fileName", zipFile);

        assertThat(responseString).isEqualTo(zipFile.getName() + "::[Organisation visualization,Organisation management]");
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void should_generateResponseJson_work_with_permissions() throws Exception {

        final File zipFile = new File(getClass().getResource("/pageWithPermissions.zip").toURI());
        final String[] permissions = new String[] {"Organisation visualization" , "Organisation management"};
        doReturn(permissions).when(pageUploadServlet).getPermissions(request, zipFile);

        final String responseString = pageUploadServlet.generateResponseJson(request, "fileName", "application/zip", zipFile);
        
        ObjectReader reader = new ObjectMapper().readerFor(Map.class);
        Map<String, Serializable> responseMap = reader.readValue(responseString);

        //jackson deserializes array to list by default
        assertThat((List<String>)responseMap.get(PageUploadServlet.PERMISSIONS_RESPONSE_ATTRIBUTE)).containsExactlyInAnyOrder("Organisation visualization" , "Organisation management");
    }
}
