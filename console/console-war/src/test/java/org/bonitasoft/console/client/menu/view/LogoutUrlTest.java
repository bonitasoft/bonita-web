/**
 * Copyright (C) 2012 BonitaSoft S.A.
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
package org.bonitasoft.console.client.menu.view;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import org.bonitasoft.web.toolkit.client.common.UrlBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class LogoutUrlTest {

    @Mock
    UrlBuilder builder;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void testUrlIsBuildProperly() throws Exception {
        new LogoutUrl(builder, "fr");

        verify(builder).setRootUrl("../logoutservice");
        verify(builder).addParameter("_l", "fr");
    }

    @Test
    public void testWeCanSetAParameter() throws Exception {
        LogoutUrl logoutUrl = new LogoutUrl(builder, "fr");
        logoutUrl.setParameter("aParameter", "itsValue");

        new LogoutUrl(builder, "fr");

        verify(builder).addParameter("aParameter", "itsValue");
    }
}
