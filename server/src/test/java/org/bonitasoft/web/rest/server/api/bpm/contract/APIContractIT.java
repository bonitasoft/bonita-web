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
package org.bonitasoft.web.rest.server.api.bpm.contract;

import static org.assertj.core.api.Assertions.assertThat;

import org.bonitasoft.test.toolkit.bpm.TestCase;
import org.bonitasoft.test.toolkit.bpm.TestHumanTask;
import org.bonitasoft.test.toolkit.bpm.TestProcessFactory;
import org.bonitasoft.test.toolkit.organization.TestUser;
import org.bonitasoft.test.toolkit.organization.TestUserFactory;
import org.bonitasoft.web.rest.model.bpm.contract.BpmContractItem;
import org.bonitasoft.web.rest.server.AbstractConsoleTest;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.junit.Test;

/**
 * @author Laurent Leseigneur
 */
public class APIContractIT extends AbstractConsoleTest {

    private APIContract apiContract;

    @Override
    public void consoleTestSetUp() throws Exception {
        apiContract = new APIContract();
        apiContract.setCaller(getAPICaller(TestUserFactory.getJohnCarpenter().getSession(), "API/bpm/contract"));
    }

    @Override
    protected TestUser getInitiator() {
        return TestUserFactory.getJohnCarpenter();
    }

    @Test
    public void should_get_contract_for_human_task() throws Exception {
        //given
        final TestCase testCase = TestProcessFactory.getDefaultHumanTaskProcess().addActor(getInitiator()).startCase();
        final TestHumanTask humanTask = testCase.getNextHumanTask();
        humanTask.assignTo(getInitiator());

        //when
        final BpmContractItem contractItem = apiContract.get(APIID.makeAPIID(humanTask.getId()));
        final String inputs = contractItem.getAttributeValue(BpmContractItem.ATTRIBUTE_INPUTS);
        final String rules = contractItem.getAttributeValue(BpmContractItem.ATTRIBUTE_RULES);

        //then
        assertThat(inputs).as("should get contract inputs").isEqualTo(
                "[{\"id\":0,\"name\":\"inputName\",\"description\":\"description\",\"type\":\"java.lang.String\"}]");
        assertThat(rules).as("should get contract rules").isEqualTo(
                "[{\"id\":0,\"name\":\"ruleName\",\"expression\":\" 1 == 1 \",\"explanation\":\"explanation\",\"inputNames\":[\"inputName\"]}]");

    }

}
