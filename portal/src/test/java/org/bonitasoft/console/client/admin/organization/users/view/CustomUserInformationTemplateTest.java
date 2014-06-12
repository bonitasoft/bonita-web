/**
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.console.client.admin.organization.users.view;

import static junit.framework.Assert.assertEquals;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwtmockito.GwtMockitoTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.bonitasoft.web.rest.model.ModelFactory;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoDefinitionItem;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoItem;
import org.bonitasoft.web.toolkit.client.ItemDefinitionFactory;

@RunWith(GwtMockitoTestRunner.class)
public class CustomUserInformationTemplateTest {

    @Before
    public void setUp() throws Exception {
        ItemDefinitionFactory.setDefaultFactory(new ModelFactory());
    }

    @Test
    public void should_render_an_editable_template_when_editable_is_set_to_true() throws Exception {
        CustomUserInformationTemplate template = new CustomUserInformationTemplate(true);
        CustomUserInfoDefinitionItem definition = createDefinition(1L, "name", "description");
        CustomUserInfoItem information = createInformation(definition, "value");

        SafeHtml render = template.render(new Cell.Context(6, 1, null), information);

        assertEquals("editable(7, name, description, value)", render.asString());
    }

    @Test
    public void should_render_a_read_only_template_when_editable_is_set_to_false() throws Exception {
        CustomUserInformationTemplate template = new CustomUserInformationTemplate(false);
        CustomUserInfoDefinitionItem definition = createDefinition(1L, "foo", "bar");
        CustomUserInfoItem information = createInformation(definition, "baz");

        SafeHtml render = template.render(new Cell.Context(0, 1, null), information);

        assertEquals("readOnly(foo, bar, baz)", render.asString());
    }

    private CustomUserInfoItem createInformation(CustomUserInfoDefinitionItem definition, String value) {
        CustomUserInfoItem information = new CustomUserInfoItem();
        information.setDefinition(definition);
        information.setValue(value);
        return information;
    }

    private CustomUserInfoDefinitionItem createDefinition(long id, String name, String description) {
        CustomUserInfoDefinitionItem definition = new CustomUserInfoDefinitionItem();
        definition.setId(id);
        definition.setName(name);
        definition.setDescription(description);
        return definition;
    }
}