package org.bonitasoft.web.rest.server.api.bpm.cases;

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
import org.bonitasoft.web.rest.model.bpm.cases.CaseInfo;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CaseInfoResourceTest {

    @Mock
    ProcessAPI processAPI;

    private CaseInfoResource caseInfoResource;

    @Before
    public void initializeMocks() {
        caseInfoResource = spy(new CaseInfoResource(processAPI));
    }

    @Test(expected = APIException.class)
    public void shouldDoGetWithNothingThowsAnApiException() throws DataNotFoundException {
        //when
        caseInfoResource.getCaseInfo();
    }

    @Test(expected = APIException.class)
    public void should_throw_exception_if_attribute_is_not_found() throws Exception {
        // given:
        doReturn(null).when(caseInfoResource).getAttribute(anyString());

        // when:
        caseInfoResource.getCaseInfo();
    }

    @Test
    public void should_return_case_info_123() throws DataNotFoundException {
        //given
        final long id = 123;
        doReturn(String.valueOf(id)).when(caseInfoResource).getAttribute(CaseInfoResource.CASE_ID);
        final HashMap<String, Map<String, Long>> taskWithCounters = new HashMap<String, Map<String, Long>>();
        when(processAPI.getFlownodeStateCounters(anyLong())).thenReturn(taskWithCounters);

        //when
        final CaseInfo caseInfo = caseInfoResource.getCaseInfo();

        //then
        assertThat(caseInfo).isNotNull();
        assertThat(caseInfo.getId()).isSameAs(id);
        assertThat(caseInfo.getFlowNodeStatesCounters()).isEqualTo(taskWithCounters);
    }
}
