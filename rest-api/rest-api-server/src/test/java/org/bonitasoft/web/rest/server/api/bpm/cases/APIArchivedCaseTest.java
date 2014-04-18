package org.bonitasoft.web.rest.server.api.bpm.cases;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import org.bonitasoft.web.rest.model.bpm.cases.ArchivedCaseItem;
import org.bonitasoft.web.rest.server.datastore.bpm.process.ProcessDatastore;
import org.bonitasoft.web.rest.server.datastore.organization.UserDatastore;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class APIArchivedCaseTest {

    @Mock
    private ItemDefinition<ArchivedCaseItem> itemDefinition;

    @Mock
    protected UserDatastore userDatastore;
    
    @Mock 
    protected ProcessDatastore processDatastore;
    
    @InjectMocks
    @Spy
    private APIArchivedCase aPIArchivedCase;
    
    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testFillDeploys() throws Exception {
        throw new RuntimeException("not yet implemented");
    }

}
