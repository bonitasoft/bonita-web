package org.bonitasoft.web.rest.server.api.bpm.flownode;


import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
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
public class ActivityDataResourceTest {

    @Mock
    ProcessAPI processAPI;

    private ActivityDataResource activityDataResource;


    @Before
    public void initializeMocks() {
        initMocks(this);
        activityDataResource = spy(new ActivityDataResource());
        doReturn(processAPI).when(activityDataResource).getEngineProcessAPI();
    }

    @Test(expected = APIException.class)
    public void should_do_get_with_nothing_thows_an_api_exception() throws DataNotFoundException {
        //when
        activityDataResource.doGet();
    }

    @Test(expected = APIException.class)
    public void should_throw_exception_if_attribute_is_not_found() throws Exception {
        // given:
        doReturn(null).when(activityDataResource).getAttribute(anyString());

        // when:
        activityDataResource.doGet();
    }

    @Test
    public void should_do_get_call_getAttribute() throws DataNotFoundException {
        //given
        doReturn("").when(activityDataResource).getAttribute(ActivityDataResource.ACTIVITYDATA_DATA_NAME);
        doReturn("1").when(activityDataResource).getAttribute(ActivityDataResource.ACTIVITYDATA_ACTIVITY_ID);
        doReturn("").when(activityDataResource).toJson(any(DataInstance.class));
        doReturn(null).when(processAPI).getActivityDataInstance(anyString(), anyLong());

        //when
        activityDataResource.doGet();

        //then
        verify(activityDataResource).getAttribute(ActivityDataResource.ACTIVITYDATA_ACTIVITY_ID);
        verify(activityDataResource).getAttribute(ActivityDataResource.ACTIVITYDATA_DATA_NAME);

    }




}
