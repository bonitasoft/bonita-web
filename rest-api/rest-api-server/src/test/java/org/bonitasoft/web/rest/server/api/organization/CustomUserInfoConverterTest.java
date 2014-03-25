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

import org.bonitasoft.engine.identity.CustomUserInfoDefinition;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoDefinitionItem;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

/**
 * @author Vincent Elcrin
 */
public class CustomUserInfoConverterTest {

    private CustomUserInfoConverter converter = new CustomUserInfoConverter();

    @Test
    public void convert_should_return_a_fully_configured_item() throws Exception {
        CustomUserInfoDefinition dummy = new EngineCustomUserInfoDefinition(1L, "foo", "bar");

        CustomUserInfoDefinitionItem definition = converter.convert(dummy);

        assertThat(definition.getAttributes()).containsOnly(
                entry("id", "1"),
                entry("name", "foo"),
                entry("description", "bar"));
    }

}
