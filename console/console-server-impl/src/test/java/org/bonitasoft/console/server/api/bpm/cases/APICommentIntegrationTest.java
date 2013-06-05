package org.bonitasoft.console.server.api.bpm.cases;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.bonitasoft.console.client.model.bpm.cases.CommentItem;
import org.bonitasoft.console.server.AbstractConsoleTest;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.test.toolkit.bpm.TestCase;
import org.bonitasoft.test.toolkit.bpm.TestProcessFactory;
import org.bonitasoft.test.toolkit.organization.TestUser;
import org.bonitasoft.test.toolkit.organization.TestUserFactory;
import org.junit.Test;

public class APICommentIntegrationTest extends AbstractConsoleTest {

    private APIComment apiComment;

    private final String content = "Commentaire du processus par défault";

    private TestCase testCase;

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.console.server.AbstractJUnitWebTest#webTestSetUp()
     */
    @Override
    public void consoleTestSetUp() throws Exception {
        this.apiComment = new APIComment();
        this.apiComment.setCaller(getAPICaller(getInitiator().getSession(), "API/bpm/comment"));

        testCase = TestProcessFactory.getDefaultHumanTaskProcess().addActor(getInitiator()).startCase();
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
    public void testAddCommentItem() throws Exception {
        final CommentItem commentItem = createCommentItem(this.content);
        this.apiComment.add(commentItem);

        assertEquals("Can't add a comment with the APIComment",
                TenantAPIAccessor.getProcessAPI(getInitiator().getSession())
                        .getComments(testCase.getId()).size(), 1);
    }

    @Test
    public void testSearchCommentItem() throws Exception {
        testCase.addComments(getInitiator(), 3, this.content);

        // Set the filters
        final HashMap<String, String> filters = new HashMap<String, String>();
        filters.put(CommentItem.ATTRIBUTE_USER_ID, String.valueOf(getInitiator().getId()));
        filters.put(CommentItem.ATTRIBUTE_PROCESS_INSTANCE_ID, String.valueOf(testCase.getId()));

        // Search the CommentItem
        final CommentItem item = this.apiComment.search(0, 10, null, null, filters).getResults().get(0);
        assertEquals("Find the wrong CommentItem with APIComment", this.content + "0",
                item.getAttributeValue(CommentItem.ATTRIBUTE_CONTENT));

    }

    /**
     * @param comment
     */
    private CommentItem createCommentItem(final String comment) {
        final CommentItem item = new CommentItem();
        item.setProcessInstanceId(testCase.getId());
        item.setContent(comment);
        return item;
    }

}
