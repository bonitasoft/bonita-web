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
import org.bonitasoft.engine.identity.User
import org.bonitasoft.engine.session.APISession
import org.json.JSONArray
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

import static org.assertj.core.api.Assertions.assertThat
import static org.mockito.Mockito.doReturn

@RunWith(MockitoJUnitRunner.class)
public class ProcessSupervisorPermissionRuleTest {

    @Mock
    def APISession apiSession
    @Mock
    def APICallContext apiCallContext
    @Mock
    def APIAccessor apiAccessor
    @Mock
    def Logger logger
    def PermissionRule rule = new ProcessSupervisorPermissionRule()
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
        doReturn(user).when(identityAPI).getUser(currentUserId)
        doReturn(currentUserId).when(apiSession).getUserId()
    }

    @Test
    public void should_check_verify_post_is_true_when_process_owner() {
        doReturn(true).when(apiCallContext).isPOST()
        doReturn(new JSONArray('''
            [{
                "process_id":"154",
                "other":"sample"
            }]
        ''')).when(apiCallContext).getBodyAsJSONArray()
        doReturn(true).when(processAPI).isUserProcessSupervisor(154l, currentUserId);


        //when
        def isAuthorized = rule.isAllowed(apiSession, apiCallContext, apiAccessor, logger)
        //then
        assertThat(isAuthorized).isTrue();
    }

    @Test
    public void should_check_verify_post_is_false_when_not_process_owner() {
        doReturn(true).when(apiCallContext).isPOST()
        doReturn(new JSONArray('''
            [{
                "process_id":"154",
                "other":"sample"
            }]
        ''')).when(apiCallContext).getBodyAsJSONArray()
        doReturn(false).when(processAPI).isUserProcessSupervisor(154l, currentUserId);


        //when
        def isAuthorized = rule.isAllowed(apiSession, apiCallContext, apiAccessor, logger)
        //then
        assertThat(isAuthorized).isFalse();
    }

    @Test
    public void should_check_verify_get_is_true_when_process_owner() {
        doReturn(true).when(apiCallContext).isGET()
        doReturn([
            "process_id":"154",
            "other":"sample"
        ]).when(apiCallContext).getFilters()
        doReturn(true).when(processAPI).isUserProcessSupervisor(154l, currentUserId);


        //when
        def isAuthorized = rule.isAllowed(apiSession, apiCallContext, apiAccessor, logger)
        //then
        assertThat(isAuthorized).isTrue();

    }

    @Test
    public void should_check_verify_get_is_false_when_not_process_owner() {
        doReturn(true).when(apiCallContext).isGET()
        doReturn([
            "process_id":"154",
            "other":"sample"
        ]).when(apiCallContext).getFilters()
        doReturn(false).when(processAPI).isUserProcessSupervisor(154l, currentUserId);


        //when
        def isAuthorized = rule.isAllowed(apiSession, apiCallContext, apiAccessor, logger)
        //then
        assertThat(isAuthorized).isFalse();
    }

    @Test
    public void should_check_verify_delete_return_false_when_not_process_owner() {
        doReturn(true).when(apiCallContext).isDELETE()
        doReturn([
            "154",
            "1",
            "2",
            "3"
        ]).when(apiCallContext).getCompoundResourceId()
        doReturn(false).when(processAPI).isUserProcessSupervisor(154l, currentUserId);

        //when
        def isAuthorized = rule.isAllowed(apiSession, apiCallContext, apiAccessor, logger)
        //then
        assertThat(isAuthorized).isFalse();

    }

    @Test
    public void should_check_verify_delete_return_true_when_process_owner() {
        doReturn(true).when(apiCallContext).isDELETE()
        doReturn([
            "154",
            "1",
            "2",
            "3"
        ]).when(apiCallContext).getCompoundResourceId()
        doReturn(true).when(processAPI).isUserProcessSupervisor(154l, currentUserId);

        //when
        def isAuthorized = rule.isAllowed(apiSession, apiCallContext, apiAccessor, logger)
        //then
        assertThat(isAuthorized).isTrue();

    }
}
