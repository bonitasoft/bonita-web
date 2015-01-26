/*******************************************************************************
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.bonitasoft.web.rest.model.application.ApplicationDefinition;
import org.bonitasoft.web.rest.model.applicationpage.ApplicationPageDefinition;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.junit.Test;


public class ModelFactoryExtTest {

    private final ModelFactory factory = new ModelFactory();

    @Test
    public void defineItemDefinitions_should_return_instanceOf_ApplicationDefinition_for_application_token() throws Exception {
        //when
        final ItemDefinition<?> definition = factory.defineItemDefinitions(ApplicationDefinition.TOKEN);

        //then
        assertThat(definition).isNotNull();
        assertThat(definition).isInstanceOf(ApplicationDefinition.class);
    }

    @Test
    public void defineItemDefinitions_should_return_instanceOf_ApplicationPageDefinition_for_applicationPage_token() throws Exception {
        //when
        final ItemDefinition<?> definition = factory.defineItemDefinitions(ApplicationPageDefinition.TOKEN);

        //then
        assertThat(definition).isNotNull();
        assertThat(definition).isInstanceOf(ApplicationPageDefinition.class);
    }

}
