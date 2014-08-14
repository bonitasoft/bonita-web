/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.rest.server.engineclient;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.contract.ContractDefinition;
import org.bonitasoft.engine.bpm.contract.InputDefinition;
import org.bonitasoft.engine.bpm.contract.RuleDefinition;
import org.bonitasoft.test.toolkit.bpm.TestCaseFactory;
import org.bonitasoft.test.toolkit.bpm.TestHumanTask;
import org.bonitasoft.test.toolkit.bpm.TestProcessFactory;
import org.bonitasoft.test.toolkit.organization.TestUser;
import org.bonitasoft.test.toolkit.organization.TestUserFactory;
import org.bonitasoft.web.rest.server.AbstractConsoleTest;
import org.junit.Test;

public class HumanTaskEngineClientContractIT extends AbstractConsoleTest {

    private ProcessEngineClient processEngineClient;

    @Override
    public void consoleTestSetUp() throws Exception {
        processEngineClient = new ProcessEngineClient(TenantAPIAccessor.getProcessAPI(getInitiator().getSession()));
    }

    @Override
    protected TestUser getInitiator() {
        return TestUserFactory.getJohnCarpenter();
    }

    @Test
    public void testStepContract() throws Exception {
        //given
        final TestHumanTask humanTask = TestCaseFactory.createRandomCase(getInitiator()).getNextHumanTask();

        //when
        final ContractDefinition contract = processEngineClient.getUserTaskContract(humanTask.getId());
        final List<InputDefinition> inputs = contract.getInputs();
        final List<RuleDefinition> rules = contract.getRules();

        //then
        assertThat(contract).as("should get contract").isNotNull();
        assertThat(inputs).as("should get contract inputs").isNotEmpty();
        assertThat(rules).as("should get contract rules").isNotEmpty();

        final InputDefinition inputDefinition = inputs.get(0);
        assertThat(inputDefinition.getName()).as("should get contract input name").isEqualTo(TestProcessFactory.CONTRACT_INPUT_NAME);
        assertThat(inputDefinition.getDescription()).as("should get contract description").isEqualTo(TestProcessFactory.CONTRACT_INPUT_DESCRIPTION);
        assertThat(inputDefinition.getType()).as("should get contract input type").isEqualTo(TestProcessFactory.CONTRACT_INPUT_TYPE);

        final RuleDefinition ruleDefinition = rules.get(0);
        assertThat(ruleDefinition.getName()).as("should get contract rule name").isEqualTo(TestProcessFactory.CONTRACT_RULE_NAME);
        assertThat(ruleDefinition.getExplanation()).as("should get contract rule explanation").isEqualTo(TestProcessFactory.CONTRACT_RULE_EXPLANATION);
        assertThat(ruleDefinition.getExpression()).as("should get contract rule expression").isEqualTo(TestProcessFactory.CONTRACT_RULE_EXPRESSION);
        assertThat(ruleDefinition.getInputNames()).as("should get contract rule input names").containsExactly(TestProcessFactory.CONTRACT_INPUT_NAME);

    }
}
