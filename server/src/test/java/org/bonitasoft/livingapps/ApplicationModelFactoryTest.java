package org.bonitasoft.livingapps;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Collections;

import org.bonitasoft.engine.api.ApplicationAPI;
import org.bonitasoft.engine.api.PageAPI;
import org.bonitasoft.engine.business.application.Application;
import org.bonitasoft.engine.business.application.impl.ApplicationImpl;
import org.bonitasoft.engine.business.application.impl.ApplicationPageImpl;
import org.bonitasoft.engine.exception.SearchException;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.impl.SearchFilter;
import org.bonitasoft.engine.search.impl.SearchResultImpl;
import org.bonitasoft.livingapps.exception.CreationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationModelFactoryTest {

    @Mock(answer = Answers.RETURNS_MOCKS)
    ApplicationAPI applicationApi;

    @Mock
    PageAPI customPageApi;

    @InjectMocks
    ApplicationModelFactory factory;

    @Test(expected = CreationException.class)
    public void should_throw_create_error_exception_when_search_fail() throws Exception {
        given(applicationApi.searchApplications(any(SearchOptions.class))).willThrow(SearchException.class);

        factory.createApplicationModel("foo");
    }

    @Test(expected = CreationException.class)
    public void should_throw_create_error_exception_when_application_is_not_found() throws Exception {
        given(applicationApi.searchApplications(any(SearchOptions.class))).willReturn(
                new SearchResultImpl<Application>(0, Collections.<Application> emptyList()));

        factory.createApplicationModel("foo");
    }

    @Test
    public void should_return_application_found() throws Exception {
        final ApplicationImpl application = new ApplicationImpl("foobar", "1.0", "bazqux");
        application.setId(3);
        given(applicationApi.searchApplications(any(SearchOptions.class))).willReturn(
                new SearchResultImpl<Application>(1, asList((Application) application)));
        given(applicationApi.getApplicationHomePage(3)).willReturn(new ApplicationPageImpl(1, 1, "home"));

        final ApplicationModel model = factory.createApplicationModel("foo");

        assertThat(model.getApplicationHomePage()).isEqualTo("home/");
    }

    @Test
    public void should_filter_search_using_given_name() throws Exception {
        final ArgumentCaptor<SearchOptions> captor = ArgumentCaptor.forClass(SearchOptions.class);
        given(applicationApi.searchApplications(any(SearchOptions.class))).willReturn(
                new SearchResultImpl<Application>(1, asList(mock(Application.class))));

        factory.createApplicationModel("bar");
        verify(applicationApi).searchApplications(captor.capture());

        final SearchFilter filter = captor.getValue().getFilters().get(0);
        assertThat(filter.getField()).isEqualTo("token");
        assertThat(filter.getValue()).isEqualTo("bar");
    }
}
