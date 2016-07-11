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

import org.bonitasoft.engine.profile.Profile;
import org.bonitasoft.engine.profile.ProfileEntry;
import org.bonitasoft.web.rest.server.engineclient.ProfileEntryEngineClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * @author Vincent Elcrin
 */
@RunWith(MockitoJUnitRunner.class)
public class TokenProfileProviderTest {

    @Mock
    private ProfileEntryEngineClient client;

    @Mock
    private ProfileEntry entry1;

    @Mock
    private ProfileEntry entry2;

    @Mock
    private Profile profile1;

    @Mock
    private Profile profile2;

    @Before
    public void setUp() throws Exception {
        given(profile1.getId()).willReturn(1L);
        given(client.getProfileEntriesByProfile(1L)).willReturn(Arrays.asList(entry1));
        given(profile2.getId()).willReturn(2L);
        given(client.getProfileEntriesByProfile(2L)).willReturn(Arrays.asList(entry2));
    }

    @Test
    public void should_return_all_page_tokens_for_each_profile_entries() throws Exception {
        given(entry1.getPage()).willReturn("token 1");
        given(entry2.getPage()).willReturn("token 2");

        TokenProfileProvider provider = new TokenProfileProvider(Arrays.asList(
                profile1,
                profile2), client);

        assertThat(provider.getTokens().toString()).isEqualTo("[token 1, token 2]");
    }

    @Test
    public void should_not_return_a_token_if_the_page_from_the_profile_entry_is_null() throws Exception {
        given(entry1.getPage()).willReturn(null);
        given(entry2.getPage()).willReturn("token 2");

        TokenProfileProvider provider = new TokenProfileProvider(Arrays.asList(
                profile1,
                profile2), client);

        assertThat(provider.getTokens().toString()).isEqualTo("[token 2]");
    }
}
