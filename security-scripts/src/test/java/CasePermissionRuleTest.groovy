/*
 * Copyright (C) 2014 BonitaSoft S.A.
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


import org.assertj.core.api.Assertions
import org.bonitasoft.engine.api.APIAccessor
import org.bonitasoft.engine.api.IdentityAPI
import org.bonitasoft.engine.api.Logger
import org.bonitasoft.engine.api.ProcessAPI
import org.bonitasoft.engine.api.permission.APICallContext
import org.bonitasoft.engine.bpm.process.ArchivedProcessInstance
import org.bonitasoft.engine.bpm.process.ArchivedProcessInstanceNotFoundException
import org.bonitasoft.engine.bpm.process.ProcessInstance
import org.bonitasoft.engine.identity.User
import org.bonitasoft.engine.search.SearchOptions
import org.bonitasoft.engine.search.impl.SearchResultImpl
import org.bonitasoft.engine.session.APISession
import org.json.JSONObject
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

import static org.assertj.core.api.Assertions.assertThat
import static org.mockito.Matchers.any
import static org.mockito.Matchers.eq
import static org.mockito.Mockito.*

@RunWith(MockitoJUnitRunner.class)
public class CasePermissionRuleTest {

    @Mock
    def APISession apiSession
    @Mock
    def APICallContext apiCallContext
    @Mock
    def APIAccessor apiAccessor
    @Mock
    def Logger logger
    def CasePermissionRule rule = new CasePermissionRule()
    @Mock
    def ProcessAPI processAPI
    @Mock
    def IdentityAPI identityAPI
    @Mock
    def User user
    def long currentUserId = 16l

    @Before
    public void before() {

        doReturn(processAPI).when(apiAccessor).getProcessAPI()
        doReturn(identityAPI).when(apiAccessor).getIdentityAPI()
        doReturn(user).when(identityAPI).getUser(currentUserId)
        doReturn(currentUserId).when(apiSession).getUserId()
    }

    @Test
    public void should_check_verify_filters_on_GET_with_different_user_involved() {
        //given
        havingFilters([user_id: "15"])
        //when
        def isAuthorized = rule.isAllowed(apiSession, apiCallContext, apiAccessor, logger)
        //then
        Assertions.assertThat(isAuthorized).isFalse();
    }

    def havingFilters(Map filters) {
        doReturn(true).when(apiCallContext).isGET()
        doReturn(filters).when(apiCallContext).getFilters()
    }

    @Test
    public void should_check_verify_filters_on_GET_with_different_user_started() {
        //given
        havingFilters([started_by: "15"])
        //when
        def isAuthorized = rule.isAllowed(apiSession, apiCallContext, apiAccessor, logger)
        //then
        Assertions.assertThat(isAuthorized).isFalse();
    }

    @Test
    public void should_check_verify_filters_on_GET_with_same_user_involved() {
        //given
        havingFilters([user_id: "16"])
        //when
        def isAuthorized = rule.isAllowed(apiSession, apiCallContext, apiAccessor, logger)
        //then
        Assertions.assertThat(isAuthorized).isTrue();
    }

    @Test
    public void should_check_verify_filters_on_GET_with_same_user_started() {
        //given
        havingFilters([started_by: "16"])
        //when
        def isAuthorized = rule.isAllowed(apiSession, apiCallContext, apiAccessor, logger)
        //then
        Assertions.assertThat(isAuthorized).isTrue();
    }

    @Test
    public void should_check_verify_filters_on_GET_nofilter_on_user() {
        //given
        havingFilters([plop: "16"])
        //when
        def isAuthorized = rule.isAllowed(apiSession, apiCallContext, apiAccessor, logger)
        //then
        Assertions.assertThat(isAuthorized).isFalse();
    }

    @Test
    public void should_check_verify_resourceId_isInvolved_on_GET() {
        //given
        havingResourceId(true)
        //when
        def isAuthorized = rule.isAllowed(apiSession, apiCallContext, apiAccessor, logger)
        //then
        Assertions.assertThat(isAuthorized).isTrue();
    }

    @Test
    public void should_check_verify_resourceId_not_isInvolved_on_GET() {
        //given
        havingResourceId(false)

        def processInstance = mock(ProcessInstance.class)
        doReturn(1024l).when(processInstance).getProcessDefinitionId()
        doReturn(processInstance).when(processAPI).getProcessInstance(45l)
        doReturn(false).when(processAPI).isUserProcessSupervisor(1024l, currentUserId)
        //when
        def isAuthorized = rule.isAllowed(apiSession, apiCallContext, apiAccessor, logger)
        //then
        Assertions.assertThat(isAuthorized).isFalse();
    }


    @Test
    public void should_check_verify_resourceId_not_isInvolved_on_GET_but_supervisor() {
        //given
        havingResourceId(false)

        def processInstance = mock(ProcessInstance.class)
        doReturn(1024l).when(processInstance).getProcessDefinitionId()
        doReturn(processInstance).when(processAPI).getProcessInstance(45l)
        doReturn(true).when(processAPI).isUserProcessSupervisor(1024l, currentUserId)
        //when
        def isAuthorized = rule.isAllowed(apiSession, apiCallContext, apiAccessor, logger)
        //then
        Assertions.assertThat(isAuthorized).isTrue();
    }

    @Test
    public void should_check_verify_resourceId_archived_isInvolved_on_GET() {
        //given
        havingArchivedResourceId()
        def instance = mock(ArchivedProcessInstance.class)
        doReturn(currentUserId).when(instance).getStartedBy()
        doReturn(instance).when(processAPI).getArchivedProcessInstance(45l);
        doReturn(new SearchResultImpl(1,[])).when(processAPI).searchArchivedProcessInstancesInvolvingUser(eq(currentUserId), any(SearchOptions.class))
        //when
        def isAuthorized = rule.isAllowed(apiSession, apiCallContext, apiAccessor, logger)
        //then
        Assertions.assertThat(isAuthorized).isTrue();
    }

    @Test
    public void should_check_verify_resourceId_archived_not_isInvolved_on_GET() {
        //given
        havingArchivedResourceId()
        doThrow(new ArchivedProcessInstanceNotFoundException(new Exception())).when(processAPI).getArchivedProcessInstance(45l);
        //when
        def isAuthorized = rule.isAllowed(apiSession, apiCallContext, apiAccessor, logger)
        //then
        Assertions.assertThat(isAuthorized).isTrue();
    }

    def havingResourceId(boolean isInvolvedIn) {
        doReturn(currentUserId).when(apiSession).getUserId()
        doReturn(true).when(apiCallContext).isGET()
        doReturn("case").when(apiCallContext).getResourceName()
        doReturn("45").when(apiCallContext).getResourceId()
        doReturn(isInvolvedIn).when(processAPI).isInvolvedInProcessInstance(currentUserId, 45l);
    }

    def havingArchivedResourceId() {
        doReturn(currentUserId).when(apiSession).getUserId()
        doReturn(true).when(apiCallContext).isGET()
        doReturn("archivedCase").when(apiCallContext).getResourceName()
        doReturn("45").when(apiCallContext).getResourceId()
    }

    @Test
    public void should_check_verify_can_start_on_post_is_true() {
        doReturn(true).when(apiCallContext).isPOST()
        doReturn(new JSONObject('''
            {
                "processDefinitionId":"154",
                "other":"sample"
            }
        ''')).when(apiCallContext).getBodyAsJSON()
        doReturn(new SearchResultImpl<User>(1, [user])).when(processAPI).searchUsersWhoCanStartProcessDefinition(eq(154l), any(SearchOptions.class));

        //when
        def isAuthorized = rule.isAllowed(apiSession, apiCallContext, apiAccessor, logger)
        //then
        Assertions.assertThat(isAuthorized).isTrue();

    }

    @Test
    public void should_check_verify_can_start_on_post_is_false() {
        doReturn(true).when(apiCallContext).isPOST()
        doReturn(new JSONObject('''
            {
                "processDefinitionId":"154",
                "other":"sample"
            }
        ''')).when(apiCallContext).getBodyAsJSON()
        doReturn(new SearchResultImpl<User>(0, [])).when(processAPI).searchUsersWhoCanStartProcessDefinition(eq(154l), any(SearchOptions.class));

        //when
        def isAuthorized = rule.isAllowed(apiSession, apiCallContext, apiAccessor, logger)
        //then
        Assertions.assertThat(isAuthorized).isFalse();
    }

    @Test
    public void should_check_verify_can_start_on_post_with_bad_body_is_true() {
        doReturn(true).when(apiCallContext).isPOST()
        doReturn(new JSONObject('''
            {
                "unknown":"154",
                "other":"sample"
            }
        ''')).when(apiCallContext).getBodyAsJSON()
        doReturn(new SearchResultImpl<User>(1, [user])).when(processAPI).searchUsersWhoCanStartProcessDefinition(eq(154l), any(SearchOptions.class));

        //when
        def isAuthorized = rule.isAllowed(apiSession, apiCallContext, apiAccessor, logger)
        //then
        Assertions.assertThat(isAuthorized).isTrue();

    }


}
