/*******************************************************************************
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.server.datastore.applicationmenu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.io.Serializable;

import org.bonitasoft.web.rest.server.datastore.applicationmenu.ApplicationMenuFilterCreator;
import org.bonitasoft.web.rest.server.datastore.applicationmenu.ApplicationMenuSearchDescriptorConverter;
import org.bonitasoft.web.rest.server.datastore.filter.Filter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class ApplicationMenuFilterCreatorTest {

    @Mock
    private ApplicationMenuSearchDescriptorConverter converter;

    @InjectMocks
    private ApplicationMenuFilterCreator creator;

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
