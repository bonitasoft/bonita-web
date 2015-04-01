package org.bonitasoft.web.rest.server.datastore.bpm.flownode;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.engine.bpm.flownode.FlowNodeInstance;
import org.bonitasoft.engine.bpm.flownode.FlowNodeInstanceSearchDescriptor;
import org.bonitasoft.engine.search.SearchFilterOperation;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.bpm.flownode.FlowNodeItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AbstractFlowNodeDatastoreTest {

    AbstractFlowNodeDatastore<FlowNodeItem, FlowNodeInstance> datastore;

    @Before
    public void setUp() throws Exception {
        final APISession engineSession = mock(APISession.class);
        datastore = new AbstractFlowNodeDatastore<FlowNodeItem, FlowNodeInstance>(engineSession);
    }

    @Test
    public void makeSearchOptionBuilder_state_Filter_Pending_Adds_multiple_entries_in_SearchOption() throws Exception {
        final Map<String, String> filters = new HashMap<String, String>();
        filters.put(FlowNodeInstanceSearchDescriptor.STATE_NAME, "pending");
        final SearchOptionsBuilder makeSearchOptionBuilder = datastore.makeSearchOptionBuilder(0, 10, "", "", filters);
        assertThat(makeSearchOptionBuilder.done().getFilters()).extracting("field", "operation", "value").contains(
                tuple(null, SearchFilterOperation.L_PARENTHESIS, null),
                tuple("state", SearchFilterOperation.EQUALS, "ready"),
                tuple(null, SearchFilterOperation.OR, null),
                tuple("state", SearchFilterOperation.EQUALS, "waiting"),
                tuple(null, SearchFilterOperation.R_PARENTHESIS, null),
                tuple("state", SearchFilterOperation.DIFFERENT, "aborted"),
                tuple("state", SearchFilterOperation.DIFFERENT, "cancelled"),
                tuple("state", SearchFilterOperation.DIFFERENT, "completed")
                );
    }

    @Test
    public void makeSearchOptionBuilder_state_Filter_Ongoing_Adds_multiple_entries_in_SearchOption() throws Exception {
        final Map<String, String> filters = new HashMap<String, String>();
        filters.put(FlowNodeInstanceSearchDescriptor.STATE_NAME, "ongoing");
        final SearchOptionsBuilder makeSearchOptionBuilder = datastore.makeSearchOptionBuilder(0, 10, "", "", filters);
        assertThat(makeSearchOptionBuilder.done().getFilters()).extracting("field", "operation", "value").contains(
                tuple(null, SearchFilterOperation.L_PARENTHESIS, null),
                tuple("state", SearchFilterOperation.EQUALS, "executing"),
                tuple(null, SearchFilterOperation.OR, null),
                tuple("state", SearchFilterOperation.EQUALS, "completing"),
                tuple(null, SearchFilterOperation.OR, null),
                tuple("state", SearchFilterOperation.EQUALS, "initializing"),
                tuple(null, SearchFilterOperation.R_PARENTHESIS, null),
                tuple("state", SearchFilterOperation.DIFFERENT, "aborted"),
                tuple("state", SearchFilterOperation.DIFFERENT, "cancelled"),
                tuple("state", SearchFilterOperation.DIFFERENT, "completed")
                );
    }

    @Test
    public void makeSearchOptionBuilder_state_Filter_Ready_Adds_single_entries_in_SearchOption() throws Exception {
        final Map<String, String> filters = new HashMap<String, String>();
        filters.put(FlowNodeInstanceSearchDescriptor.STATE_NAME, "ready");
        final SearchOptionsBuilder makeSearchOptionBuilder = datastore.makeSearchOptionBuilder(0, 10, "", "", filters);
        assertThat(makeSearchOptionBuilder.done().getFilters()).extracting("field", "operation", "value").contains(
                tuple("state", SearchFilterOperation.EQUALS, "ready"),
                tuple("state", SearchFilterOperation.DIFFERENT, "aborted"),
                tuple("state", SearchFilterOperation.DIFFERENT, "cancelled"),
                tuple("state", SearchFilterOperation.DIFFERENT, "completed")
                );
    }

    @Test
    public void makeSearchOptionBuilder_without_state_Filter_Adds_only_Unwanted_state_SearchOption() throws Exception {
        final Map<String, String> filters = new HashMap<String, String>();
        filters.put(FlowNodeInstanceSearchDescriptor.STATE_NAME, "ready");
        final SearchOptionsBuilder makeSearchOptionBuilder = datastore.makeSearchOptionBuilder(0, 10, "", "", filters);
        assertThat(makeSearchOptionBuilder.done().getFilters()).extracting("field", "operation", "value").contains(
                tuple("state", SearchFilterOperation.DIFFERENT, "aborted"),
                tuple("state", SearchFilterOperation.DIFFERENT, "cancelled"),
                tuple("state", SearchFilterOperation.DIFFERENT, "completed")
                );
    }

}
