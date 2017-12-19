package org.bonitasoft.web.rest.server.datastore.bpm.flownode;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance;
import org.bonitasoft.engine.exception.SearchException;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.impl.APISessionImpl;
import org.bonitasoft.web.rest.model.bpm.flownode.HumanTaskItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Date;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class AbstractHumanTaskDatastoreTest {

    @Mock
    private ProcessAPI processAPI;
    private APISession session = new APISessionImpl(55L, new Date(), 5000, "john", 44L, "default", 1L);
    private AbstractHumanTaskDatastore<HumanTaskItem, HumanTaskInstance> datastore = new AbstractHumanTaskDatastore<HumanTaskItem, HumanTaskInstance>(session) {
        @Override
        protected ProcessAPI getProcessAPI() {
            return processAPI;
        }
    };

    @Test
    public void should_use_searchAssignedAndPendingHumanTasks_when_filtering_ready_state() throws SearchException {
        datastore.runSearch(new SearchOptionsBuilder(1, 100), Collections.singletonMap("state", "ready"));

        verify(processAPI).searchAssignedAndPendingHumanTasks(any());
    }

    @Test
    public void should_use_searchHumanTaskInstances_when_there_is_no_particular_filter() throws SearchException {
        datastore.runSearch(new SearchOptionsBuilder(1, 100), Collections.singletonMap("tpye", "toto"));

        verify(processAPI).searchHumanTaskInstances(any());
    }

}