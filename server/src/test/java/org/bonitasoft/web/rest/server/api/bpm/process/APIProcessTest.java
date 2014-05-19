package org.bonitasoft.web.rest.server.api.bpm.process;

import static org.mockito.Mockito.when;

import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class APIProcessTest {

    @InjectMocks
    APIProcess apiProcess;

    @Mock
    private ProcessItem item;

    @Before
    public void init(){
        
    }
    
    @Test
    public void should_addTest() {
        when(item.getIcon()).thenReturn("moniquecone");
        // apiProcess.add(item);
    }
}
