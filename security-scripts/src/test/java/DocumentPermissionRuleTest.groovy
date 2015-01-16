/**
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
 **/





import org.bonitasoft.engine.api.APIAccessor
import org.bonitasoft.engine.api.IdentityAPI
import org.bonitasoft.engine.api.Logger
import org.bonitasoft.engine.api.ProcessAPI
import org.bonitasoft.engine.api.permission.APICallContext
import org.bonitasoft.engine.api.permission.PermissionRule
import org.bonitasoft.engine.bpm.process.ProcessInstance
import org.bonitasoft.engine.identity.User
import org.bonitasoft.engine.session.APISession
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

import static org.assertj.core.api.Assertions.assertThat
import static org.mockito.Mockito.doReturn
import static org.mockito.Mockito.mock

@RunWith(MockitoJUnitRunner.class)
public class DocumentPermissionRuleTest {

    @Mock
    def APISession apiSession
    @Mock
    def APICallContext apiCallContext
    @Mock
    def APIAccessor apiAccessor
    @Mock
    def Logger logger
    def PermissionRule rule = new DocumentPermissionRule()
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
    public void should_check_verify_filters_on_GET_with_user_not_involved_nor_supervisor() {
        //given
        havingFilters([processInstanceId: "46"])
        def processInstance = mock(ProcessInstance.class)
        doReturn(1024l).when(processInstance).getProcessDefinitionId()
        doReturn(processInstance).when(processAPI).getProcessInstance(46l)
        doReturn(false).when(processAPI).isUserProcessSupervisor(1024l, currentUserId)
        //when
        def isAuthorized = rule.isAllowed(apiSession, apiCallContext, apiAccessor, logger)
        //then
        assertThat(isAuthorized).isFalse();
    }

    @Test
    public void should_check_verify_filters_on_GET_with_user_not_involved_but_supervisor() {
        //given
        havingFilters([processInstanceId: "46"])
        def processInstance = mock(ProcessInstance.class)
        doReturn(1024l).when(processInstance).getProcessDefinitionId()
        doReturn(processInstance).when(processAPI).getProcessInstance(46l)
        doReturn(true).when(processAPI).isUserProcessSupervisor(1024l, currentUserId)
        //when
        def isAuthorized = rule.isAllowed(apiSession, apiCallContext, apiAccessor, logger)
        //then
        assertThat(isAuthorized).isTrue();
    }

    def havingFilters(Map filters) {
        doReturn(true).when(apiCallContext).isGET()
        doReturn(filters).when(apiCallContext).getFilters()
        doReturn(true).when(processAPI).isInvolvedInProcessInstance(currentUserId, 45l);
    }


    @Test
    public void should_check_verify_filters_on_GET_with_user_involved() {
        //given
        havingFilters([processInstanceId: "45"])
        //when
        def isAuthorized = rule.isAllowed(apiSession, apiCallContext, apiAccessor, logger)
        //then
        assertThat(isAuthorized).isTrue();
    }


    @Test
    public void should_check_verify_filters_on_GET_no_filter() {
        //given
        havingFilters([plop: "16"])
        //when
        def isAuthorized = rule.isAllowed(apiSession, apiCallContext, apiAccessor, logger)
        //then
        assertThat(isAuthorized).isFalse();
    }


    @Test
    public void should_check_verify_can_start_on_post_is_true() {
        doReturn(true).when(apiCallContext).isPOST()
        doReturn('''
            {
                "processInstanceId":"154",
                "other":"sample"
            }
        ''').when(apiCallContext).getBody()
        doReturn(true).when(processAPI).isInvolvedInProcessInstance(currentUserId, 154l);


        //when
        def isAuthorized = rule.isAllowed(apiSession, apiCallContext, apiAccessor, logger)
        //then
        assertThat(isAuthorized).isTrue();

    }

    @Test
    public void should_check_verify_can_start_on_post_is_false() {
        doReturn(true).when(apiCallContext).isPOST()
        doReturn('''
            {
                "processInstanceId":"154",
                "other":"sample"
            }
        ''').when(apiCallContext).getBody()
        doReturn(false).when(processAPI).isInvolvedInProcessInstance(currentUserId, 154l);

        //when
        def isAuthorized = rule.isAllowed(apiSession, apiCallContext, apiAccessor, logger)
        //then
        assertThat(isAuthorized).isFalse();
    }

    @Test
    public void should_check_verify_can_start_on_post_with_bad_body_is_true() {
        doReturn(true).when(apiCallContext).isPOST()
        doReturn('''
            {
                "unknown":"154",
                "other":"sample"
            }
        ''').when(apiCallContext).getBody()
        doReturn(true).when(processAPI).isInvolvedInProcessInstance(currentUserId, 154l);

        //when
        def isAuthorized = rule.isAllowed(apiSession, apiCallContext, apiAccessor, logger)
        //then
        assertThat(isAuthorized).isTrue();

    }


}
