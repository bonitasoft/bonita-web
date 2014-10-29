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

package org.bonitasoft.web.rest.security

import org.bonitasoft.engine.api.APIAccessor
import org.bonitasoft.engine.api.IdentityAPI
import org.bonitasoft.engine.api.Logger
import org.bonitasoft.engine.api.ProcessAPI
import org.bonitasoft.engine.api.permission.APICallContext
import org.bonitasoft.engine.api.permission.PermissionRule
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
import static org.mockito.Matchers.eq
import static org.mockito.Matchers.any
import static org.mockito.Mockito.doReturn

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
    def PermissionRule rule = new CasePermissionRule()
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
        def isAuthorized = rule.check(apiSession, apiCallContext, apiAccessor, logger)
        //then
        assertThat(isAuthorized).isFalse();
    }

    def havingFilters(Map filters) {
        doReturn("GET").when(apiCallContext).getMethod()
        doReturn(filters).when(apiCallContext).getFilters()
    }

    @Test
    public void should_check_verify_filters_on_GET_with_different_user_started() {
        //given
        havingFilters([started_by: "15"])
        //when
        def isAuthorized = rule.check(apiSession, apiCallContext, apiAccessor, logger)
        //then
        assertThat(isAuthorized).isFalse();
    }

    @Test
    public void should_check_verify_filters_on_GET_with_same_user_involved() {
        //given
        havingFilters([user_id: "16"])
        //when
        def isAuthorized = rule.check(apiSession, apiCallContext, apiAccessor, logger)
        //then
        assertThat(isAuthorized).isTrue();
    }

    @Test
    public void should_check_verify_filters_on_GET_with_same_user_started() {
        //given
        havingFilters([started_by: "16"])
        //when
        def isAuthorized = rule.check(apiSession, apiCallContext, apiAccessor, logger)
        //then
        assertThat(isAuthorized).isTrue();
    }

    @Test
    public void should_check_verify_filters_on_GET_nofilter_on_user() {
        //given
        havingFilters([plop: "16"])
        //when
        def isAuthorized = rule.check(apiSession, apiCallContext, apiAccessor, logger)
        //then
        assertThat(isAuthorized).isFalse();
    }

    @Test
    public void should_check_verify_resourceId_isInvolved_on_GET() {
        //given
        havingResourceId(true)
        //when
        def isAuthorized = rule.check(apiSession, apiCallContext, apiAccessor, logger)
        //then
        assertThat(isAuthorized).isTrue();
    }

    @Test
    public void should_check_verify_resourceId_not_isInvolved_on_GET() {
        //given
        havingResourceId(false)
        //when
        def isAuthorized = rule.check(apiSession, apiCallContext, apiAccessor, logger)
        //then
        assertThat(isAuthorized).isFalse();
    }

    def havingResourceId(boolean isInvolvedIn) {
        doReturn(currentUserId).when(apiSession).getUserId()
        doReturn("GET").when(apiCallContext).getMethod()
        doReturn("45").when(apiCallContext).getResourceId()
        doReturn(isInvolvedIn).when(processAPI).isInvolvedInProcessInstance(currentUserId, 45l);

    }

    @Test
    public void should_check_verify_can_start_on_post_is_true() {
        doReturn("POST").when(apiCallContext).getMethod()
        doReturn(new JSONObject('''
            {
                "processDefinitionId":"154",
                "other":"sample"
            }
        ''')).when(apiCallContext).getBodyAsJSON()
        doReturn(new SearchResultImpl<User>(1, [user])).when(processAPI).searchUsersWhoCanStartProcessDefinition(eq(154l), any(SearchOptions.class));

        //when
        def isAuthorized = rule.check(apiSession, apiCallContext, apiAccessor, logger)
        //then
        assertThat(isAuthorized).isTrue();

    }

    @Test
    public void should_check_verify_can_start_on_post_is_false() {
        doReturn("POST").when(apiCallContext).getMethod()
        doReturn(new JSONObject('''
            {
                "processDefinitionId":"154",
                "other":"sample"
            }
        ''')).when(apiCallContext).getBodyAsJSON()
        doReturn(new SearchResultImpl<User>(0, [])).when(processAPI).searchUsersWhoCanStartProcessDefinition(eq(154l), any(SearchOptions.class));

        //when
        def isAuthorized = rule.check(apiSession, apiCallContext, apiAccessor, logger)
        //then
        assertThat(isAuthorized).isFalse();
    }

    @Test
    public void should_check_verify_can_start_on_post_with_bad_body_is_false() {
        doReturn("POST").when(apiCallContext).getMethod()
        doReturn(new JSONObject('''
            {
                "unknown":"154",
                "other":"sample"
            }
        ''')).when(apiCallContext).getBodyAsJSON()
        doReturn(new SearchResultImpl<User>(1, [user])).when(processAPI).searchUsersWhoCanStartProcessDefinition(eq(154l), any(SearchOptions.class));

        //when
        def isAuthorized = rule.check(apiSession, apiCallContext, apiAccessor, logger)
        //then
        assertThat(isAuthorized).isFalse();

    }


}
