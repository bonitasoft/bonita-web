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







package org.bonitasoft.web.rest.security

import org.bonitasoft.engine.api.APIAccessor
import org.bonitasoft.engine.api.IdentityAPI
import org.bonitasoft.engine.api.Logger
import org.bonitasoft.engine.api.ProcessAPI
import org.bonitasoft.engine.api.permission.APICallContext
import org.bonitasoft.engine.api.permission.PermissionRule
import org.bonitasoft.engine.bpm.actor.ActorInstance
import org.bonitasoft.engine.bpm.process.ProcessInstance
import org.bonitasoft.engine.identity.User
import org.bonitasoft.engine.session.APISession
import org.json.JSONObject
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

import static org.assertj.core.api.Assertions.assertThat
import static org.mockito.Mockito.doReturn
import static org.mockito.Mockito.mock

@RunWith(MockitoJUnitRunner.class)
public class CaseVariablePermissionRuleTest {

    @Mock
    def APISession apiSession
    @Mock
    def APICallContext apiCallContext
    @Mock
    def APIAccessor apiAccessor
    @Mock
    def Logger logger
    def CaseVariablePermissionRule rule = new CaseVariablePermissionRule()
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
    public void should_check_return_false_on_delete() {
        doReturn("DELETE").when(apiCallContext).getMethod()

        //when
        def isAuthorized = rule.check(apiSession, apiCallContext, apiAccessor, logger)
        //then
        assertThat(isAuthorized).isFalse();

    }

    @Test
    public void should_check_return_true_on_get() {
        doReturn("GET").when(apiCallContext).getMethod()
        doReturn("158").when(apiCallContext).getResourceId()

        def instance = mock(ProcessInstance.class)
        doReturn(425l).when(instance).getProcessDefinitionId()
        doReturn(instance).when(processAPI).getProcessInstance(158l)
        doReturn(true).when(processAPI).isUserProcessSupervisor(425l,currentUserId)
        //when
        def isAuthorized = rule.check(apiSession, apiCallContext, apiAccessor, logger)
        //then
        assertThat(isAuthorized).isTrue();

    }

    @Test
    public void should_check_return_false_on_get_if_not_process_owner() {
        doReturn("GET").when(apiCallContext).getMethod()
        doReturn("158").when(apiCallContext).getResourceId()

        def instance = mock(ProcessInstance.class)
        doReturn(425l).when(instance).getProcessDefinitionId()
        doReturn(instance).when(processAPI).getProcessInstance(158l)
        doReturn(false).when(processAPI).isUserProcessSupervisor(425l,currentUserId)
        //when
        def isAuthorized = rule.check(apiSession, apiCallContext, apiAccessor, logger)
        //then
        assertThat(isAuthorized).isFalse();

    }

    @Test
    public void should_check_return_true_on_put() {
        doReturn("PUT").when(apiCallContext).getMethod()
        doReturn("158").when(apiCallContext).getResourceId()

        def instance = mock(ProcessInstance.class)
        doReturn(425l).when(instance).getProcessDefinitionId()
        doReturn(instance).when(processAPI).getProcessInstance(158l)
        doReturn(true).when(processAPI).isUserProcessSupervisor(425l,currentUserId)
        //when
        def isAuthorized = rule.check(apiSession, apiCallContext, apiAccessor, logger)
        //then
        assertThat(isAuthorized).isTrue();

    }


}
