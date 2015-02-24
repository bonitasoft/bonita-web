package org.bonitasoft.web.rest.server.api.form;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletResponse;

import org.bonitasoft.console.common.server.form.FormReference;
import org.bonitasoft.engine.api.ProcessConfigurationAPI;
import org.bonitasoft.engine.bpm.flownode.TimerEventTriggerInstance;
import org.bonitasoft.engine.exception.FormMappingNotFoundException;
import org.bonitasoft.engine.search.SearchOptions;
import org.junit.Test;
import org.restlet.resource.ResourceException;

public class FormMappingResourceTest {

    @Test
    public void searchFormMappingShouldCallEngine() throws Exception {
        // given:
        final FormMappingResource spy = spy(new FormMappingResource());
        final SearchOptions searchOptions = mock(SearchOptions.class);
        doReturn(searchOptions).when(spy).buildSearchOptions();
        doReturn(new ArrayList<TimerEventTriggerInstance>()).when(spy).runEngineSearch(eq(searchOptions));

        // when:
        spy.searchFormMappings();

        // then:
        verify(spy).runEngineSearch(searchOptions);
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
