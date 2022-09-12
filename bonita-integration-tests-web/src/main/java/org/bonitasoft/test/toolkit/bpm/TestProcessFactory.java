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
package org.bonitasoft.test.toolkit.bpm;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.bonitasoft.engine.bpm.bar.BarResource;
import org.bonitasoft.engine.bpm.bar.BusinessArchiveBuilder;
import org.bonitasoft.engine.bpm.process.InvalidProcessDefinitionException;
import org.bonitasoft.engine.bpm.process.ProcessActivationException;
import org.bonitasoft.engine.bpm.process.ProcessDefinition;
import org.bonitasoft.engine.bpm.process.impl.ProcessDefinitionBuilder;
import org.bonitasoft.engine.expression.Expression;
import org.bonitasoft.engine.expression.ExpressionBuilder;
import org.bonitasoft.engine.expression.ExpressionType;
import org.bonitasoft.engine.expression.InvalidExpressionException;
import org.bonitasoft.test.toolkit.bpm.process.TestProcessConnector;
import org.bonitasoft.test.toolkit.exception.TestToolkitException;
import org.bonitasoft.test.toolkit.organization.TestUser;

/**
 * @author Vincent Elcrin
 */
public class TestProcessFactory {

    public static final String CONTRACT_RULE_EXPLANATION = "explanation";

    public static final String CONTRACT_RULE_EXPRESSION = " 1 == 1 ";

    public static final String CONTRACT_RULE_NAME = "ruleName";

    public static final String CONTRACT_INPUT_DESCRIPTION = "description";

    public static final String CONTRACT_INPUT_TYPE = String.class.getName();

    public static final String CONTRACT_INPUT_NAME = "inputName";

    protected static final String DEFAULT_HUMAN_TASK_PROCESS_NAME = "Default human task process";

    protected static final String PROCESS_WITH_DOCUMENT_ATTACHED = "Process with document attached";

    protected static final String PROCESS_MISCONFIGURED = "Process misconfigured";

    protected static final String PROCESS_CALL_ACTIVTY = "Process call activity";

    private final Map<String, TestProcess> processList;

    private static TestProcessFactory instance;

    /**
     * Default Constructor.
     */
    public TestProcessFactory() {
        processList = new HashMap<String, TestProcess>();
    }

    public static TestProcessFactory getInstance() {
        if (instance == null) {
            instance = new TestProcessFactory();
        }
        return instance;
    }

    private static String getRandomString() {
        return String.valueOf(new Random().nextLong());
    }

    public void clear() throws Exception {
        for (TestProcess testProcess : processList.values()) {
            clear(testProcess);
        }
        processList.clear();
    }

    /**
     * @return the processList
     */
    protected Map<String, TestProcess> getProcessList() {
        return processList;
    }

    // ///////////////////////////////////////////////////////////////////////////////////
    // / Process definitions
    // ///////////////////////////////////////////////////////////////////////////////////

    protected static ProcessDefinitionBuilder getDefaultProcessDefinitionBuilder(final String processName) {
        return getDefaultProcessDefinitionBuilder(processName, "1.0");
    }

    public static ProcessDefinitionBuilder getDefaultProcessDefinitionBuilder(final String processName, final String version) {
        final ProcessDefinitionBuilder processDefinitionBuidler = new ProcessDefinitionBuilder().createNewInstance(processName, version);
        processDefinitionBuidler.addActor("Employees", true)
                .addDescription("This a default process")
                .addStartEvent("Start")
                .addUserTask("Activity 1", "Employees")
                .addEndEvent("Finish");
        return processDefinitionBuidler;
    }

    protected static ProcessDefinitionBuilder getMisconfiguredProcessDefinitionBuilder(final String processName) {
        final ProcessDefinitionBuilder processDefinitionBuidler = new ProcessDefinitionBuilder().createNewInstance(processName, "1.0");
        try {
            processDefinitionBuidler.addActor("Employees", true)
                    .addDescription("This a default process")

                    .addStartEvent("Start")
                    .addUserTask("Activity 1", "Employees")
                    .addData("FailedData", "FailedClassName", new ExpressionBuilder().createInputExpression("input", Boolean.class.getName()))
                    .addEndEvent("Finish");
        } catch (final InvalidExpressionException e) {
            throw new TestToolkitException("Invalid expression definition", e);
        }
        return processDefinitionBuidler;
    }

    protected static BusinessArchiveBuilder getBusinessArchiveWithDocumentBuilder(final String processName) {

        final ProcessDefinitionBuilder processDefinitionBuidler = new ProcessDefinitionBuilder().createNewInstance(processName, "1.0");
        processDefinitionBuidler.addDocumentDefinition("Document 1667").addContentFileName("filename.txt").addFile("attachedfile.txt");
        processDefinitionBuidler.addActor("Employees", true)
                .addStartEvent("Start")
                .addUserTask("Activity 1", "Employees")
                .addEndEvent("Finish");

        try {
            return new BusinessArchiveBuilder().createNewBusinessArchive()
                    .setFormMappings(TestProcess.createDefaultProcessFormMapping(processDefinitionBuidler.getProcess()))
                    .addDocumentResource(new BarResource("attachedfile.txt", "thisisthecontentofthedocumentattached".getBytes()))
                    .setProcessDefinition(processDefinitionBuidler.done());
        } catch (final InvalidProcessDefinitionException e) {
            throw new TestToolkitException("Invalid process definition", e);
        }
    }

    /**
     * @param processCallActivty
     * @return
     */
    private static ProcessDefinitionBuilder getCallActivityProcessDefinitionBuilder(final ProcessDefinition processToStartViaCallActivity) {
        Expression expressionName = null;
        Expression expressionVersion = null;
        try {
            expressionName = new ExpressionBuilder().createNewInstance("process name")
                    .setExpressionType(ExpressionType.TYPE_CONSTANT)
                    .setReturnType(String.class.getName())
                    .setContent(processToStartViaCallActivity.getName()).done();

            expressionVersion = new ExpressionBuilder().createNewInstance("process version")
                    .setExpressionType(ExpressionType.TYPE_CONSTANT)
                    .setReturnType(String.class.getName())
                    .setContent(processToStartViaCallActivity.getVersion()).done();
        } catch (final InvalidExpressionException e) {
            throw new TestToolkitException("Invalid expression definition", e);
        }

        final ProcessDefinitionBuilder processDefinitionBuidler = new ProcessDefinitionBuilder()
                .createNewInstance(PROCESS_CALL_ACTIVTY, "1.0");
        processDefinitionBuidler.addActor("Employees", true)
                .addStartEvent("Start")
                .addCallActivity("Call Activity", expressionName, expressionVersion)
                .addEndEvent("Finish");
        return processDefinitionBuidler;
    }

    // //////////////////////////////////////////////////////////////////////////////////
    // / Factory accessors
    // //////////////////////////////////////////////////////////////////////////////////

    public static TestProcess createRandomResolvedProcess(final TestUser actor) {
        return getRandomHumanTaskProcess().addActor(actor);
    }

    /**
     * Variables :
     * - variable1 : String
     * - variable2 : Long
     * - variable3 : Date
     * @param initiator
     */
    public static TestHumanTask createActivityWithVariables(TestUser initiator) throws InvalidExpressionException {
        final String processName = "processName";
        final ProcessDefinitionBuilder processDefinitionBuidler = new ProcessDefinitionBuilder().createNewInstance(processName+(String.valueOf(UUID.randomUUID().getLeastSignificantBits()).substring(0,5)), "1.0");
        processDefinitionBuidler.addActor("Employees", true)
                .addDescription("This a default process")
                .addStartEvent("Start")
                .addUserTask("Activity 1", "Employees")

                .addData("variable1", String.class.getName(), new ExpressionBuilder().createConstantStringExpression("defaultValue"))
                .addData("variable2", Long.class.getName(), new ExpressionBuilder().createConstantLongExpression(1))
                .addData("variable3", Date.class.getName(), new ExpressionBuilder().createConstantDateExpression("428558400000"))

                .addEndEvent("Finish");
        final TestProcess testProcess = new TestProcess(processDefinitionBuidler);
        getInstance().getProcessList().put(processName, testProcess);
        return testProcess.addActor(initiator).setEnable(true).startCase().getNextHumanTask().assignTo(initiator);
    }

    /**
     * This process contains only a human task
     *
     * @return
     */
    public static TestProcess getDefaultHumanTaskProcess() {
        return getHumanTaskProcess(DEFAULT_HUMAN_TASK_PROCESS_NAME);
    }

    public static TestProcess createProcessWithConnector(final TestProcessConnector testConnector) {
        final String aProcessWithConnector = "aProcessWithConnector";
        final ProcessDefinitionBuilder processBuilder = getDefaultProcessDefinitionBuilder(aProcessWithConnector);
        processBuilder.addConnector(testConnector.getName(), testConnector.getId(), testConnector.getVersion(), testConnector.getConnectorEvent());

        try {
            final InputStream stream = TestProcessFactory.class.getResourceAsStream(testConnector.getResourceFilePath());
            final BarResource connectorResource = new BarResource(testConnector.getResourceFileName(), IOUtils.toByteArray(stream));
            stream.close();
            final BusinessArchiveBuilder barBuilder =
                    new BusinessArchiveBuilder().createNewBusinessArchive().setProcessDefinition(processBuilder.done());
            barBuilder.addConnectorImplementation(connectorResource);
            final TestProcess testProcess = new TestProcess(barBuilder);
            getInstance().getProcessList().put(aProcessWithConnector, testProcess);
            return testProcess;
        } catch (final Exception e) {
            throw new TestToolkitException("Unable to create a process with connector", e);
        }
    }

    public static List<TestProcess> getHumanTaskProcesses(final int count) {
        final List<TestProcess> results = new ArrayList<TestProcess>(count);
        for (int i = 0; i < count; i++) {
            results.add(getHumanTaskProcess(getRandomString()));
        }
        return results;
    }

    public static TestProcess getProcessWithDocumentAttached() {
        if (getInstance().getProcessList().get(PROCESS_WITH_DOCUMENT_ATTACHED) == null) {
            final TestProcess testProcess = new TestProcess(getBusinessArchiveWithDocumentBuilder(PROCESS_WITH_DOCUMENT_ATTACHED));
            getInstance().getProcessList().put(PROCESS_WITH_DOCUMENT_ATTACHED, testProcess);
        }

        return getInstance().getProcessList().get(PROCESS_WITH_DOCUMENT_ATTACHED);
    }

    public static TestProcess createProcessWith3Actors() {
        final String processWith3Actors = "processWith3Actors";
        final ProcessDefinitionBuilder builder = getDefaultProcessDefinitionBuilder(processWith3Actors);
        builder.addActor("actor2").addDescription("description actor2").addActor("actor3").addDescription("description actor3");
        final TestProcess testProcess = new TestProcess(builder);
        getInstance().getProcessList().put(processWith3Actors, testProcess);
        return testProcess;
    }

    public static TestProcess createProcessWithVariables(final String processName, final ProcessVariable... variables) {
        final ProcessDefinitionBuilder builder = getDefaultProcessDefinitionBuilder(processName);
        for (final ProcessVariable variable : variables) {
            builder.addData(variable.getName(), variable.getClassName(), variable.getDefaultValue());
        }
        final TestProcess testProcess = new TestProcess(builder);
        getInstance().getProcessList().put(processName, testProcess);
        return testProcess;
    }

    public static TestProcess getRandomHumanTaskProcess() {
        return getHumanTaskProcess(getRandomString());
    }

    /**
     * This process contains only a human task
     *
     * @return
     */
    public static TestProcess getHumanTaskProcess(final String processName, final String version) {
        if (getInstance().getProcessList().get(processName) == null) {
            final TestProcess testProcess = new TestProcess(getDefaultProcessDefinitionBuilder(processName, version));
            getInstance().getProcessList().put(processName, testProcess);
        }

        return getInstance().getProcessList().get(processName);
    }

    /**
     * This process contains only a human task
     *
     * @return
     */
    public static TestProcess getHumanTaskProcess(final String processName) {
        if (getInstance().getProcessList().get(processName) == null) {
            final TestProcess testProcess = new TestProcess(getDefaultProcessDefinitionBuilder(processName));
            getInstance().getProcessList().put(processName, testProcess);
        }

        return getInstance().getProcessList().get(processName);
    }

    /**
     * This process contains only a human task
     *
     * @return
     */
    public static TestProcess getMisconfiguredProcess() {
        if (getInstance().getProcessList().get(PROCESS_MISCONFIGURED) == null) {
            final TestProcess testProcess = new TestProcess(getMisconfiguredProcessDefinitionBuilder(PROCESS_MISCONFIGURED));
            getInstance().getProcessList().put(PROCESS_MISCONFIGURED, testProcess);
        }

        return getInstance().getProcessList().get(PROCESS_MISCONFIGURED);
    }

    /**
     * This process contains a call activity
     *
     * @return
     */
    public static TestProcess getCallActivityProcess(final ProcessDefinition processToStartViaCallActivity) {
        if (getInstance().getProcessList().get(PROCESS_CALL_ACTIVTY) == null) {
            final TestProcess testProcess = new TestProcess(getCallActivityProcessDefinitionBuilder(processToStartViaCallActivity));
            getInstance().getProcessList().put(PROCESS_CALL_ACTIVTY, testProcess);
        }
        return getInstance().getProcessList().get(PROCESS_CALL_ACTIVTY);
    }

    public void check() {
        if (!getProcessList().isEmpty()) {
            throw new RuntimeException(this.getClass().getName() + " cannot be reset because the list is not empty: " + getProcessList());
        }
    }

    private void clear(TestProcess testProcess) throws Exception {
        testProcess.deleteCases();
        try {
            testProcess.disable();
        } catch (TestToolkitException e) {
            if (!(e.getCause() instanceof ProcessActivationException)) {
                throw e;
            }
            //ignore as the process can be disabled
        }
        testProcess.delete();
    }

    public void delete(TestProcess testProcess) throws Exception {
        clear(testProcess);
        getProcessList().remove(testProcess.getProcessDefinition().getName());
    }

    public void remove(TestProcess testProcess) {
        getProcessList().remove(testProcess.getProcessDefinition().getName());
    }

    public void add(TestProcess testProcess) {
        getProcessList().put(testProcess.getProcessDefinition().getName(), testProcess);
    }
}
