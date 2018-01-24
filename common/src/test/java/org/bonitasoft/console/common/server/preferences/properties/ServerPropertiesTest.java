/**
 * Copyright (C) 2018 Bonitasoft S.A.
 * Bonitasoft, 32 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.console.common.server.preferences.properties;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ServerPropertiesTest {
    
    ServerProperties serverProperties = ServerProperties.getInstance();

    @Test
    public void should_return_property_value() {
        assertThat(serverProperties.getValue("auth.UserLogger")).isEqualTo("org.bonitasoft.console.common.server.login.credentials.UserLogger");
    }
}
