/**
 * Copyright (C) 2017 Bonitasoft S.A.
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
package org.bonitasoft.web.rest.server.datastore.profile;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.Serializable;

import org.bonitasoft.engine.profile.ProfileSearchDescriptor;
import org.bonitasoft.web.rest.model.portal.profile.ProfileItem;
import org.bonitasoft.web.rest.server.datastore.filter.Filter;
import org.bonitasoft.web.rest.server.datastore.filter.Filter.Operator;
import org.junit.Test;

public class ProfileFilterCreatorTest {

    @Test
    public void should_create_a_filter_for_profiles_with_navigation() {
        ProfileFilterCreator profileFilterCreator = new ProfileFilterCreator(new ProfileSearchDescriptorConverter());

        Filter<? extends Serializable> filter = profileFilterCreator.create(ProfileItem.FILTER_HAS_NAVIGATION, "true");

        assertThat(filter.getField()).isEqualTo(ProfileSearchDescriptor.PROFILE_ENTRY_NAME);
        assertThat(filter.getValue()).isNull();
        assertThat(filter.getOperator()).isEqualTo(Operator.DIFFERENT_FROM);
    }

    @Test
    public void should_create_a_filter_for_profiles_without_navigation() {
        ProfileFilterCreator profileFilterCreator = new ProfileFilterCreator(new ProfileSearchDescriptorConverter());

        Filter<? extends Serializable> filter = profileFilterCreator.create(ProfileItem.FILTER_HAS_NAVIGATION, "false");

        assertThat(filter.getField()).isEqualTo(ProfileSearchDescriptor.PROFILE_ENTRY_NAME);
        assertThat(filter.getValue()).isNull();
        assertThat(filter.getOperator()).isEqualTo(Operator.EQUAL);
    }

}
