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
package org.bonitasoft.web.rest.server.api.organization;

import org.bonitasoft.engine.identity.CustomUserInfo;
import org.bonitasoft.engine.identity.CustomUserInfoDefinition;
import org.bonitasoft.engine.identity.impl.CustomUserInfoValueImpl;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoDefinitionItem;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoItem;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

/**
 * @author Vincent Elcrin
 */
public class CustomUserInfoConverterTest {

    private CustomUserInfoConverter converter = new CustomUserInfoConverter();

    @Test
    public void should_return_a_fully_configured_definition() throws Exception {
        CustomUserInfoDefinition dummy = new EngineCustomUserInfoDefinition(1L, "foo", "bar");

        CustomUserInfoDefinitionItem definition = converter.convert(dummy);

        assertThat(definition.getAttributes()).containsOnly(
                entry("id", "1"),
                entry("name", "foo"),
                entry("description", "bar"));
    }

    @Test
    public void should_return_a_fully_configured_custom_information() throws Exception {
        CustomUserInfoDefinition definition = new EngineCustomUserInfoDefinition(3L);
        CustomUserInfoValueImpl value = new CustomUserInfoValueImpl();
        value.setValue("foo");

        CustomUserInfoItem information = converter.convert(new CustomUserInfo(2L, definition, value));

        assertThat(information.getAttributes()).containsOnly(
                entry("userId", "2"),
                entry("definitionId", "3"),
                entry("value", "foo"));
        assertThat(information.getDefinition().getId()).isEqualTo(APIID.makeAPIID(3L));
    }

    @Test
    public void should_return_a_fully_configured_custom_information_form_a_value() throws Exception {
        CustomUserInfoValueImpl value = new CustomUserInfoValueImpl();
        value.setUserId(5);
        value.setDefinitionId(6);
        value.setValue("foo");

        CustomUserInfoItem information = converter.convert(value);

        assertThat(information.getAttributes()).containsOnly(
                entry("userId", "5"),
                entry("definitionId", "6"),
                entry("value", "foo"));
    }
}
