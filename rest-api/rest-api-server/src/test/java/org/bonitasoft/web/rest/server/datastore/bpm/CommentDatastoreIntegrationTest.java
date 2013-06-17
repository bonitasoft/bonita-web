package org.bonitasoft.web.rest.server.datastore.bpm;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.bonitasoft.test.toolkit.bpm.TestCase;
import org.bonitasoft.test.toolkit.bpm.TestHumanTask;
import org.bonitasoft.test.toolkit.bpm.TestProcessFactory;
import org.bonitasoft.test.toolkit.organization.TestUser;
import org.bonitasoft.test.toolkit.organization.TestUserFactory;
import org.bonitasoft.web.rest.api.model.bpm.cases.CommentItem;
import org.bonitasoft.web.rest.server.AbstractConsoleTest;
import org.bonitasoft.web.rest.server.datastore.bpm.cases.CommentDatastore;
import org.bonitasoft.web.toolkit.server.search.ItemSearchResult;
import org.junit.Test;

public class CommentDatastoreIntegrationTest extends AbstractConsoleTest {

    private static final String COMMENTS_CONTENT = "#*Ã©Ã Ã¢Ã¤Ã«ÃªÃ©~Ã§ÃžÅ¡Å’Ã˜Ã�Ã†";

    private CommentDatastore commentDatastore;

    private TestCase testCase;

    private TestHumanTask testHumanTask;

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.test.AbstractJUnitTest#getInitiator()
     */
    @Override
    protected TestUser getInitiator() {
        return TestUserFactory.getJohnCarpenter();
    }

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.console.server.AbstractJUnitWebTest#webTestSetUp()
     */
    @Override
    public void consoleTestSetUp() throws Exception {
        this.commentDatastore = new CommentDatastore(TestUserFactory.getJohnCarpenter().getSession());

        this.testCase = TestProcessFactory.getDefaultHumanTaskProcess()
                .addActor(TestUserFactory.getJohnCarpenter())
                .startCase();
        this.testHumanTask = this.testCase.getNextHumanTask();
    }

    @Test
    public void paginationCommentTest() throws Exception {
        this.testHumanTask.addComments(12, COMMENTS_CONTENT);
        final HashMap<String, String> filters = new HashMap<String, String>();
        filters.put(CommentItem.ATTRIBUTE_PROCESS_INSTANCE_ID, String.valueOf(this.testHumanTask.getId()));
        final ItemSearchResult<CommentItem> resultPage1 = this.commentDatastore.search(0, 10, null, null, filters);

        int nb = 0;
        for (final CommentItem instance : resultPage1.getResults()) {
            assertEquals("Search of the comment number " + nb + " not possible",
                    instance.getProcessInstanceId(), this.testHumanTask.getId());
            assertEquals("Search of the comment number " + nb + " not possible",
                    instance.getUserId(), TestUserFactory.getJohnCarpenter().getId());
            assertEquals("Search of the comment number " + nb + " not possible",
                    instance.getContent(), COMMENTS_CONTENT + nb);
            nb++;
        }
    }

    @Test
    public void specialCharHumanTaskTest() throws Exception {
        this.testHumanTask.addComment(COMMENTS_CONTENT);

        final HashMap<String, String> filters = new HashMap<String, String>();
        filters.put(CommentItem.ATTRIBUTE_PROCESS_INSTANCE_ID, String.valueOf(this.testHumanTask.getId()));
        final ItemSearchResult<CommentItem> result = this.commentDatastore.search(0, 10, null, null, filters);

        final CommentItem comment = result.getResults().get(0);
        assertEquals("Comment with special UTF-8 characters on a humanTask is not possible",
                comment.getContent(), COMMENTS_CONTENT);
    }

    // @Test
    // public void specialCharManualTaskTest() throws Exception {
    // // Assign the human task to john
    // testHumanTask.assignTo(TestUserFactory.getJohnCarpenter());
    //
    // // Create a subtask and add a comment
    // TestSubTask subTask = testHumanTask.addSubTask("manualTask1",
    // "Manualtask display name",
    // TestUserFactory.getJohnCarpenter().getId(),
    // "Manualtask display description",
    // new Date(), TaskPriority.NORMAL);
    // subTask.addComment(COMMENTS_CONTENT);
    //
    // // search the subtask with dataStore
    // HashMap<String, String> filters = new HashMap<String, String>();
    // filters.put(CommentItem.ATTRIBUTE_PROCESS_INSTANCE_ID, String.valueOf(subTask.getId()));
    // ItemSearchResult<CommentItem> result = commentDatastore.search(0, 10, "", "", filters);
    //
    // CommentItem comment = result.getResults().get(0);
    // assertEquals("Comment with special UTF-8 characters on a manualTask is not possible",
    // comment.getContent(), COMMENTS_CONTENT);
    // }

}
