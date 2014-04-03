package org.bonitasoft.web.rest.server.api.organization;

import javax.servlet.http.HttpSession;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.identity.CustomUserInfo;
import org.bonitasoft.engine.identity.impl.CustomUserInfoValueImpl;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoItem;
import org.bonitasoft.web.rest.server.engineclient.CustomUserInfoEngineClient;
import org.bonitasoft.web.rest.server.engineclient.CustomUserInfoEngineClientCreator;
import org.bonitasoft.web.rest.server.framework.APIServletCall;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * @author Vincent Elcrin
 */
@RunWith(MockitoJUnitRunner.class)
public class APICustomUserInfoUserTest {

    @Mock(answer = Answers.RETURNS_MOCKS)
    private APIServletCall caller;

    @Mock
    private HttpSession httpSession;

    @Mock
    private APISession apiSession;

    @Mock
    private CustomUserInfoEngineClient engine;

    @Mock(answer = Answers.RETURNS_MOCKS)
    private CustomUserInfoEngineClientCreator engineClientCreator;

    @Mock
    private IdentityAPI identityApi;

    @InjectMocks
    private APICustomUserInfoUser api;

    @Before
    public void setUp() throws Exception {
        api.setCaller(caller);
        given(caller.getHttpSession()).willReturn(httpSession);
        given(httpSession.getAttribute("apiSession")).willReturn(apiSession);
        given(engineClientCreator.create(apiSession)).willReturn(engine);

    }

    @Test
    public void should_retrieve_the_CustomUserInfo_for_a_given_user_id() throws Exception {
        given(engine.listCustomInformation(3L, 0, 2)).willReturn(Arrays.asList(
                new CustomUserInfo(3L, new EngineCustomUserInfoDefinition(5L), new CustomUserInfoValueImpl()),
                new CustomUserInfo(3L, new EngineCustomUserInfoDefinition(6L), new CustomUserInfoValueImpl())));

        List<CustomUserInfoItem> information = api.search(0, 2, "", "Fix order", Collections.singletonMap("userId", "3")).getResults();

        assertThat(information.get(0).getDefinition().getId()).isEqualTo(APIID.makeAPIID(5L));
        assertThat(information.get(1).getDefinition().getId()).isEqualTo(APIID.makeAPIID(6L));
    }
}
