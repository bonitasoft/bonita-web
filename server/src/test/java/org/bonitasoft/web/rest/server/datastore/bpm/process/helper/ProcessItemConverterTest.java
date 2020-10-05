package org.bonitasoft.web.rest.server.datastore.bpm.process.helper;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.actor.ActorInstance;
import org.bonitasoft.engine.bpm.actor.ActorNotFoundException;
import org.bonitasoft.engine.bpm.process.ActivationState;
import org.bonitasoft.engine.bpm.process.ConfigurationState;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfo;
import org.bonitasoft.engine.bpm.process.impl.internal.ProcessDeploymentInfoImpl;
import org.bonitasoft.web.rest.model.ModelFactory;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.toolkit.client.ItemDefinitionFactory;
import org.bonitasoft.web.toolkit.client.common.CommonDateFormater;
import org.bonitasoft.web.toolkit.client.common.i18n.I18n;
import org.bonitasoft.web.toolkit.server.utils.ServerDateFormater;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProcessItemConverterTest {

    private ProcessItemConverter processItemConverter;

    @Mock
    private ProcessAPI processAPI;

    @Mock
    private ActorInstance actorInstance1;

    @Mock
    private ActorInstance actorInstance2;

    private I18n i18n;

    @Before
    public void setUp() throws Exception {
        i18n = I18n.getInstance();
        CommonDateFormater.setDateFormater(new ServerDateFormater());
        ItemDefinitionFactory.setDefaultFactory(new ModelFactory());
        processItemConverter = spy(new ProcessItemConverter(processAPI, 1L));
    }

    @Test
    public void shouldReadActorInitiatorFromCacheOnSecondCall() throws ActorNotFoundException, ProcessDefinitionNotFoundException {

        when(processAPI.getActorInitiator(2)).thenReturn(actorInstance2).thenThrow(new IllegalStateException("Not supposed to call engine twice. Value should be red from cache."));
        doReturn(6L).when(actorInstance2).getId();

        ProcessDeploymentInfo processDeploymentInfo = new ProcessDeploymentInfoImpl(1, 2, "ProcessName", "Version",
        "Description",new Date(), 3, ActivationState.ENABLED,ConfigurationState.RESOLVED,"displayName", new Date(), "iconPath",
        "displayDescription");

        ProcessItem processItem = processItemConverter.convert(processDeploymentInfo);

        assertNotNull(processItem);
        assertEquals("6", processItem.getActorInitiatorId());

        processItem = processItemConverter.convert(processDeploymentInfo);
        //Assert get value from cache
        assertEquals("6", processItem.getActorInitiatorId());

    }

    @Test
    public void shouldStoreDifferentActorInitiatorIntoCache() throws ActorNotFoundException, ProcessDefinitionNotFoundException {

        when(processAPI.getActorInitiator(1)).thenReturn(actorInstance1).thenThrow(new IllegalStateException("Not supposed to call engine twice. Value should be red from cache."));
        when(processAPI.getActorInitiator(2)).thenReturn(actorInstance2).thenThrow(new IllegalStateException("Not supposed to call engine twice. Value should be red from cache."));
        doReturn(5L).when(actorInstance1).getId();
        doReturn(6L).when(actorInstance2).getId();

        ProcessDeploymentInfo firstProcessDeploymentInfo = new ProcessDeploymentInfoImpl(1, 1, "ProcessName1", "Version1",
                "Description1",new Date(), 3, ActivationState.ENABLED,ConfigurationState.RESOLVED,"displayName1", new Date(), "iconPath1",
                "displayDescription1");
        ProcessDeploymentInfo secondProcessDeploymentInfo = new ProcessDeploymentInfoImpl(2, 2, "ProcessName2", "Version2",
                "Description2",new Date(), 3, ActivationState.ENABLED,ConfigurationState.RESOLVED,"displayName2", new Date(), "iconPath2",
                "displayDescription2");

        //Get 2 ActorInitiatorId from engine then store them in cache
        ProcessItem processItem = processItemConverter.convert(firstProcessDeploymentInfo);
        assertEquals("5", processItem.getActorInitiatorId());
        processItem = processItemConverter.convert(secondProcessDeploymentInfo);
        assertEquals("6", processItem.getActorInitiatorId());

        // Read 2 different ActorInitiatorId from cache
        processItem = processItemConverter.convert(firstProcessDeploymentInfo);
        assertEquals("5", processItem.getActorInitiatorId());
        processItem = processItemConverter.convert(secondProcessDeploymentInfo);
        assertEquals("6", processItem.getActorInitiatorId());
    }



}
