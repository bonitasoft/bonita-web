package org.bonitasoft.web.rest.server.api.bpm.flownode;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.data.DataNotFoundException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ActivityVariableResourceTest {

    @Mock
    ProcessAPI processAPI;

    private ActivityVariableResource activityVariableResource;

    @Before
    public void initializeMocks() {
        initMocks(this);
        activityVariableResource = spy(new ActivityVariableResource());
        doReturn(processAPI).when(activityVariableResource).getEngineProcessAPI();
    }

    @Test(expected = APIException.class)
    public void should_do_get_with_nothing_thows_an_api_exception() throws DataNotFoundException {
        //when
        activityVariableResource.getTaskVariable();
    }

    @Test(expected = APIException.class)
    public void should_throw_exception_if_attribute_is_not_found() throws Exception {
        // given:
        doReturn(null).when(activityVariableResource).getAttribute(anyString());

        // when:
        activityVariableResource.getTaskVariable();
    }

    @Test
    public void should_do_get_call_getAttribute() throws DataNotFoundException {
        //given
        doReturn("").when(activityVariableResource).getAttribute(ActivityVariableResource.ACTIVITYDATA_DATA_NAME);
        doReturn("1").when(activityVariableResource).getAttribute(ActivityVariableResource.ACTIVITYDATA_ACTIVITY_ID);
        doReturn(null).when(processAPI).getActivityDataInstance(anyString(), anyLong());

        //when
        activityVariableResource.getTaskVariable();

        //then
        verify(activityVariableResource).getAttribute(ActivityVariableResource.ACTIVITYDATA_ACTIVITY_ID);
        verify(activityVariableResource).getAttribute(ActivityVariableResource.ACTIVITYDATA_DATA_NAME);

    }

}
