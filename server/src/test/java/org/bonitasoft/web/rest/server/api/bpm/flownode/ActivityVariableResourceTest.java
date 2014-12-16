package org.bonitasoft.web.rest.server.api.bpm.flownode;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.Serializable;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.data.DataInstance;
import org.bonitasoft.engine.bpm.data.DataNotFoundException;
import org.bonitasoft.engine.bpm.data.impl.DataInstanceImpl;
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
        activityVariableResource = spy(new ActivityVariableResource());
        doReturn(processAPI).when(activityVariableResource).getEngineProcessAPI();
    }

    @Test(expected = APIException.class)
    public void shouldDoGetWithNothingThowsAnApiException() throws DataNotFoundException {
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

    @Test
    public void should_return() throws DataNotFoundException {
        // given
        doReturn("").when(activityVariableResource).getAttribute(ActivityVariableResource.ACTIVITYDATA_DATA_NAME);
        doReturn("1").when(activityVariableResource).getAttribute(ActivityVariableResource.ACTIVITYDATA_ACTIVITY_ID);
        final DataInstanceImpl dataInstance = new DataInstanceImpl() {

            @Override
            public void setValue(final Serializable value) {
                // TODO Auto-generated method stub

            }

            @Override
            public Serializable getValue() {
                // TODO Auto-generated method stub
                return null;
            }
        };
        doReturn(dataInstance).when(processAPI).getActivityDataInstance(anyString(), anyLong());

        // when
        final DataInstance dataInstanceResult = activityVariableResource.getTaskVariable();

        // then
        assertThat(dataInstanceResult).isEqualTo(dataInstance);
    }

}
