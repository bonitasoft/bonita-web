package org.bonitasoft.web.rest.server.datastore.bpm.cases;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.bonitasoft.test.toolkit.bpm.TestCase;
import org.bonitasoft.test.toolkit.bpm.TestProcess;
import org.bonitasoft.test.toolkit.bpm.TestProcessFactory;
import org.bonitasoft.test.toolkit.organization.TestUser;
import org.bonitasoft.test.toolkit.organization.TestUserFactory;
import org.bonitasoft.web.rest.model.bpm.cases.CaseItem;
import org.bonitasoft.web.rest.server.AbstractConsoleTest;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.junit.Test;

/**
 * @author ROHART Bastien
 */
public class CaseDatastoreIntegrationTest extends AbstractConsoleTest {

    private CaseDatastore caseDatastore;

    @Override
    public void consoleTestSetUp() throws Exception {
        caseDatastore = new CaseDatastore(getInitiator().getSession());
    }

    @Override
    protected TestUser getInitiator() {
        return TestUserFactory.getJohnCarpenter();
    }

    @Test
    public void twoPoolsWithOneWithACallActivityCaseTest() throws Exception {
        final long before = caseDatastore.search(0, 100, null, null, new HashMap<String, String>()).getTotal();
        final TestProcess process2 = TestProcessFactory.getDefaultHumanTaskProcess();

        // start process1 case via call activity
        final TestProcess process1 = TestProcessFactory.getCallActivityProcess(process2.getProcessDefinition());
        final TestCase parentCase = process1.addActor(getInitiator()).startCase();

        //wait for process instance to be in a "stable" state
        parentCase.getNextHumanTask();
        // Filters for Opened Cases
        final ItemSearchResult<CaseItem> itemSearchResult = caseDatastore.search(0, 100, null, null, new HashMap<String, String>());

        assertEquals("2 cases started but one via call activity so only 1 should be opened", 1, itemSearchResult.getResults().size() - before);

        TestProcessFactory.getInstance().delete(process1);
        TestProcessFactory.getInstance().delete(process2);
    }

}
