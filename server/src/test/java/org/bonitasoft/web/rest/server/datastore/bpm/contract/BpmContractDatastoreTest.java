package org.bonitasoft.web.rest.server.datastore.bpm.contract;

import static org.assertj.core.api.Assertions.assertThat;

import org.bonitasoft.engine.bpm.contract.ContractDefinition;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.bpm.contract.BpmContractItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BpmContractDatastoreTest {

    @Mock
    private APISession engineSession;

    @Mock
    private ContractDefinition engineItem;

    @Test
    public void convertEngineToConsoleItem() throws Exception {
        //given
        final BpmContractDatastore contractDatastore = new BpmContractDatastore(engineSession);

        //when
        final BpmContractItem convertEngineToConsoleItem = contractDatastore.convertEngineToConsoleItem(engineItem);

        //then
        assertThat(convertEngineToConsoleItem.getAttributeNames()).contains(BpmContractItem.ATTRIBUTE_INPUTS);
        assertThat(convertEngineToConsoleItem.getAttributeNames()).contains(BpmContractItem.ATTRIBUTE_RULES);

    }

}
