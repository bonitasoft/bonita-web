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
package org.bonitasoft.test.toolkit.organization;

import java.util.List;

import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.api.LoginAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceCriterion;
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance;
import org.bonitasoft.engine.exception.AlreadyExistsException;
import org.bonitasoft.engine.exception.BonitaHomeNotSetException;
import org.bonitasoft.engine.exception.CreationException;
import org.bonitasoft.engine.exception.NotFoundException;
import org.bonitasoft.engine.exception.ServerAPIException;
import org.bonitasoft.engine.exception.UnknownAPITypeException;
import org.bonitasoft.engine.identity.ContactDataCreator;
import org.bonitasoft.engine.identity.User;
import org.bonitasoft.engine.identity.UserCreator;
import org.bonitasoft.engine.platform.LogoutException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.InvalidSessionException;
import org.bonitasoft.engine.session.SessionNotFoundException;
import org.bonitasoft.test.toolkit.TestToolkitUtils;
import org.bonitasoft.test.toolkit.bpm.TestActor;
import org.bonitasoft.test.toolkit.exception.TestToolkitException;

/**
 * @author Vincent Elcrin
 * 
 */
public class TestUser implements TestActor {

    private final String userName;

    private final String password;

    private User user;

    private APISession apiSession;

    private boolean loggedIn = false;

    protected TestUser(final String userName, final String password) {
        this.userName = userName;
        this.password = password;
    }

    public TestUser(final APISession apiSession, final String userName, final String password) {
        this.user = createUser(apiSession, userName, password);
        this.userName = userName;
        this.password = password;
        /*
        System.err.println("\n\n");
        System.err.println("Building user: " + user.getUserName());
        Thread.dumpStack();
        System.err.println("\n\n");
        */
    }

    // //////////////////////////////////////////////////////////////////////////////////
    // / Constructors using builders
    // //////////////////////////////////////////////////////////////////////////////////

    public TestUser(final APISession apiSession, UserCreator userCreator, final ContactDataCreator personalInfoCreator,
            final ContactDataCreator professionalInfoCreator) {
        this.user = createUser(apiSession, userCreator, personalInfoCreator, professionalInfoCreator);
        this.userName = (String) userCreator.getFields().get(UserCreator.UserField.NAME);
        this.password = (String) userCreator.getFields().get(UserCreator.UserField.PASSWORD);
        /*
        System.err.println("\n\n");
        System.err.println("Building user: " + user.getUserName());
        Thread.dumpStack();
        System.err.println("\n\n");
        */
    }

    public TestUser(final APISession apiSession, final UserCreator userBuilder) {
        this(apiSession, userBuilder, new ContactDataCreator(), new ContactDataCreator());
    }

    // //////////////////////////////////////////////////////////////////////////////////
    // / Logging
    // //////////////////////////////////////////////////////////////////////////////////

    private APISession logIn(final String userName, final String password) {
        // need to create the platform if we want to log onto it
        TestToolkitCtx.getInstance().getPlatform();

        LoginAPI loginAPI;
        APISession apiSession = null;
        try {
            loginAPI = TenantAPIAccessor.getLoginAPI();
            apiSession = loginAPI.login(userName, password);
        } catch (final Exception e) {
            throw new TestToolkitException("Can't log user <" + userName + "> in", e);
        }

        this.loggedIn = true;
        return apiSession;
    }

    private void logOut(final APISession apiSession) {
        LoginAPI loginAPI;
        try {
            loginAPI = TenantAPIAccessor.getLoginAPI();
            loginAPI.logout(apiSession);
        } catch (final BonitaHomeNotSetException e) {
            throw new TestToolkitException("Can't get api to log out. Bonita home not set", e);
        } catch (final ServerAPIException e) {
            throw new TestToolkitException("Can't get api to log out. Server api exception", e);
        } catch (final UnknownAPITypeException e) {
            throw new TestToolkitException("Can't get api to log out. Unkwown api type", e);
        } catch (final LogoutException e) {
            throw new TestToolkitException("Can't get log out user <" + apiSession.getUserName() + ">", e);
        } catch (SessionNotFoundException e) {
            throw new TestToolkitException("Can't find the session to log out", e);
        }
        this.loggedIn = false;
    }

    public APISession logIn() {
        this.apiSession = logIn(this.userName, this.password);
        return this.apiSession;
    }

    public void logOut() {
        if (this.apiSession != null) {
            logOut(this.apiSession);
        }
    }

    /**
     * @return the loggedIn
     */
    public boolean isLoggedIn() {
        return this.loggedIn;
    }

    public APISession getSession() {
        if (!this.loggedIn) {
            logIn();
        }
        return this.apiSession;
    }

    /**
     * Get identity API session
     * 
     * @param apiSession
     * @return
     */
    protected static IdentityAPI getIdentityAPI(final APISession apiSession) {
        IdentityAPI identityAPI = null;
        try {
            identityAPI = TenantAPIAccessor.getIdentityAPI(apiSession);
        } catch (final InvalidSessionException e) {
            throw new TestToolkitException("Can't get identy api. Invalid session", e);
        } catch (final BonitaHomeNotSetException e) {
            throw new TestToolkitException("Can't get identy api. Bonita home not set", e);
        } catch (final ServerAPIException e) {
            throw new TestToolkitException("Can't get identy api. Server api exception", e);
        } catch (final UnknownAPITypeException e) {
            throw new TestToolkitException("Can't get identy api. Unknown api type", e);
        }
        return identityAPI;
    }

    // //////////////////////////////////////////////////////////////////////////////////
    // / Users creation
    // //////////////////////////////////////////////////////////////////////////////////

    /**
     * Create user with only username & password
     * 
     * @param apiSession
     * @param userName
     * @param password
     * @return
     */
    private User createUser(final APISession apiSession, final String userName, final String password) {
        final IdentityAPI identityAPI = getIdentityAPI(apiSession);
        User newUser = null;
        try {
            newUser = identityAPI.createUser(userName, password);
        } catch (final AlreadyExistsException e) {
            try {
                newUser = identityAPI.getUserByUserName(userName);
            } catch (final Exception e1) {
                throw new TestToolkitException("Can't get user <" + userName + ">", e);
            }
        } catch (final Exception e) {
            throw new TestToolkitException("Can't create user <" + userName + ">", e);
        }
        return newUser;
    }

    /**
     * Create user name with user engine's builder
     * 
     * @param apiSession
     * @param userBuilder
     * @return
     */
    private User createUser(final APISession apiSession, final UserCreator creator, final ContactDataCreator personalInfoCreator,
            final ContactDataCreator professionalInfoBuilder) {
        final IdentityAPI identityAPI = getIdentityAPI(apiSession);
        User newUser = null;
        try {
            try {
                creator.setPersonalContactData(personalInfoCreator);
                creator.setPersonalContactData(professionalInfoBuilder);
                newUser = identityAPI.createUser(creator);
            } catch (final AlreadyExistsException e) {
                try {
                    String userName = (String) creator.getFields().get(UserCreator.UserField.NAME);
                    newUser = identityAPI.getUserByUserName(userName);
                } catch (final NotFoundException getEx) {
                    throw new TestToolkitException("User <" + userName + "> not found", e);
                }
            } catch (final CreationException e) {
                throw new TestToolkitException("Can't create user", e);
            }
        } catch (final InvalidSessionException e) {
            throw new TestToolkitException("Can't get identy api to create user. Invalid session", e);
        }

        return newUser;
    }

    /**
     * Create user
     * 
     * @param userBuilder
     * @return
     */
    public TestUser createUser(final UserCreator userBuilder) {
        return new TestUser(getSession(), userBuilder);
    }

    public TestUser createUser(final String userName, final String password) {
        return new TestUser(getSession(), userName, password);
    }

    // //////////////////////////////////////////////////////////////////////////////////
    // / Deletion
    // //////////////////////////////////////////////////////////////////////////////////

    public void delete(final TestUser testUser) {
        final IdentityAPI identityAPI = getIdentityAPI(getSession());
        try {
            identityAPI.deleteUser(testUser.getUser().getId());
        } catch (final Exception e) {
            throw new TestToolkitException("Can't delete user", e);
        }
    }

    public void destroy() {
        final IdentityAPI identityAPI = getIdentityAPI(TestToolkitCtx.getInstance().getAdminUser().logIn());
        try {
            identityAPI.deleteUser(this.user.getId());
            TestToolkitCtx.getInstance().getAdminUser().logOut();
        } catch (final Exception e) {
            throw new TestToolkitException("Can't destroy user", e);
        }
    }

    // //////////////////////////////////////////////////////////////////////////////////
    // / Getter
    // //////////////////////////////////////////////////////////////////////////////////

    /**
     * @return the user
     */
    public User getUser() {
        return this.user;
    }

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.test.bpm.TestActor#getId()
     */
    public long getId() {
        return this.user.getId();
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    /**
     * Get task assigned to the user
     * 
     * @param pageIndex
     * @param numberPerPage
     * @param criterion
     * @return
     */
    public List<HumanTaskInstance> getAssignedTasks(int pageIndex, int numberPerPage,
            ActivityInstanceCriterion criterion) {
        return TestToolkitUtils.getInstance().getAssignedHumanTaskInstances(getSession(), pageIndex, numberPerPage, criterion);
    }

    public List<HumanTaskInstance> getAssignedTasks() {
        return getAssignedTasks(0, 100, ActivityInstanceCriterion.DEFAULT);
    }

    protected APISession getApiSession() {
        return apiSession;
    }

    protected void setApiSession(APISession apiSession) {
        this.apiSession = apiSession;
    }

    protected void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}
