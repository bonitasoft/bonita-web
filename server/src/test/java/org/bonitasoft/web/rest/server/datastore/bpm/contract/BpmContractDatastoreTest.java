package org.bonitasoft.web.rest.server.datastore.bpm.contract;

import static org.assertj.core.api.Assertions.assertThat;

import org.bonitasoft.engine.bpm.contract.impl.ContractDefinitionImpl;
import org.bonitasoft.engine.bpm.contract.impl.InputDefinitionImpl;
import org.bonitasoft.engine.bpm.contract.impl.RuleDefinitionImpl;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.test.toolkit.bpm.TestProcessFactory;
import org.bonitasoft.web.rest.model.bpm.contract.BpmContractItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BpmContractDatastoreTest {

    @Mock
    private APISession engineSession;

    @Test
    public void convertEngineToConsoleItem() throws Exception {
        //given
        final ContractDefinitionImpl expectedContractDefinitionImpl = getContractDefinition();
        final BpmContractDatastore contractDatastore = new BpmContractDatastore(engineSession);

        //when
        final BpmContractItem convertEngineToConsoleItem = contractDatastore.convertEngineToConsoleItem(expectedContractDefinitionImpl);

        //then
        assertThat(convertEngineToConsoleItem.getAttributeNames()).contains(BpmContractItem.ATTRIBUTE_INPUTS);
        assertThat(convertEngineToConsoleItem.getAttributeNames()).contains(BpmContractItem.ATTRIBUTE_RULES);
    }

    private ContractDefinitionImpl getContractDefinition() {
        final ContractDefinitionImpl expectedContractDefinitionImpl = new ContractDefinitionImpl();
        expectedContractDefinitionImpl
        .addInput(new InputDefinitionImpl(TestProcessFactory.CONTRACT_INPUT_NAME + "1", TestProcessFactory.CONTRACT_INPUT_TYPE,
                TestProcessFactory.CONTRACT_INPUT_DESCRIPTION));
        expectedContractDefinitionImpl
        .addInput(new InputDefinitionImpl(TestProcessFactory.CONTRACT_INPUT_NAME + "2", TestProcessFactory.CONTRACT_INPUT_TYPE,
                TestProcessFactory.CONTRACT_INPUT_DESCRIPTION));
        expectedContractDefinitionImpl
        .addInput(new InputDefinitionImpl(TestProcessFactory.CONTRACT_INPUT_NAME + "3", TestProcessFactory.CONTRACT_INPUT_TYPE,
                TestProcessFactory.CONTRACT_INPUT_DESCRIPTION));

        expectedContractDefinitionImpl.addRule(new RuleDefinitionImpl(TestProcessFactory.CONTRACT_RULE_NAME + "1", TestProcessFactory.CONTRACT_RULE_EXPRESSION,
                TestProcessFactory.CONTRACT_RULE_EXPLANATION));
        expectedContractDefinitionImpl.addRule(new RuleDefinitionImpl(TestProcessFactory.CONTRACT_RULE_NAME + "2", TestProcessFactory.CONTRACT_RULE_EXPRESSION,
                TestProcessFactory.CONTRACT_RULE_EXPLANATION));
        return expectedContractDefinitionImpl;
    }

}
