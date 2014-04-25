/**
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.web.rest.server.api.system;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

/**
 * @author Vincent Elcrin
 */
@RunWith(MockitoJUnitRunner.class)
public class UserRightsBuilderTest {

    private SHA1Generator generator = new SHA1Generator();

    @Mock
    private org.bonitasoft.engine.session.APISession session;

    @Test
    public void should_build_rights_for_a_token_add_to_it() throws Exception {
        given(session.getId()).willReturn(56L);
        UserRightsBuilder builder = new UserRightsBuilder(session, new TokenListProvider(Arrays.asList(
                "token")));

        assertEquals(builder.build().get(0), generator.getHash("token56"));
    }

    @Test
    public void should_build_rights_for_all_tokens_progressively_add_to_it() throws Exception {
        given(session.getId()).willReturn(85L);
        UserRightsBuilder builder = new UserRightsBuilder(session, new TokenListProvider(Arrays.asList(
                "token 1", "token 2")));

        List<String> rights = builder.build();

        assertEquals(rights.get(0), generator.getHash("token 185"));
        assertEquals(rights.get(1), generator.getHash("token 285"));
    }
}
