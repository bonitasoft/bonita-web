package org.bonitasoft.web.rest.server.api.organization;

import javax.servlet.http.HttpSession;

import java.util.Arrays;
import java.util.Collections;

import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.identity.CustomUserInfoValue;
import org.bonitasoft.engine.identity.impl.CustomUserInfoValueImpl;
import org.bonitasoft.engine.search.Order;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.impl.SearchResultImpl;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoItem;
import org.bonitasoft.web.rest.server.engineclient.CustomUserInfoEngineClient;
import org.bonitasoft.web.rest.server.engineclient.CustomUserInfoEngineClientCreator;
import org.bonitasoft.web.rest.server.framework.APIServletCall;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.Assertions.extractProperty;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

/**
 * @author Vincent Elcrin
 */
@RunWith(MockitoJUnitRunner.class)
public class APICustomUserInfoValueTest {

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
    private APICustomUserInfoValue api;

    @Before
    public void setUp() throws Exception {
        api.setCaller(caller);
        given(caller.getHttpSession()).willReturn(httpSession);
        given(httpSession.getAttribute("apiSession")).willReturn(apiSession);
        given(engineClientCreator.create(apiSession)).willReturn(engine);
    }

    @Test
    public void should_retrieve_custom_user_info() throws Exception {
        given(engine.searchCustomUserInfoValues(any(SearchOptions.class))).willReturn(
                new SearchResultImpl<CustomUserInfoValue>(3, Arrays.<CustomUserInfoValue> asList(
                        createValue("foo"),
                        createValue("bar"))));

        ItemSearchResult<CustomUserInfoItem> result = api.search(0, 2, null, null, Collections.<String, String>emptyMap());

        assertThat(result.getPage()).isEqualTo(0);
        assertThat(result.getTotal()).isEqualTo(3);
        assertThat(result.getLength()).isEqualTo(2);
        assertThat(result.getResults().get(0).getValue()).isEqualTo("foo");
        assertThat(result.getResults().get(1).getValue()).isEqualTo("bar");
    }

    private CustomUserInfoValueImpl createValue(String value) {
        CustomUserInfoValueImpl information = new CustomUserInfoValueImpl();
        information.setValue(value);
        return information;
    }

    @Test
    public void should_retrieve_custom_user_info_sorted() throws Exception {
        given(engine.searchCustomUserInfoValues(any(SearchOptions.class))).willReturn(
                new SearchResultImpl<CustomUserInfoValue>(0, Collections.<CustomUserInfoValue> emptyList()));
        ArgumentCaptor<SearchOptions> argument = ArgumentCaptor.forClass(SearchOptions.class);

        api.search(0, 2, null, "userId ASC", Collections.<String, String> emptyMap());

        verify(engine).searchCustomUserInfoValues(argument.capture());
        assertThat(argument.getValue().getSorts().get(0).getField()).isEqualTo("userId");
        assertThat(argument.getValue().getSorts().get(0).getOrder()).isEqualTo(Order.ASC);
    }

    @Test
    public void should_retrieve_custom_user_info_filtered() throws Exception {
        given(engine.searchCustomUserInfoValues(any(SearchOptions.class))).willReturn(
                new SearchResultImpl<CustomUserInfoValue>(0, Collections.<CustomUserInfoValue> emptyList()));
        ArgumentCaptor<SearchOptions> argument = ArgumentCaptor.forClass(SearchOptions.class);

        api.search(0, 2, null, null, Collections.singletonMap(CustomUserInfoItem.ATTRIBUTE_VALUE, "bar"));

        verify(engine).searchCustomUserInfoValues(argument.capture());
        assertThat(argument.getValue().getFilters().get(0).getField()).isEqualTo(CustomUserInfoItem.ATTRIBUTE_VALUE);
        assertThat(argument.getValue().getFilters().get(0).getValue()).isEqualTo("bar");
    }

    @Test
    public void should_retrieve_custom_user_info_term_filtered() throws Exception {
        given(engine.searchCustomUserInfoValues(any(SearchOptions.class))).willReturn(
                new SearchResultImpl<CustomUserInfoValue>(0, Collections.<CustomUserInfoValue> emptyList()));
        ArgumentCaptor<SearchOptions> argument = ArgumentCaptor.forClass(SearchOptions.class);

        api.search(0, 2, "foo", null, Collections.<String, String>emptyMap());

        verify(engine).searchCustomUserInfoValues(argument.capture());
        assertThat(argument.getValue().getSearchTerm()).isEqualTo("foo");
    }
}
