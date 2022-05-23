package org.bonitasoft.web.rest.server.api.bpm.process;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.data.DataNotFoundException;
import org.bonitasoft.web.rest.model.bpm.process.ProcessInfo;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ProcessInfoResourceTest {

    @Mock
    ProcessAPI processAPI;

    private ProcessInfoResource processInfoResource;

    @Before
    public void initializeMocks() {
        processInfoResource = spy(new ProcessInfoResource(processAPI));
    }

    @Test(expected = APIException.class)
    public void shouldDoGetWithNothingThowsAnApiException() throws DataNotFoundException {
        //when
        processInfoResource.getProcessInfo();
    }

    @Test(expected = APIException.class)
    public void should_throw_exception_if_attribute_is_not_found() throws Exception {
        // given:
        doReturn(null).when(processInfoResource).getAttribute(anyString());

        // when:
        processInfoResource.getProcessInfo();
    }

    @Test
    public void should_return_case_info_123() throws DataNotFoundException {
        //given
        final long id = 123;
        doReturn(String.valueOf(id)).when(processInfoResource).getAttribute(ProcessInfoResource.PROCESS_ID);
        final HashMap<String, Map<String, Long>> taskWithCounters = new HashMap<String, Map<String, Long>>();
        when(processAPI.getActiveFlownodeStateCountersForProcessDefinition(anyLong())).thenReturn(taskWithCounters);

        //when
        final ProcessInfo processInfo = processInfoResource.getProcessInfo();

        //then
        assertThat(processInfo).isNotNull();
        assertThat(processInfo.getProcessDefinitionId()).isSameAs(id);
        assertThat(processInfo.getFlowNodeStatesCounters()).isEqualTo(taskWithCounters);
    }
}
