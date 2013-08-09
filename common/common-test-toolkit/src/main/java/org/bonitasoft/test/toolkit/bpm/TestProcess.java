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
package org.bonitasoft.test.toolkit.bpm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.actor.ActorCriterion;
import org.bonitasoft.engine.bpm.actor.ActorInstance;
import org.bonitasoft.engine.bpm.bar.BusinessArchiveBuilder;
import org.bonitasoft.engine.bpm.category.Category;
import org.bonitasoft.engine.bpm.category.CategoryCriterion;
import org.bonitasoft.engine.bpm.process.InvalidProcessDefinitionException;
import org.bonitasoft.engine.bpm.process.ProcessDefinition;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfo;
import org.bonitasoft.engine.bpm.process.ProcessInstance;
import org.bonitasoft.engine.bpm.process.ProcessInstanceSearchDescriptor;
import org.bonitasoft.engine.bpm.process.impl.ProcessDefinitionBuilder;
import org.bonitasoft.engine.bpm.supervisor.ProcessSupervisor;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.SearchException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.InvalidSessionException;
import org.bonitasoft.test.toolkit.exception.TestToolkitException;
import org.bonitasoft.test.toolkit.organization.TestGroup;
import org.bonitasoft.test.toolkit.organization.TestRole;
import org.bonitasoft.test.toolkit.organization.TestToolkitCtx;
import org.bonitasoft.test.toolkit.organization.TestUser;

/**
 * @author Vincent Elcrin
 * 
 */
public class TestProcess {

    private final ProcessDefinition processDefinition;

    private boolean enabled = false;

    public static final String COMMAND_ADD_USER_TO_SUPERVISOR = "createSupervisor";

    public static final String KEY_PROCESS_DEFINITION_ID = "processDefinitionId";

    public static final String KEY_MEMBER_TYPE = "memberType";

    public static final String KEY_USER_ID = "userId";

    public static final String KEY_GROUP_ID = "groupId";

    public static final String KEY_ROLE_ID = "roleId";

    private ProcessSupervisor processSupervisor;

    private final List<ActorInstance> actors = new ArrayList<ActorInstance>();

    /**
     * Default Constructor.
     * 
     * @throws Exception
     */
    public TestProcess(final APISession apiSession, final ProcessDefinitionBuilder processDefinitionBuilder) {
        this.processDefinition = createProcessDefinition(apiSession, processDefinitionBuilder);
    }

    public TestProcess(final ProcessDefinitionBuilder processDefinitionBuilder) {
        this(getSession(), processDefinitionBuilder);
    }

    public TestProcess(final APISession apiSession, final BusinessArchiveBuilder businessArchiveBuilder) {
        this.processDefinition = deployProcessDefinition(apiSession, businessArchiveBuilder);
    }

    public TestProcess(final BusinessArchiveBuilder businessArchiveBuilder) {
        this(getSession(), businessArchiveBuilder);
    }

    /**
     * @return
     */
    private static APISession getSession() {
        return TestToolkitCtx.getInstance().getInitiator().getSession();
    }

    /**
     * Create an archive and deploy process
     * 
     * @param apiSession
     * @param processDefinitionBuilder
     * @return
     */
    private ProcessDefinition createProcessDefinition(final APISession apiSession, final ProcessDefinitionBuilder processDefinitionBuilder) {
        try {
            return deployProcessDefinition(apiSession, new BusinessArchiveBuilder().createNewBusinessArchive()
                    .setProcessDefinition(processDefinitionBuilder.done()));
        } catch (final InvalidProcessDefinitionException e) {
            throw new TestToolkitException("Invalid process definition", e);
        }
    }

    /**
     * Deploy process from the archive
     * 
     * @param apiSession
     * @param businessArchiveBuilder
     * @return
     */
    private ProcessDefinition deployProcessDefinition(final APISession apiSession, final BusinessArchiveBuilder businessArchiveBuilder) {
        try {
            return getProcessAPI(apiSession).deploy(businessArchiveBuilder.done());
        } catch (final Exception e) {
            throw new TestToolkitException("Can't deploy business archive.", e);
        }
    }

    protected static ProcessAPI getProcessAPI(final APISession apiSession) {
        ProcessAPI processAPI = null;
        try {
            processAPI = TenantAPIAccessor.getProcessAPI(apiSession);
        } catch (final InvalidSessionException e) {
            throw new TestToolkitException("Can't get process API. Invalid session", e);
        } catch (final BonitaHomeNotSetException e) {
            throw new TestToolkitException("Can't get process API. Bonita home not set", e);
        } catch (final ServerAPIException e) {
            throw new TestToolkitException("Can't get process API. Server API exception", e);
        } catch (final UnknownAPITypeException e) {
            throw new TestToolkitException("Can't get process API. Unknown API type", e);
        }
        return processAPI;
    }

    public ProcessDefinition getProcessDefinition() {
        return this.processDefinition;
    }

    public long getId() {
        return this.processDefinition.getId();
    }
    
    public ProcessDeploymentInfo getProcessDeploymentInfo() {
        try {
            return getProcessAPI(getSession()).getProcessDeploymentInfo(getId());
        } catch (ProcessDefinitionNotFoundException e) {
            throw new TestToolkitException("Unable to get process deployement info", e);
        }
    }

    public TestProcess enable() {
        return setEnable(getSession(), true);
    }

    public TestProcess disable() {
        return setEnable(getSession(), false);
    }

    /**
     * Set process enablement
     * 
     * @param apiSession
     * @param enabled
     * @return
     */
    private TestProcess setEnable(final APISession apiSession, final boolean enabled) {
        if (enabled && !this.enabled) {
            enableProcess(apiSession);
        } else if (!enabled && this.enabled) {
            disableProcess(apiSession);
        }
        this.enabled = enabled;
        return this;
    }
    
    private void enableProcess(APISession apiSession) {
        try {
            getProcessAPI(apiSession).enableProcess(this.processDefinition.getId());
        } catch (Exception e) {
            throw new TestToolkitException("Can't enable process <" + this.processDefinition.getId() + ">", e);
        }
    }
    
    private void disableProcess(APISession apiSession) {
        try {
            getProcessAPI(apiSession).disableProcess(this.processDefinition.getId());
        } catch (Exception e) {
            throw new TestToolkitException("Can't disable process <" + this.processDefinition.getId() + ">", e);
        }
    }

    /**
     * Add actors to enable process
     * 
     * TODO: Need to evolve to choose on which Actors category the actor will be added
     * 
     * @param apiSession
     * @param actor
     * @return
     * @throws Exception
     */
    private TestProcess addActor(final APISession apiSession, final TestUser actor) {
        final ProcessAPI processAPI = getProcessAPI(apiSession);
        ActorInstance processActor = null;
        try {
            processActor = processAPI.getActors(this.processDefinition.getId(), 0, Integer.MAX_VALUE, ActorCriterion.NAME_ASC).get(this.actors.size());
            processAPI.addUserToActor(processActor.getId(), actor.getUser().getId());
            this.actors.add(processActor);
        } catch (final Exception e) {
            throw new TestToolkitException("Can't get actors for <" + this.processDefinition.getId() + ">.", e);
        }

        return this;
    }

    public TestProcess addActor(final TestGroup actor) {
        return addActor(TestToolkitCtx.getInstance().getInitiator().getSession(), actor);
    }

    private TestProcess addActor(final APISession apiSession, final TestGroup actor) {
        try {
            final ProcessAPI processAPI = getProcessAPI(apiSession);
            final ActorInstance processActor = processAPI.getActors(this.processDefinition.getId(), 0, Integer.MAX_VALUE, ActorCriterion.NAME_ASC).get(
                    this.actors.size());
            processAPI.addGroupToActor(processActor.getId(), actor.getId());
            this.actors.add(processActor);
        } catch (final IndexOutOfBoundsException e) {
            final String message = "can't add actor to process " + this.processDefinition.getId()
                    + " process definition has only " + this.actors.size() + " actors";
            throw new TestToolkitException(message, e);
        } catch (final Exception e) {
            throw new TestToolkitException("can't add actor to process " + this.processDefinition.getId(), e);
        }

        return this;
    }

    public TestProcess addActor(final TestRole actor) {
        return addActor(TestToolkitCtx.getInstance().getInitiator().getSession(), actor);
    }

    private TestProcess addActor(final APISession apiSession, final TestRole actor) {
        try {
            final ProcessAPI processAPI = getProcessAPI(apiSession);
            final ActorInstance processActor = processAPI.getActors(this.processDefinition.getId(), 0, Integer.MAX_VALUE, ActorCriterion.NAME_ASC).get(
                    this.actors.size());
            processAPI.addRoleToActor(processActor.getId(), actor.getId());
            this.actors.add(processActor);
        } catch (final IndexOutOfBoundsException e) {
            final String message = "can't add actor to process " + this.processDefinition.getId()
                    + " process definition has only " + this.actors.size() + " actors";
            throw new TestToolkitException(message, e);
        } catch (final Exception e) {
            throw new TestToolkitException("can't add actor to process " + this.processDefinition.getId(), e);
        }

        return this;
    }

    public TestProcess addActor(final TestUser initiator, final TestUser actor) {
        addActor(initiator.getSession(), actor);
        return this;
    }

    public TestProcess addActor(final TestUser actor) {
        return addActor(TestToolkitCtx.getInstance().getInitiator(), actor);
    }

    private TestCase startCase(final APISession apiSession) {
        setEnable(apiSession, true);
        TestCase testCase = new TestCase(createProcesInstance(apiSession));
        testCase.waitProcessState(apiSession, TestCase.READY_STATE);
        return testCase;
    }

    protected ProcessInstance createProcesInstance(final APISession apiSession) {
        try {
            return getProcessAPI(apiSession).startProcess(apiSession.getUserId(), processDefinition.getId());
        } catch (final Exception e) {
            throw new TestToolkitException("Can't start process <" + processDefinition.getId() + ">", e);
        }
    }

    public TestCase startCase(final TestUser initiator) {
        return startCase(initiator.getSession());
    }

    public TestCase startCase() {
        return startCase(TestToolkitCtx.getInstance().getInitiator());
    }

    /**
     * Start several cases and return them as a list
     * 
     * @param number
     * @return
     */
    public List<TestCase> startCases(final int number) {
        final List<TestCase> result = new ArrayList<TestCase>();
        for (int i = 0; i < number; i++) {
            result.add(startCase());
        }
        return result;
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // / SUPERVISOR
    // /////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Add a user as process supervisor
     * 
     * @param apiSession
     * @param user
     * @return
     */
    private TestProcess addSupervisor(final APISession apiSession, final TestUser user) {
        try {
            this.processSupervisor = getProcessAPI(apiSession).createProcessSupervisorForUser(this.processDefinition.getId(), user.getId());
        } catch (final Exception e) {
            throw new TestToolkitException("Unable to add supervisor", e);
        }
        return this;
    }

    public TestProcess addSupervisor(final TestUser initiator, final TestUser user) {
        return addSupervisor(initiator.getSession(), user);
    }

    public TestProcess addSupervisor(final TestUser user) {
        return addSupervisor(TestToolkitCtx.getInstance().getInitiator(), user);
    }

    /**
     * Add a role as process supervisor
     * 
     * @param apiSession
     * @param user
     * @return
     */
    private TestProcess addSupervisor(final APISession apiSession, final TestRole role) {
        try {
            getProcessAPI(apiSession).createProcessSupervisorForRole(this.processDefinition.getId(), role.getId());
        } catch (final Exception e) {
            throw new TestToolkitException("Unable to add supervisor", e);
        }
        return this;
    }

    public TestProcess addSupervisor(final TestUser initiator, final TestRole role) {
        return addSupervisor(initiator.getSession(), role);
    }

    public TestProcess addSupervisor(final TestRole role) {
        return addSupervisor(TestToolkitCtx.getInstance().getInitiator(), role);
    }

    /**
     * Add a group as process supervisor
     * 
     * @param apiSession
     * @param user
     * @return
     */
    private TestProcess addSupervisor(final APISession apiSession, final TestGroup group) {
        try {
            getProcessAPI(apiSession).createProcessSupervisorForGroup(this.processDefinition.getId(), group.getId());
        } catch (final Exception e) {
            throw new TestToolkitException("Unable to add supervisor", e);
        }
        return this;
    }

    public TestProcess addSupervisor(final TestUser initiator, final TestGroup group) {
        return addSupervisor(initiator.getSession(), group);
    }

    public TestProcess addSupervisor(final TestGroup group) {
        return addSupervisor(TestToolkitCtx.getInstance().getInitiator(), group);
    }

    /**
     * Add a memebership as process supervisor
     * 
     * @param apiSession
     * @param user
     * @return
     */
    private TestProcess addSupervisor(final APISession apiSession, final TestGroup group, final TestRole role) {
        try {
            getProcessAPI(apiSession).createProcessSupervisorForMembership(this.processDefinition.getId(), group.getId(), role.getId());
        } catch (final Exception e) {
            throw new TestToolkitException("Unable to add supervisor", e);
        }
        return this;
    }

    public TestProcess addSupervisor(final TestUser initiator, final TestGroup group, final TestRole role) {
        return addSupervisor(initiator.getSession(), group, role);
    }

    public TestProcess addSupervisor(final TestGroup group, final TestRole role) {
        return addSupervisor(TestToolkitCtx.getInstance().getInitiator(), group, role);
    }

    public TestProcess addCategory(final TestCategory category) {
        return addCategory(category.getId());
    }

    public TestProcess addCategory(final long categoryId) {
        try {
            TenantAPIAccessor.getProcessAPI(getSession()).addCategoriesToProcess(getId(), Arrays.asList(categoryId));
            return this;
        } catch (final Exception e) {
            throw new TestToolkitException("Can't add this process to this category. " + e.getMessage(), e);
        }

    }

    public List<TestCategory> getCategories() {

        try {
            final List<Category> categories = TenantAPIAccessor.getProcessAPI(getSession())
                    .getCategoriesOfProcessDefinition(getId(), 0, 100, CategoryCriterion.NAME_ASC);
            final List<TestCategory> results = new ArrayList<TestCategory>(categories.size());

            for (final Category category : categories) {
                results.add(new TestCategory(category));
            }

            return results;
        } catch (final Exception e) {
            throw new TestToolkitException("Can't get categories", e);
        }

    }

    public ProcessSupervisor getProcessSupervisor() {
        return this.processSupervisor;
    }

    public List<ActorInstance> getActors() {
        return this.actors;
    }

    public List<TestCase> listOpenCases() throws SearchException  {
        List<ProcessInstance> processInstances = searchOpenedProcessInstances();
        return convertToCasesList(processInstances);
    }

    private List<TestCase> convertToCasesList(List<ProcessInstance> processInstances) {
        List<TestCase> cases = new ArrayList<TestCase>();
        for (ProcessInstance instance : processInstances) {
            cases.add(new TestCase(instance));
        }
        return cases;
    }

    private List<ProcessInstance> searchOpenedProcessInstances() throws SearchException {
        final SearchOptionsBuilder builder = new SearchOptionsBuilder(0, 100);
        builder.filter(ProcessInstanceSearchDescriptor.PROCESS_DEFINITION_ID, getProcessDefinition().getId());
        return getProcessAPI(getSession()).searchOpenProcessInstances(builder.done()).getResult();
    }
}
