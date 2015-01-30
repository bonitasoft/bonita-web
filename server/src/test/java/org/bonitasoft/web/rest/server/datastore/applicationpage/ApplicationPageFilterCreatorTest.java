package org.bonitasoft.web.rest.server.datastore.applicationpage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.io.Serializable;

import org.bonitasoft.web.rest.server.datastore.applicationpage.ApplicationPageFilterCreator;
import org.bonitasoft.web.rest.server.datastore.applicationpage.ApplicationPageSearchDescriptorConverter;
import org.bonitasoft.web.rest.server.datastore.filter.Filter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class ApplicationPageFilterCreatorTest {

    @Mock
    private ApplicationPageSearchDescriptorConverter converter;

    @InjectMocks
    private ApplicationPageFilterCreator creator;

    @Test
    public void should_return_filter_based_on_given_field_and_value_on_create() throws Exception {
        //given
        given(converter.convert("name")).willReturn("name");

        //when
        final Filter<? extends Serializable> filter = creator.create("name", "a name");

        //then
        assertThat(filter).isNotNull();
        assertThat(filter.getField()).isEqualTo("name");
        assertThat(filter.getValue()).isEqualTo("a name");
    }

}
