package org.bonitasoft.web.rest.server.api.bpm.flownode;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.data.DataInstance;
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
    private ProcessAPI processAPI;

    private ActivityVariableResource activityVariableResource;

    @Before
    public void initializeMocks() {
        initMocks(this);
        activityVariableResource = spy(new ActivityVariableResource());
        doReturn(processAPI).when(activityVariableResource).getEngineProcessAPI();
    }

    @Test
    public void should_thow_an_api_exception_when_getActivityDataInstance_failed() throws DataNotFoundException {
        // Given
        doReturn("").when(activityVariableResource).getAttribute(ActivityVariableResource.ACTIVITYDATA_DATA_NAME);
        doReturn("1").when(activityVariableResource).getAttribute(ActivityVariableResource.ACTIVITYDATA_ACTIVITY_ID);
        doThrow(new DataNotFoundException(new Exception("plop"))).when(processAPI).getActivityDataInstance(anyString(), anyLong());

        try {
            // When
            activityVariableResource.getTaskVariable();
        } catch (final APIException e) {
            // Then
            assertTrue("The root cause must be a DataNotFoundException, and not : " + e.getCause().getClass(), e.getCause() instanceof DataNotFoundException);
        }
    }

    @Test
    public void should_return_data_instance() throws DataNotFoundException {
        // Given
        doReturn("").when(activityVariableResource).getAttribute(ActivityVariableResource.ACTIVITYDATA_DATA_NAME);
        doReturn("1").when(activityVariableResource).getAttribute(ActivityVariableResource.ACTIVITYDATA_ACTIVITY_ID);
        final DataInstance dataInstance = mock(DataInstance.class);
        doReturn(dataInstance).when(processAPI).getActivityDataInstance(anyString(), anyLong());

        // When
        final DataInstance result = activityVariableResource.getTaskVariable();

        // Then
        assertEquals("Should return the result of the Engine API.", dataInstance, result);
        verify(activityVariableResource).getAttribute(ActivityVariableResource.ACTIVITYDATA_ACTIVITY_ID);
        verify(activityVariableResource).getAttribute(ActivityVariableResource.ACTIVITYDATA_DATA_NAME);
    }

}
