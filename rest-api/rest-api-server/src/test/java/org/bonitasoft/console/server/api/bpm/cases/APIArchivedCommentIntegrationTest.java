/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.console.server.api.bpm.cases;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.bonitasoft.console.client.model.bpm.cases.ArchivedCommentItem;
import org.bonitasoft.console.server.AbstractConsoleTest;
import org.bonitasoft.test.toolkit.bpm.TestCase;
import org.bonitasoft.test.toolkit.bpm.TestProcessFactory;
import org.bonitasoft.test.toolkit.organization.TestUser;
import org.bonitasoft.test.toolkit.organization.TestUserFactory;
import org.bonitasoft.web.toolkit.server.search.ItemSearchResult;
import org.junit.Test;

/**
 * @author Paul AMAR
 * 
 */
public class APIArchivedCommentIntegrationTest extends AbstractConsoleTest {

    private APIArchivedComment apiArchivedComment;

    private TestCase monTest;

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.console.server.AbstractJUnitWebTest#webTestSetUp()
     */
    @Override
    public void consoleTestSetUp() throws Exception {
        this.apiArchivedComment = new APIArchivedComment();
        this.apiArchivedComment.setCaller(getAPICaller(TestUserFactory.getJohnCarpenter().getSession(),
                "API/bpm/archivedComment"));

        this.monTest = TestProcessFactory.getDefaultHumanTaskProcess().addActor(getInitiator()).startCase();
        this.monTest.getNextHumanTask().assignTo(getInitiator());
        this.monTest.addComments(getInitiator(), 12, "mon Commentaire");

        this.monTest.execute().execute();
    }

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.test.toolkit.AbstractJUnitTest#getInitiator()
     */
    @Override
    protected TestUser getInitiator() {
        return TestUserFactory.getJohnCarpenter();
    }

    @Test
    public void testSearch() throws Exception {

        // ProblÃ¨me de user ? => non ?
        final ItemSearchResult<ArchivedCommentItem> mesResultats = this.apiArchivedComment.search(0, 12, "", "", new HashMap<String, String>());
        assertEquals(mesResultats.getLength(), 12);

    }

}
