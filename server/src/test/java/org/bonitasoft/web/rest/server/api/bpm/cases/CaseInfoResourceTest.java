package org.bonitasoft.web.rest.server.api.bpm.cases;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;

import org.bonitasoft.engine.api.FlownodeCounters;
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
        caseInfoResource = spy(new CaseInfoResource());
        doReturn(processAPI).when(caseInfoResource).getEngineProcessAPI();
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
        final HashMap<String, FlownodeCounters> taskWithCounters = new HashMap<String, FlownodeCounters>();
        when(processAPI.getFlownodeStateCounters(anyLong())).thenReturn(taskWithCounters);

        //when
        final CaseInfo caseInfo = caseInfoResource.getCaseInfo();

        //then
        assertThat(caseInfo).isNotNull();
        assertThat(caseInfo.getId()).isSameAs(id);
        assertThat(caseInfo.getFlowNodeStatesCounters()).isEqualTo(taskWithCounters);
    }
}
