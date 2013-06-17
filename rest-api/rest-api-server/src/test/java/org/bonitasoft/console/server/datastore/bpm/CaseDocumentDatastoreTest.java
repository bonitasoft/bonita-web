//package org.bonitasoft.console.server.datastore.bpm;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.fail;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.bonitasoft.console.client.task.model.HumanTaskItem;
//import org.bonitasoft.console.client.task.model.ManualTaskItem;
//import org.bonitasoft.console.server.common.AbstractJUnitWebTestExt;
//import org.bonitasoft.engine.bpm.model.TaskPriority;
//import org.bonitasoft.test.toolkit.bpm.TestHumanTask;
//import org.bonitasoft.test.toolkit.bpm.TestProcessFactory;
//import org.bonitasoft.test.toolkit.organization.TestUser;
//import org.bonitasoft.test.toolkit.organization.TestUserFactory;
//import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
//import org.junit.Ignore;
//import org.junit.Test;
//
//public class CaseDocumentDatastoreTest extends AbstractJUnitWebTestExt {
//
//    private final static String DUE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
//
//    private TestHumanTask testHumanTask;
//
//    private ManualTaskDatastore manualTaskDatastore;
//
//    /*
//     * (non-Javadoc)
//     * @see org.bonitasoft.test.toolkit.AbstractJUnitTest#getInitiator()
//     */
//    @Override
//    protected TestUser getInitiator() {
//        return TestUserFactory.getRidleyScott();
//    }
//
//    /*
//     * (non-Javadoc)
//     * @see org.bonitasoft.console.server.AbstractJUnitWebTest#webTestSetUp()
//     */
//    @Override
//    public void webSPTestSetUp() throws Exception {
//        this.testHumanTask = TestProcessFactory.getDefaultHumanTaskProcess()
//                .addActor(TestUserFactory.getRidleyScott())
//                .startCase()
//                // human task settings
//                .getNextHumanTask()
//                .assignTo(TestUserFactory.getRidleyScott());
//
//        this.manualTaskDatastore = new ManualTaskDatastore(TestUserFactory.getRidleyScott().getSession());
//    }
//
//    @Test
//    public void createASubTaskToMeTest() throws Exception {
//
//        // create subtask via datastore
//        final ManualTaskItem manualTaskItem = this.manualTaskDatastore.add(createManualTaskItem(this.testHumanTask.getId(), TestUserFactory.getRidleyScott()
//                .getId()));
//
//        // fetch it back
//        final Map<String, String> filters = new HashMap<String, String>();
//        filters.put(ManualTaskItem.ATTRIBUTE_PARENT_TASK_ID, String.valueOf(this.testHumanTask.getId()));
//        final HumanTaskItem humanTaskItem = this.manualTaskDatastore.search(0, 1, null, filters, null).getResults().get(0);
//
//        // test
//        assertEquals("It's not possible to create a subtask to me", manualTaskItem.getAssignedId(),
//                humanTaskItem.getAssignedId());
//        assertEquals("It's not possible to create a subtask to me", manualTaskItem.getId(),
//                humanTaskItem.getId());
//        assertEquals("It's not possible to retrieve the description of th subtask", manualTaskItem.getDescription(),
//                humanTaskItem.getDescription());
//    }
//
//    @Test
//    public void createASubTaskToAnOtherUserTest() throws Exception {
//
//        // Create a subtask for another user
//        final ManualTaskItem manualTaskItem = this.manualTaskDatastore.add(createManualTaskItem(this.testHumanTask.getId(), TestUserFactory.getJohnCarpenter()
//                .getId()));
//
//        // Search the subtask with the humantask parent id
//        final Map<String, String> filters = new HashMap<String, String>();
//        filters.put(ManualTaskItem.ATTRIBUTE_PARENT_TASK_ID, String.valueOf(this.testHumanTask.getId()));
//        final HumanTaskItem humanTaskItem = this.manualTaskDatastore.search(0, 1, null, filters, null).getResults().get(0);
//
//        assertEquals("It's not possible to create a subtask to an other user",
//                manualTaskItem.getAssignedId(), humanTaskItem.getAssignedId());
//        assertEquals("It's not possible to create a subtask to an other user",
//                manualTaskItem.getId(), humanTaskItem.getId());
//        assertEquals("It's not possible to retrieve the description of th subtask", manualTaskItem.getDescription(),
//                humanTaskItem.getDescription());
//    }
//
//    @Test
//    public void createASubTaskAnUserWithSpecialCharTest() throws Exception {
//
//        // Create a subtask for the user with special characters
//        final ManualTaskItem manualTaskItem = this.manualTaskDatastore
//                .add(createManualTaskItem(this.testHumanTask.getId(), TestUserFactory.getMrSpechar().getId()));
//
//        // Search the subtask with the humantask parent id
//        final Map<String, String> filters = new HashMap<String, String>();
//        filters.put(ManualTaskItem.ATTRIBUTE_PARENT_TASK_ID, String.valueOf(this.testHumanTask.getId()));
//        final HumanTaskItem humanTaskItem = this.manualTaskDatastore.search(0, 1, null, filters, null).getResults().get(0);
//
//        assertEquals("It's not possible to create a subtask to an user with UTF-8 special characters",
//                manualTaskItem.getAssignedId(), humanTaskItem.getAssignedId());
//        assertEquals("It's not possible to create a subtask to an user with UTF-8 special characters",
//                manualTaskItem.getId(), humanTaskItem.getId());
//        assertEquals("It's not possible to retrieve the description of th subtask", manualTaskItem.getDescription(),
//                humanTaskItem.getDescription());
//    }
//
//    /*
//     * Assign a subtask to a false user
//     * The method add of ManualTaskDataStore should return an APIException for
//     * a false user.
//     */
//    @Ignore
//    @Test(expected = APIException.class)
//    public void createASubTaskToAnUnexistUserTest() throws Exception {
//
//        // Create a subtask for a false user bob
//        final ManualTaskItem manualTaskItem = createManualTaskItem(this.testHumanTask.getId(), Long.valueOf(-3));
//        this.manualTaskDatastore.add(manualTaskItem);
//        fail("Possible to create a subtask to an unexist user");
//    }
//
//    @Test
//    public void changePrioritySubtaskTest() throws Exception {
//        // Create a subtask for the user john
//        final ManualTaskItem manualTaskItem = this.manualTaskDatastore.add(createManualTaskItem(this.testHumanTask.getId(), TestUserFactory.getRidleyScott()
//                .getId()));
//
//        // Update priority for the subtask to highest
//        final Map<String, String> attributes = new HashMap<String, String>();
//        attributes.put(ManualTaskItem.ATTRIBUTE_PRIORITY, TaskPriority.HIGHEST.name());
//        this.manualTaskDatastore.update(manualTaskItem.getId(), attributes);
//
//        // Search the subtask with the humantask parent id
//        final Map<String, String> filters = new HashMap<String, String>();
//        filters.put(ManualTaskItem.ATTRIBUTE_PARENT_TASK_ID, String.valueOf(this.testHumanTask.getId()));
//        final HumanTaskItem humanTaskItem = this.manualTaskDatastore.search(0, 1, null, filters, null)
//                .getResults().get(0);
//
//        assertEquals("It's not possible to change the priority for a subtask",
//                manualTaskItem.getAssignedId(), humanTaskItem.getAssignedId());
//        assertEquals("It's not possible to change the priority for a subtask",
//                manualTaskItem.getId(), humanTaskItem.getId());
//        assertEquals("It's not possible to change the priority for a subtask",
//                ManualTaskItem.VALUE_PRIORITY_HIGHEST, humanTaskItem.getPriority());
//        assertEquals("It's not possible to retrieve the description of th subtask", manualTaskItem.getDescription(),
//                humanTaskItem.getDescription());
//    }
//
//    // ///////////////////////////////////////////////////////////////////////////////
//    // / Due Dates
//    // ///////////////////////////////////////////////////////////////////////////////
//
//    private final static int TWO_YEARS_MS = 63113852;
//
//    @Ignore
//    @Test(expected = APIException.class)
//    public void checkSubTaskWithDueDatePast() throws Exception {
//        final SimpleDateFormat dateFormat = new SimpleDateFormat(DUE_DATE_FORMAT);
//        final String past = dateFormat.format(new Date(0));
//        this.manualTaskDatastore.add(createManualTaskItem(this.testHumanTask.getId(), TestUserFactory.getRidleyScott().getId(), past));
//    }
//
//    @Test
//    public void checkSubTaskWithDueDateNow() throws Exception {
//        checkDueDate(new Date());
//    }
//
//    @Test
//    public void checkSubTaskWithDueDateFutur() throws Exception {
//        checkDueDate(new Date(1605277665));
//    }
//
//    @Test
//    public void checkSubTaskWithDueDate2Years() throws Exception {
//        checkDueDate(new Date(System.currentTimeMillis() + TWO_YEARS_MS));
//    }
//
//    public void checkDueDate(final Date date) throws Exception {
//        final SimpleDateFormat dateFormat = new SimpleDateFormat(DUE_DATE_FORMAT);
//        final String dateAsString = dateFormat.format(date);
//
//        this.manualTaskDatastore.add(createManualTaskItem(this.testHumanTask.getId(), TestUserFactory.getRidleyScott().getId(), dateAsString));
//
//        // Search the subtask
//        final Map<String, String> filters = new HashMap<String, String>();
//        filters.put(ManualTaskItem.ATTRIBUTE_PARENT_TASK_ID, String.valueOf(this.testHumanTask.getId()));
//        final HumanTaskItem humanTaskItem = this.manualTaskDatastore.search(0, 1, null, filters, null)
//                .getResults().get(0);
//        assertEquals("Couldnt set due date", dateAsString, humanTaskItem.getDueDate());
//    }
//
//    /**
//     * @param assignedId
//     *            id of the assigned user
//     * @return a ManualTaskItem
//     */
//    private ManualTaskItem createManualTaskItem(final long parentTaskId, final Long assigndId, final String dueDate) {
//        final ManualTaskItem manualTaskItem = new ManualTaskItem();
//        manualTaskItem.setParentTaskId(parentTaskId);
//        manualTaskItem.setAssignedId(assigndId);
//        manualTaskItem.setState(ManualTaskItem.VALUE_STATE_READY);
//        manualTaskItem.setName("manualTask1");
//        manualTaskItem.setDisplayName("Manualtask display name");
//        manualTaskItem.setDueDate(dueDate);
//        manualTaskItem.setDisplayDescription("Manualtask display description");
//        manualTaskItem.setPriority(TaskPriority.NORMAL.toString());
//        return manualTaskItem;
//    }
//
//    private ManualTaskItem createManualTaskItem(final long parentTaskId, final Long assigndId) {
//        final SimpleDateFormat dateFormat = new SimpleDateFormat(DUE_DATE_FORMAT);
//        return createManualTaskItem(parentTaskId, assigndId, dateFormat.format(new Date()));
//    }
//
//}
