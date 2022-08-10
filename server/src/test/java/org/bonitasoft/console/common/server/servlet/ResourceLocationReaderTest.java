package org.bonitasoft.console.common.server.servlet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ResourceLocationReaderTest {

    @Mock
    private HttpServletRequest req;

    @Spy
    private final ResourceLocationReader resourceLocationReader = new ResourceLocationReader();

    @Test
    public void should_use_infopath_if_location_param_is_not_defined() throws Exception {

        when(req.getParameter("location")).thenReturn(null);
        when(req.getPathInfo()).thenReturn("/bonita.css");

        final String resourceLocation = resourceLocationReader.getResourceLocationFromRequest(req);

        verify(req, times(1)).getPathInfo();
        assertThat(resourceLocation).isEqualTo("bonita.css");
    }

    @Test
    public void should_not_use_infopath_if_location_param_is_defined() throws Exception {

        when(req.getParameter("location")).thenReturn("bonita.css");

        final String resourceLocation = resourceLocationReader.getResourceLocationFromRequest(req);

        verify(req, never()).getPathInfo();
        assertThat(resourceLocation).isEqualTo("bonita.css");
    }
}
