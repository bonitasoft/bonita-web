package org.bonitasoft.web.rest.server.datastore.bpm.cases;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.List;

import org.bonitasoft.engine.bpm.process.ProcessInstanceSearchDescriptor;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.search.impl.SearchFilter;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.bpm.cases.CaseItem;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CaseDatastoreTest {

    Logger logger = LoggerFactory.getLogger(CaseDatastoreTest.class);

    private CaseDatastore caseDatastore;

    @Before
    public void setUp() throws Exception {
        caseDatastore = spy(new CaseDatastore(mock(APISession.class)));
    }

    @Test
    public void testSearch() throws Exception {
        final HashMap<String, String> filters = new HashMap<String, String>();
        final String processName = "Pool";
        filters.put(CaseItem.ATTRIBUTE_PROCESS_NAME, processName);
        final ArgumentCaptor<SearchOptionsBuilder> argument = ArgumentCaptor.forClass(SearchOptionsBuilder.class);
        doReturn(mock(SearchResult.class)).when(caseDatastore).runSearch(eq(filters), any(SearchOptionsBuilder.class));
        caseDatastore.search(0, 100, null, null, filters);
        verify(caseDatastore).runSearch(eq(filters), argument.capture());
        final List<SearchFilter> searchFilters = argument.getValue().done().getFilters();
        assertThat(searchFilters).hasSize(2);
        assertThat(searchFilters.get(0).getField()).isEqualToIgnoringCase(ProcessInstanceSearchDescriptor.NAME);
        assertThat(searchFilters.get(0).getValue()).isSameAs(processName);
        logger.info("{}", searchFilters.get(0).getField());
    }

}
