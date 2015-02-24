package org.bonitasoft.web.rest.server.api.form;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.bonitasoft.console.common.server.form.FormReference;
import org.bonitasoft.engine.api.ProcessConfigurationAPI;
import org.bonitasoft.engine.exception.FormMappingNotFoundException;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.SearchResult;
import org.junit.Test;
import org.restlet.resource.ResourceException;

// TODO refactor and improve this Classe once RestletTest is available in Community (waiting for the BDM merge)
public class FormMappingResourceTest {

    @Test
    public void searchFormMappingShouldCallEngine() throws Exception {
        // given:
        final FormMappingResource spy = spy(new FormMappingResource());
        final ProcessConfigurationAPI processConfigurationAPI = mock(ProcessConfigurationAPI.class);
        doReturn(processConfigurationAPI).when(spy).getEngineProcessConfigurationAPI();
        final SearchOptions searchOptions = mock(SearchOptions.class);
        doReturn(searchOptions).when(spy).buildSearchOptions();
        doReturn(mock(SearchResult.class)).when(processConfigurationAPI).searchFormMappings(searchOptions);

        // when:
        spy.searchFormMappings();

        // then:
        verify(processConfigurationAPI).searchFormMappings(searchOptions);
    }

    @Test
    public void updateShouldCallEngine() throws Exception {
        final FormMappingResource spy = spy(new FormMappingResource());
        final ProcessConfigurationAPI processConfigurationAPI = mock(ProcessConfigurationAPI.class);
        doReturn(processConfigurationAPI).when(spy).getEngineProcessConfigurationAPI();
        final Map<String, String> requestAttributes = new HashMap<String, String>();
        requestAttributes.put(FormMappingResource.ID_PARAM_NAME, "2");
        doReturn(requestAttributes).when(spy).getRequestAttributes();
        final FormReference formReference = mock(FormReference.class);
        doReturn("myPage").when(formReference).getForm();
        doReturn(false).when(formReference).isExternal();

        spy.updateFormMapping(formReference);

        verify(processConfigurationAPI).updateFormMapping(2L, "myPage", false);
    }

    @Test
    public void updateShouldHandleNullId() throws Exception {
        final FormMappingResource spy = spy(new FormMappingResource());
        doReturn(Collections.EMPTY_MAP).when(spy).getRequestAttributes();

        try {
            spy.updateFormMapping(mock(FormReference.class));

            fail("Expecting Resource Exception");
        } catch (final ResourceException e) {
            assertEquals(HttpServletResponse.SC_BAD_REQUEST, e.getStatus().getCode());
        }
    }

    @Test
    public void updateShouldHandleNotFound() throws Exception {

        final FormMappingResource spy = spy(new FormMappingResource());
        doReturn(Collections.EMPTY_MAP).when(spy).getRequestAttributes();
        final ProcessConfigurationAPI processConfigurationAPI = mock(ProcessConfigurationAPI.class);
        doReturn(processConfigurationAPI).when(spy).getEngineProcessConfigurationAPI();
        final FormReference formReference = mock(FormReference.class);
        doReturn("myPage").when(formReference).getForm();
        doReturn(false).when(formReference).isExternal();
        doThrow(FormMappingNotFoundException.class).when(processConfigurationAPI).updateFormMapping(1L, "myPage", false);

        doReturn("1").when(spy).getAttribute(FormMappingResource.ID_PARAM_NAME);

        try {
            spy.updateFormMapping(formReference);

            fail("Expecting Resource Exception");
        } catch (final ResourceException e) {
            assertEquals(HttpServletResponse.SC_NOT_FOUND, e.getStatus().getCode());
        }
    }
}
