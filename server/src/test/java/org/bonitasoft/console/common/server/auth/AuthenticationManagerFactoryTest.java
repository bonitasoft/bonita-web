/**
 * Copyright (C) 2012-2021 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.console.common.server.auth;

import static org.assertj.core.api.Assertions.assertThat;

import org.bonitasoft.console.common.server.auth.impl.standard.StandardAuthenticationManagerImpl;
import org.junit.Test;

/**
 * @author Rohart Bastien
 * @author Emmanuel Duchastenier
 */
public class AuthenticationManagerFactoryTest {

    @Test
    public void testGetLoginManager() throws AuthenticationManagerNotFoundException {
        assertThat(AuthenticationManagerFactory.getAuthenticationManager(0L)).as("Cannot get the login manager").isNotNull();
    }

    @Test
    public void default_manager_implementation_should_be_StandardAuthenticationManagerImpl_class() throws AuthenticationManagerNotFoundException {
        // when:
        AuthenticationManager managerImpl = AuthenticationManagerFactory.getAuthenticationManager(1678L);

        // then:
        assertThat(managerImpl).isInstanceOf(StandardAuthenticationManagerImpl.class);
    }
}
